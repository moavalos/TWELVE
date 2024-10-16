package org.twelve.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.PaisRepository;
import org.twelve.dominio.CategoriaService;
import org.twelve.dominio.MovieService;
import org.twelve.dominio.ServicioLogin;
import org.twelve.dominio.UsuarioService;
import org.twelve.dominio.entities.Usuario;
import org.twelve.presentacion.dto.PerfilDTO;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
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
}