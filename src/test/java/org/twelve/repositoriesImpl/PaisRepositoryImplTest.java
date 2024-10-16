package org.twelve.repositoriesImpl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.twelve.dominio.entities.Pais;
import org.twelve.infraestructura.PaisRepositoryImpl;
import org.twelve.integracion.config.HibernateTestConfig;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestConfig.class})
public class PaisRepositoryImplTest {

    @Autowired
    private SessionFactory sessionFactory;
    private PaisRepositoryImpl paisRepository;

    @BeforeEach
    public void init() {
        this.paisRepository = new PaisRepositoryImpl(sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarTodosLosPaisesSinResultados() {
        List<Pais> paises = paisRepository.findAll();
        assertTrue(paises.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindAllDevuelveListaVaciaSiNoHayPaises() {
        List<Pais> paises = paisRepository.findAll();
        assertTrue(paises.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindAllDevuelveListaCorrectaDespuesDeEliminarPais() {
        Pais pais = new Pais();
        pais.setNombre("Chile");
        sessionFactory.getCurrentSession().save(pais);

        sessionFactory.getCurrentSession().delete(pais);
        sessionFactory.getCurrentSession().flush();

        List<Pais> paises = paisRepository.findAll();
        assertTrue(paises.isEmpty());
    }
}
