package org.twelve.dominio.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.twelve.dominio.MovieRepository;
import org.twelve.dominio.MovieService;
import org.twelve.dominio.entities.Categoria;
import org.twelve.dominio.entities.Movie;
import org.twelve.presentacion.dto.MovieDTO;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.twelve.presentacion.dto.MovieDTO.convertToDTO;
import static org.twelve.presentacion.dto.MovieDTO.convertToEntity;

@Service("movieService")
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<MovieDTO> getAll() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream().map(MovieDTO::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public MovieDTO getById(Integer id) {
        Movie movie = movieRepository.findById(id);
        return convertToDTO(movie);
    }

    @Override
    public MovieDTO create(MovieDTO movieDTO) {
        Movie movie = convertToEntity(movieDTO);
        Movie savedMovie = movieRepository.guardar(movie);
        return convertToDTO(savedMovie);
    }

    @Override
    public List<MovieDTO> searchByTitle(String title) {
        List<Movie> movies = movieRepository.findByTitle(title);
        return movies.stream()
                .map(MovieDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieDTO> getMovieMasVista() {
        List<Movie> mostViewedMovies = movieRepository.findMostViewed();
        return mostViewedMovies.stream().map(MovieDTO::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<MovieDTO> getMovieByValoracion() {
        List<Movie> topRatedMovies = movieRepository.findTopRated();
        return topRatedMovies.stream().map(MovieDTO::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<MovieDTO> getMoviesByCategory(Integer idCategoria) {
        List<Movie> movies = movieRepository.findByCategoriaId(idCategoria);
        return movies.stream().map(MovieDTO::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<MovieDTO> getMoviesByFilter(Integer id, String filter, Function<Integer, List<Movie>> findMoviesById,
                                            Function<Integer, List<Movie>> findMoviesByIdTopRated,
                                            Function<Integer, List<Movie>> findMoviesByIdNewest) {
        List<Movie> movies;

        if (filter != null) {
            switch (filter) {
                case "topRated":
                    movies = findMoviesByIdTopRated.apply(id);
                    break;
                case "newest":
                    movies = findMoviesByIdNewest.apply(id);
                    break;
                default:
                    movies = findMoviesById.apply(id);
                    break;
            }
        } else {
            movies = findMoviesById.apply(id);
        }

        return movies.stream().map(MovieDTO::convertToDTO).collect(Collectors.toList());
    }


    @Override
    public List<MovieDTO> getMovieByAnio() {
        List<Movie> newestMovies = movieRepository.findNewestMovie();
        return newestMovies.stream().map(MovieDTO::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<MovieDTO> getSimilarMovies(Integer movieId) {
        Movie pelicula = movieRepository.findById(movieId);
        if (pelicula == null) {
            throw new IllegalArgumentException("La película con ID " + movieId + " no existe.");
        }

        Set<Categoria> categorias = pelicula.getCategorias();

        List<Movie> similarMovies = movieRepository.findSimilarMovies(movieId, categorias);

        return similarMovies.stream()
                .map(MovieDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieDTO> getMoviesByCategory(Integer idCategoria, String filter) {
        return getMoviesByFilter(
                idCategoria,
                filter,
                movieRepository::findByCategoriaId,
                movieRepository::findByCategoriaIdTopRated,
                movieRepository::findByCategoriaIdNewest
        );
    }

    @Override
    public List<MovieDTO> getMoviesByPais(Integer idPais, String filter) {
        return getMoviesByFilter(
                idPais,
                filter,
                movieRepository::findByPaisId,
                movieRepository::findByPaisIdTopRated,
                movieRepository::findByPaisIdNewest
        );
    }


    @Override
    public List<MovieDTO> getUpcomingMovies() {
        List<Movie> upcomingMovies = movieRepository.findUpcomingMovies();

        return upcomingMovies.stream()
                .map(movie -> {
                    MovieDTO movieDTO = MovieDTO.convertToDTO(movie);
                    if (movie.getFechaLanzamiento() != null) {
                        long diasParaEstreno = ChronoUnit.DAYS.between(LocalDate.now(), movie.getFechaLanzamiento());
                        movieDTO.setDiasParaEstreno((int) diasParaEstreno);
                    }
                    return movieDTO;
                })
                .collect(Collectors.toList());
    }


    @Override
    public Boolean isMovieReleased(MovieDTO movie) {
        LocalDate today = LocalDate.now();
        return movie.getFechaLanzamiento().isBefore(today) || movie.getFechaLanzamiento().isEqual(today);
    }

    @Override
    public List<MovieDTO> getUpcomingMoviesByCategory(Integer idCategoria) {
        List<Movie> upcomingMovies = movieRepository.findUpcomingMoviesByCategoria(idCategoria);

        return upcomingMovies.stream()
                .map(movie -> {
                    MovieDTO movieDTO = MovieDTO.convertToDTO(movie);
                    if (movie.getFechaLanzamiento() != null) {
                        long diasParaEstreno = ChronoUnit.DAYS.between(LocalDate.now(), movie.getFechaLanzamiento());
                        movieDTO.setDiasParaEstreno((int) diasParaEstreno);
                    }
                    return movieDTO;
                })
                .collect(Collectors.toList());
    }
}

