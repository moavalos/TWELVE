package org.twelve.dominio;

import org.twelve.dominio.entities.Comentario;
import org.twelve.dominio.entities.Movie;

import java.util.List;

public interface MovieRepository {

    List<Movie> findAll();

    Movie findById(Integer id);

    Movie save(Movie movie);

    List<Movie> findByTitle(String title);

    List<Movie> findMostViewed();

    List<Movie> findTopRated();

    List<Movie> findByCategoriaId(Integer idCategoria);

    List<Movie> findNewestMovie();

    
    //para buscara el comentario por peli
    List<Comentario> findComentariosByMovieId(Integer idMovie);
}
