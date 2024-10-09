package org.twelve.dominio;

import org.twelve.dominio.entities.Usuario;

import java.util.List;

public interface RepositorioUsuario {

    Usuario buscarUsuario(String email, String password);

    Usuario guardar(Usuario usuario);

    Usuario buscar(String email);

    void modificar(Usuario usuario);

    Usuario buscarUsuarioPorEmail(String mail);

    Usuario buscarPorId(Long id);

    List<Usuario> encontrarTodos();

    Usuario buscarPorUsername(String username);
}

