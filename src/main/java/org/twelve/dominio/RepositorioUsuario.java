package org.twelve.dominio;

import org.twelve.dominio.entities.Seguidor;
import org.twelve.dominio.entities.Usuario;

import java.util.List;

public interface RepositorioUsuario {

    Usuario guardar(Usuario usuario);

    Usuario buscarUsuarioPorEmail(String mail);

    Usuario buscarPorId(Integer id);

    List<Usuario> encontrarTodos();

    void seguirUsuario(Usuario usuario, Usuario seguido);

    void dejarDeSeguirUsuario(Usuario usuario, Usuario seguido);

    Boolean estaSiguiendo(Usuario usuario, Usuario seguido);

    List<Seguidor> obtenerSeguidos(Integer usuarioId);

    List<Seguidor> obtenerSeguidores(Integer usuarioId);

}

