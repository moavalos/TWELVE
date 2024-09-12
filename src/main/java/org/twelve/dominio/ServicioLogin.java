package org.twelve.dominio;

import org.twelve.dominio.entities.Usuario;
import org.twelve.dominio.excepcion.UsuarioExistente;

public interface ServicioLogin {

    Usuario consultarUsuario(String email, String password);
    void registrar(Usuario usuario) throws UsuarioExistente;

}
