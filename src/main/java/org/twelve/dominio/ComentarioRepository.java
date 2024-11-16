package org.twelve.dominio;

import org.twelve.dominio.entities.Comentario;

import java.util.List;

public interface ComentarioRepository {


    Comentario findById(Integer id);

    void save(Comentario comentario);

    List<Comentario> findByIdMovie(Integer idMovie);

    List<Comentario> findTop3ByUsuarioId(Integer idUsuario);

    Comentario actualizar(Comentario comentario);
}
