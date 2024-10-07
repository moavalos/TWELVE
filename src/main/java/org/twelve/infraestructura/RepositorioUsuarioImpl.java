package org.twelve.infraestructura;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.twelve.dominio.RepositorioUsuario;
import org.twelve.dominio.entities.Usuario;

import javax.persistence.Query;

@Repository("repositorioUsuario")
public class RepositorioUsuarioImpl implements RepositorioUsuario {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioUsuarioImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Usuario buscarUsuario(String email, String password) {
        String hql = "FROM Usuario WHERE email = :email AND password = :password";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("email", email);
        query.setParameter("password", password);

        return (Usuario) query.getSingleResult();
    }

    @Override
    public void guardar(Usuario usuario) {
        sessionFactory.getCurrentSession().save(usuario);
    }

    @Override
    public Usuario buscar(String email) {
        String hql = "FROM Usuario WHERE email = :email";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("email", email);

        return (Usuario) query.getSingleResult();
    }

    @Override
    public void modificar(Usuario usuario) {
        sessionFactory.getCurrentSession().update(usuario);
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

        return (Usuario) query.getSingleResult();
    }

    @Override
    public Usuario buscarPorId(Long id) {
        String hql = "FROM Usuario WHERE id = :id";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", id);

        return (Usuario) query.getSingleResult();
    }

}
