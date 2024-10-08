package org.twelve.infraestructura;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.twelve.dominio.RepositorioUsuario;
import org.twelve.dominio.entities.Usuario;

import java.util.List;

@Repository("UsuarioRepository")
public class RepositorioUsuarioImpl implements RepositorioUsuario {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioUsuarioImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Usuario> encontrarTodos() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Usuario", Usuario.class).list();
    }

    @Override
    public Usuario buscarPorId(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Usuario.class, id);
    }

    @Override
    public Usuario guardar(Usuario Usuario) {
        Session session = sessionFactory.getCurrentSession();
        session.save(Usuario);
        return Usuario;
    }

    @Override
    public List<Usuario> buscarPorUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "from Usuario where lower(username) like :username";
        return session.createQuery(hql, Usuario.class)
                .setParameter("username", "%" + username.toLowerCase() + "%")
                .list();
    }

    @Override
    public Usuario buscarUsuarioPorEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "from Usuario where lower(email) like :email";
        return (Usuario) session.createQuery(hql, Usuario.class)
                .setParameter("email", "%" + email.toLowerCase() + "%")
                .list();
    }

    @Override
    public void modificar(Usuario usuario) {

    }


}
