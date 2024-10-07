package org.twelve.repositoriesImpl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.twelve.dominio.entities.Movie;
import org.twelve.infraestructura.MovieRepositoryImpl;
import org.twelve.integracion.config.HibernateTestConfig;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestConfig.class})
public class MovieRepositoryImplTest {

    @Autowired
    private SessionFactory sessionFactory;
    private MovieRepositoryImpl movieRepository;

    @BeforeEach
    public void init() {
        this.movieRepository = new MovieRepositoryImpl(sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    public void testGuardarPeliculaGuardaCorrectamente() {
        Movie movie = new Movie();
        movie.setNombre("The Matrix");
        movie.setAñoLanzamiento("1999");
        movie.setIdCategoria(4);

        movieRepository.save(movie);

        String hql = "FROM Movie WHERE nombre = :nombre";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("nombre", "The Matrix");

        Movie foundMovie = (Movie) query.getSingleResult();
        assertNotNull(foundMovie);
        assertEquals("The Matrix", foundMovie.getNombre());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPeliculaPorIdDevuelveCorrectamente() {
        Movie movie = new Movie();
        movie.setNombre("Inception");
        movie.setAñoLanzamiento("2010");
        movie.setIdCategoria(4);

        movieRepository.save(movie);

        Movie foundMovie = movieRepository.findById(movie.getId());

        assertNotNull(foundMovie);
        assertEquals(movie.getId(), foundMovie.getId());
        assertEquals("Inception", foundMovie.getNombre());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPeliculaPorTituloDevuelveCorrectamente() {
        Movie movie = new Movie();
        movie.setIdCategoria(9);
        movie.setNombre("Avatar");
        movieRepository.save(movie);

        List<Movie> foundMovies = movieRepository.findByTitle("Avatar");
        assertFalse(foundMovies.isEmpty());
        assertEquals("Avatar", foundMovies.get(0).getNombre());
    }

    @Test
    @Transactional
    @Rollback
    public void testModificarPeliculaModificaCorrectamente() {
        Movie movie = new Movie();
        movie.setNombre("Titanic");
        movie.setAñoLanzamiento("1997");
        movie.setIdCategoria(4);
        movieRepository.save(movie);

        movie.setAñoLanzamiento("1998");
        movieRepository.save(movie);

        String hql = "FROM Movie WHERE nombre = :nombre";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("nombre", "Titanic");

        Movie modifiedMovie = (Movie) query.getSingleResult();
        assertEquals("1998", modifiedMovie.getAñoLanzamiento());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPeliculasMasVistas() {
        Movie movie1 = new Movie();
        movie1.setNombre("Coraline");
        movie1.setCantVistas(100);
        movie1.setIdCategoria(2);
        movieRepository.save(movie1);

        Movie movie2 = new Movie();
        movie2.setNombre("Avatar");
        movie2.setCantVistas(200);
        movie2.setIdCategoria(3);
        movieRepository.save(movie2);

        List<Movie> mostViewedMovies = movieRepository.findMostViewed();
        assertFalse(mostViewedMovies.isEmpty());
        assertEquals("Avatar", mostViewedMovies.get(0).getNombre());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPeliculasMejorValoradas() {
        Movie movie1 = new Movie();
        movie1.setId(1);
        movie1.setNombre("Coraline");
        movie1.setValoracion(4.5);
        movie1.setIdCategoria(3);

        movieRepository.save(movie1);

        Movie movie2 = new Movie();
        movie2.setId(2);
        movie2.setNombre("Avatar");
        movie2.setValoracion(4.8);
        movie2.setIdCategoria(5);
        movieRepository.save(movie2);

        List<Movie> topRatedMovies = movieRepository.findTopRated();
        assertFalse(topRatedMovies.isEmpty());
        assertEquals("Avatar", topRatedMovies.get(0).getNombre());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPeliculasPorCategoria() {
        Movie movie = new Movie();
        movie.setNombre("Joker");
        movie.setIdCategoria(1);
        movieRepository.save(movie);

        List<Movie> foundMovies = movieRepository.findByCategoriaId(1);
        assertFalse(foundMovies.isEmpty());
        assertEquals(1, foundMovies.get(0).getIdCategoria());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPeliculasMasNuevas() {
        Movie movie1 = new Movie();
        movie1.setNombre("pelicula vieja");
        movie1.setAñoLanzamiento("1980");
        movie1.setIdCategoria(1);
        movieRepository.save(movie1);

        Movie movie2 = new Movie();
        movie2.setNombre("pelicula nueva");
        movie2.setAñoLanzamiento("2020");
        movie2.setIdCategoria(2);
        movieRepository.save(movie2);

        List<Movie> newestMovies = movieRepository.findNewestMovie();
        assertFalse(newestMovies.isEmpty());
        assertEquals("pelicula nueva", newestMovies.get(0).getNombre());
    }


}
