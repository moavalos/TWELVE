package org.twelve.infraestructura;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.twelve.dominio.UsuarioComentarioRepository;
import org.twelve.dominio.entities.UsuarioComentario;

import javax.transaction.Transactional;
import java.util.List;

@Repository("usuarioComentarioRepository")
@Transactional
public class UsuarioComentarioRepositoryImpl implements UsuarioComentarioRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UsuarioComentarioRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean existsByComentarioAndUsuario(Integer idComentario, Integer idUsuario) {
        String hql = "SELECT COUNT(cl) > 0 FROM UsuarioComentario cl WHERE cl.comentario.id = :idComentario AND cl.usuario.id = :idUsuario";
        return (boolean) sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("idComentario", idComentario)
                .setParameter("idUsuario", idUsuario)
                .uniqueResult();
    }

    @Override
    public void save(UsuarioComentario comentarioLike) {
        sessionFactory.getCurrentSession().save(comentarioLike);
    }


    @Override
    public UsuarioComentario findByComentarioAndUsuario(Integer idComentario, Integer idUsuario) {
        String hql = "FROM UsuarioComentario cl WHERE cl.comentario.id = :idComentario AND cl.usuario.id = :idUsuario";
        return (UsuarioComentario) sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("idComentario", idComentario)
                .setParameter("idUsuario", idUsuario)
                .uniqueResult();
    }

    @Override
    public void delete(UsuarioComentario comentarioLike) {
        sessionFactory.getCurrentSession().delete(comentarioLike);
    }

    @Override
    public List<Integer> findComentarioIdsByUsuarioId(Integer idUsuario) {
        String hql = "SELECT cl.comentario.id FROM UsuarioComentario cl WHERE cl.usuario.id = :idUsuario";
        return sessionFactory.getCurrentSession().createQuery(hql, Integer.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
    }

}
