package org.twelve.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.PaisService;
import org.twelve.dominio.ServicioLogin;
import org.twelve.dominio.entities.Usuario;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ControladorLoginTest {

    private ControladorLogin controladorLogin;
    private Usuario usuarioMock;
    private DatosLogin datosLoginMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private ServicioLogin servicioLoginMock;
    private PaisService paisServiceMock;

    @BeforeEach
    public void init() {
        datosLoginMock = new DatosLogin("dami@unlam.com", "123");
        usuarioMock = mock(Usuario.class);
        when(usuarioMock.getEmail()).thenReturn("dami@unlam.com");
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
        // preparacion
        Usuario usuarioEncontradoMock = mock(Usuario.class);
        when(usuarioEncontradoMock.getRol()).thenReturn("ADMIN");

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenReturn(usuarioEncontradoMock);

        // ejecucion
        ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);

        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/home"));
        verify(sessionMock, times(1)).setAttribute("ROL", usuarioEncontradoMock.getRol());
    }

    @Test
    public void irALogin_DeberiaRetornarVistaLoginYModeloConDatosLogin() {
        ModelAndView modelAndView = controladorLogin.irALogin();

        assertThat(modelAndView.getViewName(), equalTo("login"));

        assertThat(modelAndView.getModel().containsKey("datosLogin"), equalTo(true));

        assertThat(modelAndView.getModel().get("datosLogin"), instanceOf(DatosLogin.class));
    }

    @Test
    public void inicio_DeberiaRedirigirALogin() {
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
    public void nuevoUsuario_DeberiaRetornarVistaUsuarioDatosConModeloConUsuario() {
        ModelAndView modelAndView = controladorLogin.nuevoUsuario();

        assertThat(modelAndView.getViewName(), equalTo("usuario-datos"));

        assertThat(modelAndView.getModel().containsKey("usuario"), equalTo(true));

        assertThat(modelAndView.getModel().get("usuario"), instanceOf(Usuario.class));
    }

    @Test
    public void nuevoUsuarioConUsuarioExistente_DeberiaMostrarError() {
        usuarioMock.setEmail("dami@unlam.com");
        when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenReturn(usuarioMock);

        ModelAndView modelAndView = controladorLogin.nuevoUsuario();

        assertThat(modelAndView.getViewName(), equalTo("usuario-datos"));
        assertThat(modelAndView.getModel().containsKey("error"), equalTo(false));
    }

}
