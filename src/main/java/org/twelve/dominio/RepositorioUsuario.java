package org.twelve.dominio;


import org.twelve.dominio.entities.Usuario;

import java.util.List;

public interface RepositorioUsuario {

    List<Usuario> findAll();

    Usuario findById(Integer id);

    Usuario save(Usuario Usuario);

    List<Usuario> findByUserName(String username);

}