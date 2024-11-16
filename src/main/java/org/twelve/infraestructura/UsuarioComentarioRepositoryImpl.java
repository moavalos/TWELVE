package org.twelve.infraestructura;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.twelve.dominio.UsuarioComentarioRepository;
import org.twelve.dominio.entities.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("usuarioComentarioRepository")
@Transactional
public class UsuarioComentarioRepositoryImpl implements UsuarioComentarioRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UsuarioComentarioRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public long obtenerCantidadDelikes(Comentario comentario) {
        if (comentario == null) {
            throw new IllegalArgumentException("El comentario no puede ser nulo");
        }

        String hql = "SELECT COUNT(uml.id) FROM UsuarioComentario uml WHERE uml.comentario = :comentario AND uml.esLike = TRUE";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("comentario", comentario);

        Long count = (Long) query.uniqueResult();
        return count != null ? count : 0;
    }

    @Override
    @Transactional
    public Optional<UsuarioComentario> buscarMeGustaPorUsuario(Usuario usuario, Comentario comentario) {
        if (usuario == null)
            throw new IllegalArgumentException("Usuario no puede ser nulo");

        if (comentario == null)
            throw new IllegalArgumentException("comentario no puede ser nula");

        String hql = "FROM UsuarioComentario uml WHERE uml.usuario = :usuario AND uml.comentario = :comentario AND uml.esLike = true";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("usuario", usuario);
        query.setParameter("comentario", comentario);

        UsuarioComentario result = (UsuarioComentario) query.uniqueResult();
        return Optional.ofNullable(result);
    }

    @Override
    public void guardar(UsuarioComentario usuarioComentario) {
        if (usuarioComentario.getUsuario() == null)
            throw new IllegalArgumentException("El usuario no puede ser nulo");

        if (usuarioComentario.getComentario() == null)
            throw new IllegalArgumentException("El comentario no puede ser nulo");

        sessionFactory.getCurrentSession().saveOrUpdate(usuarioComentario);
    }

    @Override
    @Transactional
    public void borrarMeGusta(UsuarioComentario usuarioComentario) {
        if (usuarioComentario == null)
            throw new IllegalArgumentException("La entidad UsuarioComentario no puede ser nula");

        if (usuarioComentario.getUsuario() == null)
            throw new IllegalArgumentException("El usuario no puede ser nulo");

        if (usuarioComentario.getComentario() == null)
            throw new IllegalArgumentException("El comentario no puede ser nulo");

        sessionFactory.getCurrentSession().delete(usuarioComentario);
    }

    @Override
    @Transactional
    public List<Comentario> obtenerComentariosLikeados(Integer usuarioId) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("El ID de usuario no puede ser nulo");
        }
        if (usuarioId < 0) {
            throw new IllegalArgumentException("El ID de usuario no puede ser negativo");
        }

        String hql = "SELECT up.comentario FROM UsuarioComentario up WHERE up.usuario.id = :usuarioId AND up.esLike = true";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("usuarioId", usuarioId);

        List<Comentario> comentariosLikeados = query.getResultList();
        return comentariosLikeados != null ? comentariosLikeados : new ArrayList<>();
    }

    @Override
    @Transactional
    public boolean obtenerUsuarioYComentarioLikeado(Usuario usuario, Comentario comentario) {
        String hql = "SELECT COUNT(1) FROM UsuarioComentario uc WHERE uc.usuario = :usuario AND uc.comentario = :comentario AND uc.esLike = true";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("usuario", usuario);
        query.setParameter("comentario", comentario);
        Long count = (Long) query.uniqueResult();
        return count != null && count > 0;
    }


}
