package org.twelve.dominio.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.twelve.dominio.RepositorioUsuario;
import org.twelve.dominio.ServicioLogin;
import org.twelve.dominio.entities.Usuario;
import org.twelve.dominio.excepcion.ContrasenasNoCoinciden;
import org.twelve.dominio.excepcion.UsuarioExistente;

import javax.transaction.Transactional;

@Service("servicioLogin")
public class ServicioLoginImpl implements ServicioLogin {

    private RepositorioUsuario repositorioUsuario;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ServicioLoginImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public Usuario consultarUsuario(String email, String password) {
        Usuario usuario = repositorioUsuario.buscarUsuarioPorEmail(email);
        if (usuario != null && passwordEncoder.matches(password, usuario.getPassword())) {
            return usuario;
        }
        return null;
    }

    // busco solo por mail, comparo password encriptada de la bdd con la del usuario
    @Override
    public Usuario registrar(Usuario usuario, String confirmPassword) throws Exception {
        validarContrasenas(usuario.getPassword(), confirmPassword);
        verificarUsuarioExistente(usuario);

        // encripto contraseña antes de guardar
        String passwordEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(passwordEncriptada);
        return repositorioUsuario.guardar(usuario);
    }

    @Override
    public void validarContrasenas(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new ContrasenasNoCoinciden();
        }
    }

    @Override
    public void verificarUsuarioExistente(Usuario usuario) throws UsuarioExistente {
        Usuario usuarioEncontrado = repositorioUsuario.buscarUsuarioPorEmail(usuario.getEmail());
        if (usuarioEncontrado != null) {
            throw new UsuarioExistente();
        }
    }

}

