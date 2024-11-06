package org.twelve.dominio;

import org.twelve.dominio.entities.Usuario;
import org.twelve.presentacion.dto.PerfilDTO;

import java.util.List;

public interface UsuarioService {

    List<PerfilDTO> encontrarTodos();

    PerfilDTO buscarPorId(Integer id);

    PerfilDTO crear(PerfilDTO usuario);

    void seguirUsuario(Integer usuarioId, Integer seguidoId);

    void dejarDeSeguirUsuario(Integer usuarioId, Integer seguidoId);

    Boolean estaSiguiendo(Integer usuarioId, Integer seguidoId);

    void guardarMeGusta(PerfilDTO usuario, MovieDTO movie);

    boolean haDadoLike(PerfilDTO usuarioDTO, MovieDTO movieDTO);

    Usuario convertToEntity(PerfilDTO perfilDTO);
    PerfilDTO convertToDTO(Usuario tempUsuario);

    void actualizarFotoPerfil(Integer userId, String path);
    long obtenerCantidadDeLikes(MovieDTO movieDTO);

    List<Movie> obtenerPeliculasFavoritas(Integer usuarioId);

}
