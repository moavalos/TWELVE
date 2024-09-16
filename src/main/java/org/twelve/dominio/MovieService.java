package org.twelve.dominio;

import org.twelve.presentacion.dto.MovieDTO;

import java.util.List;

public interface MovieService {

    List<MovieDTO> getAll();

    MovieDTO getById(Integer id);

    MovieDTO create(MovieDTO movie);

    //Movie update(Movie movie);

    MovieDTO searchByTitle(String title);

    List<MovieDTO> getMovieMasVista();

    List<MovieDTO> getMovieByValoracion(); // mejor valoradas

    List<MovieDTO> getMoviesByCategory(Integer idCategoria);

    List<MovieDTO> getMovieByAnio();
}
