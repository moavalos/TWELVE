package org.twelve.servicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.twelve.dominio.MovieRepository;
import org.twelve.dominio.entities.Categoria;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.serviceImpl.MovieServiceImpl;
import org.twelve.presentacion.dto.CategoriaDTO;
import org.twelve.presentacion.dto.MovieDTO;
import org.twelve.presentacion.dto.PaisDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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
    public void testGetAllCuandoHayPeliculasDeberiaRetornarListaDeMovieDTO() {
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
    public void testGetByIdCuandoPeliculaExisteDeberiaRetornarMovieDTO() {
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
    public void testCreateCuandoSeCreaPeliculaDeberiaRetornarMovieDTO() {
        Categoria categoria1 = new Categoria(1, "Acción");
        Categoria categoria2 = new Categoria(2, "Ciencia Ficción");

        Set<Categoria> categoriasSet = new HashSet<>(Arrays.asList(categoria1, categoria2));

        when(movie1.getNombre()).thenReturn("Matrix");
        when(movie1.getDuracion()).thenReturn(136.8);
        when(movie1.getCategorias()).thenReturn(categoriasSet);

        List<CategoriaDTO> categoriaDTOList = Arrays.asList(
                new CategoriaDTO(1, "Acción"),
                new CategoriaDTO(2, "Ciencia Ficción")
        );

        PaisDTO paisDTO = new PaisDTO(1, "Pais");

        MovieDTO movieDTO = new MovieDTO(
                1,
                "Matrix",
                "A hacker discovers...",
                "Welcome to the real world",
                136.8,
                paisDTO,
                5000,
                categoriaDTOList,
                "1999",
                "matrix.jpg",
                3000,
                9.0,
                "Lana Wachowski, Lilly Wachowski", // director
                "Lana Wachowski, Lilly Wachowski", // escritor
                "Inglés",                         // idioma
                "The Matrix, Matrix"// también conocida como
        );

        when(movieRepository.guardar(any(Movie.class))).thenReturn(movie1);

        MovieDTO result = movieServiceImpl.create(movieDTO);

        assertNotNull(result);
        assertEquals("Matrix", result.getNombre());
        assertEquals(136.8, result.getDuracion());
        assertEquals(categoriaDTOList.size(), result.getCategorias().size());

        verify(movieRepository, times(1)).guardar(any(Movie.class));
    }


    @Test
    public void testSearchByTitleCuandoTituloExisteDeberiaRetornarListaDeMovieDTO() {
        when(movie1.getNombre()).thenReturn("Matrix");

        when(movieRepository.findByTitle("Matrix")).thenReturn(Collections.singletonList(movie1));

        List<MovieDTO> result = movieServiceImpl.searchByTitle("Matrix");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Matrix", result.get(0).getNombre());

        verify(movieRepository, times(1)).findByTitle("Matrix");
    }

    @Test
    public void testGetMovieMasVistaDeberiaRetornarListaDeMovieDTO() {
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
    public void testGetMovieByValoracionDeberiaRetornarListaDeMovieDTO() {
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
    public void testGetMoviesByCategoryCuandoCategoriaExisteDeberiaRetornarListaDeMovieDTO() {
        Categoria categoria = new Categoria(1, "Acción");

        Set<Categoria> categoriasSet1 = new HashSet<>(List.of(categoria));
        Set<Categoria> categoriasSet2 = new HashSet<>(List.of(categoria));

        when(movie1.getCategorias()).thenReturn(categoriasSet1);
        when(movie2.getCategorias()).thenReturn(categoriasSet2);

        when(movieRepository.findByCategoriaId(1)).thenReturn(Arrays.asList(movie1, movie2));

        List<MovieDTO> result = movieServiceImpl.getMoviesByCategory(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0).getCategorias().stream().anyMatch(c -> c.getId().equals(1)));
        assertTrue(result.get(1).getCategorias().stream().anyMatch(c -> c.getId().equals(1)));

        verify(movieRepository, times(1)).findByCategoriaId(1);
    }

    @Test
    public void testGetMovieByAnioDeberiaRetornarListaDeMovieDTO() {
        when(movie2.getAñoLanzamiento()).thenReturn("2010");

        when(movieRepository.findNewestMovie()).thenReturn(Collections.singletonList(movie2));

        List<MovieDTO> result = movieServiceImpl.getMovieByAnio();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("2010", result.get(0).getAnioLanzamiento());

        verify(movieRepository, times(1)).findNewestMovie();
    }

    @Test
    public void testGetMoviesByCategorySinFiltroDeberiaRetornarPeliculasPorCategoria() {
        Integer idCategoria = 1;

        when(movieRepository.findByCategoriaId(idCategoria)).thenReturn(Arrays.asList(movie1, movie2));

        List<MovieDTO> result = movieServiceImpl.getMoviesByCategory(idCategoria, null);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(movieRepository, times(1)).findByCategoriaId(idCategoria);
    }

    @Test
    public void testGetMoviesByCategoryConFilterTopRatedLlamaAlMetodoTopRated() {

        Integer idCategoria = 1;
        String filter = "topRated";

        movieServiceImpl.getMoviesByCategory(idCategoria, filter);

        verify(movieRepository).findByCategoriaIdTopRated(idCategoria);
        verify(movieRepository, never()).findByCategoriaIdNewest(idCategoria);
        verify(movieRepository, never()).findByCategoriaId(idCategoria);
    }

    @Test
    public void testGetMoviesByCategoryConFilterNewestLlamaAlMetodoCorrecto() {
        Integer idCategoria = 1;
        String filter = "newest";

        movieServiceImpl.getMoviesByCategory(idCategoria, filter);

        verify(movieRepository).findByCategoriaIdNewest(idCategoria);
        verify(movieRepository, never()).findByCategoriaIdTopRated(idCategoria);
        verify(movieRepository, never()).findByCategoriaId(idCategoria);
    }

    @Test
    public void testGetMoviesByCategorySinFiltroLlamaAlMetodoCorrecto() {
        Integer idCategoria = 1;
        String filter = null;

        movieServiceImpl.getMoviesByCategory(idCategoria, filter);

        verify(movieRepository).findByCategoriaId(idCategoria);
        verify(movieRepository, never()).findByCategoriaIdTopRated(idCategoria);
        verify(movieRepository, never()).findByCategoriaIdNewest(idCategoria);
    }

    @Test
    public void testGetSimilarMoviesCuandoPeliculaNoExisteDeberiaLanzarExcepcion() {
        when(movieRepository.findById(99)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            movieServiceImpl.getSimilarMovies(99);
        });

        assertEquals("La película con ID 99 no existe.", exception.getMessage());

        verify(movieRepository, times(1)).findById(99);
    }



    @Test
    public void testGetSimilarMoviesCuandoExistenPeliculasSimilaresDeberiaRetornarListaDeMovieDTO() {
        Categoria categoria1 = new Categoria(1, "Acción");
        Categoria categoria2 = new Categoria(2, "Ciencia Ficción");
        Set<Categoria> categorias = new HashSet<>(Arrays.asList(categoria1, categoria2));

        Movie movie1 = new Movie();
        movie1.setId(1);
        movie1.setNombre("Matrix");
        movie1.setCategorias(categorias);

        Movie movie2 = new Movie();
        movie2.setId(2);
        movie2.setNombre("Blade Runner");
        movie2.setCategorias(Collections.singleton(categoria2));

        when(movieRepository.findById(1)).thenReturn(movie1);
        when(movieRepository.findSimilarMovies(1, categorias)).thenReturn(Arrays.asList(movie2));

        List<MovieDTO> result = movieServiceImpl.getSimilarMovies(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Blade Runner", result.get(0).getNombre());

        verify(movieRepository, times(1)).findById(1);
        verify(movieRepository, times(1)).findSimilarMovies(1, categorias);
    }


}
