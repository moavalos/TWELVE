package org.twelve.dominio.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.twelve.dominio.MovieRepository;
import org.twelve.dominio.MovieService;
import org.twelve.dominio.entities.Movie;
import org.twelve.presentacion.dto.MovieDTO;

import java.util.List;
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
    public List<MovieDTO> getMoviesByCategory(Integer idCategoria, String filter) {
        List<Movie> movies;

        if (filter != null) {
            switch (filter) {
                case "topRated":
                    movies = movieRepository.findByCategoriaIdTopRated(idCategoria);
                    break;
                case "newest":
                    movies = movieRepository.findByCategoriaIdNewest(idCategoria);
                    break;
                default:
                    movies = movieRepository.findByCategoriaId(idCategoria);
                    break;
            }
        } else {
            movies = movieRepository.findByCategoriaId(idCategoria);
        }

        return movies.stream().map(MovieDTO::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<MovieDTO> getMovieByAnio() {
        List<Movie> newestMovies = movieRepository.findNewestMovie();
        return newestMovies.stream().map(MovieDTO::convertToDTO).collect(Collectors.toList());
    }
}

