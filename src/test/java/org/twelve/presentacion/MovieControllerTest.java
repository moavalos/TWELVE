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
    public void testGetAllMoviesViewWithTopRatedFilter() {
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
    public void testGetAllMoviesViewWithNewestFilter() {
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
    public void testGetAllMoviesViewWithCategoryFilter() {
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
    public void testGetAllMoviesViewWithoutFilterAndWithoutCategory() {
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
    public void testGetAllMoviesViewWithoutCategoryAndWithoutFilter() {
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
    public void testGetMoviesByCategory() {
        List<MovieDTO> moviesMock = Arrays.asList(mock(MovieDTO.class));
        when(movieService.getMoviesByCategory(1)).thenReturn(moviesMock);

        ResponseEntity<List<MovieDTO>> response = movieController.getMoviesByCategory(1);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertThat(response.getBody().size(), is(1));
    }

    @Test
    public void testSearchMovies_MovieFound() {
        MovieDTO movieMock = mock(MovieDTO.class);
        when(movieService.searchByTitle("Coraline")).thenReturn(movieMock);

        ResponseEntity<MovieDTO> response = movieController.searchMovies("Coraline");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(movieMock, response.getBody());
    }

    @Test
    public void testSearchMovies_MovieNotFound() {
        when(movieService.searchByTitle("NonExistentMovie")).thenReturn(null);

        ResponseEntity<MovieDTO> response = movieController.searchMovies("NonExistentMovie");

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    public void testGetMostViewedMovies_MoviesFound() {
        List<MovieDTO> moviesMock = Arrays.asList(mock(MovieDTO.class), mock(MovieDTO.class));
        when(movieService.getMovieMasVista()).thenReturn(moviesMock);

        ResponseEntity<List<MovieDTO>> response = movieController.getMostViewedMovies();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetMostViewedMovies_NoMoviesFound() {
        List<MovieDTO> emptyMoviesList = new ArrayList<>();
        when(movieService.getMovieMasVista()).thenReturn(emptyMoviesList);

        ResponseEntity<List<MovieDTO>> response = movieController.getMostViewedMovies();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    public void testGetMovieById_ValidId() {
        MovieDTO movieMock = mock(MovieDTO.class);
        when(movieService.getById(1)).thenReturn(movieMock);

        ResponseEntity<MovieDTO> response = movieController.getMovieById(1);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetMovieById_InvalidId() {
        when(movieService.getById(99)).thenReturn(null);

        ResponseEntity<MovieDTO> response = movieController.getMovieById(99);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testAddMovie() {
        MovieDTO movieMock = mock(MovieDTO.class);
        when(movieService.create(any(MovieDTO.class))).thenReturn(movieMock);

        ResponseEntity<MovieDTO> response = movieController.addMovie(movieMock);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    public void testUpdateMovie_ValidId() {
        MovieDTO movieMock = mock(MovieDTO.class);
        when(movieService.getById(1)).thenReturn(movieMock);
        when(movieService.create(any(MovieDTO.class))).thenReturn(movieMock);

        ResponseEntity<MovieDTO> response = movieController.updateMovie(1, movieMock);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    public void testUpdateMovie_InvalidId() {
        MovieDTO movieMock = mock(MovieDTO.class);
        when(movieService.getById(99)).thenReturn(null);

        ResponseEntity<MovieDTO> response = movieController.updateMovie(99, movieMock);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testGetMoviesByCategory_NoMoviesFound() {
        List<MovieDTO> emptyMoviesList = new ArrayList<>();
        when(movieService.getMoviesByCategory(1)).thenReturn(emptyMoviesList);

        ResponseEntity<List<MovieDTO>> response = movieController.getMoviesByCategory(1);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    public void testGetMoviesByCategory_NullMoviesList() {
        when(movieService.getMoviesByCategory(1)).thenReturn(null);

        ResponseEntity<List<MovieDTO>> response = movieController.getMoviesByCategory(1);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}
