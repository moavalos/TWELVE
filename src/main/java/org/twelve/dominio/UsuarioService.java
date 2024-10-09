package org.twelve.dominio;

import org.twelve.presentacion.dto.PerfilDTO;

import java.util.List;

public interface UsuarioService {

    List<PerfilDTO> encontrarTodos();

    PerfilDTO buscarPorId(Long id);

    PerfilDTO crear(PerfilDTO usuario);

    List<PerfilDTO> buscarPorUsername(String username);

    void actualizarPerfil(PerfilDTO usuarioExistente);
}
