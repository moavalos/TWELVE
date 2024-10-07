package org.twelve.dominio;


import org.twelve.dominio.entities.Usuario;

import org.springframework.stereotype.Service;


import org.twelve.dominio.entities.Usuario;

import java.util.List;

public interface UsuarioService {
    
    Usuario findByUsername(String username);
    List<Usuario> findAll();

    Usuario buscarPorId(Long id);

    void actualizarPerfil(Usuario usuarioExistente);

    Usuario getCurrentUser();
}
