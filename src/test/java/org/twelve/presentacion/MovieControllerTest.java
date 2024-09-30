package org.twelve.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.CategoriaService;
import org.twelve.dominio.MovieService;
import org.twelve.dominio.entities.Movie;
import org.twelve.presentacion.dto.CategoriaDTO;
import org.twelve.presentacion.dto.MovieDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MovieControllerTest {


    private MovieController movieController;
    private Movie movie;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private MovieService movieService;
    private CategoriaService categoriaService;


    @BeforeEach
    public void init() {
        movie = mock(Movie.class);
        when(movie.getDescripcion()).thenReturn("Coraline");
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        movieService = mock(MovieService.class);
        categoriaService = mock(CategoriaService.class);
        movieController = new MovieController(movieService, categoriaService);
    }

    // Todo casos

    @Test
    public void testObtenerVistaDeTodasLasPeliculasConFiltroDeMejorValoradas() {
        List<MovieDTO> topRatedMoviesMock = Arrays.asList(mock(MovieDTO.class), mock(MovieDTO.class));
        List<CategoriaDTO> categoriasMock = Arrays.asList(mock(CategoriaDTO.class));

        when(movieService.getMovieByValoracion()).thenReturn(topRatedMoviesMock);
        when(categoriaService.getAll()).thenReturn(categoriasMock);

        ModelAndView modelAndView = movieController.getAllMoviesView(null, "topRated");

        assertThat(modelAndView.getViewName(), is("movies"));
        assertThat(((List<MovieDTO>) modelAndView.getModel().get("movies")).size(), is(2));
        assertThat(((List<CategoriaDTO>) modelAndView.getModel().get("categorias")).size(), is(1));
        assertThat(modelAndView.getModel().get("selectedFilter"), is("topRated"));
    }

    @Test
    public void testObtenerVistaDeTodasLasPeliculasConFiltroDeMasRecientes() {
        List<MovieDTO> newestMoviesMock = Arrays.asList(mock(MovieDTO.class), mock(MovieDTO.class));
        List<CategoriaDTO> categoriasMock = Arrays.asList(mock(CategoriaDTO.class));

        when(movieService.getMovieByAnio()).thenReturn(newestMoviesMock);
        when(categoriaService.getAll()).thenReturn(categoriasMock);

        ModelAndView modelAndView = movieController.getAllMoviesView(null, "newest");

        assertThat(modelAndView.getViewName(), is("movies"));
        assertThat(((List<MovieDTO>) modelAndView.getModel().get("movies")).size(), is(2));
        assertThat(((List<CategoriaDTO>) modelAndView.getModel().get("categorias")).size(), is(1));
        assertThat(modelAndView.getModel().get("selectedFilter"), is("newest"));
    }

    @Test
    public void testObtenerVistaDeTodasLasPeliculasConFiltroDeCategoria() {
        List<MovieDTO> categoryMoviesMock = Arrays.asList(mock(MovieDTO.class));
        List<CategoriaDTO> categoriasMock = Arrays.asList(mock(CategoriaDTO.class));

        when(movieService.getMoviesByCategory(1)).thenReturn(categoryMoviesMock);
        when(categoriaService.getAll()).thenReturn(categoriasMock);

        ModelAndView modelAndView = movieController.getAllMoviesView(1, null);

        assertThat(modelAndView.getViewName(), is("movies"));
        assertThat(((List<MovieDTO>) modelAndView.getModel().get("movies")).size(), is(1));
        assertThat(((List<CategoriaDTO>) modelAndView.getModel().get("categorias")).size(), is(1));
        assertNull(modelAndView.getModel().get("selectedFilter"));
    }

    @Test
    public void testObtenerVistaDeTodasLasPeliculasSinFiltroYSinCategoria() {
        List<MovieDTO> moviesMock = Arrays.asList(mock(MovieDTO.class), mock(MovieDTO.class));
        List<CategoriaDTO> categoriasMock = Arrays.asList(mock(CategoriaDTO.class));

        when(movieService.getAll()).thenReturn(moviesMock);
        when(categoriaService.getAll()).thenReturn(categoriasMock);

        ModelAndView modelAndView = movieController.getAllMoviesView(null, null);

        assertThat(modelAndView.getViewName(), is("movies"));
        assertThat(((List<MovieDTO>) modelAndView.getModel().get("movies")).size(), is(2));
        assertThat(((List<CategoriaDTO>) modelAndView.getModel().get("categorias")).size(), is(1));
        assertNull(modelAndView.getModel().get("selectedFilter"));
    }

    @Test
    public void testObtenerVistaDeTodasLasPeliculasSinCategoriaYSinFiltro() {
        List<MovieDTO> moviesMock = Arrays.asList(mock(MovieDTO.class), mock(MovieDTO.class));
        List<CategoriaDTO> categoriasMock = Arrays.asList(mock(CategoriaDTO.class));

        when(movieService.getAll()).thenReturn(moviesMock);
        when(categoriaService.getAll()).thenReturn(categoriasMock);

        ModelAndView modelAndView = movieController.getAllMoviesView(null, null);

        assertThat(modelAndView.getViewName(), is("movies"));
        assertThat(((List<MovieDTO>) modelAndView.getModel().get("movies")).size(), is(2));
        assertThat(((List<CategoriaDTO>) modelAndView.getModel().get("categorias")).size(), is(1));
    }

    @Test
    public void testObtenerPelículasPorCategoria() {
        List<MovieDTO> moviesMock = Arrays.asList(mock(MovieDTO.class));
        when(movieService.getMoviesByCategory(1)).thenReturn(moviesMock);

        ResponseEntity<List<MovieDTO>> response = movieController.getMoviesByCategory(1);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertThat(response.getBody().size(), is(1));
    }

    @Test
    public void testBuscarPeliculas_PeliculaEncontrada() {
        List<MovieDTO> movieListMock = Arrays.asList(mock(MovieDTO.class));
        when(movieService.searchByTitle("Coraline")).thenReturn(movieListMock);

        ModelAndView modelAndView = movieController.searchMovies("Coraline");

        assertEquals("search-results", modelAndView.getViewName());

        assertNotNull(modelAndView.getModel().get("movies"));
        assertEquals(movieListMock, modelAndView.getModel().get("movies"));
    }

    @Test
    public void testBuscarPeliculas_PeliculaNoEncontrada() {
        when(movieService.searchByTitle("NonExistentMovie")).thenReturn(Collections.emptyList());

        ModelAndView modelAndView = movieController.searchMovies("NonExistentMovie");

        assertEquals("search-results", modelAndView.getViewName());

        assertNotNull(modelAndView.getModel().get("message"));
        assertEquals("No se encontraron películas con el título proporcionado.", modelAndView.getModel().get("message"));
    }

    @Test
    public void testObtenerPeliculasMasVistas_PeliculasEncontradas() {
        List<MovieDTO> moviesMock = Arrays.asList(mock(MovieDTO.class), mock(MovieDTO.class));
        when(movieService.getMovieMasVista()).thenReturn(moviesMock);

        ResponseEntity<List<MovieDTO>> response = movieController.getMostViewedMovies();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testObtenerPeliculasMasVistas_NoSeEncontraronPeliculas() {
        List<MovieDTO> emptyMoviesList = new ArrayList<>();
        when(movieService.getMovieMasVista()).thenReturn(emptyMoviesList);

        ResponseEntity<List<MovieDTO>> response = movieController.getMostViewedMovies();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    public void testObtenerPeliculaPorId_IdValido() {
        MovieDTO movieMock = mock(MovieDTO.class);
        when(movieService.getById(1)).thenReturn(movieMock);

        ResponseEntity<MovieDTO> response = movieController.getMovieById(1);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    public void testObtenerPeliculaPorId_IdInvalido() {
        when(movieService.getById(99)).thenReturn(null);

        ResponseEntity<MovieDTO> response = movieController.getMovieById(99);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testAgregarPelicula() {
        MovieDTO movieMock = mock(MovieDTO.class);
        when(movieService.create(any(MovieDTO.class))).thenReturn(movieMock);

        ResponseEntity<MovieDTO> response = movieController.addMovie(movieMock);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    public void testActualizarPelicula_IdValido() {
        MovieDTO movieMock = mock(MovieDTO.class);
        when(movieService.getById(1)).thenReturn(movieMock);
        when(movieService.create(any(MovieDTO.class))).thenReturn(movieMock);

        ResponseEntity<MovieDTO> response = movieController.updateMovie(1, movieMock);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    public void testActualizarPelicula_IdInvalido() {
        MovieDTO movieMock = mock(MovieDTO.class);
        when(movieService.getById(99)).thenReturn(null);

        ResponseEntity<MovieDTO> response = movieController.updateMovie(99, movieMock);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testObtenerPeliculasPorCategoria_NoSeEncontraronPeliculas() {
        List<MovieDTO> emptyMoviesList = new ArrayList<>();
        when(movieService.getMoviesByCategory(1)).thenReturn(emptyMoviesList);

        ResponseEntity<List<MovieDTO>> response = movieController.getMoviesByCategory(1);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    public void testObtenerPeliculasPorCategoria_ListaDePeliculasNula() {
        when(movieService.getMoviesByCategory(1)).thenReturn(null);

        ResponseEntity<List<MovieDTO>> response = movieController.getMoviesByCategory(1);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    public void testObtenerLas4PeliculasMejorValoradasEnVistaDeInicio() {
        //preparacion
        List<MovieDTO> topRatedMoviesMock = Arrays.asList(mock(MovieDTO.class), mock(MovieDTO.class), mock(MovieDTO.class), mock(MovieDTO.class));
        when(movieService.getMovieByValoracion()).thenReturn(topRatedMoviesMock);

        //ejecucion
        ModelAndView modelAndView = movieController.getTopRatedMovies();

        //validacion

        assertThat(modelAndView.getViewName(), is("home"));

        assertThat(((List<MovieDTO>) modelAndView.getModel().get("movies")).size(), is(4));
    }


}
