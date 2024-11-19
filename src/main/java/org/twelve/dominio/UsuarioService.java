package org.twelve.dominio;

import org.twelve.dominio.entities.Movie;
import org.twelve.presentacion.dto.MovieDTO;
import org.twelve.presentacion.dto.PerfilDTO;
import org.twelve.presentacion.dto.UsuarioMovieDTO;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface UsuarioService {

    List<PerfilDTO> encontrarTodos();

    PerfilDTO buscarPorId(Integer id);

    PerfilDTO crear(PerfilDTO usuario);

    void seguirUsuario(Integer usuarioId, Integer seguidoId);

    void dejarDeSeguirUsuario(Integer usuarioId, Integer seguidoId);

    Boolean estaSiguiendo(Integer usuarioId, Integer seguidoId);

    void guardarMeGusta(PerfilDTO usuario, MovieDTO movie);

    boolean haDadoLike(PerfilDTO usuarioDTO, MovieDTO movieDTO);

    long obtenerCantidadDeLikes(MovieDTO movieDTO);

    List<Movie> obtenerPeliculasFavoritas(Integer usuarioId);

    List<UsuarioMovieDTO> obtenerHistorialDePeliculasVistas(Integer usuarioId);

    boolean estaEnListaVerMasTarde(PerfilDTO usuarioDTO, MovieDTO movieDTO);

    void agregarEnVerMasTarde(PerfilDTO usuarioDTO, MovieDTO movieDTO);

    List<UsuarioMovieDTO> obtenerListaVerMasTarde(Integer usuarioId);

    boolean sonAmigos(Integer usuario1Id, Integer usuario2Id);

    List<PerfilDTO> obtenerAmigos(Integer usuarioId);

}
