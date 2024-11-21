package org.twelve.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.*;
import org.twelve.dominio.entities.Movie;
import org.twelve.presentacion.dto.*;

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
import static org.mockito.Mockito.*;

public class MovieControllerTest {

    private MovieController movieController;
    private Movie movie;
    private MovieDTO movieDTO;
    private PerfilDTO perfilDTOMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private MovieService movieService;
    private CategoriaService categoriaService;
    private ComentarioService comentarioService;
    private UsuarioService usuarioService;
    private PaisService paisService;
    private ListaColaborativaService listaColaborativaService;

    @BeforeEach
    public void init() {
        movie = mock(Movie.class);
        when(movie.getDescripcion()).thenReturn("Coraline");

        movieDTO = mock(MovieDTO.class);
        perfilDTOMock = mock(PerfilDTO.class);
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        movieService = mock(MovieService.class);
        categoriaService = mock(CategoriaService.class);
        comentarioService = mock(ComentarioService.class);
        usuarioService = mock(UsuarioService.class);
        paisService = mock(PaisService.class);
        listaColaborativaService = mock(ListaColaborativaService.class);
        movieController = new MovieController(movieService, categoriaService, comentarioService, usuarioService, paisService, listaColaborativaService);
    }

    @Test
    public void testObtenerVistaDeTodasLasPeliculasConFiltroDeMejorValoradas() {
        List<MovieDTO> topRatedMoviesMock = Arrays.asList(mock(MovieDTO.class), mock(MovieDTO.class));
        List<CategoriaDTO> categoriasMock = Collections.singletonList(mock(CategoriaDTO.class));

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
        List<CategoriaDTO> categoriasMock = Collections.singletonList(mock(CategoriaDTO.class));

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
        List<MovieDTO> categoryMoviesMock = Collections.singletonList(mock(MovieDTO.class));
        List<CategoriaDTO> categoriasMock = Collections.singletonList(mock(CategoriaDTO.class));

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
        List<CategoriaDTO> categoriasMock = Collections.singletonList(mock(CategoriaDTO.class));

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
        List<CategoriaDTO> categoriasMock = Collections.singletonList(mock(CategoriaDTO.class));

        when(movieService.getAll()).thenReturn(moviesMock);
        when(categoriaService.getAll()).thenReturn(categoriasMock);

        ModelAndView modelAndView = movieController.getAllMoviesView(null, null);

        assertThat(modelAndView.getViewName(), is("movies"));
        assertThat(((List<MovieDTO>) modelAndView.getModel().get("movies")).size(), is(2));
        assertThat(((List<CategoriaDTO>) modelAndView.getModel().get("categorias")).size(), is(1));
    }

    @Test
    public void testObtenerPelículasPorCategoria() {
        List<MovieDTO> moviesMock = Collections.singletonList(mock(MovieDTO.class));
        when(movieService.getMoviesByCategory(1)).thenReturn(moviesMock);

        ResponseEntity<List<MovieDTO>> response = movieController.getMoviesByCategory(1);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertThat(response.getBody().size(), is(1));
    }

    @Test
    public void testBuscarPeliculasPeliculaEncontrada() {
        List<MovieDTO> movieListMock = Collections.singletonList(mock(MovieDTO.class));
        when(movieService.searchByTitle("Coraline")).thenReturn(movieListMock);

        ModelAndView modelAndView = movieController.searchMovies("Coraline");

        assertEquals("search-results", modelAndView.getViewName());

        assertNotNull(modelAndView.getModel().get("movies"));
        assertEquals(movieListMock, modelAndView.getModel().get("movies"));
    }

    @Test
    public void testBuscarPeliculasPeliculaNoEncontrada() {
        when(movieService.searchByTitle("NonExistentMovie")).thenReturn(Collections.emptyList());

        ModelAndView modelAndView = movieController.searchMovies("NonExistentMovie");

        assertEquals("search-results", modelAndView.getViewName());

        assertNotNull(modelAndView.getModel().get("message"));
        assertEquals("No se encontraron películas con el título proporcionado.", modelAndView.getModel().get("message"));
    }

    @Test
    public void testObtenerPeliculasMasVistasPeliculasEncontradas() {
        List<MovieDTO> moviesMock = Arrays.asList(mock(MovieDTO.class), mock(MovieDTO.class));
        when(movieService.getMovieMasVista()).thenReturn(moviesMock);

        ResponseEntity<List<MovieDTO>> response = movieController.getMostViewedMovies();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testObtenerPeliculasMasVistasNoSeEncontraronPeliculas() {
        List<MovieDTO> emptyMoviesList = new ArrayList<>();
        when(movieService.getMovieMasVista()).thenReturn(emptyMoviesList);

        ResponseEntity<List<MovieDTO>> response = movieController.getMostViewedMovies();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    public void testObtenerPeliculaPorIdIdValido() {
        MovieDTO movieMock = mock(MovieDTO.class);
        when(movieService.getById(1)).thenReturn(movieMock);

        ResponseEntity<MovieDTO> response = movieController.getMovieById(1);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    public void testObtenerPeliculaPorIdIdInvalido() {
        when(movieService.getById(99)).thenReturn(null);

        ResponseEntity<MovieDTO> response = movieController.getMovieById(99);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testObtenerPeliculasPorCategoriaNoSeEncontraronPeliculas() {
        List<MovieDTO> emptyMoviesList = new ArrayList<>();
        when(movieService.getMoviesByCategory(1)).thenReturn(emptyMoviesList);

        ResponseEntity<List<MovieDTO>> response = movieController.getMoviesByCategory(1);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    public void testObtenerPeliculasPorCategoriaListaDePeliculasNula() {
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

    @Test
    public void testGetMovieCategoryPageDevuelveVistaDeCategoria() {
        Integer categoriaId = 1;
        String filtro = null;
        List<MovieDTO> mockMovies = Arrays.asList(mock(MovieDTO.class), mock(MovieDTO.class));

        when(movieService.getMoviesByCategory(categoriaId, filtro)).thenReturn(mockMovies);

        ModelAndView modelAndView = movieController.getMovieCategoryPage(categoriaId, filtro);

        assertThat(modelAndView.getViewName(), is("movies-categoria"));
    }

    @Test
    public void testTraerDetallePeliculaPeliculaExistenteYUsuarioHaDadoLike() {
        Integer movieId = 1;
        Integer usuarioId = 1;

        // spy para guardar el estado de likes
        MovieDTO movieMock = spy(new MovieDTO());
        movieMock.setLikes(0);

        List<ComentarioDTO> comentariosMock = Arrays.asList(mock(ComentarioDTO.class), mock(ComentarioDTO.class));
        PerfilDTO usuarioMock = mock(PerfilDTO.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(usuarioId);
        when(movieService.getById(movieId)).thenReturn(movieMock);
        when(usuarioService.obtenerCantidadDeLikes(movieMock)).thenReturn(10L);
        when(comentarioService.obtenerComentariosPorPelicula(movieId)).thenReturn(comentariosMock);
        when(usuarioService.buscarPorId(usuarioId)).thenReturn(usuarioMock);
        when(usuarioService.haDadoLike(usuarioMock, movieMock)).thenReturn(true);

        ModelAndView modelAndView = movieController.traerDetallePelicula(movieId, requestMock);

        assertEquals("detalle-pelicula", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel().get("movie"));
        assertNotNull(modelAndView.getModel().get("comentarios"));
        assertNotNull(modelAndView.getModel().get("usuario"));
        assertEquals(true, modelAndView.getModel().get("haDadoLike"));
        assertEquals(10, movieMock.getLikes());
    }

    @Test
    public void testTraerDetallePeliculaUsuarioNoHaDadoLike() {
        Integer movieId = 1;
        Integer usuarioId = 2;

        MovieDTO movieMock = spy(new MovieDTO());
        movieMock.setLikes(0);

        PerfilDTO usuarioMock = mock(PerfilDTO.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(usuarioId);
        when(movieService.getById(movieId)).thenReturn(movieMock);
        when(usuarioService.obtenerCantidadDeLikes(movieMock)).thenReturn(5L);
        when(usuarioService.buscarPorId(usuarioId)).thenReturn(usuarioMock);
        when(usuarioService.haDadoLike(usuarioMock, movieMock)).thenReturn(false);

        ModelAndView modelAndView = movieController.traerDetallePelicula(movieId, requestMock);

        assertEquals("detalle-pelicula", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel().get("movie"));
        assertNotNull(modelAndView.getModel().get("usuario"));
        assertEquals(false, modelAndView.getModel().get("haDadoLike"));
        assertEquals(5, movieMock.getLikes());
    }


    @Test
    public void testDarMeGustaUsuarioNoEnSesion() {
        Integer movieId = 1;

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(null);

        String redirectUrl = movieController.darMeGusta(movieId, requestMock);

        assertEquals("redirect:/login", redirectUrl);
    }

    @Test
    public void testDarMeGustaPeliculaNoExistente() {
        Integer movieId = 99;
        Integer usuarioId = 1;

        PerfilDTO usuarioMock = mock(PerfilDTO.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(usuarioId);
        when(usuarioService.buscarPorId(usuarioId)).thenReturn(usuarioMock);
        when(movieService.getById(movieId)).thenReturn(null);

        String redirectUrl = movieController.darMeGusta(movieId, requestMock);

        assertEquals("redirect:/detalle-pelicula/" + movieId, redirectUrl);
        verify(usuarioService, never()).guardarMeGusta(any(PerfilDTO.class), any(MovieDTO.class));
    }

    @Test
    public void testDarMeGustaUsuarioYMovieExisten() {
        Integer movieId = 1;
        Integer usuarioId = 1;

        PerfilDTO usuarioMock = mock(PerfilDTO.class);
        MovieDTO movieMock = mock(MovieDTO.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(usuarioId);
        when(usuarioService.buscarPorId(usuarioId)).thenReturn(usuarioMock);
        when(movieService.getById(movieId)).thenReturn(movieMock);

        String redirectUrl = movieController.darMeGusta(movieId, requestMock);

        assertEquals("redirect:/detalle-pelicula/" + movieId, redirectUrl);
        verify(usuarioService, times(1)).guardarMeGusta(usuarioMock, movieMock);
    }

    @Test
    public void testDarMeGustaUsuarioExistenteSinPelicula() {
        Integer movieId = 1;
        Integer usuarioId = 1;

        PerfilDTO usuarioMock = mock(PerfilDTO.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(usuarioId);
        when(usuarioService.buscarPorId(usuarioId)).thenReturn(usuarioMock);
        when(movieService.getById(movieId)).thenReturn(null);

        String redirectUrl = movieController.darMeGusta(movieId, requestMock);

        assertEquals("redirect:/detalle-pelicula/" + movieId, redirectUrl);
        verify(usuarioService, never()).guardarMeGusta(usuarioMock, null);
    }

    @Test
    public void testDarMeGustaSinUsuarioYPeliculaExistente() {
        Integer movieId = 1;

        MovieDTO movieMock = mock(MovieDTO.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(null);
        when(movieService.getById(movieId)).thenReturn(movieMock);

        String redirectUrl = movieController.darMeGusta(movieId, requestMock);

        assertEquals("redirect:/login", redirectUrl);
        verify(usuarioService, never()).guardarMeGusta(any(PerfilDTO.class), any(MovieDTO.class));
    }

    @Test
    public void testGetMoviesByPaisPageDevuelveVistaDePais() {
        Integer paisId = 1;
        String filtro = "accion";
        List<MovieDTO> mockMovies = Arrays.asList(mock(MovieDTO.class), mock(MovieDTO.class));

        when(movieService.getMoviesByPais(paisId, filtro)).thenReturn(mockMovies);

        PaisDTO mockPaisDTO = new PaisDTO(paisId, "España");
        when(paisService.findById(paisId)).thenReturn(mockPaisDTO);

        ModelAndView modelAndView = movieController.getMoviesByPaisPage(paisId, filtro);

        assertThat(modelAndView.getViewName(), is("movies-pais"));
        assertThat(modelAndView.getModel().get("nombrePais"), is("España"));
    }

    @Test
    public void testVerMasTardeUsuarioNoLogueadoRedirigeALogin() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(null);

        String result = movieController.verMasTarde(1, requestMock);

        assertThat(result, is("redirect:/login"));
    }

    @Test
    public void testVerMasTardePeliculaNoEncontradaRedirigeADetallePelicula() {
        Integer movieId = 1;
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(1);
        when(movieService.getById(movieId)).thenReturn(null);
        when(usuarioService.buscarPorId(1)).thenReturn(perfilDTOMock);

        String result = movieController.verMasTarde(movieId, requestMock);

        assertThat(result, is("redirect:/detalle-pelicula/" + movieId));
        verify(usuarioService, never()).agregarEnVerMasTarde(any(PerfilDTO.class), any(MovieDTO.class));
    }

    @Test
    public void testVerMasTardeUsuarioNoEncontradoRedirigeADetallePelicula() {
        Integer movieId = 1;
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(1);
        when(movieService.getById(movieId)).thenReturn(movieDTO);
        when(usuarioService.buscarPorId(1)).thenReturn(null);

        String result = movieController.verMasTarde(movieId, requestMock);

        assertThat(result, is("redirect:/detalle-pelicula/" + movieId));
        verify(usuarioService, never()).agregarEnVerMasTarde(any(PerfilDTO.class), any(MovieDTO.class));
    }

    @Test
    public void testVerMasTardeUsuarioYMovieExisten() {
        Integer movieId = 1;
        Integer usuarioId = 1;

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(usuarioId);
        when(movieService.getById(movieId)).thenReturn(movieDTO);
        when(usuarioService.buscarPorId(usuarioId)).thenReturn(perfilDTOMock);

        String result = movieController.verMasTarde(movieId, requestMock);

        assertThat(result, is("redirect:/detalle-pelicula/" + movieId));
        verify(usuarioService, times(1)).agregarEnVerMasTarde(perfilDTOMock, movieDTO);
    }

    @Test
    public void testGetAllUpcomingMoviesViewWithCategoryFilter() {
        // Mocks
        List<MovieDTO> upcomingMoviesByCategoryMock = Collections.singletonList(mock(MovieDTO.class));
        List<CategoriaDTO> categoriasMock = Collections.singletonList(mock(CategoriaDTO.class));

        when(movieService.getUpcomingMoviesByCategory(1)).thenReturn(upcomingMoviesByCategoryMock);
        when(categoriaService.getAll()).thenReturn(categoriasMock);

        ModelAndView modelAndView = movieController.getAllUcomingMoviesView(1);

        // Verificaciones
        assertThat(modelAndView.getViewName(), is("upcoming-movies"));
        assertThat(((List<MovieDTO>) modelAndView.getModel().get("movies")).size(), is(1));
        assertThat(((List<CategoriaDTO>) modelAndView.getModel().get("categorias")).size(), is(1));
    }

    @Test
    public void testGetAllUpcomingMoviesViewWithoutCategoryFilter() {
        // Mocks
        List<MovieDTO> upcomingMoviesMock = Arrays.asList(mock(MovieDTO.class), mock(MovieDTO.class));
        List<CategoriaDTO> categoriasMock = Collections.singletonList(mock(CategoriaDTO.class));

        when(movieService.getUpcomingMovies()).thenReturn(upcomingMoviesMock);
        when(categoriaService.getAll()).thenReturn(categoriasMock);

        ModelAndView modelAndView = movieController.getAllUcomingMoviesView(null);

        // Verificaciones
        assertThat(modelAndView.getViewName(), is("upcoming-movies"));
        assertThat(((List<MovieDTO>) modelAndView.getModel().get("movies")).size(), is(2));
        assertThat(((List<CategoriaDTO>) modelAndView.getModel().get("categorias")).size(), is(1));
    }


    public void testAgregarPeliculaAListaConUsuarioLogueadoYExito() {
        Integer peliculaId = 1;
        Integer listaId = 10;
        Integer usuarioId = 100;

        MovieDTO movieMock = mock(MovieDTO.class);
        List<ListaColaborativaDTO> listasMock = Collections.singletonList(mock(ListaColaborativaDTO.class));
        List<ComentarioDTO> comentariosMock = Collections.singletonList(mock(ComentarioDTO.class));
        List<MovieDTO> similaresMock = Arrays.asList(mock(MovieDTO.class), mock(MovieDTO.class));
        ListaColaborativaDTO listaActualizadaMock = mock(ListaColaborativaDTO.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(usuarioId);
        when(movieService.getById(peliculaId)).thenReturn(movieMock);
        when(listaColaborativaService.obtenerListasPorUsuario(usuarioId)).thenReturn(listasMock);
        when(comentarioService.obtenerComentariosPorPelicula(peliculaId)).thenReturn(comentariosMock);
        when(listaColaborativaService.agregarPeliculaALista(listaId, peliculaId, usuarioId)).thenReturn(listaActualizadaMock);
        when(movieService.getSimilarMovies(peliculaId)).thenReturn(similaresMock);

        ModelAndView modelAndView = movieController.agregarPeliculaALista(peliculaId, listaId, requestMock);

        assertThat(modelAndView.getViewName(), is("detalle-pelicula"));
        assertThat(modelAndView.getModel().get("success"), is("Película agregada a la lista con éxito."));
        assertNotNull(modelAndView.getModel().get("movie"));
        assertNotNull(modelAndView.getModel().get("listasColaborativas"));
        assertNotNull(modelAndView.getModel().get("comentarios"));
        assertNotNull(modelAndView.getModel().get("peliculasSimilares"));
        verify(listaColaborativaService, times(1)).agregarPeliculaALista(listaId, peliculaId, usuarioId);
    }

    @Test
    public void testAgregarPeliculaAListaConUsuarioLogueadoYError() {
        Integer peliculaId = 1;
        Integer listaId = 10;
        Integer usuarioId = 100;

        MovieDTO movieMock = mock(MovieDTO.class);
        List<ListaColaborativaDTO> listasMock = Collections.singletonList(mock(ListaColaborativaDTO.class));
        List<ComentarioDTO> comentariosMock = Collections.singletonList(mock(ComentarioDTO.class));
        List<MovieDTO> similaresMock = Arrays.asList(mock(MovieDTO.class), mock(MovieDTO.class));

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(usuarioId);
        when(movieService.getById(peliculaId)).thenReturn(movieMock);
        when(listaColaborativaService.obtenerListasPorUsuario(usuarioId)).thenReturn(listasMock);
        when(comentarioService.obtenerComentariosPorPelicula(peliculaId)).thenReturn(comentariosMock);
        when(listaColaborativaService.agregarPeliculaALista(listaId, peliculaId, usuarioId))
                .thenThrow(new RuntimeException("Error al agregar la película a la lista."));
        when(movieService.getSimilarMovies(peliculaId)).thenReturn(similaresMock);

        ModelAndView modelAndView = movieController.agregarPeliculaALista(peliculaId, listaId, requestMock);

        assertThat(modelAndView.getViewName(), is("detalle-pelicula"));
        assertThat(modelAndView.getModel().get("error"), is("Error al agregar la película a la lista."));
        assertNotNull(modelAndView.getModel().get("movie"));
        assertNotNull(modelAndView.getModel().get("listasColaborativas"));
        assertNotNull(modelAndView.getModel().get("comentarios"));
        assertNotNull(modelAndView.getModel().get("peliculasSimilares"));
        verify(listaColaborativaService, times(1)).agregarPeliculaALista(listaId, peliculaId, usuarioId);
    }

    @Test
    public void testAgregarPeliculaAListaSinUsuarioLogueado() {
        Integer peliculaId = 1;
        Integer listaId = 10;

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(null);

        ModelAndView modelAndView = movieController.agregarPeliculaALista(peliculaId, listaId, requestMock);

        assertThat(modelAndView.getViewName(), is("detalle-pelicula"));
        assertThat(modelAndView.getModel().get("error"), is("No se pudo agregar la película, sesión no iniciada."));
        verify(listaColaborativaService, never()).agregarPeliculaALista(anyInt(), anyInt(), anyInt());
    }


}
