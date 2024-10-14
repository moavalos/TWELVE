/*package org.twelve.servicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.twelve.dominio.RepositorioUsuario;
import org.twelve.dominio.entities.Usuario;
import org.twelve.dominio.serviceImpl.UsuarioServiceImpl;
import org.twelve.presentacion.dto.PerfilDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceImplTest {

    private Usuario usuarioMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private UsuarioServiceImpl usuarioServiceImpl;
    private RepositorioUsuario repositorioUsuario;

    @BeforeEach
    public void init() {
        usuarioMock = mock(Usuario.class);
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        repositorioUsuario = mock(RepositorioUsuario.class);
        usuarioServiceImpl = new UsuarioServiceImpl(repositorioUsuario);
    }

    @Test
    public void testActualizarPerfil_CuandoUsuarioValido_DeberiaLlamarModificar() {
        usuarioServiceImpl.actualizarPerfil(usuarioMock);

        verify(repositorioUsuario, times(1)).modificar(usuarioMock);
    }

    @Test
    public void testBuscarPorId_CuandoIdExiste_DeberiaRetornarUsuario() {
        when(usuarioMock.getNombre()).thenReturn("Juan Perez");
        when(usuarioMock.getEmail()).thenReturn("juan.perez@unlam.com");

        when(repositorioUsuario.buscarPorId(1L)).thenReturn(usuarioMock);

        PerfilDTO result = usuarioServiceImpl.buscarPorId(1L);

        assertNotNull(result);
        assertEquals("Juan Perez", result.getNombre());
        assertEquals("juan.perez@unlam.com", result.getEmail());

        verify(repositorioUsuario, times(1)).buscarPorId(1L);
    }

    @Test
    public void testBuscarPorId_CuandoIdNoExiste_DeberiaRetornarNull() {
        when(repositorioUsuario.buscarPorId(1L)).thenReturn(null);

        PerfilDTO result = usuarioServiceImpl.buscarPorId(1L);

        assertNull(result);

        verify(repositorioUsuario, times(1)).buscarPorId(1L);
    }
}
*/