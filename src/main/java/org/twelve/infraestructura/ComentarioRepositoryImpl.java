package org.twelve.infraestructura;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.twelve.dominio.ComentarioRepository;
import org.twelve.dominio.entities.Comentario;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ComentarioRepositoryImpl implements ComentarioRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public ComentarioRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List findByIdMovie(Integer idMovie) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM Comentario c WHERE c.movie.id = :idMovie";
        Query query = session.createQuery(hql, Comentario.class);
        query.setParameter("idMovie", idMovie);
        return query.getResultList();
    }

    @Override
    @Transactional
    public void save(Comentario comentario) {
        sessionFactory.getCurrentSession().save(comentario);
    }

}
