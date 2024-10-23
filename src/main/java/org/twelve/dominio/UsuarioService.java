package org.twelve.dominio;

import org.twelve.presentacion.dto.PerfilDTO;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface UsuarioService {

    List<PerfilDTO> encontrarTodos();

    PerfilDTO buscarPorId(Integer id);

    PerfilDTO crear(PerfilDTO usuario);

    List<PerfilDTO> buscarPorUsername(String username);

    void seguirUsuario(Integer usuarioId, Integer seguidoId);

    void dejarDeSeguirUsuario(Integer usuarioId, Integer seguidoId);

    Boolean estaSiguiendo(Integer usuarioId, Integer seguidoId);

}
