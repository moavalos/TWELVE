package org.twelve.repositoriesImpl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.twelve.dominio.entities.*;
import org.twelve.infraestructura.UsuarioComentarioRepositoryImpl;
import org.twelve.integracion.config.HibernateTestConfig;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestConfig.class})
public class UsuarioComentarioRepositoryImplTest {

    @Autowired
    private SessionFactory sessionFactory;
    private UsuarioComentarioRepositoryImpl usuarioComentarioRepository;

    @BeforeEach
    public void setUp() {
        this.usuarioComentarioRepository = new UsuarioComentarioRepositoryImpl(sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    @Disabled
    public void testObtenerComentariosLikeados() {

        //preparacion
        //usuarios que hacen comentario
        Usuario usuarioComentario1 = new Usuario();
        usuarioComentario1.setUsername("usuarioComentario1");
        usuarioComentario1.setId(1);

        Usuario usuarioComentario2 = new Usuario();
        usuarioComentario2.setUsername("usuarioComentario2");
        usuarioComentario2.setId(2);

        //usuario que le da like a los comentarios
        Usuario usuarioQueDaLike = new Usuario();
        usuarioQueDaLike.setUsername("usuarioQueDaLike");
        usuarioQueDaLike.setId(3);

        Movie movie = new Movie();
        movie.setId(1);

        Comentario comentario1 = new Comentario();
        comentario1.setId(1);
        comentario1.setUsuario(usuarioComentario1);
        comentario1.setMovie(movie);
        Comentario comentario2 = new Comentario();
        comentario2.setUsuario(usuarioComentario2);
        comentario2.setId(2);
        comentario2.setMovie(movie);


        sessionFactory.getCurrentSession().save(usuarioComentario1);
        sessionFactory.getCurrentSession().save(usuarioComentario2);
        sessionFactory.getCurrentSession().save(usuarioQueDaLike);
        sessionFactory.getCurrentSession().save(movie);
        sessionFactory.getCurrentSession().save(comentario1);
        sessionFactory.getCurrentSession().save(comentario2);


        UsuarioComentario like1 = new UsuarioComentario();
        like1.setUsuario(usuarioQueDaLike);
        like1.setComentario(comentario1);
        like1.setEsLike(true);

        UsuarioComentario like2 = new UsuarioComentario();
        like2.setUsuario(usuarioQueDaLike);
        like2.setComentario(comentario1);
        like2.setEsLike(true);


        sessionFactory.getCurrentSession().save(like1);
        sessionFactory.getCurrentSession().save(like2);

        //hacer lista de comentarios liekados DALEEE
        List<Comentario> comentariosLikeados = usuarioComentarioRepository.obtenerComentariosLikeados(usuarioQueDaLike.getId());


        //validacion
        assertNotNull(comentariosLikeados);
        assertEquals(2, comentariosLikeados.size());
        assertTrue(comentariosLikeados.stream().anyMatch(m -> comentario1.getId().equals(1)));
        assertTrue(comentariosLikeados.stream().anyMatch(m -> comentario2.getId().equals(2)));
    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerComentariosLikeadosSinResultados() {
        Integer usuarioId = 999;

        List<Comentario> peliculasFavoritas = usuarioComentarioRepository.obtenerComentariosLikeados(usuarioId);

        assertNotNull(peliculasFavoritas);
        assertTrue(peliculasFavoritas.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerComentariosLikeadosConUsuarioIdNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            usuarioComentarioRepository.obtenerComentariosLikeados(null);
        });
    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerComentariosLikeadosConUsuarioIdInvalido() {
        Integer usuarioId = -1;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioComentarioRepository.obtenerComentariosLikeados(usuarioId);
        });

        assertEquals("El ID de usuario no puede ser negativo", exception.getMessage());
    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerCantidadDeLikesConLikesEnComentario() {

        Movie movie = new Movie();
        movie.setId(1);
        movie.setNombre("Inception");
        sessionFactory.getCurrentSession().save(movie);

        Usuario usuarioQueComenta = new Usuario();
        usuarioQueComenta.setId(1);
        usuarioQueComenta.setUsername("usuarioQueComenta");
        sessionFactory.getCurrentSession().save(usuarioQueComenta);


        Comentario comentario = new Comentario();
        comentario.setId(1);
        comentario.setUsuario(usuarioQueComenta);
        comentario.setMovie(movie);
        comentario.setDescripcion("extraordinario");
        sessionFactory.getCurrentSession().save(comentario);


        Usuario usuario1 = new Usuario();
        usuario1.setId(2);
        Usuario usuario2 = new Usuario();
        usuario2.setId(3);
        sessionFactory.getCurrentSession().save(usuario1);
        sessionFactory.getCurrentSession().save(usuario2);

        UsuarioComentario usuarioQueDaLike1 = new UsuarioComentario();
        usuarioQueDaLike1.setUsuario(usuario1);
        usuarioQueDaLike1.setComentario(comentario);
        usuarioQueDaLike1.setEsLike(true);

        UsuarioComentario usuarioQueDaLike2 = new UsuarioComentario();
        usuarioQueDaLike2.setUsuario(usuario1);
        usuarioQueDaLike2.setComentario(comentario);
        usuarioQueDaLike2.setEsLike(true);

        sessionFactory.getCurrentSession().save(usuarioQueDaLike1);
        sessionFactory.getCurrentSession().save(usuarioQueDaLike2);


        long cantidadDelikes = usuarioComentarioRepository.obtenerCantidadDelikes(comentario);

        assertEquals(2, cantidadDelikes);
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarMeGustaPorUsuarioCuandoExisteRelacion() {

        //usuario y comentario en una pelicula
        Usuario usuario = new Usuario();
        usuario.setId(1);
        Movie movie = new Movie();
        movie.setId(1);
        Comentario comentario = new Comentario();
        comentario.setId(1);
        comentario.setUsuario(usuario);
        comentario.setMovie(movie);

        sessionFactory.getCurrentSession().save(usuario);
        sessionFactory.getCurrentSession().save(movie);
        sessionFactory.getCurrentSession().save(comentario);

        //usuario que likea al comentario
        Usuario usuarioQueDaLike = new Usuario();
        usuarioQueDaLike.setId(2);
        sessionFactory.getCurrentSession().save(usuarioQueDaLike);

        UsuarioComentario usuarioComentario = new UsuarioComentario();
        usuarioComentario.setUsuario(usuarioQueDaLike);
        usuarioComentario.setComentario(comentario);
        usuarioComentario.setEsLike(true);

        sessionFactory.getCurrentSession().save(usuarioComentario);

        Optional<UsuarioComentario> resultado = usuarioComentarioRepository.buscarMeGustaPorUsuario(usuarioQueDaLike, comentario);


        //validacion
        assertTrue(resultado.isPresent());
        assertEquals(usuarioQueDaLike, resultado.get().getUsuario());
        assertEquals(comentario, resultado.get().getComentario());
        assertTrue(resultado.get().getEsLike());
    }

    @Test
    @Transactional
    @Rollback
    public void testQueUnUsuarioPuedaDarleLikeAUnComentario() {
        //pelicula, usuario y comentario
        Movie movie = new Movie();
        movie.setId(1);
        movie.setNombre("Inception");
        sessionFactory.getCurrentSession().save(movie);

        Usuario usuarioQueComenta = new Usuario();
        usuarioQueComenta.setId(1);
        usuarioQueComenta.setUsername("usuarioQueComenta");
        sessionFactory.getCurrentSession().save(usuarioQueComenta);

        Comentario comentario = new Comentario();
        comentario.setId(1);
        comentario.setUsuario(usuarioQueComenta);
        comentario.setMovie(movie);
        comentario.setDescripcion("Una obra maestra.");
        sessionFactory.getCurrentSession().save(comentario);

        // usuario q da like
        Usuario usuarioQueDaLike = new Usuario();
        usuarioQueDaLike.setId(2);
        usuarioQueDaLike.setUsername("usuarioQueDaLike");
        sessionFactory.getCurrentSession().save(usuarioQueDaLike);

        UsuarioComentario usuarioComentario = new UsuarioComentario();
        usuarioComentario.setUsuario(usuarioQueDaLike);
        usuarioComentario.setComentario(comentario);
        usuarioComentario.setEsLike(true);
        sessionFactory.getCurrentSession().save(usuarioComentario);

        //validacion
        Optional<UsuarioComentario> resultado = usuarioComentarioRepository.buscarMeGustaPorUsuario(usuarioQueDaLike, comentario);
        assertTrue(resultado.isPresent());
        assertEquals(usuarioQueDaLike, resultado.get().getUsuario());
        assertEquals(comentario, resultado.get().getComentario());
        assertTrue(resultado.get().getEsLike());
    }

    @Test
    @Transactional
    @Rollback
    public void testQueUnUsuarioPuedaQuitarUnMegustaAUnComentario() {

        //usuario, pelicula y comentario
        Movie movie = new Movie();
        movie.setId(1);
        movie.setNombre("Inception");
        sessionFactory.getCurrentSession().save(movie);

        Usuario usuarioQueComenta = new Usuario();
        usuarioQueComenta.setId(1);
        usuarioQueComenta.setUsername("usuarioQueComenta");
        sessionFactory.getCurrentSession().save(usuarioQueComenta);

        Comentario comentario = new Comentario();
        comentario.setId(1);
        comentario.setUsuario(usuarioQueComenta);
        comentario.setMovie(movie);
        comentario.setDescripcion("Increíble película");
        sessionFactory.getCurrentSession().save(comentario);

        //usuario q da like
        Usuario usuarioQueDaLike = new Usuario();
        usuarioQueDaLike.setId(2);
        usuarioQueDaLike.setUsername("usuarioQueDaLike");
        sessionFactory.getCurrentSession().save(usuarioQueDaLike);

        //ejecucion
        UsuarioComentario usuarioComentario = new UsuarioComentario();
        usuarioComentario.setUsuario(usuarioQueDaLike);
        usuarioComentario.setComentario(comentario);
        usuarioComentario.setEsLike(true);
        sessionFactory.getCurrentSession().save(usuarioComentario);

        // existe el like
        Optional<UsuarioComentario> resultadoAntesDeQuitar = usuarioComentarioRepository.buscarMeGustaPorUsuario(usuarioQueDaLike, comentario);
        assertTrue(resultadoAntesDeQuitar.isPresent());

        usuarioComentarioRepository.borrarMeGusta(resultadoAntesDeQuitar.get());

        // validacion
        Optional<UsuarioComentario> resultadoDespuesDeQuitar = usuarioComentarioRepository.buscarMeGustaPorUsuario(usuarioQueDaLike, comentario);
        assertFalse(resultadoDespuesDeQuitar.isPresent());
    }


}