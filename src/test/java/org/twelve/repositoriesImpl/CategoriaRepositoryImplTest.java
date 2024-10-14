package org.twelve.repositoriesImpl;

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

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;

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
    public void dadoQueExistenCategoriasDebeDevolverTodasLasCategorias() {
        Categoria categoria1 = new Categoria();
        categoria1.setNombre("Electronica");
        this.sessionFactory.getCurrentSession().save(categoria1);

        Categoria categoria2 = new Categoria();
        categoria2.setNombre("Ropa");
        this.sessionFactory.getCurrentSession().save(categoria2);

        List<Categoria> categorias = new ArrayList<>();
        categorias.add(categoria1);
        categorias.add(categoria2);

        this.categoriaRepository.findAll();

        assertThat(categorias.size(), equalTo(2));
        assertThat(categorias.get(0).getNombre(), equalToIgnoringCase("Electronica"));
        assertThat(categorias.get(1).getNombre(), equalToIgnoringCase("Ropa"));
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueNoExistenCategoriasDebeDevolverUnaListaVacia() {
        List<Categoria> categorias = new ArrayList<>();
        this.categoriaRepository.findAll();
        assertThat(categorias.size(), equalTo(0));
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueSeGuardaUnaCategoriaDebePoderBuscarsePorSuNombre() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Juguetes");
        this.sessionFactory.getCurrentSession().save(categoria);

        String hql = "FROM Categoria WHERE nombre = :nombre";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("nombre", "Juguetes");

        Categoria foundCategoria = (Categoria) query.getSingleResult();
        assertThat(foundCategoria.getNombre(), equalToIgnoringCase("Juguetes"));
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueSeActualizaUnaCategoriaDebeModificarElNombreCorrectamente() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Tecnología");
        this.sessionFactory.getCurrentSession().save(categoria);

        categoria.setNombre("Tecnología y Gadgets");
        this.sessionFactory.getCurrentSession().update(categoria);

        String hql = "FROM Categoria WHERE nombre = :nombre";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("nombre", "Tecnología y Gadgets");

        Categoria updatedCategoria = (Categoria) query.getSingleResult();
        assertThat(updatedCategoria.getNombre(), equalToIgnoringCase("Tecnología y Gadgets"));
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueSeEliminaUnaCategoriaDebeDejarDeExistir() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Alimentos");
        this.sessionFactory.getCurrentSession().save(categoria);

        this.sessionFactory.getCurrentSession().delete(categoria);
        this.sessionFactory.getCurrentSession().flush();

        String hql = "FROM Categoria WHERE nombre = :nombre";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("nombre", "Alimentos");

        List<Categoria> foundCategorias = query.getResultList();
        assertThat(foundCategorias.size(), equalTo(0));
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueExistenCategoriasDuplicadasDebeEncontrarAmbas() {
        Categoria categoria1 = new Categoria();
        categoria1.setNombre("Libros");
        this.sessionFactory.getCurrentSession().save(categoria1);

        Categoria categoria2 = new Categoria();
        categoria2.setNombre("Libros");
        this.sessionFactory.getCurrentSession().save(categoria2);

        String hql = "FROM Categoria WHERE nombre = :nombre";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("nombre", "Libros");

        List<Categoria> foundCategorias = query.getResultList();
        assertThat(foundCategorias.size(), equalTo(2));
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueNoExisteLaCategoriaDebeDevolverUnaListaVacia() {
        String hql = "FROM Categoria WHERE nombre = :nombre";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("nombre", "CategoriaInexistente");

        List<Categoria> foundCategorias = query.getResultList();
        assertThat(foundCategorias.size(), equalTo(0));
    }

}
