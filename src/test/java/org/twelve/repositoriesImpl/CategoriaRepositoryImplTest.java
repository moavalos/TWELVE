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

}
