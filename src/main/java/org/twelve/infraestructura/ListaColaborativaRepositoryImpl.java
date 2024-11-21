package org.twelve.infraestructura;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.twelve.dominio.ListaColaborativaRepository;
import org.twelve.dominio.entities.ListaColaborativa;
import org.twelve.dominio.entities.ListaMovie;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.Usuario;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository("listaColaborativaRepository")
@Transactional
public class ListaColaborativaRepositoryImpl implements ListaColaborativaRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public ListaColaborativaRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<ListaColaborativa> encontrarTodos() {
        String hql = "FROM ListaColaborativa";
        return sessionFactory.getCurrentSession().createQuery(hql, ListaColaborativa.class).getResultList();
    }

    @Override
    public ListaColaborativa guardar(ListaColaborativa listaColaborativa) {
        sessionFactory.getCurrentSession().saveOrUpdate(listaColaborativa);
        return listaColaborativa;
    }

    @Override
    public ListaColaborativa buscarPorId(Integer id) {
        return sessionFactory.getCurrentSession().get(ListaColaborativa.class, id);
    }

    @Override
    public void guardarListaPelicula(ListaMovie listaPelicula) {
        sessionFactory.getCurrentSession().saveOrUpdate(listaPelicula);
    }

    @Override
    public List<ListaColaborativa> buscarPorUsuarioId(Integer usuarioId) {
        String hql = "FROM ListaColaborativa l WHERE l.creador.id = :usuarioId OR l.colaborador.id = :usuarioId";
        Query query = sessionFactory.getCurrentSession().createQuery(hql, ListaColaborativa.class);
        query.setParameter("usuarioId", usuarioId);
        return query.getResultList();
    }

    @Override
    public List<ListaMovie> buscarPeliculasPorListaId(Integer listaId) {
        String hql = "FROM ListaMovie lm WHERE lm.lista.id = :listaId";
        Query query = sessionFactory.getCurrentSession().createQuery(hql, ListaMovie.class);
        query.setParameter("listaId", listaId);
        return query.getResultList();
    }

    @Override
    public boolean existePeliculaEnLista(Integer listaId, Integer movieId) {
        String hql = "SELECT COUNT(lm) FROM ListaMovie lm WHERE lm.lista.id = :listaId AND lm.pelicula.id = :movieId";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("listaId", listaId);
        query.setParameter("movieId", movieId);

        Long count = (Long) query.getSingleResult();
        return count > 0;
    }

    @Override
    public boolean existeListaConNombreParaUsuario(Integer usuarioId, String nombreLista) {
        String hql = "SELECT COUNT(l) FROM ListaColaborativa l WHERE l.creador.id = :usuarioId AND l.nombre = :nombreLista";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("usuarioId", usuarioId);
        query.setParameter("nombreLista", nombreLista);

        Long count = (Long) query.getSingleResult();
        return count > 0;
    }

    @Override
    public void eliminar(Integer listaId) {
        ListaColaborativa lista = buscarPorId(listaId);
        if (lista != null) {
            sessionFactory.getCurrentSession().delete(lista);
        }
    }

    @Override
    public void eliminarPelicula(ListaMovie listaMovie) {
        sessionFactory.getCurrentSession().delete(listaMovie);
    }


}
