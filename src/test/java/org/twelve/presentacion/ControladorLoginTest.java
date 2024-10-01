package org.twelve.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.ServicioLogin;
import org.twelve.dominio.entities.Usuario;
import org.twelve.dominio.excepcion.ContrasenasNoCoinciden;
import org.twelve.dominio.excepcion.UsuarioExistente;

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


    @BeforeEach
    public void init() {
        datosLoginMock = new DatosLogin("dami@unlam.com", "123");
        usuarioMock = mock(Usuario.class);
        when(usuarioMock.getEmail()).thenReturn("dami@unlam.com");
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        servicioLoginMock = mock(ServicioLogin.class);
        controladorLogin = new ControladorLogin(servicioLoginMock);
    }


	@Test
	public void loginConUsuarioYPasswordInorrectosDeberiaLlevarALoginNuevamente(){
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
	public void loginConUsuarioYPasswordCorrectosDeberiaLLevarAHome(){
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
	public void registrameSiUsuarioNoExisteDeberiaCrearUsuarioYVolverAlLogin() throws UsuarioExistente {

		String confirmPassword = usuarioMock.getPassword();
		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, confirmPassword);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/completarPerfil?id=0"));
		verify(servicioLoginMock, times(1)).registrar(usuarioMock,confirmPassword);
	}

	@Test
	public void registrarmeSiUsuarioExisteDeberiaVolverAFormularioYMostrarError() throws UsuarioExistente {
		// preparacion
		String confirmPassword = usuarioMock.getPassword();
		doThrow(UsuarioExistente.class).when(servicioLoginMock).registrar(usuarioMock, confirmPassword);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock,confirmPassword);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("El usuario ya existe"));
	}

	@Test
	public void errorEnRegistrarmeDeberiaVolverAFormularioYMostrarError() throws UsuarioExistente {
		// preparacion
		String confirmPassword = usuarioMock.getPassword();
		doThrow(RuntimeException.class).when(servicioLoginMock).registrar(usuarioMock, confirmPassword);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock,confirmPassword);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error al registrar el nuevo usuario"));
	}

	@Test
	public void registrarmeSiLasContrasenasNoCoincidenDeberiaVolverAFormularioYMostrarError() throws UsuarioExistente {
		// preparación
		String confirmPassword = "otraContraseña";
		doThrow(ContrasenasNoCoinciden.class).when(servicioLoginMock).registrar(usuarioMock, confirmPassword);

		// ejecución
		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, confirmPassword);

		// validación
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Las contraseñas no coinciden"));
	}

	@Test
	public void irALogin_DeberiaRetornarVistaLoginYModeloConDatosLogin() {
		ModelAndView modelAndView = controladorLogin.irALogin();

		assertThat(modelAndView.getViewName(), equalTo("login"));

		assertThat(modelAndView.getModel().containsKey("datosLogin"), equalTo(true));

		assertThat(modelAndView.getModel().get("datosLogin"), instanceOf(DatosLogin.class));
	}

	@Test
	public void nuevoUsuario_DeberiaRetornarVistaNuevoUsuarioYModeloUsuario() {
		ModelAndView modelAndView = controladorLogin.nuevoUsuario();

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));

		assertThat(modelAndView.getModel().containsKey("usuario"), equalTo(true));
		assertThat(modelAndView.getModel().get("usuario"), equalTo(new Usuario()));
	}

	@Test
	public void inicio_DeberiaRedirigirALogin() {
		ModelAndView modelAndView = controladorLogin.inicio();

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
	}

}
