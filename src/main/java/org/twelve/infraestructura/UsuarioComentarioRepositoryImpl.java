package org.twelve.infraestructura;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.twelve.dominio.UsuarioComentarioRepository;
import org.twelve.dominio.entities.UsuarioComentario;

import javax.transaction.Transactional;

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
}
