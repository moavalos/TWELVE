package org.twelve.dominio.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.twelve.dominio.MovieRepository;
import org.twelve.dominio.MovieService;
import org.twelve.dominio.entities.Movie;

import javax.transaction.Transactional;
import java.util.List;

@Service("movieService")
@Transactional
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<Movie> getAll() {
        return movieRepository.findAll();
    }

    @Override
    public Movie getById(Integer id) {
        return movieRepository.findById(id);
    }

    @Override
    public Movie create(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public Movie searchByTitle(String title) {
        List<Movie> movies = movieRepository.findByTitle(title);
        return movies.isEmpty() ? null : movies.get(0);  // devuelvo la primera coincidencia
    }

    @Override
    public List<Movie> getMovieMasVista() {
        return movieRepository.findMostViewed();
    }

    @Override
    public List<Movie> getMovieByValoracion() {
        return movieRepository.findTopRated();
    }
}

