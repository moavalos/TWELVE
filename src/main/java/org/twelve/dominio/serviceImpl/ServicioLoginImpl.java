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
import org.twelve.presentacion.dto.PerfilDTO;

@Service("servicioLogin")
public class ServicioLoginImpl implements ServicioLogin {

    private final RepositorioUsuario repositorioUsuario;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ServicioLoginImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public PerfilDTO consultarUsuario(String email, String password) {
        Usuario usuario = repositorioUsuario.buscarUsuarioPorEmail(email);

        if (usuario != null && passwordEncoder.matches(password, usuario.getPassword())) {
            return PerfilDTO.convertToDTO(usuario);
        }

        return null;
    }

    // busco solo por mail, comparo password encriptada de la bdd con la del usuario
    @Override
    public PerfilDTO registrar(PerfilDTO perfilDTO, String confirmPassword) throws Exception {
        validarContrasenas(perfilDTO.getPassword(), confirmPassword);
        verificarUsuarioExistente(perfilDTO);

        String passwordEncriptada = passwordEncoder.encode(perfilDTO.getPassword());
        perfilDTO.setPassword(passwordEncriptada);

        perfilDTO.setActivo(true);
        Usuario usuario = PerfilDTO.convertToEntity(perfilDTO);
        Usuario usuarioGuardado = repositorioUsuario.guardar(usuario);

        return PerfilDTO.convertToDTO(usuarioGuardado);
    }

    @Override
    public void validarContrasenas(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new ContrasenasNoCoinciden();
        }
    }

    @Override
    public void verificarUsuarioExistente(PerfilDTO perfilDTO) throws UsuarioExistente {
        Usuario usuarioEncontrado = repositorioUsuario.buscarUsuarioPorEmail(perfilDTO.getEmail());

        if (usuarioEncontrado != null) {
            throw new UsuarioExistente();
        }
    }

}

