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

        Movie peliculaEncontrada = movieRepository.findById(movie.getId());

        assertNotNull(peliculaEncontrada);
        assertEquals(movie.getId(), peliculaEncontrada.getId());
        assertEquals("Inception", peliculaEncontrada.getNombre());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPeliculaPorTituloDevuelveCorrectamente() {
        Movie movie = new Movie();
        movie.setIdCategoria(9);
        movie.setNombre("Avatar");
        movieRepository.save(movie);

        List<Movie> peliculasEncontradas = movieRepository.findByTitle("Avatar");
        assertFalse(peliculasEncontradas.isEmpty());
        assertEquals("Avatar", peliculasEncontradas.get(0).getNombre());
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

        Movie peliculaModificada = (Movie) query.getSingleResult();
        assertEquals("1998", peliculaModificada.getAñoLanzamiento());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPeliculasMasVistasEntreDosTitulos() {
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

        List<Movie> peliculasMasVistas = movieRepository.findMostViewed();
        assertFalse(peliculasMasVistas.isEmpty());
        assertEquals("Avatar", peliculasMasVistas.get(0).getNombre());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPeliculasMejorValoradasEntreDosTitulos() {
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

        List<Movie> peliculasEncontradas = movieRepository.findByCategoriaId(1);
        assertFalse(peliculasEncontradas.isEmpty());
        assertEquals(1, peliculasEncontradas.get(0).getIdCategoria());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPeliculasMasNuevasEntreDosTitulos() {
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

    @Test
    @Transactional
    @Rollback
    public void testBuscarPeliculasPorAñoLanzamiento() {
        Movie movie1 = new Movie();
        movie1.setNombre("Matrix");
        movie1.setAñoLanzamiento("1999");
        movie1.setIdCategoria(2);
        movieRepository.save(movie1);

        Movie movie2 = new Movie();
        movie2.setNombre("Toy Story");
        movie2.setAñoLanzamiento("1999");
        movie2.setIdCategoria(4);
        movieRepository.save(movie2);

        String hql = "FROM Movie WHERE añoLanzamiento = :año";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("año", "1999");

        List<Movie> movies = query.getResultList();
        assertEquals(2, movies.size());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPeliculasPorTituloSinResultados() {
        List<Movie> foundMovies = movieRepository.findByTitle("PeliculaNoExistente");
        assertTrue(foundMovies.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testEliminarPeliculaGuardaYEliminaCorrectamente() {
        Movie movie = new Movie();
        movie.setNombre("The Godfather");
        movie.setAñoLanzamiento("1972");
        movie.setIdCategoria(5);
        movieRepository.save(movie);

        sessionFactory.getCurrentSession().delete(movie);
        sessionFactory.getCurrentSession().flush();

        String hql = "FROM Movie WHERE nombre = :nombre";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("nombre", "The Godfather");

        List<Movie> foundMovies = query.getResultList();
        assertTrue(foundMovies.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testActualizarPeliculaInexistenteDebeFallar() {
        Movie movie = new Movie();
        movie.setId(9999);
        movie.setNombre("Pelicula Fantasma");

        assertThrows(Exception.class, () -> {
            movieRepository.save(movie);
            sessionFactory.getCurrentSession().flush();
        });
    }

}
