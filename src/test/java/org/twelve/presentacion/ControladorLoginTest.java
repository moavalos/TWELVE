package org.twelve.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.PaisService;
import org.twelve.dominio.ServicioLogin;
import org.twelve.dominio.entities.Usuario;
import org.twelve.presentacion.dto.PerfilDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ControladorLoginTest {

    private ControladorLogin controladorLogin;
    private DatosLogin datosLoginMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private ServicioLogin servicioLoginMock;
    private PaisService paisServiceMock;

    @BeforeEach
    public void init() {
        datosLoginMock = new DatosLogin("dami@unlam.com", "123");
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        servicioLoginMock = mock(ServicioLogin.class);
        paisServiceMock = mock(PaisService.class);
        controladorLogin = new ControladorLogin(servicioLoginMock, paisServiceMock);
    }

    @Test
    public void loginConUsuarioYPasswordInorrectosDeberiaLlevarALoginNuevamente() {
        // preparacion
        when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenReturn(null);

        // ejecucion
        ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);

        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("login"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Usuario o clave incorrecta"));
        verify(sessionMock, times(0)).setAttribute("ROL", "ADMIN");
    }

    @Test
    public void loginConUsuarioYPasswordCorrectosDeberiaLLevarAHome() {
        PerfilDTO perfilMock = mock(PerfilDTO.class);
        when(perfilMock.getRol()).thenReturn("ADMIN");

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenReturn(perfilMock);

        ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/home"));
        verify(sessionMock, times(1)).setAttribute("ROL", perfilMock.getRol());
    }

    @Test
    public void irALoginDeberiaRetornarVistaLoginYModeloConDatosLogin() {
        ModelAndView modelAndView = controladorLogin.irALogin();

        assertThat(modelAndView.getViewName(), equalTo("login"));

        assertThat(modelAndView.getModel().containsKey("datosLogin"), equalTo(true));

        assertThat(modelAndView.getModel().get("datosLogin"), instanceOf(DatosLogin.class));
    }

    @Test
    public void inicioDeberiaRedirigirALogin() {
        ModelAndView modelAndView = controladorLogin.inicio();

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void cuandoPresionoElBotonDeSalirEnElNavSeCierraLaSesion() {
        //preparacion
        when(requestMock.getSession()).thenReturn(sessionMock);
        //ejecucion
        ModelAndView modelAndView = controladorLogin.salir(requestMock);
        //validacion
        assertThat(modelAndView.getViewName(), is("redirect:/home"));
        verify(sessionMock).invalidate();
    }

    @Test
    public void nuevoUsuarioDeberiaRetornarVistaUsuarioDatosConModeloConUsuario() {
        ModelAndView modelAndView = controladorLogin.nuevoUsuario();

        assertThat(modelAndView.getViewName(), equalTo("usuario-datos"));

        assertThat(modelAndView.getModel().containsKey("usuario"), equalTo(true));

        assertThat(modelAndView.getModel().get("usuario"), instanceOf(PerfilDTO.class));
    }

    @Test
    public void nuevoUsuarioConUsuarioExistenteDeberiaMostrarError() {
        when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenReturn(null);

        ModelAndView modelAndView = controladorLogin.nuevoUsuario();

        assertThat(modelAndView.getViewName(), equalTo("usuario-datos"));
        assertThat(modelAndView.getModel().containsKey("error"), equalTo(false));
    }

}
