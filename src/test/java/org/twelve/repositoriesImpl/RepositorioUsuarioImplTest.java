package org.twelve.repositoriesImpl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.twelve.dominio.entities.Usuario;
import org.twelve.infraestructura.RepositorioUsuarioImpl;
import org.twelve.integracion.config.HibernateTestConfig;

import javax.persistence.Query;
import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
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
    public void testBuscarUsuarioPorEmailYPasswordDevuelveCorrectamente() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@unlam.com");
        usuario.setPassword("12345");
        repositorioUsuario.guardar(usuario);

        Usuario usuarioEncontrado = repositorioUsuario.buscarUsuario("test@unlam.com", "12345");
        assertNotNull(usuarioEncontrado);
        assertEquals("test@unlam.com", usuarioEncontrado.getEmail());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarUsuarioSoloPorEmailDevuelveCorrectamente() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@unlam.com");
        usuario.setPassword("12345");
        repositorioUsuario.guardar(usuario);

        Usuario usuarioEncontrado = repositorioUsuario.buscar("test@unlam.com");
        assertNotNull(usuarioEncontrado);
        assertEquals("test@unlam.com", usuarioEncontrado.getEmail());
    }

    @Test
    @Transactional
    @Rollback
    public void testModificarUsuarioModificaCorrectamente() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@unlam.com");
        usuario.setPassword("12345");
        repositorioUsuario.guardar(usuario);

        usuario.setPassword("nuevacontrasena");
        repositorioUsuario.modificar(usuario);

        String hql = "FROM Usuario WHERE email = :email";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("email", "test@unlam.com");

        Usuario usuarioModificado = (Usuario) query.getSingleResult();
        assertEquals("nuevacontrasena", usuarioModificado.getPassword());
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
    public void testGuardarYBuscarMultiplesUsuarios() throws Exception {
        Usuario usuario1 = new Usuario();
        usuario1.setEmail("user1@unlam.com");
        usuario1.setPassword("contrasena1");

        Usuario usuario2 = new Usuario();
        usuario2.setEmail("user2@unlam.com");
        usuario2.setPassword("contrasena2");

        repositorioUsuario.guardar(usuario1);
        repositorioUsuario.guardar(usuario2);

        Usuario usuarioEncontrado = repositorioUsuario.buscar("user1@unlam.com");
        Usuario usuarioEncontrado2 = repositorioUsuario.buscar("user2@unlam.com");

        assertNotNull(usuarioEncontrado);
        assertNotNull(usuarioEncontrado2);
        assertEquals("user1@unlam.com", usuarioEncontrado.getEmail());
        assertEquals("user2@unlam.com", usuarioEncontrado2.getEmail());
    }

    @Test
    @Transactional
    @Rollback
    public void testGuardarYBuscarUsuarioConCaracteresEspeciales() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("especial+caracter@unlam.com");
        usuario.setPassword("contrasena");

        repositorioUsuario.guardar(usuario);

        Usuario usuarioEncontrado = repositorioUsuario.buscar("especial+caracter@unlam.com");
        assertNotNull(usuarioEncontrado);
        assertEquals("especial+caracter@unlam.com", usuarioEncontrado.getEmail());
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
    public void testModificarUsuarioConEmailDuplicadoNoSePermite() throws Exception {
        Usuario usuario1 = new Usuario();
        usuario1.setEmail("duplicado@unlam.com");
        usuario1.setPassword("contrasena1");
        repositorioUsuario.guardar(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setEmail("duplicado@unlam.com");
        usuario2.setPassword("contrasena2");

        Exception excepcion = assertThrows(Exception.class, () -> {
            repositorioUsuario.guardar(usuario2);
        });

        String esperado = "El email ya está en uso";
        String actual = excepcion.getMessage();

        assertTrue(actual.contains(esperado));
    }


}
