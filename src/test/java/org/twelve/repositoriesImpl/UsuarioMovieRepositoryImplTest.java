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
import org.twelve.dominio.entities.UsuarioMovie;
import org.twelve.infraestructura.UsuarioMovieRepositoryImpl;
import org.twelve.integracion.config.HibernateTestConfig;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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
    public void testObtenerPeliculasFavoritasConResultados() {
        Usuario usuario = new Usuario();
        usuario.setId(1);

        Movie movie1 = new Movie();
        movie1.setNombre("Inception");
        Movie movie2 = new Movie();
        movie2.setNombre("The Matrix");

        sessionFactory.getCurrentSession().save(usuario);
        sessionFactory.getCurrentSession().save(movie1);
        sessionFactory.getCurrentSession().save(movie2);

        UsuarioMovie usuarioMovie1 = new UsuarioMovie();
        usuarioMovie1.setUsuario(usuario);
        usuarioMovie1.setPelicula(movie1);
        usuarioMovie1.setEsLike(true);

        UsuarioMovie usuarioMovie2 = new UsuarioMovie();
        usuarioMovie2.setUsuario(usuario);
        usuarioMovie2.setPelicula(movie2);
        usuarioMovie2.setEsLike(true);

        sessionFactory.getCurrentSession().save(usuarioMovie1);
        sessionFactory.getCurrentSession().save(usuarioMovie2);

        List<Movie> peliculasFavoritas = usuarioMovieRepository.obtenerPeliculasFavoritas(usuario.getId());

        assertNotNull(peliculasFavoritas);
        assertEquals(2, peliculasFavoritas.size());
        assertTrue(peliculasFavoritas.stream().anyMatch(m -> m.getNombre().equals("Inception")));
        assertTrue(peliculasFavoritas.stream().anyMatch(m -> m.getNombre().equals("The Matrix")));
    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerPeliculasFavoritasSinResultados1() {
        Integer usuarioId = 999;

        List<Movie> peliculasFavoritas = usuarioMovieRepository.obtenerPeliculasFavoritas(usuarioId);

        assertNotNull(peliculasFavoritas);
        assertTrue(peliculasFavoritas.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerPeliculasFavoritasConUsuarioSinLikes() {
        Usuario usuario = new Usuario();
        usuario.setId(2);

        sessionFactory.getCurrentSession().save(usuario);

        List<Movie> peliculasFavoritas = usuarioMovieRepository.obtenerPeliculasFavoritas(usuario.getId());

        assertNotNull(peliculasFavoritas);
        assertTrue(peliculasFavoritas.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerPeliculasFavoritasConUsuarioIdNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            usuarioMovieRepository.obtenerPeliculasFavoritas(null);
        });
    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerPeliculasFavoritasConUsuarioIdInvalido() {
        Integer usuarioId = -1;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioMovieRepository.obtenerPeliculasFavoritas(usuarioId);
        });

        assertEquals("El ID de usuario no puede ser negativo", exception.getMessage());
    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerCantidadDeLikesConLikes() {
        Movie movie = new Movie();
        movie.setNombre("Inception");
        sessionFactory.getCurrentSession().save(movie);

        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        sessionFactory.getCurrentSession().save(usuario1);
        sessionFactory.getCurrentSession().save(usuario2);

        UsuarioMovie usuarioMovie1 = new UsuarioMovie();
        usuarioMovie1.setUsuario(usuario1);
        usuarioMovie1.setPelicula(movie);
        usuarioMovie1.setEsLike(true);

        UsuarioMovie usuarioMovie2 = new UsuarioMovie();
        usuarioMovie2.setUsuario(usuario2);
        usuarioMovie2.setPelicula(movie);
        usuarioMovie2.setEsLike(true);

        sessionFactory.getCurrentSession().save(usuarioMovie1);
        sessionFactory.getCurrentSession().save(usuarioMovie2);

        long cantidadDeLikes = usuarioMovieRepository.obtenerCantidadDeLikes(movie);

        assertEquals(2, cantidadDeLikes);
    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerCantidadDeLikesSinLikes() {
        Movie movie = new Movie();
        movie.setNombre("The Matrix");
        sessionFactory.getCurrentSession().save(movie);

        long cantidadDeLikes = usuarioMovieRepository.obtenerCantidadDeLikes(movie);

        assertEquals(0, cantidadDeLikes);
    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerCantidadDeLikesConAlgunosDislikes() {
        Movie movie = new Movie();
        movie.setNombre("Interstellar");
        sessionFactory.getCurrentSession().save(movie);

        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        Usuario usuario3 = new Usuario();
        sessionFactory.getCurrentSession().save(usuario1);
        sessionFactory.getCurrentSession().save(usuario2);
        sessionFactory.getCurrentSession().save(usuario3);

        UsuarioMovie usuarioMovie1 = new UsuarioMovie();
        usuarioMovie1.setUsuario(usuario1);
        usuarioMovie1.setPelicula(movie);
        usuarioMovie1.setEsLike(true);

        UsuarioMovie usuarioMovie2 = new UsuarioMovie();
        usuarioMovie2.setUsuario(usuario2);
        usuarioMovie2.setPelicula(movie);
        usuarioMovie2.setEsLike(true);

        UsuarioMovie usuarioMovie3 = new UsuarioMovie();
        usuarioMovie3.setUsuario(usuario3);
        usuarioMovie3.setPelicula(movie);
        usuarioMovie3.setEsLike(false);

        sessionFactory.getCurrentSession().save(usuarioMovie1);
        sessionFactory.getCurrentSession().save(usuarioMovie2);
        sessionFactory.getCurrentSession().save(usuarioMovie3);

        long cantidadDeLikes = usuarioMovieRepository.obtenerCantidadDeLikes(movie);

        assertEquals(2, cantidadDeLikes);
    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerCantidadDeLikesConPeliculaNula() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioMovieRepository.obtenerCantidadDeLikes(null);
        });

        assertEquals("La película no puede ser nula", exception.getMessage());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarMeGustaPorUsuarioCuandoExisteRelacion() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        Movie movie = new Movie();
        movie.setNombre("Inception");

        sessionFactory.getCurrentSession().save(usuario);
        sessionFactory.getCurrentSession().save(movie);

        UsuarioMovie usuarioMovie = new UsuarioMovie();
        usuarioMovie.setUsuario(usuario);
        usuarioMovie.setPelicula(movie);
        usuarioMovie.setEsLike(true);

        sessionFactory.getCurrentSession().save(usuarioMovie);

        Optional<UsuarioMovie> resultado = usuarioMovieRepository.buscarMeGustaPorUsuario(usuario, movie);

        assertTrue(resultado.isPresent());
        assertEquals(usuario, resultado.get().getUsuario());
        assertEquals(movie, resultado.get().getPelicula());
        assertTrue(resultado.get().getEsLike());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarMeGustaPorUsuarioCuandoNoExisteRelacion() {
        Usuario usuario = new Usuario();
        usuario.setId(2);
        Movie movie = new Movie();
        movie.setNombre("The Matrix");

        sessionFactory.getCurrentSession().save(usuario);
        sessionFactory.getCurrentSession().save(movie);

        Optional<UsuarioMovie> resultado = usuarioMovieRepository.buscarMeGustaPorUsuario(usuario, movie);

        assertFalse(resultado.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarMeGustaPorUsuarioConUsuarioNulo() {
        Movie movie = new Movie();
        movie.setNombre("Inception");

        sessionFactory.getCurrentSession().save(movie);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioMovieRepository.buscarMeGustaPorUsuario(null, movie);
        });

        assertEquals("Usuario no puede ser nulo", exception.getMessage());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarMeGustaPorUsuarioConMovieNulo() {
        Usuario usuario = new Usuario();
        usuario.setId(1);

        sessionFactory.getCurrentSession().save(usuario);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioMovieRepository.buscarMeGustaPorUsuario(usuario, null);
        });

        assertEquals("Película no puede ser nula", exception.getMessage());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarMeGustaPorUsuarioConRelacionNoEsLike() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        Movie movie = new Movie();
        movie.setNombre("Avatar");

        sessionFactory.getCurrentSession().save(usuario);
        sessionFactory.getCurrentSession().save(movie);

        UsuarioMovie usuarioMovie = new UsuarioMovie();
        usuarioMovie.setUsuario(usuario);
        usuarioMovie.setPelicula(movie);
        usuarioMovie.setEsLike(false);
        sessionFactory.getCurrentSession().save(usuarioMovie);

        Optional<UsuarioMovie> resultado = usuarioMovieRepository.buscarMeGustaPorUsuario(usuario, movie);

        assertFalse(resultado.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    public void testGuardarNuevoUsuarioMovie() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        Movie movie = new Movie();
        movie.setNombre("Inception");

        sessionFactory.getCurrentSession().save(usuario);
        sessionFactory.getCurrentSession().save(movie);

        UsuarioMovie usuarioMovie = new UsuarioMovie();
        usuarioMovie.setUsuario(usuario);
        usuarioMovie.setPelicula(movie);
        usuarioMovie.setEsLike(true);

        usuarioMovieRepository.guardar(usuarioMovie);

        Optional<UsuarioMovie> resultado = usuarioMovieRepository.buscarMeGustaPorUsuario(usuario, movie);
        assertTrue(resultado.isPresent());
        assertEquals(usuario, resultado.get().getUsuario());
        assertEquals(movie, resultado.get().getPelicula());
        assertTrue(resultado.get().getEsLike());
    }

    @Test
    @Transactional
    @Rollback
    public void testActualizarUsuarioMovieExistente() {
        Usuario usuario = new Usuario();
        usuario.setId(2);
        Movie movie = new Movie();
        movie.setNombre("The Matrix");

        sessionFactory.getCurrentSession().save(usuario);
        sessionFactory.getCurrentSession().save(movie);

        UsuarioMovie usuarioMovie = new UsuarioMovie();
        usuarioMovie.setUsuario(usuario);
        usuarioMovie.setPelicula(movie);
        usuarioMovie.setEsLike(false);

        usuarioMovieRepository.guardar(usuarioMovie);

        usuarioMovie.setEsLike(true);

        usuarioMovieRepository.guardar(usuarioMovie);

        Optional<UsuarioMovie> resultado = usuarioMovieRepository.buscarMeGustaPorUsuario(usuario, movie);
        assertTrue(resultado.isPresent());
        assertTrue(resultado.get().getEsLike());
    }

    @Test
    @Transactional
    @Rollback
    public void testGuardarConUsuarioNuloLanzaExcepcion() {
        Movie movie = new Movie();
        movie.setNombre("Avatar");
        sessionFactory.getCurrentSession().save(movie);

        UsuarioMovie usuarioMovie = new UsuarioMovie();
        usuarioMovie.setUsuario(null);
        usuarioMovie.setPelicula(movie);
        usuarioMovie.setEsLike(true);

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioMovieRepository.guardar(usuarioMovie);
        });
    }

    @Test
    @Transactional
    @Rollback
    public void testGuardarConPeliculaNulaLanzaExcepcion() {
        Usuario usuario = new Usuario();
        usuario.setId(3);
        sessionFactory.getCurrentSession().save(usuario);

        UsuarioMovie usuarioMovie = new UsuarioMovie();
        usuarioMovie.setUsuario(usuario);
        usuarioMovie.setPelicula(null);
        usuarioMovie.setEsLike(true);

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioMovieRepository.guardar(usuarioMovie);
        });
    }

    @Test
    @Transactional
    @Rollback
    public void testBorrarMeGustaExistente() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        Movie movie = new Movie();
        movie.setNombre("Inception");

        sessionFactory.getCurrentSession().save(usuario);
        sessionFactory.getCurrentSession().save(movie);

        UsuarioMovie usuarioMovie = new UsuarioMovie();
        usuarioMovie.setUsuario(usuario);
        usuarioMovie.setPelicula(movie);
        usuarioMovie.setEsLike(true);
        usuarioMovieRepository.guardar(usuarioMovie);

        usuarioMovieRepository.borrarMeGusta(usuarioMovie);

        Optional<UsuarioMovie> resultado = usuarioMovieRepository.buscarMeGustaPorUsuario(usuario, movie);
        assertFalse(resultado.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    public void testBorrarMeGustaNoExistente() {
        Usuario usuario = new Usuario();
        usuario.setId(2);
        Movie movie = new Movie();
        movie.setNombre("The Matrix");

        sessionFactory.getCurrentSession().save(usuario);
        sessionFactory.getCurrentSession().save(movie);

        UsuarioMovie usuarioMovie = new UsuarioMovie();
        usuarioMovie.setUsuario(usuario);
        usuarioMovie.setPelicula(movie);
        usuarioMovie.setEsLike(true);

        assertDoesNotThrow(() -> usuarioMovieRepository.borrarMeGusta(usuarioMovie));
    }

    @Test
    @Transactional
    @Rollback
    public void testBorrarMeGustaConUsuarioNuloLanzaExcepcion() {
        Movie movie = new Movie();
        movie.setNombre("Avatar");
        sessionFactory.getCurrentSession().save(movie);

        UsuarioMovie usuarioMovie = new UsuarioMovie();
        usuarioMovie.setUsuario(null);
        usuarioMovie.setPelicula(movie);
        usuarioMovie.setEsLike(true);

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioMovieRepository.borrarMeGusta(usuarioMovie);
        });
    }

    @Test
    @Transactional
    @Rollback
    public void testBorrarMeGustaConPeliculaNulaLanzaExcepcion() {
        Usuario usuario = new Usuario();
        usuario.setId(3);
        sessionFactory.getCurrentSession().save(usuario);

        UsuarioMovie usuarioMovie = new UsuarioMovie();
        usuarioMovie.setUsuario(usuario);
        usuarioMovie.setPelicula(null);
        usuarioMovie.setEsLike(true);

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioMovieRepository.borrarMeGusta(usuarioMovie);
        });
    }

    @Test
    @Transactional
    @Rollback
    public void testBorrarMeGustaConEntidadNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            usuarioMovieRepository.borrarMeGusta(null);
        });
    }

}
