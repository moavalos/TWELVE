package org.twelve.infraestructura;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.twelve.dominio.CategoriaRepository;
import org.twelve.dominio.entities.Categoria;

import java.util.List;

@Repository("categoriaRepository")
public class CategoriaRepositoryImpl implements CategoriaRepository {

    private SessionFactory sessionFactory;

    @Autowired
    public CategoriaRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Categoria> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Categoria", Categoria.class).list();
    }
}
