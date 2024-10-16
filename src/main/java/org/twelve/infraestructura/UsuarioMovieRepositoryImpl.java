package org.twelve.infraestructura;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.twelve.dominio.UsuarioMovieRepository;
import org.twelve.dominio.entities.Movie;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        String hql = "SELECT up.pelicula FROM UsuarioMovie up WHERE up.usuario.id = :usuarioId AND up.esFavorita = true";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("usuarioId", usuarioId);

        List<Movie> peliculasFavoritas = query.getResultList();
        return peliculasFavoritas != null ? peliculasFavoritas : new ArrayList<>();
    }


}
