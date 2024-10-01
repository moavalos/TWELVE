package org.twelve.servicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.twelve.dominio.RepositorioUsuario;
import org.twelve.dominio.entities.Usuario;
import org.twelve.dominio.excepcion.ContrasenasNoCoinciden;
import org.twelve.dominio.excepcion.UsuarioExistente;
import org.twelve.dominio.serviceImpl.ServicioLoginImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServicioLoginImplTest {

    private Usuario usuarioMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private ServicioLoginImpl servicioLogin;
    private RepositorioUsuario repositorioUsuarioMock;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void init() {
        usuarioMock = mock(Usuario.class);
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        repositorioUsuarioMock = mock(RepositorioUsuario.class);
        passwordEncoder = new BCryptPasswordEncoder();
        servicioLogin = new ServicioLoginImpl(repositorioUsuarioMock);
    }

    @Test
    public void testRegistrarUsuarioNuevo() throws UsuarioExistente {
        String email = "test@unlam.com";
        String password = "password";
        String confirmPassword = "password";

        when(usuarioMock.getEmail()).thenReturn(email);
        when(usuarioMock.getPassword()).thenReturn(password);
        when(repositorioUsuarioMock.buscarUsuarioPorEmail(email)).thenReturn(null);

        servicioLogin.registrar(usuarioMock, confirmPassword);

        verify(repositorioUsuarioMock).guardar(any(Usuario.class));
        assertNotNull(usuarioMock.getPassword());
        assertFalse(passwordEncoder.matches(password, usuarioMock.getPassword()));
    }

    @Test
    public void testConsultarUsuarioContrasenaCorrecta() {
        String email = "test@unlam.com";
        String password = "password";
        String passwordEncriptada = passwordEncoder.encode(password);

        when(repositorioUsuarioMock.buscarUsuarioPorEmail(email)).thenReturn(usuarioMock);
        when(usuarioMock.getPassword()).thenReturn(passwordEncriptada);

        Usuario result = servicioLogin.consultarUsuario(email, password);

        assertNotNull(result);
        assertEquals(usuarioMock, result);
    }

    @Test
    public void testConsultarUsuarioContrasenaIncorrecta() {
        String email = "test@unlam.com";
        String password = "wrongPassword";
        String passwordEncriptada = passwordEncoder.encode("password");

        when(repositorioUsuarioMock.buscarUsuarioPorEmail(email)).thenReturn(usuarioMock);
        when(usuarioMock.getPassword()).thenReturn(passwordEncriptada);

        Usuario result = servicioLogin.consultarUsuario(email, password);

        assertNull(result);
    }

    @Test
    public void testRegistrarContrasenasNoCoinciden() {
        String email = "test@unlam.com";
        String password = "password";
        String confirmPassword = "differentPassword";

        when(usuarioMock.getEmail()).thenReturn(email);
        when(usuarioMock.getPassword()).thenReturn(password);

        Exception exception = assertThrows(ContrasenasNoCoinciden.class, () -> {
            servicioLogin.registrar(usuarioMock, confirmPassword);
        });

        assertEquals("Las contraseñas no coinciden", exception.getMessage());
    }

    @Test
    public void testConsultarUsuarioContrasenaCorrecta1() {
        String email = "test@unlam.com";
        String password = "password";
        String passwordEncriptada = passwordEncoder.encode(password);

        when(repositorioUsuarioMock.buscarUsuarioPorEmail(email)).thenReturn(usuarioMock);
        when(usuarioMock.getPassword()).thenReturn(passwordEncriptada);

        Usuario resultado = servicioLogin.consultarUsuario(email, password);

        assertNotNull(resultado);
        assertEquals(usuarioMock, resultado);
    }

    @Test
    public void testConsultarUsuarioContrasenaIncorrecta2() {
        String email = "test@unlam.com";
        String passwordIncorrecta = "wrongPassword";
        String passwordEncriptada = passwordEncoder.encode("password");

        when(repositorioUsuarioMock.buscarUsuarioPorEmail(email)).thenReturn(usuarioMock);
        when(usuarioMock.getPassword()).thenReturn(passwordEncriptada);

        Usuario resultado = servicioLogin.consultarUsuario(email, passwordIncorrecta);

        assertNull(resultado);
    }

    @Test
    public void testConsultarUsuarioNoExiste() {
        String email = "test@unlam.com";
        String password = "password";

        when(repositorioUsuarioMock.buscarUsuarioPorEmail(email)).thenReturn(null);

        Usuario resultado = servicioLogin.consultarUsuario(email, password);

        assertNull(resultado);
    }
    @Test
    public void testVerificarUsuarioExistente_CuandoUsuarioYaExiste_DeberiaLanzarExcepcion() {
        String email = "test@unlam.com";

        when(usuarioMock.getEmail()).thenReturn(email);
        when(repositorioUsuarioMock.buscarUsuarioPorEmail(email)).thenReturn(usuarioMock);

        assertThrows(UsuarioExistente.class, () -> {
            servicioLogin.verificarUsuarioExistente(usuarioMock);
        });

        verify(repositorioUsuarioMock).buscarUsuarioPorEmail(email);
    }

    @Test
    public void testVerificarUsuarioExistente_CuandoUsuarioNoExiste_NoLanzaExcepcion() {
        String email = "nuevo@unlam.com";

        when(usuarioMock.getEmail()).thenReturn(email);
        when(repositorioUsuarioMock.buscarUsuarioPorEmail(email)).thenReturn(null);

        assertDoesNotThrow(() -> servicioLogin.verificarUsuarioExistente(usuarioMock));

        verify(repositorioUsuarioMock).buscarUsuarioPorEmail(email);
    }
}
