package org.twelve.infraestructura;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.twelve.dominio.MovieRepository;
import org.twelve.dominio.entities.Movie;

import java.util.List;

@Repository("movieRepository")
public class MovieRepositoryImpl implements MovieRepository {

    private SessionFactory sessionFactory;

    @Autowired
    public MovieRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Movie> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Movie", Movie.class).list();
    }

    @Override
    public Movie findById(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Movie.class, id);
    }

    @Override
    public Movie save(Movie movie) {
        Session session = sessionFactory.getCurrentSession();
        session.save(movie);
        return movie;
    }

    @Override
    public List<Movie> findByTitle(String title) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "from Movie where nombre like :title";
        return session.createQuery(hql, Movie.class)
                .setParameter("title", "%" + title + "%")
                .list();
    }

    @Override
    public List<Movie> findMostViewed() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "from Movie order by cantVistas desc";
        return session.createQuery(hql, Movie.class).setMaxResults(10).list();
    }

    @Override
    public List<Movie> findTopRated() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "from Movie order by valoracion desc";
        return session.createQuery(hql, Movie.class).setMaxResults(10).list();
    }

    @Override
    public List<Movie> findByCategoriaId(Integer idCategoria) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "from Movie where idCategoria = :idCategoria";
        return session.createQuery(hql, Movie.class)
                .setParameter("idCategoria", idCategoria)
                .list();
    }

    @Override
    public List<Movie> findNewestMovie() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "from Movie order by añoLanzamiento desc";
        return session.createQuery(hql, Movie.class).setMaxResults(10).list();
    }
}


