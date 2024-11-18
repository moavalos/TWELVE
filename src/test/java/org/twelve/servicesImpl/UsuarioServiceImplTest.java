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
import org.twelve.presentacion.dto.UsuarioMovieDTO;

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
    private PerfilDTO perfilMock;
    private MovieDTO movieMock;
    private UsuarioServiceImpl usuarioServiceImpl;
    private RepositorioUsuario repositorioUsuario;
    private UsuarioMovieRepository usuarioMovieRepository;
    private MovieRepository movieRepository;

    @BeforeEach
    public void init() {
        usuarioMock = mock(Usuario.class);
        seguidoMock = mock(Usuario.class);
        perfilMock = mock(PerfilDTO.class);
        movieMock = mock(MovieDTO.class);
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

    @Test
    public void testObtenerHistorialDePeliculasVistasConResultados() {
        Integer usuarioId = 1;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        UsuarioMovie usuarioMovie1 = new UsuarioMovie();
        usuarioMovie1.setId(1);
        usuarioMovie1.setUsuario(usuario);
        usuarioMovie1.setEsLike(true);
        usuarioMovie1.setVistaPorUsuario(true);

        UsuarioMovie usuarioMovie2 = new UsuarioMovie();
        usuarioMovie2.setId(2);
        usuarioMovie2.setUsuario(usuario);
        usuarioMovie2.setEsLike(false);
        usuarioMovie2.setVistaPorUsuario(true);

        List<Object[]> results = List.of(
                new Object[]{usuarioMovie1, 8.5},
                new Object[]{usuarioMovie2, 9.0}
        );

        when(repositorioUsuario.buscarPorId(usuarioId)).thenReturn(usuario);
        when(usuarioMovieRepository.buscarPeliculasDondeElUsuarioTuvoInteraccion(usuarioId)).thenReturn(results);

        List<UsuarioMovieDTO> historial = usuarioServiceImpl.obtenerHistorialDePeliculasVistas(usuarioId);

        assertNotNull(historial);
        assertEquals(2, historial.size());
        assertEquals(1, historial.get(0).getId());
        assertEquals(8.5, historial.get(0).getValoracion());
        assertEquals(2, historial.get(1).getId());
        assertEquals(9.0, historial.get(1).getValoracion());
    }

    @Test
    public void testObtenerHistorialDePeliculasVistasUsuarioNoTieneInteracciones() {
        Integer usuarioId = 1;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        when(repositorioUsuario.buscarPorId(usuarioId)).thenReturn(usuario);
        when(usuarioMovieRepository.buscarPeliculasDondeElUsuarioTuvoInteraccion(usuarioId)).thenReturn(Collections.emptyList());

        List<UsuarioMovieDTO> historial = usuarioServiceImpl.obtenerHistorialDePeliculasVistas(usuarioId);

        assertNotNull(historial);
        assertTrue(historial.isEmpty());
    }

    @Test
    public void testObtenerHistorialDePeliculasVistasUsuarioNoEncontrado() {
        Integer usuarioId = 999;

        when(repositorioUsuario.buscarPorId(usuarioId)).thenReturn(null);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            usuarioServiceImpl.obtenerHistorialDePeliculasVistas(usuarioId);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    public void testObtenerHistorialDePeliculasVistasConUsuarioIdNulo() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioServiceImpl.obtenerHistorialDePeliculasVistas(null);
        });

        assertEquals("El ID de usuario no puede ser nulo", exception.getMessage());
    }

    @Test
    public void testEstaEnListaVerMasTardeCuandoEstaEnLista() {
        perfilMock.setId(1);
        movieMock.setId(10);

        Usuario usuario = PerfilDTO.convertToEntity(perfilMock);
        Movie movie = MovieDTO.convertToEntity(movieMock);

        when(usuarioMovieRepository.buscarVerMasTardePorUsuario(usuario, movie)).thenReturn(Optional.of(new UsuarioMovie()));

        boolean resultado = usuarioServiceImpl.estaEnListaVerMasTarde(perfilMock, movieMock);

        assertTrue(resultado);
        verify(usuarioMovieRepository, times(1)).buscarVerMasTardePorUsuario(usuario, movie);
    }

    @Test
    public void testEstaEnListaVerMasTardeCuandoNoEstaEnLista() {
        movieMock.setId(20);

        Usuario usuario = PerfilDTO.convertToEntity(perfilMock);
        Movie movie = MovieDTO.convertToEntity(movieMock);

        when(usuarioMovieRepository.buscarVerMasTardePorUsuario(usuario, movie)).thenReturn(Optional.empty());

        boolean resultado = usuarioServiceImpl.estaEnListaVerMasTarde(perfilMock, movieMock);

        assertFalse(resultado);
        verify(usuarioMovieRepository, times(1)).buscarVerMasTardePorUsuario(usuario, movie);
    }

    @Test
    public void testEstaEnListaVerMasTardeConUsuarioNuloDeberiaLanzarExcepcion() {
        movieMock.setId(1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioServiceImpl.estaEnListaVerMasTarde(null, movieMock);
        });

        assertEquals("Usuario o película no pueden ser nulos", exception.getMessage());
        verify(usuarioMovieRepository, never()).buscarVerMasTardePorUsuario(any(), any());
    }

    @Test
    public void testEstaEnListaVerMasTardeConMovieDTONuloDeberiaLanzarExcepcion() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioServiceImpl.estaEnListaVerMasTarde(perfilMock, null);
        });

        assertEquals("Usuario o película no pueden ser nulos", exception.getMessage());
        verify(usuarioMovieRepository, never()).buscarVerMasTardePorUsuario(any(), any());
    }

    @Test
    public void testAgregarEnVerMasTardeCuandoNoEstaEnLista() {
        perfilMock.setId(1);
        movieMock.setId(10);

        Usuario usuario = PerfilDTO.convertToEntity(perfilMock);
        Movie movie = MovieDTO.convertToEntity(movieMock);

        when(usuarioMovieRepository.buscarVerMasTardePorUsuario(usuario, movie)).thenReturn(Optional.empty());

        usuarioServiceImpl.agregarEnVerMasTarde(perfilMock, movieMock);

        verify(usuarioMovieRepository, times(1)).guardar(any(UsuarioMovie.class));
        verify(usuarioMovieRepository, never()).borrarVerMasTarde(any(UsuarioMovie.class));
    }

    @Test
    public void testAgregarEnVerMasTardeCuandoYaEstaEnLista() {
        perfilMock.setId(2);
        movieMock.setId(20);

        Usuario usuario = PerfilDTO.convertToEntity(perfilMock);
        Movie movie = MovieDTO.convertToEntity(movieMock);

        UsuarioMovie usuarioMovie = new UsuarioMovie();
        usuarioMovie.setUsuario(usuario);
        usuarioMovie.setPelicula(movie);
        usuarioMovie.setEsVerMasTarde(true);

        when(usuarioMovieRepository.buscarVerMasTardePorUsuario(usuario, movie)).thenReturn(Optional.of(usuarioMovie));

        usuarioServiceImpl.agregarEnVerMasTarde(perfilMock, movieMock);

        verify(usuarioMovieRepository, times(1)).borrarVerMasTarde(usuarioMovie);
        verify(usuarioMovieRepository, never()).guardar(any(UsuarioMovie.class));
    }

    @Test
    public void testAgregarEnVerMasTardeConUsuarioNuloDeberiaLanzarExcepcion() {
        movieMock.setId(1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioServiceImpl.agregarEnVerMasTarde(null, movieMock);
        });

        assertEquals("Usuario o película no pueden ser nulos", exception.getMessage());
        verify(usuarioMovieRepository, never()).guardar(any(UsuarioMovie.class));
        verify(usuarioMovieRepository, never()).borrarVerMasTarde(any(UsuarioMovie.class));
    }

    @Test
    public void testAgregarEnVerMasTardeConMovieDTONuloDeberiaLanzarExcepcion() {
        perfilMock.setId(1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioServiceImpl.agregarEnVerMasTarde(perfilMock, null);
        });

        assertEquals("Usuario o película no pueden ser nulos", exception.getMessage());
        verify(usuarioMovieRepository, never()).guardar(any(UsuarioMovie.class));
        verify(usuarioMovieRepository, never()).borrarVerMasTarde(any(UsuarioMovie.class));
    }

    @Test
    public void testObtenerListaVerMasTardeCuandoExistenPeliculas() {
        Integer usuarioId = 1;

        UsuarioMovie usuarioMovie1 = new UsuarioMovie();
        usuarioMovie1.setId(1);
        UsuarioMovie usuarioMovie2 = new UsuarioMovie();
        usuarioMovie2.setId(2);

        List<UsuarioMovie> verMasTardeMovies = List.of(usuarioMovie1, usuarioMovie2);
        when(usuarioMovieRepository.obtenerPeliculasVerMasTarde(usuarioId)).thenReturn(verMasTardeMovies);

        List<UsuarioMovieDTO> resultado = usuarioServiceImpl.obtenerListaVerMasTarde(usuarioId);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1, resultado.get(0).getId());
        assertEquals(2, resultado.get(1).getId());

        verify(usuarioMovieRepository, times(1)).obtenerPeliculasVerMasTarde(usuarioId);
    }

    @Test
    public void testObtenerListaVerMasTardeCuandoNoExistenPeliculas() {
        Integer usuarioId = 2;

        when(usuarioMovieRepository.obtenerPeliculasVerMasTarde(usuarioId)).thenReturn(Collections.emptyList());

        List<UsuarioMovieDTO> resultado = usuarioServiceImpl.obtenerListaVerMasTarde(usuarioId);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(usuarioMovieRepository, times(1)).obtenerPeliculasVerMasTarde(usuarioId);
    }

    @Test
    public void testObtenerListaVerMasTardeConUsuarioIdNuloDeberiaLanzarExcepcion() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioServiceImpl.obtenerListaVerMasTarde(null);
        });

        assertEquals("El ID de usuario no puede ser nulo", exception.getMessage());
        verify(usuarioMovieRepository, never()).obtenerPeliculasVerMasTarde(any());
    }

    @Test
    public void testSonAmigosCuandoAmbosSonAmigos() {
        when(repositorioUsuario.existeRelacion(1, 2)).thenReturn(true);
        when(repositorioUsuario.existeRelacion(2, 1)).thenReturn(true);

        boolean resultado = usuarioServiceImpl.sonAmigos(1, 2);

        assertTrue(resultado);
        verify(repositorioUsuario, times(1)).existeRelacion(1, 2);
        verify(repositorioUsuario, times(1)).existeRelacion(2, 1);
    }

    @Test
    public void testSonAmigosCuandoUnoNoSigueAlOtro() {
        when(repositorioUsuario.existeRelacion(1, 2)).thenReturn(true);
        when(repositorioUsuario.existeRelacion(2, 1)).thenReturn(false);

        boolean resultado = usuarioServiceImpl.sonAmigos(1, 2);

        assertFalse(resultado);
        verify(repositorioUsuario, times(1)).existeRelacion(1, 2);
        verify(repositorioUsuario, times(1)).existeRelacion(2, 1);
    }

    @Test
    @Disabled
    public void testSonAmigosCuandoNingunoSigueAlOtro() {
        when(repositorioUsuario.existeRelacion(1, 2)).thenReturn(false);
        when(repositorioUsuario.existeRelacion(2, 1)).thenReturn(false);

        boolean resultado = usuarioServiceImpl.sonAmigos(1, 2);

        assertFalse(resultado);
        verify(repositorioUsuario, times(1)).existeRelacion(1, 2);
        verify(repositorioUsuario, times(1)).existeRelacion(2, 1);
    }

    @Test
    public void testObtenerAmigosCuandoHayAmigos() {
        Usuario seguido1 = new Usuario();
        seguido1.setId(2);
        seguido1.setNombre("Usuario 2");

        Usuario seguido2 = new Usuario();
        seguido2.setId(3);
        seguido2.setNombre("Usuario 3");

        when(repositorioUsuario.obtenerUsuariosSeguidos(1)).thenReturn(List.of(seguido1, seguido2));
        when(repositorioUsuario.existeRelacion(2, 1)).thenReturn(true);
        when(repositorioUsuario.existeRelacion(3, 1)).thenReturn(false);

        List<PerfilDTO> amigos = usuarioServiceImpl.obtenerAmigos(1);

        assertNotNull(amigos);
        assertEquals(1, amigos.size());
        assertEquals(2, amigos.get(0).getId());
        //assertEquals("Usuario 2", amigos.get(0).getNombre());

        verify(repositorioUsuario, times(1)).obtenerUsuariosSeguidos(1);
        verify(repositorioUsuario, times(1)).existeRelacion(2, 1);
        verify(repositorioUsuario, times(1)).existeRelacion(3, 1);
    }

    @Test
    public void testObtenerAmigosCuandoNoHayAmigos() {
        Usuario seguido1 = new Usuario();
        seguido1.setId(2);
        seguido1.setNombre("Usuario 2");

        Usuario seguido2 = new Usuario();
        seguido2.setId(3);
        seguido2.setNombre("Usuario 3");

        when(repositorioUsuario.obtenerUsuariosSeguidos(1)).thenReturn(List.of(seguido1, seguido2));
        when(repositorioUsuario.existeRelacion(2, 1)).thenReturn(false);
        when(repositorioUsuario.existeRelacion(3, 1)).thenReturn(false);

        List<PerfilDTO> amigos = usuarioServiceImpl.obtenerAmigos(1);

        assertNotNull(amigos);
        assertTrue(amigos.isEmpty());

        verify(repositorioUsuario, times(1)).obtenerUsuariosSeguidos(1);
        verify(repositorioUsuario, times(1)).existeRelacion(2, 1);
        verify(repositorioUsuario, times(1)).existeRelacion(3, 1);
    }

    @Test
    public void testObtenerAmigosCuandoNoSigueANadie() {
        when(repositorioUsuario.obtenerUsuariosSeguidos(1)).thenReturn(Collections.emptyList());

        List<PerfilDTO> amigos = usuarioServiceImpl.obtenerAmigos(1);

        assertNotNull(amigos);
        assertTrue(amigos.isEmpty());

        verify(repositorioUsuario, times(1)).obtenerUsuariosSeguidos(1);
        verify(repositorioUsuario, never()).existeRelacion(anyInt(), eq(1));
    }



}