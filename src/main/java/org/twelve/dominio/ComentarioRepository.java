package org.twelve.dominio;

import org.twelve.dominio.entities.Comentario;

import java.util.List;
import java.util.Optional;

public interface ComentarioRepository {


    void save(Comentario comentario);

    List<Comentario> findByIdMovie(Integer idMovie);

    List<Comentario> findTop3ByUsuarioId(Integer idUsuario);

    Optional<Comentario> findById(Integer id);

    List<Comentario> obtener3ComentariosConMasLikes();
}
