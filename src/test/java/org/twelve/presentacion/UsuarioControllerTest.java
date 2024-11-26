package org.twelve.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.ComentarioService;
import org.twelve.dominio.PaisService;
import org.twelve.dominio.UsuarioService;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.serviceImpl.PaisServiceImpl;
import org.twelve.presentacion.dto.ComentarioDTO;
import org.twelve.presentacion.dto.PerfilDTO;
import org.twelve.presentacion.dto.UsuarioMovieDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioControllerTest {

    private UsuarioController usuarioController;
    private UsuarioService usuarioServiceMock;
    private ComentarioService comentarioServiceMock;
    private UsuarioMovieDTO usuarioMovieDTO;
    private UsuarioMovieDTO usuarioMovieDTO2;
    private PerfilDTO perfilMock;
    private HttpSession sessionMock;
    private HttpServletRequest requestMock;
    private PaisService paisServiceMock;

    @BeforeEach
    public void setUp() {
        usuarioServiceMock = mock(UsuarioService.class);
        perfilMock = mock(PerfilDTO.class);
        usuarioMovieDTO = mock(UsuarioMovieDTO.class);
        usuarioMovieDTO2 = mock(UsuarioMovieDTO.class);
        sessionMock = mock(HttpSession.class);
        requestMock = mock(HttpServletRequest.class);
        comentarioServiceMock = mock(ComentarioService.class);
        paisServiceMock = mock(PaisService.class);
        usuarioController = new UsuarioController(usuarioServiceMock, sessionMock, comentarioServiceMock, paisServiceMock);
    }

    @Test
    public void testBuscarPorIdUsuarioNoEncontrado() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(usuarioServiceMock.buscarPorId(1)).thenReturn(null);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(1);

        ModelAndView modelAndView = usuarioController.verPerfil(1, requestMock);

        assertEquals("perfil", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel().get("error"));
        assertEquals("Usuario no encontrado", modelAndView.getModel().get("error"));
    }

    @Test
    public void testBuscarPorIdUsuarioEncontrado() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(usuarioServiceMock.buscarPorId(1)).thenReturn(perfilMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(1);
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
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(usuarioServiceMock.buscarPorId(1)).thenReturn(perfilMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(1);
        List<Movie> peliculasFavoritas = Arrays.asList(new Movie(), new Movie());
        when(perfilMock.getPeliculasFavoritas()).thenReturn(peliculasFavoritas);

        ModelAndView modelAndView = usuarioController.verPerfil(1, requestMock);

        assertEquals("perfil", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel().get("usuario"));
        assertEquals(peliculasFavoritas, modelAndView.getModel().get("peliculasFavoritas"));
        assertEquals(2, peliculasFavoritas.size());
    }

    @Test
    public void testUsuarioNoLogueadoRedirigeALogin() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(null);

        ModelAndView modelAndView = usuarioController.verPerfil(1, requestMock);

        assertEquals("redirect:/login", modelAndView.getViewName());
    }

    @Test
    public void testUsuarioLogueadoVePerfilDeOtroUsuario() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(2);
        when(usuarioServiceMock.buscarPorId(1)).thenReturn(perfilMock);

        when(usuarioServiceMock.estaSiguiendo(2, 1)).thenReturn(false);
        when(perfilMock.getCantidadPeliculasVistas()).thenReturn(10);
        when(perfilMock.getCantidadPeliculasVistasEsteAno()).thenReturn(3);
        when(perfilMock.getPeliculasFavoritas()).thenReturn(Collections.emptyList());

        ModelAndView modelAndView = usuarioController.verPerfil(1, requestMock);

        assertEquals("perfil", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel().get("usuario"));
        assertFalse((Boolean) modelAndView.getModel().get("estaSiguiendo"));
        assertEquals("/seguir/1", modelAndView.getModel().get("seguirODejarUrl"));
    }

    @Test
    public void testUsuarioNoEncontradoMuestraError() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(1);
        when(usuarioServiceMock.buscarPorId(1)).thenReturn(null);

        ModelAndView modelAndView = usuarioController.verPerfil(1, requestMock);

        assertEquals("perfil", modelAndView.getViewName());
        assertEquals("Usuario no encontrado", modelAndView.getModel().get("error"));
    }

    @Test
    public void testUsuarioLogueadoViendoSuPropioPerfil() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(1);
        when(usuarioServiceMock.buscarPorId(1)).thenReturn(perfilMock);

        ModelAndView modelAndView = usuarioController.verPerfil(1, requestMock);

        assertEquals("perfil", modelAndView.getViewName());
        assertEquals(perfilMock, modelAndView.getModel().get("usuario"));
        assertTrue((Boolean) modelAndView.getModel().get("esPerfilPropio"));
    }

    @Test
    public void testUsuarioLogueadoViendoOtroPerfil() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(2);
        when(usuarioServiceMock.buscarPorId(1)).thenReturn(perfilMock);
        when(usuarioServiceMock.estaSiguiendo(2, 1)).thenReturn(false);

        ModelAndView modelAndView = usuarioController.verPerfil(1, requestMock);

        assertEquals("perfil", modelAndView.getViewName());
        assertEquals(perfilMock, modelAndView.getModel().get("usuario"));
        assertFalse((Boolean) modelAndView.getModel().get("esPerfilPropio"));
        assertFalse((Boolean) modelAndView.getModel().get("estaSiguiendo"));
        assertEquals("/seguir/1", modelAndView.getModel().get("seguirODejarUrl"));
    }

    @Test
    public void testUsuarioLogueadoViendoOtroPerfilQueSigue() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(2);
        when(usuarioServiceMock.buscarPorId(1)).thenReturn(perfilMock);
        when(usuarioServiceMock.estaSiguiendo(2, 1)).thenReturn(true);

        ModelAndView modelAndView = usuarioController.verPerfil(1, requestMock);

        assertEquals("perfil", modelAndView.getViewName());
        assertTrue((Boolean) modelAndView.getModel().get("estaSiguiendo"));
        assertEquals("/dejarDeSeguir/1", modelAndView.getModel().get("seguirODejarUrl"));
    }

    @Test
    public void testPerfilConComentariosRecientes() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(1);
        when(usuarioServiceMock.buscarPorId(1)).thenReturn(perfilMock);
        List<ComentarioDTO> comentarios = Collections.singletonList(mock(ComentarioDTO.class));
        when(comentarioServiceMock.obtenerUltimosTresComentarios(1)).thenReturn(comentarios);

        ModelAndView modelAndView = usuarioController.verPerfil(1, requestMock);

        assertEquals("perfil", modelAndView.getViewName());
        assertEquals(comentarios, modelAndView.getModel().get("comentariosRecientes"));
    }

    @Test
    public void testUsuarioLogueadoYaSigueAlUsuario() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(2);
        when(usuarioServiceMock.buscarPorId(1)).thenReturn(perfilMock);
        when(usuarioServiceMock.estaSiguiendo(2, 1)).thenReturn(true);

        ModelAndView modelAndView = usuarioController.verPerfil(1, requestMock);

        assertEquals("perfil", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel().get("usuario"));
        assertTrue((Boolean) modelAndView.getModel().get("estaSiguiendo"));
        assertEquals("/dejarDeSeguir/1", modelAndView.getModel().get("seguirODejarUrl"));
    }

    @Test
    public void testPerfilSinSeguidores() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(2);
        when(usuarioServiceMock.buscarPorId(1)).thenReturn(perfilMock);
        when(perfilMock.getSeguidores()).thenReturn(Collections.emptyList());

        ModelAndView modelAndView = usuarioController.verPerfil(1, requestMock);

        assertEquals("perfil", modelAndView.getViewName());
        assertTrue(((List<?>) modelAndView.getModel().get("seguidores")).isEmpty());
    }

    @Test
    public void testCantidadPeliculasVistasEsteAno() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(2);
        when(usuarioServiceMock.buscarPorId(1)).thenReturn(perfilMock);
        when(perfilMock.getCantidadPeliculasVistasEsteAno()).thenReturn(7);

        ModelAndView modelAndView = usuarioController.verPerfil(1, requestMock);

        assertEquals("perfil", modelAndView.getViewName());
        assertEquals(7, modelAndView.getModel().get("cantidadPeliculasVistasEsteAno"));
    }

    @Test
    public void testPerfilConErrorEnBusqueda() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(2);
        when(usuarioServiceMock.buscarPorId(1)).thenReturn(null);

        ModelAndView modelAndView = usuarioController.verPerfil(1, requestMock);

        assertEquals("perfil", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel().get("error"));
        assertEquals("Usuario no encontrado", modelAndView.getModel().get("error"));
    }

    @Test
    public void testSeguirUsuarioExitoso() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(1);

        String result = usuarioController.seguirUsuario(2, requestMock);

        verify(usuarioServiceMock).seguirUsuario(1, 2);
        assertEquals("redirect:/perfil/2", result);
    }

    @Test
    public void testSeguirUsuarioServicioLanzaExcepcion() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(1);
        doThrow(new RuntimeException("Error inesperado")).when(usuarioServiceMock).seguirUsuario(1, 2);

        String result = usuarioController.seguirUsuario(2, requestMock);

        verify(usuarioServiceMock).seguirUsuario(1, 2);
        assertEquals("redirect:/error", result);
    }

    @Test
    public void testSeguirUsuarioMismoUsuario() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(1);

        String result = usuarioController.seguirUsuario(1, requestMock);

        verify(usuarioServiceMock, never()).seguirUsuario(anyInt(), anyInt());
        assertEquals("redirect:/error", result);
    }

    @Test
    public void testSeguirUsuarioRedirigeCorrectamenteDespuesDeSeguir() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(1);

        String result = usuarioController.seguirUsuario(3, requestMock);

        verify(usuarioServiceMock).seguirUsuario(1, 3);
        assertEquals("redirect:/perfil/3", result);
    }

    @Test
    public void testDejarDeSeguirUsuarioExitoso() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(1);

        String result = usuarioController.dejarDeSeguirUsuario(2, requestMock);

        verify(usuarioServiceMock).dejarDeSeguirUsuario(1, 2);
        assertEquals("redirect:/perfil/2", result);
    }

    @Test
    public void testDejarDeSeguirUsuarioRedirigeCorrectamenteDespuesDeDejarDeSeguir() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(1);

        String result = usuarioController.dejarDeSeguirUsuario(3, requestMock);

        verify(usuarioServiceMock).dejarDeSeguirUsuario(1, 3);
        assertEquals("redirect:/perfil/3", result);
    }

    @Test
    public void testVerFavoritosUsuarioNoLogueado() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(null);

        ModelAndView modelAndView = usuarioController.verFavoritos(requestMock);

        assertEquals("redirect:/login", modelAndView.getViewName());
    }

    @Test
    public void testVerFavoritosUsuarioSinFavoritos() {
        Integer usuarioId = 1;

        PerfilDTO usuarioMock = mock(PerfilDTO.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(usuarioId);
        when(usuarioServiceMock.buscarPorId(usuarioId)).thenReturn(usuarioMock);
        when(usuarioServiceMock.obtenerPeliculasFavoritas(usuarioId)).thenReturn(Collections.emptyList());

        ModelAndView modelAndView = usuarioController.verFavoritos(requestMock);

        assertEquals("favoritos", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel().get("peliculasFavoritas"));
        assertTrue(((List<?>) modelAndView.getModel().get("peliculasFavoritas")).isEmpty());
    }

    @Test
    public void testVerFavoritosUsuarioConFavoritosNulos() {
        Integer usuarioId = 1;

        PerfilDTO usuarioMock = mock(PerfilDTO.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(usuarioId);
        when(usuarioServiceMock.buscarPorId(usuarioId)).thenReturn(usuarioMock);
        when(usuarioServiceMock.obtenerPeliculasFavoritas(usuarioId)).thenReturn(null);

        ModelAndView modelAndView = usuarioController.verFavoritos(requestMock);

        assertEquals("favoritos", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel().get("peliculasFavoritas"));
        assertTrue(((List<?>) modelAndView.getModel().get("peliculasFavoritas")).isEmpty());
    }

    @Test
    public void testVerHistorialUsuarioNoLogueado() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(null);

        ModelAndView modelAndView = usuarioController.verHistorial(requestMock);

        assertEquals("redirect:/login", modelAndView.getViewName());
    }

    @Test
    public void testVerHistorialUsuarioLogueadoSinHistorial() {
        Integer usuarioId = 1;
        PerfilDTO usuarioMock = mock(PerfilDTO.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(usuarioId);
        when(usuarioServiceMock.buscarPorId(usuarioId)).thenReturn(usuarioMock);
        when(usuarioServiceMock.obtenerHistorialDePeliculasVistas(usuarioId)).thenReturn(Collections.emptyList());

        ModelAndView modelAndView = usuarioController.verHistorial(requestMock);

        assertEquals("historial", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel().get("historial"));
        assertTrue(((List<?>) modelAndView.getModel().get("historial")).isEmpty());
    }

    @Test
    public void testUsuarioLogueadoConListaVerMasTardeVacia() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(1);
        when(usuarioServiceMock.buscarPorId(1)).thenReturn(perfilMock);
        when(perfilMock.getId()).thenReturn(1);
        when(usuarioServiceMock.obtenerListaVerMasTarde(1)).thenReturn(Collections.emptyList());

        ModelAndView modelAndView = usuarioController.verMasTarde(requestMock);

        assertEquals("verMasTarde", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel().get("verMasTarde"));
        assertTrue(((List<?>) modelAndView.getModel().get("verMasTarde")).isEmpty());
    }

    @Test
    public void testUsuarioLogueadoConListaVerMasTardeConElementos() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(1);
        when(usuarioServiceMock.buscarPorId(1)).thenReturn(perfilMock);
        when(perfilMock.getId()).thenReturn(1);

        UsuarioMovieDTO movieMock = mock(UsuarioMovieDTO.class);
        List<UsuarioMovieDTO> verMasTardeList = Collections.singletonList(movieMock);
        when(usuarioServiceMock.obtenerListaVerMasTarde(1)).thenReturn(verMasTardeList);

        ModelAndView modelAndView = usuarioController.verMasTarde(requestMock);

        assertEquals("verMasTarde", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel().get("verMasTarde"));
        assertEquals(verMasTardeList, modelAndView.getModel().get("verMasTarde"));
    }

}
