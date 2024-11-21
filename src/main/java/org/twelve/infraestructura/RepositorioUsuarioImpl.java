package org.twelve.infraestructura;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.twelve.dominio.RepositorioUsuario;
import org.twelve.dominio.entities.Seguidor;
import org.twelve.dominio.entities.Usuario;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository("repositorioUsuario")
@Transactional
public class RepositorioUsuarioImpl implements RepositorioUsuario {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioUsuarioImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {
        sessionFactory.getCurrentSession().save(usuario);
        return usuario;
    }

    /*
     * createCriteria establece el tipo de entidad a consultar
     * Restrictions.eq("email", email) añade restrinccion para filtrar por mail
     * uniqueResult() devuelve el resultado o null
     */
    @Override
    public Usuario buscarUsuarioPorEmail(String email) {
        String hql = "FROM Usuario WHERE email = :email";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("email", email);

        List<Usuario> usuarios = query.getResultList();
        if (usuarios.isEmpty()) {
            return null;  // Si no se encuentra el usuario, devuelve null
        }

        return usuarios.get(0);  // Devuelve el primer (y único) resultado
    }

    @Override
    public Usuario buscarPorId(Integer id) {
        String hql = "FROM Usuario WHERE id = :id";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", id);

        return (Usuario) query.getSingleResult();
    }

    @Override
    public List<Usuario> encontrarTodos() {
        String hql = "FROM Usuario";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        return query.getResultList();
    }

    @Override
    public void seguirUsuario(Usuario usuario, Usuario seguido) {
        Seguidor seguidor = new Seguidor();
        seguidor.setUsuario(usuario);
        seguidor.setSeguido(seguido);
        sessionFactory.getCurrentSession().save(seguidor);
    }

    @Override
    public void dejarDeSeguirUsuario(Usuario usuario, Usuario seguido) {
        String hql = "DELETE FROM Seguidor WHERE usuario.id = :usuarioId AND seguido.id = :seguidoId";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("usuarioId", usuario.getId());
        query.setParameter("seguidoId", seguido.getId());
        query.executeUpdate();
    }

    @Override
    public Boolean estaSiguiendo(Usuario usuario, Usuario seguido) {
        String hql = "SELECT COUNT(*) FROM Seguidor WHERE usuario.id = :usuarioId AND seguido.id = :seguidoId";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("usuarioId", usuario.getId());
        query.setParameter("seguidoId", seguido.getId());
        Long count = (Long) query.getSingleResult();
        return count > 0;
    }

    @Override
    public List<Seguidor> obtenerSeguidores(Integer usuarioId) {
        String hql = "FROM Seguidor WHERE seguido.id = :usuarioId";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("usuarioId", usuarioId);
        return query.getResultList();
    }

    @Override
    public List<Seguidor> obtenerSeguidos(Integer usuarioId) {
        String hql = "FROM Seguidor WHERE usuario.id = :usuarioId";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("usuarioId", usuarioId);
        return query.getResultList();
    }

    @Override
    public boolean existeRelacion(Integer usuarioId, Integer seguidoId) {
        String hql = "SELECT COUNT(*) FROM Seguidor WHERE usuario.id = :usuarioId AND seguido.id = :seguidoId";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("usuarioId", usuarioId);
        query.setParameter("seguidoId", seguidoId);

        Long count = (Long) query.getSingleResult();
        return count > 0;
    }

    @Override
    public List<Usuario> obtenerUsuariosSeguidos(Integer usuarioId) {
        String hql = "SELECT s.seguido FROM Seguidor s WHERE s.usuario.id = :usuarioId";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("usuarioId", usuarioId);

        List<Usuario> usuariosSeguidos = query.getResultList();
        return usuariosSeguidos;
    }

}
