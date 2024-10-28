package org.twelve.dominio;

import org.twelve.dominio.entities.Usuario;

import java.util.List;

public interface RepositorioUsuario {

    Usuario guardar(Usuario usuario);

    Usuario buscarUsuarioPorEmail(String mail);

    Usuario buscarPorId(Integer id);

    List<Usuario> encontrarTodos();

    Usuario buscarPorUsername(String username);

}

