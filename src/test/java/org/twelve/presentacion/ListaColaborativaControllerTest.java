package org.twelve.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.*;
import org.twelve.dominio.entities.ListaMovie;
import org.twelve.presentacion.dto.ListaColaborativaDTO;
import org.twelve.presentacion.dto.MovieDTO;
import org.twelve.presentacion.dto.PerfilDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ListaColaborativaControllerTest {

    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private MovieService movieService;
    private UsuarioService usuarioService;
    private ListaColaborativaService listaColaborativaService;
    private ListaColaborativaController listaColaborativaController;

    @BeforeEach
    public void init() {
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        movieService = mock(MovieService.class);
        usuarioService = mock(UsuarioService.class);
        listaColaborativaService = mock(ListaColaborativaService.class);
        listaColaborativaController = new ListaColaborativaController(usuarioService, listaColaborativaService, movieService);
    }

    @Test
    public void testMostrarFormularioCrearListaConUsuarioLogueado() {
        Integer usuarioLogueadoId = 1;
        List<PerfilDTO> amigosMock = Arrays.asList(mock(PerfilDTO.class), mock(PerfilDTO.class));

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(usuarioLogueadoId);
        when(usuarioService.obtenerAmigos(usuarioLogueadoId)).thenReturn(amigosMock);

        ModelAndView modelAndView = listaColaborativaController.mostrarFormularioCrearLista(requestMock);

        assertThat(modelAndView.getViewName(), is("crearListaColaborativa"));
        assertNotNull(modelAndView.getModel().get("usuarios"));
        assertThat(((List<PerfilDTO>) modelAndView.getModel().get("usuarios")).size(), is(2));
    }

    @Test
    public void testMostrarFormularioCrearListaSinUsuarioLogueado() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(null);

        ModelAndView modelAndView = listaColaborativaController.mostrarFormularioCrearLista(requestMock);

        assertThat(modelAndView.getViewName(), is("crearListaColaborativa"));
        assertNotNull(modelAndView.getModel().get("usuarios"));
        assertTrue(((List<PerfilDTO>) modelAndView.getModel().get("usuarios")).isEmpty());
    }

    @Test
    public void testCrearListaColaborativaConUsuariosAmigos() {
        Integer usuarioLogueadoId = 1;
        Integer usuarioColaboradorId = 2;
        String nombreLista = "Lista de prueba";

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(usuarioLogueadoId);
        when(usuarioService.sonAmigos(usuarioLogueadoId, usuarioColaboradorId)).thenReturn(true);

        ModelAndView modelAndView = listaColaborativaController.crearListaColaborativa(requestMock, nombreLista, usuarioColaboradorId);

        assertThat(modelAndView.getViewName(), is("redirect:/listas/" + usuarioLogueadoId));
    }

    @Test
    public void testCrearListaColaborativaConUsuariosNoAmigos() {
        Integer usuarioLogueadoId = 1;
        Integer usuarioColaboradorId = 2;
        String nombreLista = "Lista de prueba";
        List<PerfilDTO> usuariosMock = Arrays.asList(mock(PerfilDTO.class), mock(PerfilDTO.class));

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(usuarioLogueadoId);
        when(usuarioService.sonAmigos(usuarioLogueadoId, usuarioColaboradorId)).thenReturn(false);
        when(usuarioService.encontrarTodos()).thenReturn(usuariosMock);

        ModelAndView modelAndView = listaColaborativaController.crearListaColaborativa(requestMock, nombreLista, usuarioColaboradorId);

        assertThat(modelAndView.getViewName(), is("crearListaColaborativa"));
        assertThat(modelAndView.getModel().get("error"), is("Solo puedes crear listas con usuarios que te sigan y a quienes tú sigas."));
        assertNotNull(modelAndView.getModel().get("usuarios"));
        assertThat(((List<PerfilDTO>) modelAndView.getModel().get("usuarios")).size(), is(2));
    }

    @Test
    public void testCrearListaColaborativaSinUsuarioLogueado() {
        String nombreLista = "Lista de prueba";
        Integer usuarioColaboradorId = 2;

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(null);

        ModelAndView modelAndView = listaColaborativaController.crearListaColaborativa(requestMock, nombreLista, usuarioColaboradorId);

        assertThat(modelAndView.getViewName(), is("redirect:/login"));
    }

    @Test
    public void testMostrarFormularioAgregarPelicula() {
        List<ListaColaborativaDTO> listasMock = Arrays.asList(mock(ListaColaborativaDTO.class), mock(ListaColaborativaDTO.class));
        List<MovieDTO> peliculasMock = Arrays.asList(mock(MovieDTO.class), mock(MovieDTO.class), mock(MovieDTO.class));

        when(listaColaborativaService.obtenerTodasLasListasColaborativas()).thenReturn(listasMock);
        when(movieService.getAll()).thenReturn(peliculasMock);

        ModelAndView modelAndView = listaColaborativaController.mostrarFormularioAgregarPelicula();

        assertThat(modelAndView.getViewName(), is("agregarPeliculasLista"));
        assertNotNull(modelAndView.getModel().get("listas"));
        assertNotNull(modelAndView.getModel().get("peliculas"));
        assertThat(((List<ListaColaborativaDTO>) modelAndView.getModel().get("listas")).size(), is(2));
        assertThat(((List<MovieDTO>) modelAndView.getModel().get("peliculas")).size(), is(3));
    }

    @Test
    public void testMostrarFormularioAgregarPeliculaSinListasNiPeliculas() {
        when(listaColaborativaService.obtenerTodasLasListasColaborativas()).thenReturn(Arrays.asList());
        when(movieService.getAll()).thenReturn(Arrays.asList());

        ModelAndView modelAndView = listaColaborativaController.mostrarFormularioAgregarPelicula();

        assertThat(modelAndView.getViewName(), is("agregarPeliculasLista"));
        assertNotNull(modelAndView.getModel().get("listas"));
        assertNotNull(modelAndView.getModel().get("peliculas"));
        assertTrue(((List<ListaColaborativaDTO>) modelAndView.getModel().get("listas")).isEmpty());
        assertTrue(((List<MovieDTO>) modelAndView.getModel().get("peliculas")).isEmpty());
    }

    @Test
    public void testMostrarListasUsuarioConUsuarioLogueado() {
        Integer usuarioLogueadoId = 1;
        List<ListaColaborativaDTO> listasMock = Arrays.asList(mock(ListaColaborativaDTO.class), mock(ListaColaborativaDTO.class));

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(usuarioLogueadoId);
        when(listaColaborativaService.obtenerListasPorUsuario(usuarioLogueadoId)).thenReturn(listasMock);

        ModelAndView modelAndView = listaColaborativaController.mostrarListasUsuario(usuarioLogueadoId, requestMock);

        assertThat(modelAndView.getViewName(), is("listas"));
        assertNotNull(modelAndView.getModel().get("listas"));
        assertThat(((List<ListaColaborativaDTO>) modelAndView.getModel().get("listas")).size(), is(2));
    }

    @Test
    public void testMostrarListasUsuarioSinUsuarioLogueado() {
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(null);

        ModelAndView modelAndView = listaColaborativaController.mostrarListasUsuario(1, requestMock);

        assertThat(modelAndView.getViewName(), is("redirect:/login"));
    }

    @Test
    public void testMostrarListasUsuarioSinListas() {
        Integer usuarioLogueadoId = 1;

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(usuarioLogueadoId);
        when(listaColaborativaService.obtenerListasPorUsuario(usuarioLogueadoId)).thenReturn(Arrays.asList());

        ModelAndView modelAndView = listaColaborativaController.mostrarListasUsuario(usuarioLogueadoId, requestMock);

        assertThat(modelAndView.getViewName(), is("listas"));
        assertNotNull(modelAndView.getModel().get("listas"));
        assertTrue(((List<ListaColaborativaDTO>) modelAndView.getModel().get("listas")).isEmpty());
    }

    @Test
    public void testMostrarDetalleListaConListaExistente() {
        Integer listaId = 1;
        ListaColaborativaDTO listaMock = mock(ListaColaborativaDTO.class);
        List<ListaMovie> peliculasMock = Arrays.asList(mock(ListaMovie.class), mock(ListaMovie.class));

        when(listaColaborativaService.obtenerDetalleLista(listaId)).thenReturn(listaMock);
        when(listaColaborativaService.obtenerPeliculasPorListaId(listaId)).thenReturn(peliculasMock);

        ModelAndView modelAndView = listaColaborativaController.mostrarDetalleLista(listaId);

        assertThat(modelAndView.getViewName(), is("detalleLista"));
        assertNotNull(modelAndView.getModel().get("lista"));
        assertNotNull(modelAndView.getModel().get("peliculas"));
        assertThat(((List<ListaMovie>) modelAndView.getModel().get("peliculas")).size(), is(2));
    }

    @Test
    public void testMostrarDetalleListaConListaNoExistente() {
        Integer listaId = 1;

        when(listaColaborativaService.obtenerDetalleLista(listaId)).thenReturn(null);

        ModelAndView modelAndView = listaColaborativaController.mostrarDetalleLista(listaId);

        assertThat(modelAndView.getViewName(), is("error"));
        assertNotNull(modelAndView.getModel().get("error"));
        assertThat(modelAndView.getModel().get("error"), is("La lista solicitada no existe."));
    }

    @Test
    public void testMostrarDetalleListaConListaSinPeliculas() {
        Integer listaId = 1;
        ListaColaborativaDTO listaMock = mock(ListaColaborativaDTO.class);

        when(listaColaborativaService.obtenerDetalleLista(listaId)).thenReturn(listaMock);
        when(listaColaborativaService.obtenerPeliculasPorListaId(listaId)).thenReturn(Arrays.asList());

        ModelAndView modelAndView = listaColaborativaController.mostrarDetalleLista(listaId);

        assertThat(modelAndView.getViewName(), is("detalleLista"));
        assertNotNull(modelAndView.getModel().get("lista"));
        assertNotNull(modelAndView.getModel().get("peliculas"));
        assertTrue(((List<ListaMovie>) modelAndView.getModel().get("peliculas")).isEmpty());
    }

    @Test
    public void testCrearListaColaborativaConNombreDuplicado() {
        Integer usuarioLogueadoId = 1;
        Integer usuarioColaboradorId = 2;
        String nombreLista = "Lista Existente";

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(usuarioLogueadoId);
        when(usuarioService.sonAmigos(usuarioLogueadoId, usuarioColaboradorId)).thenReturn(true);
        when(listaColaborativaService.crearListaColaborativa(usuarioLogueadoId, usuarioColaboradorId, nombreLista))
                .thenThrow(new RuntimeException("Ya existe una lista con este nombre para el usuario."));

        ModelAndView modelAndView = listaColaborativaController.crearListaColaborativa(requestMock, nombreLista, usuarioColaboradorId);

        assertThat(modelAndView.getViewName(), is("crearListaColaborativa"));
        assertThat(modelAndView.getModel().get("error"), is("Ya existe una lista con este nombre para el usuario."));
        assertNotNull(modelAndView.getModel().get("usuarios"));
    }

    @Test
    public void testCrearListaColaborativaConErrorGenerico() {
        Integer usuarioLogueadoId = 1;
        Integer usuarioColaboradorId = 2;
        String nombreLista = "Nueva Lista";

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(usuarioLogueadoId);
        when(usuarioService.sonAmigos(usuarioLogueadoId, usuarioColaboradorId)).thenReturn(true);
        when(listaColaborativaService.crearListaColaborativa(usuarioLogueadoId, usuarioColaboradorId, nombreLista))
                .thenThrow(new RuntimeException("Error inesperado"));

        ModelAndView modelAndView = listaColaborativaController.crearListaColaborativa(requestMock, nombreLista, usuarioColaboradorId);

        assertThat(modelAndView.getViewName(), is("crearListaColaborativa"));
        assertThat(modelAndView.getModel().get("error"), is("Ocurrió un error al crear la lista. Intenta nuevamente."));
        assertNotNull(modelAndView.getModel().get("usuarios"));
    }

    @Test
    public void testCrearListaColaborativaExito() {
        Integer usuarioLogueadoId = 1;
        Integer usuarioColaboradorId = 2;
        String nombreLista = "Lista Nueva";

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(usuarioLogueadoId);
        when(usuarioService.sonAmigos(usuarioLogueadoId, usuarioColaboradorId)).thenReturn(true);

        ModelAndView modelAndView = listaColaborativaController.crearListaColaborativa(requestMock, nombreLista, usuarioColaboradorId);

        assertThat(modelAndView.getViewName(), is("redirect:/listas/" + usuarioLogueadoId));
    }

}
