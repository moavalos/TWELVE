package org.twelve.dominio;

import org.twelve.dominio.entities.Movie;

import java.util.List;

public interface MovieService {

    List<Movie> getAll();

    Movie getById(Integer id);

    Movie create(Movie movie);

    //Movie update(Movie movie);

    Movie searchByTitle(String title);

    List<Movie> getMovieMasVista();

    List<Movie> getMovieByValoracion(); // mejor valoradas


}
