package org.twelve.dominio.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.twelve.dominio.MovieRepository;
import org.twelve.dominio.MovieService;
import org.twelve.dominio.entities.Categoria;
import org.twelve.dominio.entities.Movie;
import org.twelve.presentacion.dto.CategoriaDTO;
import org.twelve.presentacion.dto.MovieDTO;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public List<MovieDTO> searchByTitle(String title) {
        List<Movie> movies = movieRepository.findByTitle(title);
        return movies.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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
    public List<MovieDTO> getMoviesByCategory(Integer idCategoria) {
        List<Movie> movies = movieRepository.findByCategoriaId(idCategoria);
        return movies.stream().map(this::convertToDTO).collect(Collectors.toList());
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

        return movies.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    @Override
    public List<MovieDTO> getMovieByAnio() {
        List<Movie> newestMovies = movieRepository.findNewestMovie();
        return newestMovies.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // dto a entidad en
    private Movie convertToEntity(MovieDTO movieDTO) {
        Movie movie = new Movie();
        movie.setId(movieDTO.getId());
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
        movie.setDirector(movieDTO.getDirector());
        movie.setEscritor(movieDTO.getEscritor());
        movie.setIdioma(movieDTO.getIdioma());
        movie.setTambienConocidaComo(movieDTO.getTambienConocidaComo());


        if (movieDTO.getCategorias() != null) {
            Set<Categoria> categorias = new HashSet<>();
            for (CategoriaDTO categoriaDTO : movieDTO.getCategorias()) {
                Categoria categoria = new Categoria();
                categoria.setId(categoriaDTO.getId());
                categoria.setNombre(categoriaDTO.getNombre());
                categorias.add(categoria);
            }
            movie.setCategorias(categorias);
        }

        return movie;
    }

    private MovieDTO convertToDTO(Movie movie) {
        List<CategoriaDTO> categoriasDTOs = new ArrayList<>();
        for (Categoria categoria : movie.getCategorias()) {
            CategoriaDTO categoriaDTO = new CategoriaDTO();
            categoriaDTO.setId(categoria.getId());
            categoriaDTO.setNombre(categoria.getNombre());
            categoriasDTOs.add(categoriaDTO);
        }

        return new MovieDTO(
                movie.getId(),
                movie.getNombre(),
                movie.getDescripcion(),
                movie.getFrase(),
                movie.getDuracion(),
                movie.getPais(),
                movie.getCantVistas(),
                categoriasDTOs,
                movie.getAñoLanzamiento(),
                movie.getImagen(),
                movie.getLikes(),
                movie.getValoracion(),
                movie.getDirector(),
                movie.getEscritor(),
                movie.getIdioma(),
                movie.getTambienConocidaComo()
        );
    }

}

