package org.twelve.infraestructura;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.twelve.dominio.RepositorioUsuario;
import org.twelve.dominio.entities.Usuario;

import javax.persistence.Query;
import java.util.List;

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
    public Usuario guardar(Usuario usuario) {
        Query query = sessionFactory.getCurrentSession().createQuery("FROM Usuario WHERE email = :email");
        query.setParameter("email", usuario.getEmail());


        sessionFactory.getCurrentSession().save(usuario);
        return usuario;
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

        List<Usuario> usuarios = query.getResultList();
        if (usuarios.isEmpty()) {
            return null;  // Si no se encuentra el usuario, devuelve null
        }

        return usuarios.get(0);  // Devuelve el primer (y único) resultado
    }

    @Override
    public Usuario buscarPorId(Long id) {
        String hql = "FROM Usuario WHERE id = :id";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", id);

        return (Usuario) query.getSingleResult();
    }

    @Override
    public List<Usuario> encontrarTodos() {
        return List.of();
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        String hql = "FROM Usuario WHERE username = :username";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("username", username);
        return (Usuario) query.getSingleResult();
    }

}
