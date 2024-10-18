package org.twelve.dominio;

import org.twelve.dominio.excepcion.UsuarioExistente;
import org.twelve.presentacion.dto.PerfilDTO;

import javax.transaction.Transactional;

@Transactional
public interface ServicioLogin {

    PerfilDTO consultarUsuario(String email, String password);

    PerfilDTO registrar(PerfilDTO usuario, String confirmPassword) throws Exception;

    void validarContrasenas(String password, String confirmPassword);

    void verificarUsuarioExistente(PerfilDTO usuario) throws UsuarioExistente;

}