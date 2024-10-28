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
import org.twelve.dominio.entities.Usuario;
import org.twelve.infraestructura.UsuarioMovieRepositoryImpl;
import org.twelve.integracion.config.HibernateTestConfig;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestConfig.class})
public class UsuarioMovieRepositoryImplTest {

    @Autowired
    private SessionFactory sessionFactory;
    private UsuarioMovieRepositoryImpl usuarioMovieRepository;

    @BeforeEach
    public void setUp() {
        this.usuarioMovieRepository = new UsuarioMovieRepositoryImpl(sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerPeliculasFavoritasSinResultados() {
        Integer usuarioId = 1;

        List<Movie> peliculasFavoritas = usuarioMovieRepository.obtenerPeliculasFavoritas(usuarioId);

        assertNotNull(peliculasFavoritas);
        assertTrue(peliculasFavoritas.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerCantidadPeliculasVistasSinPeliculas() {
        Usuario usuario = new Usuario();
        Integer usuarioId = usuario.getId();

        Integer cantidadPeliculasVistas = usuarioMovieRepository.obtenerCantidadPeliculasVistas(usuarioId);

        assertNotNull(cantidadPeliculasVistas);
        assertEquals(0, cantidadPeliculasVistas);
    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerCantidadPeliculasVistasEsteAnoSinPeliculas() {
        Usuario usuario = new Usuario();
        Integer usuarioId = usuario.getId();

        Integer cantidadPeliculasVistasEsteAno = usuarioMovieRepository.obtenerCantidadPeliculasVistasEsteAno(usuarioId);

        assertNotNull(cantidadPeliculasVistasEsteAno);
        assertEquals(0, cantidadPeliculasVistasEsteAno);
    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerPeliculasFavoritasSinResultados2() {
        Usuario usuario = new Usuario();
        Integer usuarioId = usuario.getId();

        List<Movie> peliculasFavoritas = usuarioMovieRepository.obtenerPeliculasFavoritas(usuarioId);

        assertNotNull(peliculasFavoritas);
        assertTrue(peliculasFavoritas.isEmpty());
    }
}
