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

}
