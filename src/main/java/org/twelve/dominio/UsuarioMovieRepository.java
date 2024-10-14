package org.twelve.dominio;

import org.twelve.dominio.entities.Movie;

import java.util.List;

public interface UsuarioMovieRepository {

    Integer obtenerCantidadPeliculasVistas(Integer usuarioId);

    Integer obtenerCantidadPeliculasVistasEsteAno(Integer usuarioId);

    List<Movie> obtenerPeliculasFavoritas(Integer usuarioId);
}
