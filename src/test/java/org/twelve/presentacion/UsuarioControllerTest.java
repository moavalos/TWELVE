package org.twelve.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.UsuarioService;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.Usuario;
import org.twelve.presentacion.dto.PerfilDTO;

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

    @BeforeEach
    public void setUp() {
        usuarioServiceMock = mock(UsuarioService.class);
        usuarioMock = mock(Usuario.class);
        perfilMock = mock(PerfilDTO.class);
        usuarioController = new UsuarioController(usuarioServiceMock);
    }

    @Test
    public void testBuscarPorIdUsuarioNoEncontrado() {
        when(usuarioServiceMock.buscarPorId(1)).thenReturn(null);

        ModelAndView modelAndView = usuarioController.buscarPorId(1);

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

        ModelAndView modelAndView = usuarioController.buscarPorId(1);

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

        ModelAndView modelAndView = usuarioController.buscarPorId(1);

        assertEquals("perfil", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel().get("usuario"));
        assertEquals(peliculasFavoritas, modelAndView.getModel().get("peliculasFavoritas"));
        assertEquals(2, peliculasFavoritas.size());
    }
}