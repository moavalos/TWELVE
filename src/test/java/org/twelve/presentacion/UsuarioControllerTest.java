package org.twelve.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.PaisRepository;
import org.twelve.dominio.CategoriaService;
import org.twelve.dominio.MovieService;
import org.twelve.dominio.ServicioLogin;
import org.twelve.dominio.UsuarioService;
import org.twelve.dominio.entities.Usuario;
import org.twelve.presentacion.dto.PerfilDTO;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UsuarioControllerTest {


    private PaisRepository paisRepositoryMock;
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
        paisRepositoryMock = mock(PaisRepository.class);
        usuarioController = new UsuarioController(usuarioServiceMock, paisRepositoryMock, servicioLoginMock);
    }

    @Test
    public void testBuscarPorId_usuarioEncontrado() {
        // Arrange
        Integer id = 1;
        PerfilDTO perfilDTO = new PerfilDTO();
        perfilDTO.setCantidadPeliculasVistas(10);
        perfilDTO.setCantidadPeliculasVistasEsteAno(5);


        when(usuarioServiceMock.buscarPorId(id)).thenReturn(perfilDTO);

        // Act
        ModelAndView modelAndView = usuarioController.buscarPorId(id);

        // Assert
        assertEquals("perfil", modelAndView.getViewName());
        ModelMap modelMap = modelAndView.getModelMap();
        assertEquals(perfilDTO, modelMap.get("usuario"));
        assertEquals(10, modelMap.get("cantidadPeliculasVistas"));
        assertEquals(5, modelMap.get("cantidadPeliculasVistasEsteAno"));
       
    }

    @Test
    public void testBuscarPorId_usuarioNoEncontrado() {
        // Arrange
        Integer id = 1;

        when(usuarioServiceMock.buscarPorId(id)).thenReturn(null);

        // Act
        ModelAndView modelAndView = usuarioController.buscarPorId(id);

        // Assert
        assertEquals("perfil", modelAndView.getViewName());
        assertEquals("Usuario no encontrado", modelAndView.getModelMap().get("error"));
    }
}