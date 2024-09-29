package org.twelve.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.UsuarioService;
import org.twelve.dominio.entities.Usuario;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UsuarioControllerTest {

    private UsuarioController usuarioController;
    private UsuarioService usuarioServiceMock;
    private Usuario usuarioMock;

    @BeforeEach
    public void setUp() {
        usuarioServiceMock = mock(UsuarioService.class);
        usuarioMock = mock(Usuario.class);
        usuarioController = new UsuarioController(usuarioServiceMock);
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
    public void mostrarCompletarPerfilUsuarioEncontradoDebeDevolverVista() {
        // preparación
        when(usuarioServiceMock.buscarPorId(anyLong())).thenReturn(usuarioMock);

        // ejecución
        ModelAndView modelAndView = usuarioController.mostrarCompletarPerfil(usuarioMock.getId());

        // validación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("usuario-datos"));
        assertThat(modelAndView.getModel().get("usuario"), equalTo(usuarioMock));
    }

    @Test
    public void completarPerfilConExitoDebeRedirigirAlLogin() {
        // preparación
        when(usuarioServiceMock.buscarPorId(anyLong())).thenReturn(usuarioMock);

        // ejecución
        ModelAndView modelAndView = usuarioController.completarPerfil(usuarioMock);

        // validación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
        verify(usuarioServiceMock, times(1)).actualizarPerfil(usuarioMock);
    }

    @Test
    public void completarPerfilConErrorDebeDevolverError() {

        when(usuarioServiceMock.buscarPorId(anyLong())).thenReturn(usuarioMock);
        doThrow(new RuntimeException()).when(usuarioServiceMock).actualizarPerfil(usuarioMock);

        // ejecución
        ModelAndView modelAndView = usuarioController.completarPerfil(usuarioMock);

        // validación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("usuario-datos"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error al completar el perfil"));
    }
}
