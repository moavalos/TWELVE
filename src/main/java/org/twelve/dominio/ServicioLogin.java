package org.twelve.dominio;

import org.twelve.dominio.entities.Usuario;
import org.twelve.dominio.excepcion.UsuarioExistente;

public interface ServicioLogin {

    Usuario consultarUsuario(String email, String password);

    Usuario registrar(Usuario usuario, String confirmPassword) throws Exception;

    void validarContrasenas(String password, String confirmPassword);

    void verificarUsuarioExistente(Usuario usuario) throws UsuarioExistente;

}