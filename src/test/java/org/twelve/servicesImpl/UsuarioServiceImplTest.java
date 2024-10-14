package org.twelve.servicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.twelve.dominio.RepositorioUsuario;
import org.twelve.dominio.UsuarioMovieRepository;
import org.twelve.dominio.entities.Usuario;
import org.twelve.dominio.serviceImpl.UsuarioServiceImpl;
import org.twelve.presentacion.dto.PerfilDTO;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceImplTest {

    private Usuario usuarioMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private UsuarioServiceImpl usuarioServiceImpl;
    private RepositorioUsuario repositorioUsuario;
    private UsuarioMovieRepository usuarioMovieRepository;

    @BeforeEach
    public void init() {
        usuarioMock = mock(Usuario.class);
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        repositorioUsuario = mock(RepositorioUsuario.class);
        usuarioMovieRepository = mock(UsuarioMovieRepository.class);
        usuarioServiceImpl = new UsuarioServiceImpl(repositorioUsuario, usuarioMovieRepository);
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
    @Disabled
    public void testBuscarPorUsernameCuandoUsuarioNoExisteDeberiaRetornarListaVacia() {
        when(repositorioUsuario.buscarPorUsername("inexistente")).thenReturn((Usuario) Collections.emptyList());

        List<PerfilDTO> resultado = usuarioServiceImpl.buscarPorUsername("inexistente");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(repositorioUsuario, times(1)).buscarPorUsername("inexistente");
    }

    @Test
    public void testConvertToEntityDeberiaConvertirPerfilDTOAUsuario() {
        PerfilDTO perfilDTO = new PerfilDTO();
        perfilDTO.setNombre("Luis Gomez");
        perfilDTO.setEmail("luis.gomez@ejemplo.com");

        Usuario usuario = usuarioServiceImpl.convertToEntity(perfilDTO);

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

        PerfilDTO perfilDTO = usuarioServiceImpl.convertToDTO(usuario);

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
}