package org.twelve.dominio;


import org.twelve.dominio.entities.Comentario;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.Usuario;
import org.twelve.dominio.entities.UsuarioComentario;

import java.util.List;
import java.util.Optional;

public interface UsuarioComentarioRepository {


    long obtenerCantidadDelikes(Comentario comentario);

    Optional<UsuarioComentario> buscarMeGustaPorUsuario(Usuario usuario, Comentario comentario);

    void guardar(UsuarioComentario usuarioComentario);

    void borrarMeGusta(UsuarioComentario usuarioComentario);

    List<Comentario> obtenerComentariosLikeados(Integer usuarioId);

    boolean obtenerUsuarioYComentarioLikeado(Usuario usuario, Comentario comentario);
}