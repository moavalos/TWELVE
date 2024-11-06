package org.twelve.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.twelve.dominio.ComentarioService;
import org.twelve.dominio.entities.Comentario;
import org.twelve.presentacion.dto.ComentarioDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ComentarioControllerTest {

    private Comentario comentario;
    private ComentarioController comentarioController;
    private ComentarioService comentarioService;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;

    @BeforeEach
    public void init() {
        comentario = mock(Comentario.class);
        comentarioService = mock(ComentarioService.class);
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        comentarioController = new ComentarioController(comentarioService);
    }

    @Test
    public void testGuardarComentarioDeberiaRedireccionarALaPelicula() {
        ComentarioDTO comentarioDTO = new ComentarioDTO();
        comentarioDTO.setIdMovie(1);

        String resultado = comentarioController.guardarComentario(comentarioDTO, requestMock);

        verify(comentarioService, times(1)).agregarComentario(comentarioDTO);

        assertEquals("redirect:/detalle-pelicula/1", resultado);
    }
}
