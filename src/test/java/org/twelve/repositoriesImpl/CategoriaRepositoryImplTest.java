package org.twelve.repositoriesImpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.twelve.dominio.entities.Categoria;
import org.twelve.infraestructura.CategoriaRepositoryImpl;
import org.twelve.integracion.config.HibernateTestConfig;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestConfig.class})
public class CategoriaRepositoryImplTest {

    @Autowired
    private SessionFactory sessionFactory;

    private CategoriaRepositoryImpl categoriaRepository;

    @BeforeEach
    public void init() {
        this.categoriaRepository = new CategoriaRepositoryImpl(sessionFactory);
    }

    @Test
    @Rollback
    @Transactional
    void testBuscarTodasLasCategoriasYQueEncuentreDos() {
        Session session = sessionFactory.getCurrentSession();

        Categoria categoria1 = new Categoria();
        categoria1.setNombre("ROMANCE");

        Categoria categoria2 = new Categoria();
        categoria2.setNombre("TERROR");

        session.save(categoria1);
        session.save(categoria2);
        session.flush();

        List<Categoria> categorias = categoriaRepository.findAll();

        assertNotNull(categorias);
        assertEquals(2, categorias.size());

        assertTrue(categorias.stream().anyMatch(c -> c.getNombre().equals("ROMANCE")));
        assertTrue(categorias.stream().anyMatch(c -> c.getNombre().equals("TERROR")));
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarTodasCategoriasPeroLaListaEstaVacia() {
        List<Categoria> categorias = categoriaRepository.findAll();

        assertNotNull(categorias);
        assertTrue(categorias.isEmpty(), "La lista debería estar vacía cuando no hay categorías en la base de datos");
    }

}
