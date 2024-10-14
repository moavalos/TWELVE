package org.twelve.dominio;

import org.twelve.dominio.entities.Comentario;
import org.twelve.presentacion.dto.ComentarioDTO;

import java.util.List;

public interface ComentarioService {

    void agregarComentario(ComentarioDTO comentarioDTO);

    List<ComentarioDTO> obtenerComentariosPorPelicula(Integer idMovie);

    Comentario convertToEntity(ComentarioDTO comentarioDTO);

}
