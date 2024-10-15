package org.twelve.infraestructura;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.twelve.dominio.PaisRepository;
import org.twelve.dominio.entities.Pais;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public class PaisRepositoryImpl implements PaisRepository {

    private SessionFactory sessionFactory;

    @Autowired
    public PaisRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<Pais> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Pais", Pais.class).list();
    }
}
