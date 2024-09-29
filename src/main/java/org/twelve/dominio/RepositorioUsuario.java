package org.twelve.dominio;

import org.twelve.dominio.entities.Usuario;

public interface RepositorioUsuario {

    Usuario buscarUsuario(String email, String password);

    void guardar(Usuario usuario);

    Usuario buscar(String email);

    void modificar(Usuario usuario);

    Usuario buscarUsuarioPorEmail(String mail);
}

