package org.twelve.dominio;

import org.twelve.presentacion.dto.PerfilDTO;

import java.util.List;

public interface UsuarioService {

    List<PerfilDTO> getAll();

    PerfilDTO getById(Long id);

    PerfilDTO create(PerfilDTO usuario);

    List<PerfilDTO> findByUserName(String username);


}
