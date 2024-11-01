package org.twelve.dominio;

import org.twelve.dominio.entities.Movie;

import java.util.List;

public interface MovieRepository {

    List<Movie> findAll();

    Movie findById(Integer id);

    Movie guardar(Movie movie);

    Movie actualizar(Movie movie);

    List<Movie> findByTitle(String title);

    List<Movie> findMostViewed();

    List<Movie> findTopRated();

    List<Movie> findByCategoriaId(Integer idCategoria);

    List<Movie> findByCategoriaIdTopRated(Integer idCategoria);

    List<Movie> findByCategoriaIdNewest(Integer idCategoria);

    List<Movie> findNewestMovie();
}
