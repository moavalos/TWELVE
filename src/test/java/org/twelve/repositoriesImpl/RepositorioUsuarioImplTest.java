package org.twelve.repositoriesImpl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.twelve.dominio.entities.Seguidor;
import org.twelve.dominio.entities.Usuario;
import org.twelve.infraestructura.RepositorioUsuarioImpl;
import org.twelve.integracion.config.HibernateTestConfig;

import javax.persistence.Query;
import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestConfig.class})
public class RepositorioUsuarioImplTest {

    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioUsuarioImpl repositorioUsuario;

    @BeforeEach
    public void setUp() {
        this.repositorioUsuario = new RepositorioUsuarioImpl(sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    public void testGuardarUsuarioGuardaCorrectamente() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@unlam.com");
        usuario.setPassword("12345");

        repositorioUsuario.guardar(usuario);

        String hql = "FROM Usuario WHERE email = :email";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("email", "test@unlam.com");

        Usuario usuarioEncontrado = (Usuario) query.getSingleResult();
        assertNotNull(usuarioEncontrado);
        assertEquals("test@unlam.com", usuarioEncontrado.getEmail());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarUsuarioPorIdDevuelveCorrectamente() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@unlam.com");
        usuario.setPassword("12345");
        repositorioUsuario.guardar(usuario);

        Usuario usuarioEncontrado = repositorioUsuario.buscarPorId(usuario.getId());
        assertNotNull(usuarioEncontrado);
        assertEquals(usuario.getId(), usuarioEncontrado.getId());
        assertEquals("test@unlam.com", usuarioEncontrado.getEmail());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarUsuarioPorEmailSeteandoContrasenaDevuelveCorrectamente() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("email@unlam.com");
        usuario.setPassword("contrasena");
        repositorioUsuario.guardar(usuario);

        Usuario usuarioEncontrado = repositorioUsuario.buscarUsuarioPorEmail("email@unlam.com");
        assertNotNull(usuarioEncontrado);
        assertEquals("email@unlam.com", usuarioEncontrado.getEmail());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPorEmailDevuelveNullSiNoExiste() {
        Usuario usuarioEncontrado = repositorioUsuario.buscarUsuarioPorEmail("inexistente@unlam.com");
        assertNull(usuarioEncontrado);
    }

    @Test
    @Transactional
    @Rollback
    public void testEncontrarTodosCuandoHayUsuariosDevuelveListaCorrecta() throws Exception {
        Usuario usuario1 = new Usuario();
        usuario1.setEmail("usuario1@unlam.com");
        usuario1.setPassword("12345");
        repositorioUsuario.guardar(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setEmail("usuario2@unlam.com");
        usuario2.setPassword("54321");
        repositorioUsuario.guardar(usuario2);

        List<Usuario> usuarios = repositorioUsuario.encontrarTodos();

        assertNotNull(usuarios);
        assertEquals(2, usuarios.size());
        assertTrue(usuarios.contains(usuario1));
        assertTrue(usuarios.contains(usuario2));
    }

    @Test
    @Transactional
    @Rollback
    public void testEncontrarTodosCuandoNoHayUsuariosDevuelveListaVacia() throws Exception {
        List<Usuario> usuarios = repositorioUsuario.encontrarTodos();

        assertNotNull(usuarios);
        assertTrue(usuarios.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testEncontrarTodosDevuelveListaConObjetosUsuarioCorrectos() throws Exception {
        Usuario usuario1 = new Usuario();
        usuario1.setEmail("user1@unlam.com");
        usuario1.setPassword("pass1");
        repositorioUsuario.guardar(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setEmail("user2@unlam.com");
        usuario2.setPassword("pass2");
        repositorioUsuario.guardar(usuario2);

        Usuario usuario3 = new Usuario();
        usuario3.setEmail("user3@unlam.com");
        usuario3.setPassword("pass3");
        repositorioUsuario.guardar(usuario3);

        List<Usuario> usuarios = repositorioUsuario.encontrarTodos();

        assertEquals(3, usuarios.size());
        assertTrue(usuarios.stream().anyMatch(u -> u.getEmail().equals("user1@unlam.com")));
        assertTrue(usuarios.stream().anyMatch(u -> u.getEmail().equals("user2@unlam.com")));
        assertTrue(usuarios.stream().anyMatch(u -> u.getEmail().equals("user3@unlam.com")));
    }

    @Test
    @Transactional
    @Rollback
    public void testEncontrarTodosConDiferentesContrasenasDevuelveUsuariosCorrectos() throws Exception {
        Usuario usuario1 = new Usuario();
        usuario1.setEmail("test1@unlam.com");
        usuario1.setPassword("password1");
        repositorioUsuario.guardar(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setEmail("test2@unlam.com");
        usuario2.setPassword("password2");
        repositorioUsuario.guardar(usuario2);

        List<Usuario> usuarios = repositorioUsuario.encontrarTodos();

        assertEquals(2, usuarios.size());
        assertTrue(usuarios.stream().anyMatch(u -> u.getPassword().equals("password1")));
        assertTrue(usuarios.stream().anyMatch(u -> u.getPassword().equals("password2")));
    }

    @Test
    @Transactional
    @Rollback
    public void testDejarDeSeguirUsuarioCuandoAmbosUsuariosExisten() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("user@unlam.com");
        usuario.setPassword("12345");
        repositorioUsuario.guardar(usuario);

        Usuario seguido = new Usuario();
        seguido.setEmail("seguido@unlam.com");
        seguido.setPassword("54321");
        repositorioUsuario.guardar(seguido);

        repositorioUsuario.seguirUsuario(usuario, seguido);

        repositorioUsuario.dejarDeSeguirUsuario(usuario, seguido);

        assertFalse(repositorioUsuario.estaSiguiendo(usuario, seguido));
    }

    @Test
    @Transactional
    @Rollback
    public void testDejarDeSeguirUsuarioCuandoNoEstabaSiguiendo() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("user@unlam.com");
        usuario.setPassword("12345");
        repositorioUsuario.guardar(usuario);

        Usuario seguido = new Usuario();
        seguido.setEmail("seguido@unlam.com");
        seguido.setPassword("54321");
        repositorioUsuario.guardar(seguido);

        repositorioUsuario.dejarDeSeguirUsuario(usuario, seguido);

        assertDoesNotThrow(() -> repositorioUsuario.dejarDeSeguirUsuario(usuario, seguido));
    }

    @Test
    @Transactional
    @Rollback
    public void testEstaSiguiendoCuandoElUsuarioSigueAlOtroDevuelveVerdadero() throws Exception {
        Usuario usuario1 = new Usuario();
        usuario1.setEmail("user1@unlam.com");
        usuario1.setPassword("pass1");
        repositorioUsuario.guardar(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setEmail("user2@unlam.com");
        usuario2.setPassword("pass2");
        repositorioUsuario.guardar(usuario2);

        Seguidor seguidor = new Seguidor();
        seguidor.setUsuario(usuario1);
        seguidor.setSeguido(usuario2);
        sessionFactory.getCurrentSession().persist(seguidor);

        Boolean resultado = repositorioUsuario.estaSiguiendo(usuario1, usuario2);
        assertTrue(resultado);
    }

    @Test
    @Transactional
    @Rollback
    public void testEstaSiguiendoCuandoElUsuarioNoSigueAlOtroDevuelveFalso() throws Exception {
        Usuario usuario1 = new Usuario();
        usuario1.setEmail("user1@unlam.com");
        usuario1.setPassword("pass1");
        repositorioUsuario.guardar(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setEmail("user2@unlam.com");
        usuario2.setPassword("pass2");
        repositorioUsuario.guardar(usuario2);

        Boolean resultado = repositorioUsuario.estaSiguiendo(usuario1, usuario2);
        assertFalse(resultado);
    }

    @Test
    @Transactional
    @Rollback
    public void testEstaSiguiendoCuandoElUsuarioYElSeguidoNoExistenDevuelveFalso() throws Exception {
        Usuario usuario1 = new Usuario();
        usuario1.setEmail("user1@unlam.com");
        usuario1.setPassword("pass1");
        repositorioUsuario.guardar(usuario1);

        Usuario usuarioInexistente = new Usuario();
        usuarioInexistente.setId(999);
        usuarioInexistente.setEmail("user999@unlam.com");

        Boolean resultado = repositorioUsuario.estaSiguiendo(usuario1, usuarioInexistente);
        assertFalse(resultado);
    }

    @Test
    @Transactional
    @Rollback
    public void testEstaSiguiendoCuandoAmbosUsuariosNoExistenDevuelveFalso() throws Exception {
        Usuario usuarioInexistente1 = new Usuario();
        usuarioInexistente1.setId(999);

        Usuario usuarioInexistente2 = new Usuario();
        usuarioInexistente2.setId(888);

        Boolean resultado = repositorioUsuario.estaSiguiendo(usuarioInexistente1, usuarioInexistente2);
        assertFalse(resultado);
    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerSeguidoresCuandoNoHaySeguidoresDevuelveListaVacia() throws Exception {
        Usuario seguido = new Usuario();
        seguido.setEmail("seguido@unlam.com");
        seguido.setPassword("12345");
        repositorioUsuario.guardar(seguido);

        List<Seguidor> seguidores = repositorioUsuario.obtenerSeguidores(seguido.getId());

        assertNotNull(seguidores);
        assertTrue(seguidores.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerSeguidoresConUsuarioInexistenteDevuelveListaVacia() throws Exception {
        List<Seguidor> seguidores = repositorioUsuario.obtenerSeguidores(999);

        assertNotNull(seguidores);
        assertTrue(seguidores.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerSeguidosCuandoNoHaySeguidosDevuelveListaVacia() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("usuario@unlam.com");
        usuario.setPassword("12345");
        repositorioUsuario.guardar(usuario);

        List<Seguidor> seguidos = repositorioUsuario.obtenerSeguidos(usuario.getId());

        assertNotNull(seguidos);
        assertTrue(seguidos.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerSeguidosConUsuarioInexistenteDevuelveListaVacia() throws Exception {
        List<Seguidor> seguidos = repositorioUsuario.obtenerSeguidos(999);

        assertNotNull(seguidos);
        assertTrue(seguidos.isEmpty());
    }
}
