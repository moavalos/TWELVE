package org.twelve.dominio;

import org.twelve.dominio.entities.Movie;
import org.twelve.presentacion.dto.MovieDTO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.function.Function;

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

    List<MovieDTO> getMoviesByFilter(Integer id, String filter, Function<Integer, List<Movie>> findMoviesById,
                                     Function<Integer, List<Movie>> findMoviesByIdTopRated,
                                     Function<Integer, List<Movie>> findMoviesByIdNewest);

    List<MovieDTO> getMovieByAnio();

    List<MovieDTO> getSimilarMovies(Integer movieId);

    List<MovieDTO> getMoviesByPais(Integer idPais, String filter);

    List<MovieDTO> getUpcomingMovies();

    Boolean isMovieReleased(MovieDTO movie);

    List<MovieDTO> getUpcomingMoviesByCategory(Integer idCategoria);
}
