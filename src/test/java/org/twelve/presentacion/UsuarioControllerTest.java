package org.twelve.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.UsuarioService;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.Usuario;
import org.twelve.presentacion.dto.PerfilDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UsuarioControllerTest {

    private UsuarioController usuarioController;
    private UsuarioService usuarioServiceMock;
    private PerfilDTO perfilMock;
    private Usuario usuarioMock;
    private HttpSession sessionMock;
    private HttpServletRequest requestMock;

    @BeforeEach
    public void setUp() {
        usuarioServiceMock = mock(UsuarioService.class);
        usuarioMock = mock(Usuario.class);
        perfilMock = mock(PerfilDTO.class);
        sessionMock = mock(HttpSession.class);
        requestMock = mock(HttpServletRequest.class);
        usuarioController = new UsuarioController(usuarioServiceMock, sessionMock);
    }

    @Test
    public void testBuscarPorIdUsuarioNoEncontrado() {
        when(usuarioServiceMock.buscarPorId(1)).thenReturn(null);

        ModelAndView modelAndView = usuarioController.verPerfil(1, requestMock);

        assertEquals("perfil", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel().get("error"));
        assertEquals("Usuario no encontrado", modelAndView.getModel().get("error"));
    }

    @Test
    public void testBuscarPorIdUsuarioEncontrado() {
        when(usuarioServiceMock.buscarPorId(1)).thenReturn(perfilMock);
        when(perfilMock.getCantidadPeliculasVistas()).thenReturn(5);
        when(perfilMock.getCantidadPeliculasVistasEsteAno()).thenReturn(2);
        when(perfilMock.getPeliculasFavoritas()).thenReturn(Collections.emptyList());

        ModelAndView modelAndView = usuarioController.verPerfil(1, requestMock);

        assertEquals("perfil", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel().get("usuario"));
        assertEquals(perfilMock, modelAndView.getModel().get("usuario"));
        assertEquals(5, modelAndView.getModel().get("cantidadPeliculasVistas"));
        assertEquals(2, modelAndView.getModel().get("cantidadPeliculasVistasEsteAno"));
        assertTrue(((List<?>) modelAndView.getModel().get("peliculasFavoritas")).isEmpty());
    }

    @Test
    public void testBuscarPorIdUsuarioConPeliculasFavoritas() {
        when(usuarioServiceMock.buscarPorId(1)).thenReturn(perfilMock);
        List<Movie> peliculasFavoritas = Arrays.asList(new Movie(), new Movie());
        when(perfilMock.getPeliculasFavoritas()).thenReturn(peliculasFavoritas);

        ModelAndView modelAndView = usuarioController.verPerfil(1, requestMock);

        assertEquals("perfil", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel().get("usuario"));
        assertEquals(peliculasFavoritas, modelAndView.getModel().get("peliculasFavoritas"));
        assertEquals(2, peliculasFavoritas.size());
    }
}