package org.twelve.servicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.twelve.dominio.MovieRepository;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.serviceImpl.MovieServiceImpl;
import org.twelve.presentacion.dto.MovieDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class MovieServiceImplTest {

    private Movie movie1;
    private Movie movie2;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private MovieServiceImpl movieServiceImpl;
    private MovieRepository movieRepository;

    @BeforeEach
    public void init() {
        movie1 = mock(Movie.class);
        movie2 = mock(Movie.class);
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        movieRepository = mock(MovieRepository.class);
        movieServiceImpl = new MovieServiceImpl(movieRepository);
    }

    @Test
    public void testGetAll_CuandoHayPeliculas_DeberiaRetornarListaDeMovieDTO() {
        when(movie1.getNombre()).thenReturn("Matrix");
        when(movie2.getNombre()).thenReturn("Inception");

        when(movieRepository.findAll()).thenReturn(Arrays.asList(movie1, movie2));

        List<MovieDTO> result = movieServiceImpl.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Matrix", result.get(0).getNombre());
        assertEquals("Inception", result.get(1).getNombre());

        verify(movieRepository, times(1)).findAll();
    }

    @Test
    public void testGetById_CuandoPeliculaExiste_DeberiaRetornarMovieDTO() {
        when(movie1.getNombre()).thenReturn("Matrix");
        when(movie1.getDescripcion()).thenReturn("A hacker discovers...");

        when(movieRepository.findById(1)).thenReturn(movie1);

        MovieDTO result = movieServiceImpl.getById(1);

        assertNotNull(result);
        assertEquals("Matrix", result.getNombre());
        assertEquals("A hacker discovers...", result.getDescripcion());

        verify(movieRepository, times(1)).findById(1);
    }

    @Test
    public void testCreate_CuandoSeCreaPelicula_DeberiaRetornarMovieDTO() {
        when(movie1.getNombre()).thenReturn("Matrix");
        when(movie1.getDuracion()).thenReturn(136.8);

        MovieDTO movieDTO = new MovieDTO(1, "Matrix", "A hacker discovers...", "Welcome to the real world",
                136.8, "USA", 5000, 1, "1999", "matrix.jpg", 3000, 9.0);

        when(movieRepository.save(any(Movie.class))).thenReturn(movie1);

        MovieDTO result = movieServiceImpl.create(movieDTO);

        assertNotNull(result);
        assertEquals("Matrix", result.getNombre());
        assertEquals(136.8, result.getDuracion());

        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    public void testSearchByTitle_CuandoTituloExiste_DeberiaRetornarListaDeMovieDTO() {
        when(movie1.getNombre()).thenReturn("Matrix");

        when(movieRepository.findByTitle("Matrix")).thenReturn(Arrays.asList(movie1));

        List<MovieDTO> result = movieServiceImpl.searchByTitle("Matrix");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Matrix", result.get(0).getNombre());

        verify(movieRepository, times(1)).findByTitle("Matrix");
    }

    @Test
    public void testGetMovieMasVista_DeberiaRetornarListaDeMovieDTO() {
        when(movie1.getCantVistas()).thenReturn(5000);
        when(movie2.getCantVistas()).thenReturn(6000);

        when(movieRepository.findMostViewed()).thenReturn(Arrays.asList(movie1, movie2));

        List<MovieDTO> result = movieServiceImpl.getMovieMasVista();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(5000, result.get(0).getCantVistas());
        assertEquals(6000, result.get(1).getCantVistas());

        verify(movieRepository, times(1)).findMostViewed();
    }

    @Test
    public void testGetMovieByValoracion_DeberiaRetornarListaDeMovieDTO() {
        when(movie1.getValoracion()).thenReturn(9.0);
        when(movie2.getValoracion()).thenReturn(8.8);

        when(movieRepository.findTopRated()).thenReturn(Arrays.asList(movie1, movie2));

        List<MovieDTO> result = movieServiceImpl.getMovieByValoracion();

        assertNotNull(result);
        assertEquals(9.0, result.get(0).getValoracion());
        assertEquals(8.8, result.get(1).getValoracion());

        verify(movieRepository, times(1)).findTopRated();
    }

    @Test
    public void testGetMoviesByCategory_CuandoCategoriaExiste_DeberiaRetornarListaDeMovieDTO() {
        when(movie1.getIdCategoria()).thenReturn(1);
        when(movie2.getIdCategoria()).thenReturn(1);

        when(movieRepository.findByCategoriaId(1)).thenReturn(Arrays.asList(movie1, movie2));

        List<MovieDTO> result = movieServiceImpl.getMoviesByCategory(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getIdCategoria());
        assertEquals(1, result.get(1).getIdCategoria());

        verify(movieRepository, times(1)).findByCategoriaId(1);
    }

    @Test
    public void testGetMovieByAnio_DeberiaRetornarListaDeMovieDTO() {
        when(movie2.getAñoLanzamiento()).thenReturn("2010");

        when(movieRepository.findNewestMovie()).thenReturn(Arrays.asList(movie2));

        List<MovieDTO> result = movieServiceImpl.getMovieByAnio();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("2010", result.get(0).getAnioLanzamiento());

        verify(movieRepository, times(1)).findNewestMovie();
    }
}
