package org.twelve.infraestructura;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.twelve.dominio.MovieRepository;
import org.twelve.dominio.entities.Categoria;
import org.twelve.dominio.entities.Movie;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Repository("movieRepository")
@Transactional
public class MovieRepositoryImpl implements MovieRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public MovieRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Movie> findAll() {
        String hql = "FROM Movie";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        return query.getResultList();
    }

    @Override
    public Movie findById(Integer id) {
        String hql = "FROM Movie WHERE id = :id";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", id);
        return (Movie) query.getSingleResult();
    }

    @Override
    public Movie guardar(Movie movie) {
        sessionFactory.getCurrentSession().save(movie);
        return movie;
    }

    @Override
    public Movie actualizar(Movie movie) {
        Movie movieActualizar = findById(movie.getId());
        sessionFactory.getCurrentSession().update(movieActualizar);
        return movie;
    }

    @Override
    public List<Movie> findByTitle(String title) {
        String hql = "FROM Movie WHERE lower(nombre) LIKE :title";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("title", "%" + title.toLowerCase() + "%");
        return query.getResultList();
    }

    @Override
    public List<Movie> findMostViewed() {
        String hql = "FROM Movie ORDER BY cantVistas DESC";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setMaxResults(10);
        return query.getResultList();
    }

    @Override
    public List<Movie> findTopRated() {
        String hql = "FROM Movie ORDER BY valoracion DESC";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setMaxResults(10);
        return query.getResultList();
    }

    @Override
    public List<Movie> findByCategoriaId(Integer idCategoria) {
        String hql = "SELECT m FROM Movie m JOIN FETCH m.categorias c WHERE c.id = :idCategoria ";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idCategoria", idCategoria);
        return query.getResultList();
    }

    @Override
    public List<Movie> findByCategoriaIdTopRated(Integer idCategoria) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT m FROM Movie m JOIN FETCH m.categorias c WHERE c.id = :idCategoria ORDER BY m.valoracion DESC";
        return session.createQuery(hql, Movie.class)
                .setParameter("idCategoria", idCategoria)
                .list();
    }

    @Override
    public List<Movie> findByCategoriaIdNewest(Integer idCategoria) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT m FROM Movie m JOIN FETCH m.categorias c WHERE c.id = :idCategoria ORDER BY m.añoLanzamiento DESC";
        return session.createQuery(hql, Movie.class)
                .setParameter("idCategoria", idCategoria)
                .list();
    }


    @Override
    public List<Movie> findNewestMovie() {
        String hql = "FROM Movie ORDER BY añoLanzamiento DESC";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setMaxResults(10);
        return query.getResultList();
    }

    @Override
    public List<Movie> findSimilarMovies(Integer movieId, Set<Categoria> categorias) {
        String hql = "SELECT m FROM Movie m " +
                "JOIN m.categorias c " +
                "WHERE c IN :categorias AND m.id <> :movieId " +
                "GROUP BY m.id " +
                "ORDER BY COUNT(c) DESC";

        Query query = sessionFactory.getCurrentSession().createQuery(hql, Movie.class);

        query.setParameter("categorias", categorias);
        query.setParameter("movieId", movieId);

        query.setMaxResults(5);

        return query.getResultList();
    }

    @Override
    public List<Movie> findByPaisId(Integer idPais) {
        String hql = "SELECT m FROM Movie m WHERE m.pais.id = :idPais";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("idPais", idPais);
        return query.getResultList();
    }

    @Override
    public List<Movie> findByPaisIdTopRated(Integer idPais) {
        String hql = "SELECT m FROM Movie m WHERE m.pais.id = :idPais ORDER BY m.valoracion DESC";
        Query query = sessionFactory.getCurrentSession().createQuery(hql, Movie.class);
        query.setParameter("idPais", idPais);
        return query.getResultList();
    }

    @Override
    public List<Movie> findByPaisIdNewest(Integer idPais) {
        String hql = "SELECT m FROM Movie m WHERE m.pais.id = :idPais ORDER BY m.añoLanzamiento DESC";
        Query query = sessionFactory.getCurrentSession().createQuery(hql, Movie.class);
        query.setParameter("idPais", idPais);
        return query.getResultList();
    }


}


