package org.twelve.repositoriesImpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.twelve.dominio.entities.Movie;
import org.twelve.infraestructura.MovieRepositoryImpl;
import org.twelve.integracion.config.HibernateTestConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestConfig.class})
public class MovieRepositoryImplTest {

    private Movie movie;
    private Movie movie2;
    private SessionFactory sessionFactory;
    private Session session;
    private MovieRepositoryImpl movieRepository;

    @BeforeEach
    public void setUp() {
        movie = mock(Movie.class);
        movie2 = mock(Movie.class);
        sessionFactory = mock(SessionFactory.class);
        session = mock(Session.class);
        movieRepository = new MovieRepositoryImpl(sessionFactory);

        when(sessionFactory.getCurrentSession()).thenReturn(session);

        when(movie.getId()).thenReturn(1);
        when(movie2.getId()).thenReturn(2);
        when(movie.getNombre()).thenReturn("Coraline");
        when(movie2.getNombre()).thenReturn("Avatar");
    }

    @Test
    public void testFindById() {
        when(session.get(Movie.class, 1)).thenReturn(movie);

        Movie result = movieRepository.findById(1);

        assertNotNull(result);
        verify(sessionFactory).getCurrentSession();
        verify(session).get(Movie.class, 1);
    }

    @Test
    public void testSave() {
        Movie movieToSave = mock(Movie.class);

        movieRepository.save(movieToSave);

        verify(session).save(movieToSave);
    }

    @Test
    @Disabled
    public void testFindAll() {
        List<Movie> mockMovies = Arrays.asList(movie, movie2);

        when(session.createQuery("from Movie", Movie.class).list()).thenReturn(mockMovies);

        List<Movie> result = movieRepository.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(movie));
        assertTrue(result.contains(movie2));

        verify(session).createQuery("from Movie", Movie.class);
    }
}
