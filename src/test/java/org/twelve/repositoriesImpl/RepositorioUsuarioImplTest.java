package org.twelve.repositoriesImpl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.twelve.dominio.entities.Usuario;
import org.twelve.infraestructura.RepositorioUsuarioImpl;
import org.twelve.integracion.config.HibernateTestConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestConfig.class})
public class RepositorioUsuarioImplTest {

    private Usuario usuario;
    private SessionFactory sessionFactory;
    private Session session;
    private Criteria criteria;
    private RepositorioUsuarioImpl repositorioUsuario;

    @BeforeEach
    public void setUp() {
        usuario = mock(Usuario.class);
        sessionFactory = mock(SessionFactory.class);
        session = mock(Session.class);
        criteria = mock(Criteria.class);
        repositorioUsuario = new RepositorioUsuarioImpl(sessionFactory);

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.createCriteria(Usuario.class)).thenReturn(criteria);
    }

    @Test
    public void testBuscarUsuario() {
        when(criteria.add(any(Criterion.class))).thenReturn(criteria);
        when(criteria.uniqueResult()).thenReturn(usuario);

        Usuario result = repositorioUsuario.buscarUsuario("test@unlam.com", "password123");

        assertNotNull(result);
        verify(criteria, times(2)).add(any(Criterion.class));
        verify(criteria).uniqueResult();
    }

    @Test
    public void testGuardar() {
        repositorioUsuario.guardar(usuario);

        verify(sessionFactory).getCurrentSession();
        verify(session).save(usuario);
    }

    @Test
    public void testBuscar() {
        when(criteria.add(any(Criterion.class))).thenReturn(criteria);
        when(criteria.uniqueResult()).thenReturn(usuario);

        Usuario result = repositorioUsuario.buscar("test@unlam.com");

        assertNotNull(result);
        verify(sessionFactory).getCurrentSession();
        verify(session).createCriteria(Usuario.class);
        verify(criteria).add(any(Criterion.class));
        verify(criteria).uniqueResult();
    }

    @Test
    public void testModificar() {
        repositorioUsuario.modificar(usuario);

        verify(sessionFactory).getCurrentSession();
        verify(session).update(usuario);
    }

    @Test
    public void testBuscarUsuarioPorEmail() {
        when(criteria.uniqueResult()).thenReturn(usuario);

        Usuario result = repositorioUsuario.buscarUsuarioPorEmail("test@unlam.com");

        assertNotNull(result);
        verify(sessionFactory).getCurrentSession();
        verify(criteria).add(any(Criterion.class));
        verify(criteria).uniqueResult();
    }

    @Test
    public void testBuscarPorId() {
        when(criteria.add(any(Criterion.class))).thenReturn(criteria);
        when(criteria.uniqueResult()).thenReturn(usuario);

        Usuario result = repositorioUsuario.buscarPorId(1L);

        assertNotNull(result);
        verify(sessionFactory).getCurrentSession();
        verify(session).createCriteria(Usuario.class);
        verify(criteria).add(any(Criterion.class));
        verify(criteria).uniqueResult();
    }

}
