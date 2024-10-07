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
    public void testGuardarUsuarioGuardaCorrectamente() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");
        usuario.setPassword("12345");

        repositorioUsuario.guardar(usuario);

        String hql = "FROM Usuario WHERE email = :email";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("email", "test@test.com");

        Usuario foundUsuario = (Usuario) query.getSingleResult();
        assertNotNull(foundUsuario);
        assertEquals("test@test.com", foundUsuario.getEmail());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarUsuarioPorEmailYPasswordDevuelveCorrectamente() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");
        usuario.setPassword("12345");
        repositorioUsuario.guardar(usuario);

        Usuario foundUsuario = repositorioUsuario.buscarUsuario("test@test.com", "12345");
        assertNotNull(foundUsuario);
        assertEquals("test@test.com", foundUsuario.getEmail());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarUsuarioPorEmailDevuelveCorrectamente2() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");
        usuario.setPassword("12345");
        repositorioUsuario.guardar(usuario);

        Usuario foundUsuario = repositorioUsuario.buscar("test@test.com");
        assertNotNull(foundUsuario);
        assertEquals("test@test.com", foundUsuario.getEmail());
    }

    @Test
    @Transactional
    @Rollback
    public void testModificarUsuarioModificaCorrectamente() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");
        usuario.setPassword("12345");
        repositorioUsuario.guardar(usuario);

        usuario.setPassword("newpassword");
        repositorioUsuario.modificar(usuario);

        String hql = "FROM Usuario WHERE email = :email";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("email", "test@test.com");

        Usuario modifiedUsuario = (Usuario) query.getSingleResult();
        assertEquals("newpassword", modifiedUsuario.getPassword());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarUsuarioPorIdDevuelveCorrectamente() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");
        usuario.setPassword("12345");
        repositorioUsuario.guardar(usuario);

        Usuario foundUsuario = repositorioUsuario.buscarPorId(usuario.getId());
        assertNotNull(foundUsuario);
        assertEquals(usuario.getId(), foundUsuario.getId());
        assertEquals("test@test.com", foundUsuario.getEmail());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarUsuarioPorEmailDevuelveCorrectamente() {
        Usuario usuario = new Usuario();
        usuario.setEmail("email@correo.com");
        usuario.setPassword("contraseña");
        repositorioUsuario.guardar(usuario);

        Usuario foundUsuario = repositorioUsuario.buscarUsuarioPorEmail("email@correo.com");
        assertNotNull(foundUsuario);
        assertEquals("email@correo.com", foundUsuario.getEmail());
    }

}
