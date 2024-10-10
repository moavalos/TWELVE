package org.twelve.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

public class PerfilControllerTest {

    private PerfilController perfilController;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;

    @BeforeEach
    public void init() {
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        perfilController = new PerfilController();
    }

    /*@Test
    public void testMostrarPerfil() {
        ModelAndView modelAndView = perfilController.mostrarPerfil();

        assertThat(modelAndView.getViewName(), is("perfil"));
    }*/
}
