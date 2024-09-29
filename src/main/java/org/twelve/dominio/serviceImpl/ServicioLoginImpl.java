package org.twelve.dominio.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.twelve.dominio.RepositorioUsuario;
import org.twelve.dominio.ServicioLogin;
import org.twelve.dominio.entities.Usuario;
import org.twelve.dominio.excepcion.UsuarioExistente;

import javax.transaction.Transactional;

@Service("servicioLogin")
@Transactional
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
    public void registrar(Usuario usuario) throws UsuarioExistente {
        Usuario usuarioExistente = repositorioUsuario.buscarUsuarioPorEmail(usuario.getEmail());
        if (usuarioExistente != null) {
            throw new UsuarioExistente();
        }
        // encripto contraselña antes de guardar
        String passwordEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(passwordEncriptada);
        repositorioUsuario.guardar(usuario);
    }
}

