package org.twelve.dominio;


import org.twelve.dominio.entities.Usuario;

import java.util.List;

public interface RepositorioUsuario {

    List<Usuario> encontrarTodos();

    Usuario buscarPorId(Long id);

    Usuario guardar(Usuario Usuario);

    List<Usuario> buscarPorUsername(String username);

    Usuario buscarUsuarioPorEmail(String email);
}