package org.twelve.dominio.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.twelve.dominio.MovieRepository;
import org.twelve.dominio.MovieService;
import org.twelve.dominio.entities.Movie;
import org.twelve.presentacion.dto.MovieDTO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service("movieService")
@Transactional
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<MovieDTO> getAll() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public MovieDTO getById(Integer id) {
        Movie movie = movieRepository.findById(id);
        return convertToDTO(movie);
    }

    @Override
    public MovieDTO create(MovieDTO movieDTO) {
        Movie movie = convertToEntity(movieDTO);
        Movie savedMovie = movieRepository.save(movie);
        return convertToDTO(savedMovie);
    }

    @Override
    public MovieDTO searchByTitle(String title) {
        List<Movie> movies = movieRepository.findByTitle(title);
        return movies.isEmpty() ? null : convertToDTO(movies.get(0)); // devuelvo la primera coincidencia
    }

    @Override
    public List<MovieDTO> getMovieMasVista() {
        List<Movie> mostViewedMovies = movieRepository.findMostViewed();
        return mostViewedMovies.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<MovieDTO> getMovieByValoracion() {
        List<Movie> topRatedMovies = movieRepository.findTopRated();
        return topRatedMovies.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<MovieDTO> getMovieByAnio() {
        List<Movie> newestMovies = movieRepository.findNewestMovie();
        return newestMovies.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    // dto a entidad en
    private Movie convertToEntity(MovieDTO movieDTO) {
        Movie movie = new Movie();
        movie.setNombre(movieDTO.getNombre());
        movie.setDescripcion(movieDTO.getDescripcion());
        movie.setFrase(movieDTO.getFrase());
        movie.setDuracion(movieDTO.getDuracion());
        movie.setPais(movieDTO.getPais());
        movie.setCantVistas(movieDTO.getCantVistas());
        movie.setAñoLanzamiento(movieDTO.getAnioLanzamiento());
        movie.setImagen(movieDTO.getImagen());
        movie.setLikes(movieDTO.getLikes());
        movie.setValoracion(movieDTO.getValoracion());
        return movie;
    }

    // entidad a DTO
    private MovieDTO convertToDTO(Movie movie) {
        return new MovieDTO(
                movie.getNombre(),
                movie.getDescripcion(),
                movie.getFrase(),
                movie.getDuracion(),
                movie.getPais(),
                movie.getCantVistas(),
                movie.getAñoLanzamiento(),
                movie.getImagen(),
                movie.getLikes(),
                movie.getValoracion()
        );
    }
}

