package org.twelve.infraestructura;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.twelve.dominio.CategoriaRepository;
import org.twelve.dominio.entities.Categoria;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository("categoriaRepository")
@Transactional
public class CategoriaRepositoryImpl implements CategoriaRepository {

    private SessionFactory sessionFactory;

    @Autowired
    public CategoriaRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Categoria> findAll() {
        String hql = "FROM Categoria";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        return query.getResultList();
    }

    @Override
    public Categoria save(Categoria categoria) {
        sessionFactory.getCurrentSession().save(categoria);
        return categoria;
    }
}
