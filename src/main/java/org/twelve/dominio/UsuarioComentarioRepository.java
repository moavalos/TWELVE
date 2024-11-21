package org.twelve.dominio;

import org.twelve.dominio.entities.UsuarioComentario;

import java.util.List;

public interface UsuarioComentarioRepository {

    boolean existsByComentarioAndUsuario(Integer idComentario, Integer idUsuario);

    void save(UsuarioComentario comentarioLike);

    UsuarioComentario findByComentarioAndUsuario(Integer idComentario, Integer idUsuario);

    void delete(UsuarioComentario comentarioLike);

    List<Integer> findComentarioIdsByUsuarioId(Integer idUsuario);

}
