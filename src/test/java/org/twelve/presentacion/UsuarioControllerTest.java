package org.twelve.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.ServicioLogin;
import org.twelve.dominio.UsuarioService;
import org.twelve.dominio.entities.Usuario;
import org.twelve.presentacion.dto.PerfilDTO;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class UsuarioControllerTest {

    private UsuarioController usuarioController;
    private UsuarioService usuarioServiceMock;
    private ServicioLogin servicioLoginMock;
    private PerfilDTO perfilMock;
    private Usuario usuarioMock;

    @BeforeEach
    public void setUp() {
        usuarioServiceMock = mock(UsuarioService.class);
        usuarioMock = mock(Usuario.class);
        perfilMock = mock(PerfilDTO.class);
        servicioLoginMock = mock(ServicioLogin.class);
        usuarioController = new UsuarioController(usuarioServiceMock, servicioLoginMock);
    }

    @Test
    public void mostrarCompletarPerfilUsuarioNoEncontradoDebeDevolverError() {

        // ejecución
        ModelAndView modelAndView = usuarioController.mostrarCompletarPerfil(null);

        // validación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("usuario-datos"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Usuario no encontrado"));
    }

    @Test
    @Disabled
    public void mostrarCompletarPerfilUsuarioEncontradoDebeDevolverVista() {
        // preparación
        when(usuarioServiceMock.buscarPorId(anyInt())).thenReturn(perfilMock);

        // ejecución
        ModelAndView modelAndView = usuarioController.mostrarCompletarPerfil(perfilMock.getId());

        // validación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("usuario-datos"));
        assertThat(modelAndView.getModel().get("perfil"), equalTo(perfilMock));
    }
}