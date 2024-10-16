package org.twelve.dominio;

import org.twelve.dominio.entities.Usuario;
import org.twelve.presentacion.dto.PerfilDTO;

import java.util.List;

public interface UsuarioService {

    List<PerfilDTO> encontrarTodos();

    PerfilDTO buscarPorId(Integer id);

    PerfilDTO crear(PerfilDTO usuario);

    List<PerfilDTO> buscarPorUsername(String username);

    Usuario convertToEntity(PerfilDTO perfilDTO);

    PerfilDTO convertToDTO(Usuario tempUsuario);
}
