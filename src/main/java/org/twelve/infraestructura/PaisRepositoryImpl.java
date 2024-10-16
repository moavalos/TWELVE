package org.twelve.infraestructura;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.twelve.dominio.PaisRepository;
import org.twelve.dominio.entities.Pais;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository("paisRepository")
@Transactional
public class PaisRepositoryImpl implements PaisRepository {

    private SessionFactory sessionFactory;

    @Autowired
    public PaisRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Pais> findAll() {
        String hql = "FROM Pais";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        return query.getResultList();
    }
}
