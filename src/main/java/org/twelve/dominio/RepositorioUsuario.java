package org.twelve.dominio;

import org.twelve.dominio.entities.Usuario;

import java.util.Optional;



import org.twelve.dominio.entities.Usuario;

import java.util.List;

public interface RepositorioUsuario {
    void save(Usuario usuario);
    Usuario findById(Long id);
    Usuario findByUsername(String username);
    List<Usuario> findAll();

    void guardar(Usuario usuario);

    Usuario buscarUsuarioPorEmail(String email);
}


