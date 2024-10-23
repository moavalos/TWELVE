package org.twelve.dominio;

import org.twelve.presentacion.dto.MovieDTO;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface MovieService {

    List<MovieDTO> getAll();

    MovieDTO getById(Integer id);

    MovieDTO create(MovieDTO movie);

    //Movie update(Movie movie);

    List<MovieDTO> searchByTitle(String title);

    List<MovieDTO> getMovieMasVista();

    List<MovieDTO> getMovieByValoracion(); // mejor valoradas

    List<MovieDTO> getMoviesByCategory(Integer idCategoria);

    List<MovieDTO> getMoviesByCategory(Integer idCategoria, String filter);

    List<MovieDTO> getMovieByAnio();

}
