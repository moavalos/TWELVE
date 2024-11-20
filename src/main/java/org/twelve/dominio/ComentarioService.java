package org.twelve.dominio;

import org.twelve.dominio.entities.Movie;
import org.twelve.presentacion.dto.ComentarioDTO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Transactional
public interface ComentarioService {

    void agregarComentario(ComentarioDTO comentarioDTO);

    List<ComentarioDTO> obtenerComentariosPorPelicula(Integer idMovie);

    List<ComentarioDTO> obtenerUltimosTresComentarios(Integer idUsuario);

    void actualizarValoracionPelicula(Movie movie);

    void darMeGustaComentario(Integer idComentario, Integer idUsuario);

    void quitarMeGustaComentario(Integer idComentario, Integer idUsuario);

    Set<Integer> obtenerLikesPorUsuario(Integer idUsuario);
}
