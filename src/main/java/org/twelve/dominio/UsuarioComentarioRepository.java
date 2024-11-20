package org.twelve.dominio;

import org.twelve.dominio.entities.UsuarioComentario;

public interface UsuarioComentarioRepository {

    boolean existsByComentarioAndUsuario(Integer idComentario, Integer idUsuario);

    void save(UsuarioComentario comentarioLike);
}
