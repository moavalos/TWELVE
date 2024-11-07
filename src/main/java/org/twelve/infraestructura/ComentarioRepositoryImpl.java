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

@Repository("comentarioRepository")
@Transactional
public class ComentarioRepositoryImpl implements ComentarioRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public ComentarioRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List findByIdMovie(Integer idMovie) {
        String hql = "FROM Comentario c WHERE c.movie.id = :idMovie";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idMovie", idMovie);
        return query.getResultList();
    }

    @Override
    public List<Comentario> findTop3ByUsuarioId(Integer idUsuario) {
        String hql = "FROM Comentario c WHERE c.usuario.id = :idUsuario ORDER BY c.id DESC ";
        Query query = sessionFactory.getCurrentSession().createQuery(hql, Comentario.class);
        query.setParameter("idUsuario", idUsuario);
        query.setMaxResults(3);

        return query.getResultList();
    }

    @Override
    public void save(Comentario comentario) {
        sessionFactory.getCurrentSession().save(comentario);
    }

}
