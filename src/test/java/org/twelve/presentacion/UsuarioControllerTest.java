package org.twelve.presentacion;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.CategoriaService;
import org.twelve.dominio.MovieService;
import org.twelve.dominio.UsuarioService;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.Usuario;
import org.twelve.presentacion.MovieController;
import org.twelve.presentacion.UsuarioController;
import org.twelve.presentacion.dto.CategoriaDTO;
import org.twelve.presentacion.dto.MovieDTO;
import org.twelve.presentacion.dto.PerfilDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UsuarioControllerTest {


    private UsuarioController usuarioController;
    private Usuario usuario;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private UsuarioService usuarioService;



    @BeforeEach
    public void init() {
        // Crear un usuario simulado
        usuario = mock(Usuario.class);

        // Mock para la solicitud y la sesión
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);

        // Mock del servicio
        usuarioService = mock(UsuarioService.class);

        // Mock de los perfiles
        PerfilDTO perfil1 = mock(PerfilDTO.class);
        when(perfil1.getDescripcion()).thenReturn("Perfil 1");
        PerfilDTO perfil2 = mock(PerfilDTO.class);
        when(perfil2.getDescripcion()).thenReturn("Perfil 2");
        List<PerfilDTO> perfiles = Arrays.asList(perfil1, perfil2);

        // Configurar el mock del servicio para que devuelva la lista de perfiles
        when(usuarioService.encontrarTodos()).thenReturn(perfiles);

        // Inicializar el controlador
        usuarioController = new UsuarioController(usuarioService);
    }

    // Todo casos

    @Test
        public void testObtenerTodosLosPerfiles() {
            // Preparación: Crear una lista de perfiles simulados
            List<PerfilDTO> perfiles = Arrays.asList(mock(PerfilDTO.class), mock(PerfilDTO.class));

            // Configurar el mock del servicio para que devuelva la lista de perfiles
            when(usuarioService.encontrarTodos()).thenReturn(perfiles);

            // Ejecución: Obtener la lista de perfiles
            List<PerfilDTO> resultados = usuarioService.encontrarTodos();

            // Validación: Verificar que se encontraron todos los perfiles
            assertThat(resultados.size(), is(2)); // Verifica que se encontraron 2 perfiles
            assertThat(resultados, is(perfiles)); // Verifica que los resultados son los mismos que los perfiles mockeados
        }


   /* @Test
    public void testObtenerUsuarioPorId_IdValido() {
        PerfilDTO usuarioMock = mock(PerfilDTO.class);
        when(usuarioService.buscarPorId(1L)).thenReturn(usuarioMock);

        ResponseEntity<PerfilDTO> response = usuarioController.buscarPorId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }*/

    /*@Test
    public void testObtenerUsuarioPorId_IdInvalido() {
        when(usuarioService.buscarPorId(99L)).thenReturn(null);

        ResponseEntity<PerfilDTO> response = usuarioController.buscarPorId(99L);

        assertEquals(404, response.getStatusCodeValue());
    }*/



    @Test
    public void testActualizarUsuario_IdValido() {
        PerfilDTO usuarioMock = mock(PerfilDTO.class);
        when(usuarioService.buscarPorId(1L)).thenReturn(usuarioMock);
        when(usuarioService.crear(any(PerfilDTO.class))).thenReturn(usuarioMock);

        ResponseEntity<PerfilDTO> response = usuarioController.actualizarPerfil(1L, usuarioMock);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    public void testActualizarUsuario_IdInvalido() {
        PerfilDTO usuarioMock = mock(PerfilDTO.class);
        when(usuarioService.buscarPorId(99L)).thenReturn(null);

        ResponseEntity<PerfilDTO> response = usuarioController.actualizarPerfil(99L, usuarioMock);


        assertEquals(404, response.getStatusCodeValue());
    }

}
