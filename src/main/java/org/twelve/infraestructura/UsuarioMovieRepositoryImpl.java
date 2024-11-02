package org.twelve.infraestructura;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.twelve.dominio.UsuarioMovieRepository;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.Usuario;
import org.twelve.dominio.entities.UsuarioMovie;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("usuarioMovieRepository")
@Transactional
public class UsuarioMovieRepositoryImpl implements UsuarioMovieRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UsuarioMovieRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public Integer obtenerCantidadPeliculasVistas(Integer usuarioId) {
        String hql = "SELECT COUNT(up.id) FROM UsuarioMovie up WHERE up.usuario.id = :usuarioId";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("usuarioId", usuarioId);
        Long cantidad = (Long) query.uniqueResult();
        return cantidad != null ? cantidad.intValue() : 0;
    }

    @Override
    @Transactional
    public Integer obtenerCantidadPeliculasVistasEsteAno(Integer usuarioId) {
        String hql = "SELECT COUNT(up.id) FROM UsuarioMovie up WHERE up.usuario.id = :usuarioId AND up.fechaVista BETWEEN :inicioAno AND :finAno";

        LocalDate inicioAno = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate finAno = LocalDate.of(LocalDate.now().getYear(), 12, 31);

        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("usuarioId", usuarioId);
        query.setParameter("inicioAno", inicioAno);
        query.setParameter("finAno", finAno);

        Long cantidad = (Long) query.uniqueResult();
        return cantidad != null ? cantidad.intValue() : 0;
    }

    @Override
    @Transactional
    public List<Movie> obtenerPeliculasFavoritas(Integer usuarioId) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("El ID de usuario no puede ser nulo");
        }
        if (usuarioId < 0) {
            throw new IllegalArgumentException("El ID de usuario no puede ser negativo");
        }

        String hql = "SELECT up.pelicula FROM UsuarioMovie up WHERE up.usuario.id = :usuarioId AND up.esLike = true";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("usuarioId", usuarioId);

        List<Movie> peliculasFavoritas = query.getResultList();
        return peliculasFavoritas != null ? peliculasFavoritas : new ArrayList<>();
    }

    @Override
    @Transactional
    public long obtenerCantidadDeLikes(Movie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("La película no puede ser nula");
        }

        String hql = "SELECT COUNT(uml.id) FROM UsuarioMovie uml WHERE uml.pelicula = :movie AND uml.esLike = TRUE";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("movie", movie);

        Long count = (Long) query.uniqueResult();
        return count != null ? count : 0;
    }

    @Override
    @Transactional
    public Optional<UsuarioMovie> buscarMeGustaPorUsuario(Usuario usuario, Movie movie) {
        if (usuario == null)
            throw new IllegalArgumentException("Usuario no puede ser nulo");

        if (movie == null)
            throw new IllegalArgumentException("Película no puede ser nula");

        String hql = "FROM UsuarioMovie uml WHERE uml.usuario = :usuario AND uml.pelicula = :movie AND uml.esLike = true";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("usuario", usuario);
        query.setParameter("movie", movie);

        UsuarioMovie result = (UsuarioMovie) query.uniqueResult();
        return Optional.ofNullable(result);
    }

    @Override
    @Transactional
    public void guardar(UsuarioMovie usuarioMovie) {
        if (usuarioMovie.getUsuario() == null)
            throw new IllegalArgumentException("El usuario no puede ser nulo");

        if (usuarioMovie.getPelicula() == null)
            throw new IllegalArgumentException("La película no puede ser nula");

        sessionFactory.getCurrentSession().saveOrUpdate(usuarioMovie);
    }

    @Override
    @Transactional
    public void borrarMeGusta(UsuarioMovie usuarioMovie) {
        if (usuarioMovie == null)
            throw new IllegalArgumentException("La entidad UsuarioMovie no puede ser nula");

        if (usuarioMovie.getUsuario() == null)
            throw new IllegalArgumentException("El usuario no puede ser nulo");

        if (usuarioMovie.getPelicula() == null)
            throw new IllegalArgumentException("La película no puede ser nula");

        sessionFactory.getCurrentSession().delete(usuarioMovie);
    }

}
