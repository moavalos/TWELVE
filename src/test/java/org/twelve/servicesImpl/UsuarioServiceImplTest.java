package org.twelve.servicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.twelve.dominio.MovieRepository;
import org.twelve.dominio.RepositorioUsuario;
import org.twelve.dominio.UsuarioMovieRepository;
import org.twelve.dominio.entities.Usuario;
import org.twelve.dominio.serviceImpl.UsuarioServiceImpl;
import org.twelve.presentacion.dto.PerfilDTO;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        Usuario usuario = PerfilDTO.convertToEntity(perfilDTO);

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
}