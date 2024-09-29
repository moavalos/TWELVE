package org.twelve.dominio;


import org.springframework.stereotype.Service;
import org.twelve.dominio.entities.Usuario;

@Service
public interface UsuarioService {

    void actualizarPerfil(Usuario usuario);

    Usuario buscarPorId(Long id);
}
