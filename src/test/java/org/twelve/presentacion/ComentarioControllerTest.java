package org.twelve.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.twelve.dominio.ComentarioService;
import org.twelve.presentacion.dto.ComentarioDTO;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class ComentarioControllerTest {

    private ComentarioController comentarioController;
    private HttpServletRequest requestMock;
    private ComentarioService comentarioService;


    @BeforeEach
    public void init() {
        comentarioService = mock(ComentarioService.class);
        comentarioController = new ComentarioController();
        comentarioController.comentarioService = comentarioService;
        requestMock = mock(HttpServletRequest.class);

    }

    @Test
    public void testGuardarComentarioDeberiaRedireccionarALaPelicula() {


        // preparacion
        ComentarioDTO comentarioDTO = new ComentarioDTO();
        comentarioDTO.setIdMovie(1);
        comentarioDTO.setIdUsuario(1);
        comentarioDTO.setDescripcion("buenisima");
        comentarioDTO.setLikes(0);
        comentarioDTO.setValoracion(8.0);

        // ejecucion
        String result = comentarioController.guardarComentario(comentarioDTO, requestMock);

        // validacion
        assertThat(result, is("redirect:/detalle-pelicula/1"));
        verify(comentarioService).agregarComentario(comentarioDTO);
    }
}
