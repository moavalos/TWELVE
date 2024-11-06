package org.twelve.servicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.twelve.dominio.MovieRepository;
import org.twelve.dominio.RepositorioUsuario;
import org.twelve.dominio.UsuarioMovieRepository;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.Usuario;
import org.twelve.dominio.entities.UsuarioMovie;
import org.twelve.dominio.serviceImpl.UsuarioServiceImpl;
import org.twelve.presentacion.dto.MovieDTO;
import org.twelve.presentacion.dto.PerfilDTO;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.twelve.presentacion.dto.PerfilDTO.convertToEntity;

public class UsuarioServiceImplTest {

    private Usuario usuarioMock;
    private Usuario seguidoMock;
    private UsuarioServiceImpl usuarioServiceImpl;
    private RepositorioUsuario repositorioUsuario;
    private UsuarioMovieRepository usuarioMovieRepository;
    private MovieRepository movieRepository;

    @BeforeEach
    public void init() {
        usuarioMock = mock(Usuario.class);
        seguidoMock = mock(Usuario.class);
        repositorioUsuario = mock(RepositorioUsuario.class);
        usuarioMovieRepository = mock(UsuarioMovieRepository.class);
        movieRepository = mock(MovieRepository.class);
        usuarioServiceImpl = new UsuarioServiceImpl(repositorioUsuario, usuarioMovieRepository, movieRepository);
    }

    @Test
    public void testBuscarPorIdCuandoIdExisteDeberiaRetornarUsuario() {
        when(repositorioUsuario.buscarPorId(1)).thenReturn(usuarioMock);
        when(usuarioMovieRepository.obtenerCantidadPeliculasVistas(1)).thenReturn(10);
        when(usuarioMovieRepository.obtenerCantidadPeliculasVistasEsteAno(1)).thenReturn(2);
        when(usuarioMovieRepository.obtenerPeliculasFavoritas(1)).thenReturn(Collections.emptyList());

        PerfilDTO result = usuarioServiceImpl.buscarPorId(1);

        assertNotNull(result);
        assertEquals(10, result.getCantidadPeliculasVistas());
        assertEquals(2, result.getCantidadPeliculasVistasEsteAno());
        assertTrue(result.getPeliculasFavoritas().isEmpty());

        verify(repositorioUsuario, times(1)).buscarPorId(1);
        verify(usuarioMovieRepository, times(1)).obtenerCantidadPeliculasVistas(1);
    }

    @Test
    public void testBuscarPorIdCuandoIdNoExisteDeberiaLanzarExcepcion() {
        when(repositorioUsuario.buscarPorId(999)).thenReturn(null);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            usuarioServiceImpl.buscarPorId(999);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(repositorioUsuario, times(1)).buscarPorId(999);
    }

    @Test
    public void testCrearUsuarioExitosoDeberiaRetornarPerfilDTO() {
        PerfilDTO perfilDTO = new PerfilDTO();
        perfilDTO.setNombre("Carlos Diaz");
        perfilDTO.setEmail("carlos.diaz@ejemplo.com");

        Usuario usuarioNuevo = new Usuario();
        usuarioNuevo.setNombre("Carlos Diaz");
        usuarioNuevo.setEmail("carlos.diaz@ejemplo.com");

        when(repositorioUsuario.guardar(any(Usuario.class))).thenReturn(usuarioNuevo);

        PerfilDTO resultado = usuarioServiceImpl.crear(perfilDTO);

        assertNotNull(resultado);
        assertEquals("Carlos Diaz", resultado.getNombre());
        assertEquals("carlos.diaz@ejemplo.com", resultado.getEmail());

        verify(repositorioUsuario, times(1)).guardar(any(Usuario.class));
    }

    @Test
    public void testConvertToEntityDeberiaConvertirPerfilDTOAUsuario() {
        PerfilDTO perfilDTO = new PerfilDTO();
        perfilDTO.setNombre("Luis Gomez");
        perfilDTO.setEmail("luis.gomez@ejemplo.com");

        Usuario usuario = convertToEntity(perfilDTO);

        assertNotNull(usuario);
        assertEquals("Luis Gomez", usuario.getNombre());
        assertEquals("luis.gomez@ejemplo.com", usuario.getEmail());
    }

    @Test
    public void testConvertToDTODeberiaConvertirUsuarioAPerfilDTO() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Maria Lopez");
        usuario.setEmail("maria.lopez@ejemplo.com");

        PerfilDTO perfilDTO = PerfilDTO.convertToDTO(usuario);

        assertNotNull(perfilDTO);
        assertEquals(1, perfilDTO.getId());
        assertEquals("Maria Lopez", perfilDTO.getNombre());
        assertEquals("maria.lopez@ejemplo.com", perfilDTO.getEmail());
    }

    @Test
    public void testEncontrarTodosSinUsuariosDeberiaRetornarListaVacia() {
        when(repositorioUsuario.encontrarTodos()).thenReturn(Collections.emptyList());

        List<PerfilDTO> resultado = usuarioServiceImpl.encontrarTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(repositorioUsuario, times(1)).encontrarTodos();
    }

    @Test
    public void testSeguirUsuarioExitosoDeberiaGuardarRelacion() {
        when(repositorioUsuario.buscarPorId(1)).thenReturn(usuarioMock);
        when(repositorioUsuario.buscarPorId(2)).thenReturn(seguidoMock);
        when(repositorioUsuario.estaSiguiendo(usuarioMock, seguidoMock)).thenReturn(false);

        usuarioServiceImpl.seguirUsuario(1, 2);

        verify(repositorioUsuario, times(1)).seguirUsuario(usuarioMock, seguidoMock);
    }

    @Test
    public void testSeguirUsuarioYaSiguiendoNoDeberiaGuardarRelacion() {
        when(repositorioUsuario.buscarPorId(1)).thenReturn(usuarioMock);
        when(repositorioUsuario.buscarPorId(2)).thenReturn(seguidoMock);
        when(repositorioUsuario.estaSiguiendo(usuarioMock, seguidoMock)).thenReturn(true);

        usuarioServiceImpl.seguirUsuario(1, 2);

        verify(repositorioUsuario, never()).seguirUsuario(usuarioMock, seguidoMock);
    }

    @Test
    public void testSeguirUsuarioConUsuarioNoEncontradoDeberiaLanzarExcepcion() {
        when(repositorioUsuario.buscarPorId(999)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioServiceImpl.seguirUsuario(999, 2);
        });

        assertEquals("Usuario o seguido no encontrado", exception.getMessage());
        verify(repositorioUsuario, never()).seguirUsuario(any(), any());
    }

    @Test
    public void testSeguirUsuarioConSeguidoNoEncontradoDeberiaLanzarExcepcion() {
        when(repositorioUsuario.buscarPorId(1)).thenReturn(usuarioMock);
        when(repositorioUsuario.buscarPorId(999)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioServiceImpl.seguirUsuario(1, 999);
        });

        assertEquals("Usuario o seguido no encontrado", exception.getMessage());
        verify(repositorioUsuario, never()).seguirUsuario(any(), any());
    }

    @Test
    public void testSeguirUsuarioConAmbosNoEncontradosDeberiaLanzarExcepcion() {
        when(repositorioUsuario.buscarPorId(999)).thenReturn(null);
        when(repositorioUsuario.buscarPorId(888)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioServiceImpl.seguirUsuario(999, 888);
        });

        assertEquals("Usuario o seguido no encontrado", exception.getMessage());
        verify(repositorioUsuario, never()).seguirUsuario(any(), any());
    }

    @Test
    public void testDejarDeSeguirUsuarioExitosoDeberiaEliminarRelacion() {
        when(repositorioUsuario.buscarPorId(1)).thenReturn(usuarioMock);
        when(repositorioUsuario.buscarPorId(2)).thenReturn(seguidoMock);
        when(repositorioUsuario.estaSiguiendo(usuarioMock, seguidoMock)).thenReturn(true);

        usuarioServiceImpl.dejarDeSeguirUsuario(1, 2);

        verify(repositorioUsuario, times(1)).dejarDeSeguirUsuario(usuarioMock, seguidoMock);
    }

    @Test
    public void testDejarDeSeguirUsuarioNoSiguiendoNoDeberiaEliminarRelacion() {
        when(repositorioUsuario.buscarPorId(1)).thenReturn(usuarioMock);
        when(repositorioUsuario.buscarPorId(2)).thenReturn(seguidoMock);
        when(repositorioUsuario.estaSiguiendo(usuarioMock, seguidoMock)).thenReturn(false);

        usuarioServiceImpl.dejarDeSeguirUsuario(1, 2);

        verify(repositorioUsuario, never()).dejarDeSeguirUsuario(usuarioMock, seguidoMock);
    }

    @Test
    public void testDejarDeSeguirUsuarioConUsuarioNoEncontradoDeberiaLanzarExcepcion() {
        when(repositorioUsuario.buscarPorId(999)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioServiceImpl.dejarDeSeguirUsuario(999, 2);
        });

        assertEquals("Usuario o seguido no encontrado", exception.getMessage());
        verify(repositorioUsuario, never()).dejarDeSeguirUsuario(any(), any());
    }

    @Test
    public void testDejarDeSeguirUsuarioConSeguidoNoEncontradoDeberiaLanzarExcepcion() {
        when(repositorioUsuario.buscarPorId(1)).thenReturn(usuarioMock);
        when(repositorioUsuario.buscarPorId(999)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioServiceImpl.dejarDeSeguirUsuario(1, 999);
        });

        assertEquals("Usuario o seguido no encontrado", exception.getMessage());
        verify(repositorioUsuario, never()).dejarDeSeguirUsuario(any(), any());
    }

    @Test
    public void testDejarDeSeguirUsuarioConAmbosNoEncontradosDeberiaLanzarExcepcion() {
        when(repositorioUsuario.buscarPorId(999)).thenReturn(null);
        when(repositorioUsuario.buscarPorId(888)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioServiceImpl.dejarDeSeguirUsuario(999, 888);
        });

        assertEquals("Usuario o seguido no encontrado", exception.getMessage());
        verify(repositorioUsuario, never()).dejarDeSeguirUsuario(any(), any());
    }

    @Test
    public void testEstaSiguiendoUsuarioCuandoAmbosExistenDeberiaRetornarTrue() {
        when(repositorioUsuario.buscarPorId(1)).thenReturn(usuarioMock);
        when(repositorioUsuario.buscarPorId(2)).thenReturn(seguidoMock);
        when(repositorioUsuario.estaSiguiendo(usuarioMock, seguidoMock)).thenReturn(true);

        Boolean resultado = usuarioServiceImpl.estaSiguiendo(1, 2);

        assertTrue(resultado);
        verify(repositorioUsuario, times(1)).buscarPorId(1);
        verify(repositorioUsuario, times(1)).buscarPorId(2);
        verify(repositorioUsuario, times(1)).estaSiguiendo(usuarioMock, seguidoMock);
    }

    @Test
    public void testEstaSiguiendoUsuarioCuandoAmbosExistenPeroNoSiguiendoDeberiaRetornarFalse() {
        when(repositorioUsuario.buscarPorId(1)).thenReturn(usuarioMock);
        when(repositorioUsuario.buscarPorId(2)).thenReturn(seguidoMock);
        when(repositorioUsuario.estaSiguiendo(usuarioMock, seguidoMock)).thenReturn(false);

        Boolean resultado = usuarioServiceImpl.estaSiguiendo(1, 2);

        assertFalse(resultado);
        verify(repositorioUsuario, times(1)).buscarPorId(1);
        verify(repositorioUsuario, times(1)).buscarPorId(2);
        verify(repositorioUsuario, times(1)).estaSiguiendo(usuarioMock, seguidoMock);
    }

    @Test
    @Disabled
    public void testGuardarMeGustaActualizaLikesEnLaPelicula() {
        PerfilDTO usuarioDTO = new PerfilDTO();
        usuarioDTO.setId(1);

        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setLikes(5);

        Usuario usuario = new Usuario();
        Movie movie = MovieDTO.convertToEntity(movieDTO);

        when(usuarioMovieRepository.buscarMeGustaPorUsuario(usuario, movie)).thenReturn(Optional.empty());

        usuarioServiceImpl.guardarMeGusta(usuarioDTO, movieDTO);

        assertEquals(6, movie.getLikes());
        verify(movieRepository, times(1)).actualizar(movie);
    }

    @Test
    public void testGuardarMeGustaConUsuarioInvalidoDeberiaLanzarExcepcion() {
        PerfilDTO usuarioDTO = null;
        MovieDTO movieDTO = new MovieDTO();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioServiceImpl.guardarMeGusta(usuarioDTO, movieDTO);
        });

        assertEquals("Usuario o película no puede ser nulo", exception.getMessage());
    }

    @Test
    public void testGuardarMeGustaConPeliculaInvalidaDeberiaLanzarExcepcion() {
        PerfilDTO usuarioDTO = new PerfilDTO();
        usuarioDTO.setId(1);
        MovieDTO movieDTO = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioServiceImpl.guardarMeGusta(usuarioDTO, movieDTO);
        });

        assertEquals("Usuario o película no puede ser nulo", exception.getMessage());
    }

    @Test
    public void testObtenerCantidadDeLikesCuandoPeliculaExisteDeberiaRetornarCantidadDeLikes() {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(1);

        Movie movie = MovieDTO.convertToEntity(movieDTO);
        long expectedLikes = 10;

        when(usuarioMovieRepository.obtenerCantidadDeLikes(movie)).thenReturn(expectedLikes);

        long result = usuarioServiceImpl.obtenerCantidadDeLikes(movieDTO);

        assertEquals(expectedLikes, result);
        verify(usuarioMovieRepository, times(1)).obtenerCantidadDeLikes(movie);
    }

    @Test
    public void testObtenerCantidadDeLikesCuandoPeliculaNoExisteDeberiaRetornarCero() {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(999);

        Movie movie = MovieDTO.convertToEntity(movieDTO);
        long expectedLikes = 0;

        when(usuarioMovieRepository.obtenerCantidadDeLikes(movie)).thenReturn(expectedLikes);

        long result = usuarioServiceImpl.obtenerCantidadDeLikes(movieDTO);

        assertEquals(expectedLikes, result);
        verify(usuarioMovieRepository, times(1)).obtenerCantidadDeLikes(movie);
    }

    @Test
    public void testObtenerCantidadDeLikesConMovieDTONuloDeberiaLanzarExcepcion() {
        MovieDTO movieDTO = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioServiceImpl.obtenerCantidadDeLikes(movieDTO);
        });

        assertEquals("MovieDTO no puede ser nulo", exception.getMessage());
        verify(usuarioMovieRepository, never()).obtenerCantidadDeLikes(any());
    }

    @Test
    public void testObtenerCantidadDeLikesConIdDePeliculaInvalidoDeberiaLanzarExcepcion() {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(-1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioServiceImpl.obtenerCantidadDeLikes(movieDTO);
        });

        assertEquals("ID de película no puede ser negativo", exception.getMessage());
        verify(usuarioMovieRepository, never()).obtenerCantidadDeLikes(any());
    }

    @Test
    public void testHaDadoLikeCuandoUsuarioHaDadoLikeDeberiaRetornarTrue() {
        PerfilDTO usuarioDTO = new PerfilDTO();
        usuarioDTO.setId(1);
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(10);

        Usuario usuario = convertToEntity(usuarioDTO);
        Movie movie = MovieDTO.convertToEntity(movieDTO);

        when(usuarioMovieRepository.buscarMeGustaPorUsuario(usuario, movie)).thenReturn(Optional.of(new UsuarioMovie()));

        boolean resultado = usuarioServiceImpl.haDadoLike(usuarioDTO, movieDTO);

        assertTrue(resultado);
        verify(usuarioMovieRepository, times(1)).buscarMeGustaPorUsuario(usuario, movie);
    }

    @Test
    public void testHaDadoLikeCuandoUsuarioNoHaDadoLikeDeberiaRetornarFalse() {
        PerfilDTO usuarioDTO = new PerfilDTO();
        usuarioDTO.setId(2);
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(20);

        Usuario usuario = convertToEntity(usuarioDTO);
        Movie movie = MovieDTO.convertToEntity(movieDTO);

        when(usuarioMovieRepository.buscarMeGustaPorUsuario(usuario, movie)).thenReturn(Optional.empty());

        boolean resultado = usuarioServiceImpl.haDadoLike(usuarioDTO, movieDTO);

        assertFalse(resultado);
        verify(usuarioMovieRepository, times(1)).buscarMeGustaPorUsuario(usuario, movie);
    }

    @Test
    public void testHaDadoLikeConUsuarioNuloDeberiaLanzarExcepcion() {
        PerfilDTO usuarioDTO = null;
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioServiceImpl.haDadoLike(usuarioDTO, movieDTO);
        });

        assertEquals("Usuario o película no pueden ser nulos", exception.getMessage());
        verify(usuarioMovieRepository, never()).buscarMeGustaPorUsuario(any(), any());
    }

    @Test
    public void testHaDadoLikeConMovieDTONuloDeberiaLanzarExcepcion() {
        PerfilDTO usuarioDTO = new PerfilDTO();
        usuarioDTO.setId(1);
        MovieDTO movieDTO = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioServiceImpl.haDadoLike(usuarioDTO, movieDTO);
        });

        assertEquals("Usuario o película no pueden ser nulos", exception.getMessage());
        verify(usuarioMovieRepository, never()).buscarMeGustaPorUsuario(any(), any());
    }

    @Test
    public void testHaDadoLikeConMovieYUsuarioInvalidosDeberiaLanzarExcepcion() {
        PerfilDTO usuarioDTO = new PerfilDTO();
        usuarioDTO.setId(-1);
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(-1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioServiceImpl.haDadoLike(usuarioDTO, movieDTO);
        });

        assertEquals("ID de usuario o película no pueden ser negativos", exception.getMessage());
        verify(usuarioMovieRepository, never()).buscarMeGustaPorUsuario(any(), any());
    }

    @Test
    public void testObtenerPeliculasFavoritasCuandoUsuarioTienePeliculasDeberiaRetornarLista() {
        Integer usuarioId = 1;
        List<Movie> peliculasFavoritas = List.of(new Movie(), new Movie());

        when(usuarioMovieRepository.obtenerPeliculasFavoritas(usuarioId)).thenReturn(peliculasFavoritas);

        List<Movie> resultado = usuarioServiceImpl.obtenerPeliculasFavoritas(usuarioId);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(usuarioMovieRepository, times(1)).obtenerPeliculasFavoritas(usuarioId);
    }

    @Test
    public void testObtenerPeliculasFavoritasCuandoUsuarioNoTienePeliculasDeberiaRetornarListaVacia() {
        Integer usuarioId = 2;

        when(usuarioMovieRepository.obtenerPeliculasFavoritas(usuarioId)).thenReturn(Collections.emptyList());

        List<Movie> resultado = usuarioServiceImpl.obtenerPeliculasFavoritas(usuarioId);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(usuarioMovieRepository, times(1)).obtenerPeliculasFavoritas(usuarioId);
    }

    @Test
    public void testObtenerPeliculasFavoritasConUsuarioIdNuloDeberiaLanzarExcepcion() {
        Integer usuarioId = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioServiceImpl.obtenerPeliculasFavoritas(usuarioId);
        });

        assertEquals("El ID de usuario no puede ser nulo", exception.getMessage());
        verify(usuarioMovieRepository, never()).obtenerPeliculasFavoritas(any());
    }

    @Test
    public void testObtenerPeliculasFavoritasConUsuarioIdNegativoDeberiaLanzarExcepcion() {
        Integer usuarioId = -1;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioServiceImpl.obtenerPeliculasFavoritas(usuarioId);
        });

        assertEquals("El ID de usuario no puede ser negativo", exception.getMessage());
        verify(usuarioMovieRepository, never()).obtenerPeliculasFavoritas(any());
    }

}