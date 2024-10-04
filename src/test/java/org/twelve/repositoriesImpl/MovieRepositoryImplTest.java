package org.twelve.repositoriesImpl;

import org.hibernate.Session;
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

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    public void testFindAllDevuelveTodasLasPeliculas() {
        Session session = sessionFactory.getCurrentSession();
        Movie movie1 = new Movie(1, "Coraline", "", "", 213.5, "", 5000, 1, "2020", "", 1, 500, 4.5, "", "", "", "");
        Movie movie2 = new Movie(2, "Avatar", "", "", 213.5, "", 5000, 1, "2020", "", 1, 500, 4.5, "", "", "", "");

        session.save(movie1);
        session.save(movie2);
        session.flush();

        List<Movie> movies = movieRepository.findAll();

        assertNotNull(movies);
        assertEquals(2, movies.size());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByIdDevuelvePeliculaCorrecta() {
        Session session = sessionFactory.getCurrentSession();
        Movie movie = new Movie(1, "Coraline", "", "", 213.5, "", 5000, 1, "2020", "", 1, 500, 4.5, "", "", "", "");
        session.save(movie);
        session.flush();

        Movie foundMovie = movieRepository.findById(movie.getId());

        assertNotNull(foundMovie);
        assertEquals(movie.getId(), foundMovie.getId());
        assertEquals(movie.getNombre(), foundMovie.getNombre());
    }

    @Test
    @Transactional
    @Rollback
    public void testGuardarPeliculaGuardaCorrectamente() {
        Movie movie = new Movie(1, "Coraline", "", "", 213.5, "", 5000, 1, "2020", "", 1, 500, 4.5, "", "", "", "");

        movieRepository.save(movie);

        Movie foundMovie = sessionFactory.getCurrentSession().get(Movie.class, movie.getId());
        assertNotNull(foundMovie);
        assertEquals("Coraline", foundMovie.getNombre());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByTitleDevuelvePeliculasCorrectas() {
        Session session = sessionFactory.getCurrentSession();

        Movie movie1 = new Movie(1, "El Señor de los Anillos", "", "", 213.5, "", 5000, 1, "2020", "", 1, 500, 4.5, "", "", "", "");
        Movie movie2 = new Movie(2, "El Hobbit", "", "", 213.5, "", 5000, 1, "2020", "", 1, 500, 4.5, "", "", "", "");

        session.save(movie1);
        session.save(movie2);
        session.flush();

        List<Movie> foundMovies = movieRepository.findByTitle("señor");

        assertNotNull(foundMovies);
        assertEquals(1, foundMovies.size());
        assertEquals("El Señor de los Anillos", foundMovies.get(0).getNombre());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindMostViewedDevuelvePeliculasMasVistas() {
        Session session = sessionFactory.getCurrentSession();
        Movie movie1 = new Movie(1, "El Señor de los Anillos", "", "", 213.5, "", 5000, 1, "2000", "", 1, 500, 4.5, "", "", "", "");
        Movie movie2 = new Movie(2, "Coraline", "", "", 111.1, "", 5, 2, "2020", "", 0, 100, 6.5, "", "", "", "");
        Movie movie3 = new Movie(3, "Avatar", "", "", 212.1, "", 8080, 4, "2021", "", 6, 5600, 1.5, "", "", "", "");

        session.save(movie1);
        session.save(movie2);
        session.save(movie3);
        session.flush();

        List<Movie> mostViewedMovies = movieRepository.findMostViewed();

        assertNotNull(mostViewedMovies);
        assertEquals(3, mostViewedMovies.size());
        assertEquals("Avatar", mostViewedMovies.get(0).getNombre());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindTopRatedDevuelvePeliculasMejorValoradas() {
        Session session = sessionFactory.getCurrentSession();
        Movie movie1 = new Movie(1, "El Señor de los Anillos", "", "", 213.5, "", 5000, 1, "2000", "", 1, 500, 4.5, "", "", "", "");
        Movie movie2 = new Movie(2, "Coraline", "", "", 111.1, "", 5, 2, "2020", "", 0, 100, 6.5, "", "", "", "");
        Movie movie3 = new Movie(3, "Avatar", "", "", 212.1, "", 8080, 4, "2021", "", 6, 5600, 1.5, "", "", "", "");

        session.save(movie1);
        session.save(movie2);
        session.save(movie3);
        session.flush();

        List<Movie> topRatedMovies = movieRepository.findTopRated();

        assertNotNull(topRatedMovies);
        assertEquals(3, topRatedMovies.size());
        assertEquals("Coraline", topRatedMovies.get(0).getNombre());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByCategoriaIdDevuelvePeliculasCorrectas() {
        Session session = sessionFactory.getCurrentSession();
        Movie movie1 = new Movie(1, "El Señor de los Anillos", "", "", 213.5, "", 5000, 1, "2000", "", 1, 500, 4.5, "", "", "", "");
        movie1.setIdCategoria(1);
        Movie movie2 = new Movie(2, "Coraline", "", "", 111.1, "", 5, 2, "2020", "", 0, 100, 6.5, "", "", "", "");
        movie2.setIdCategoria(2);

        session.save(movie1);
        session.save(movie2);
        session.flush();

        List<Movie> categoryMovies = movieRepository.findByCategoriaId(1);

        assertNotNull(categoryMovies);
        assertEquals(1, categoryMovies.size());
        assertEquals(1, categoryMovies.get(0).getIdCategoria().intValue());
    }


}
