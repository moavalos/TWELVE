package org.twelve.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.twelve.dominio.ServicioLogin;
import org.twelve.dominio.UsuarioService;
import org.twelve.dominio.entities.Usuario;
import org.twelve.presentacion.dto.PerfilDTO;

import static org.mockito.Mockito.mock;

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
}