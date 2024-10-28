package org.twelve.dominio;

import org.twelve.dominio.entities.Movie;
import org.twelve.presentacion.dto.ComentarioDTO;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ComentarioService {

    void agregarComentario(ComentarioDTO comentarioDTO);

    List<ComentarioDTO> obtenerComentariosPorPelicula(Integer idMovie);

    void actualizarValoracionPelicula(Movie movie);
}
