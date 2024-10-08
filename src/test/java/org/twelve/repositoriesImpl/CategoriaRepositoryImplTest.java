package org.twelve.repositoriesImpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.twelve.dominio.entities.Categoria;
import org.twelve.infraestructura.CategoriaRepositoryImpl;
import org.twelve.integracion.config.HibernateTestConfig;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestConfig.class})
public class CategoriaRepositoryImplTest {

    private Categoria categoria1;
    private Categoria categoria2;
    private SessionFactory sessionFactory;
    private Session session;
    private CategoriaRepositoryImpl categoriaRepository;

    @BeforeEach
    public void setUp() {
        categoria1 = mock(Categoria.class);
        categoria2 = mock(Categoria.class);
        sessionFactory = mock(SessionFactory.class);
        session = mock(Session.class);
        categoriaRepository = new CategoriaRepositoryImpl(sessionFactory);

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(categoria1.getId()).thenReturn(1);
        when(categoria2.getId()).thenReturn(2);
        when(categoria1.getNombre()).thenReturn("ROMANCE");
        when(categoria2.getNombre()).thenReturn("TERROR");
    }

    @Test
    public void testFindAll() {

        List<Categoria> categorias = Arrays.asList(categoria1, categoria2);

        // simulacion del objeto query
        org.hibernate.query.Query<Categoria> queryMock = mock(org.hibernate.query.Query.class);

        when(session.createQuery("from Categoria", Categoria.class)).thenReturn(queryMock);
        when(queryMock.list()).thenReturn(categorias);

        List<Categoria> result = categoriaRepository.findAll();

        assertEquals(2, result.size());
        assertEquals("ROMANCE", result.get(0).getNombre());
        assertEquals("TERROR", result.get(1).getNombre());

        verify(session, times(1)).createQuery("from Categoria", Categoria.class);
        verify(sessionFactory).getCurrentSession();
        verify(queryMock).list();
    }

}
