package org.twelve.repositoriesImpl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.twelve.dominio.entities.Comentario;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.Usuario;
import org.twelve.dominio.entities.UsuarioComentario;
import org.twelve.infraestructura.UsuarioComentarioRepositoryImpl;
import org.twelve.integracion.config.HibernateTestConfig;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestConfig.class})
public class UsuarioComentarioRepositoryImplTest {

    @Autowired
    private SessionFactory sessionFactory;

    private UsuarioComentarioRepositoryImpl usuarioComentarioRepository;

    @BeforeEach
    public void setUp() {
        usuarioComentarioRepository = new UsuarioComentarioRepositoryImpl(sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueUnUsuarioLeDaLikeAUnComentarioDeberiaRertornarTrue() {

        //preparacion
        Usuario usuarioQueComenta = new Usuario();
        usuarioQueComenta.setId(2);
        usuarioQueComenta.setUsername("usr1");
        sessionFactory.getCurrentSession().save(usuarioQueComenta);

        Movie pelicula1 = new Movie();
        pelicula1.setId(1);
        pelicula1.setNombre("hercules");
        sessionFactory.getCurrentSession().save(pelicula1);

        Usuario usuarioQueDaLikeAUnComentario = new Usuario();
        usuarioQueDaLikeAUnComentario.setId(1);
        usuarioQueDaLikeAUnComentario.setUsername("agusmd");
        sessionFactory.getCurrentSession().save(usuarioQueDaLikeAUnComentario);


        Comentario comentario1 = new Comentario();
        comentario1.setId(1);
        comentario1.setUsuario(usuarioQueComenta);
        comentario1.setMovie(pelicula1);
        comentario1.setDescripcion("buena");
        sessionFactory.getCurrentSession().save(comentario1);

        UsuarioComentario usuarioComentario = new UsuarioComentario();
        usuarioComentario.setComentario(comentario1);
        usuarioComentario.setUsuario(usuarioQueDaLikeAUnComentario);
        usuarioComentario.setLikeComentario(true);
        sessionFactory.getCurrentSession().save(usuarioComentario);

        //ejecucion
        boolean dioLike = usuarioComentarioRepository.existsByComentarioAndUsuario(comentario1.getId(), usuarioQueDaLikeAUnComentario.getId());

        assertTrue(dioLike);
    }

    @Test
    @Transactional
    @Rollback
    public void dadoQueUnUsuarioNoLeDaLikeAUnComentarioDeberiaRertornarFalse() {

        //preparacion
        Usuario usuarioQueComenta = new Usuario();
        usuarioQueComenta.setId(2);
        usuarioQueComenta.setUsername("usr1");
        sessionFactory.getCurrentSession().save(usuarioQueComenta);

        Movie pelicula1 = new Movie();
        pelicula1.setId(1);
        pelicula1.setNombre("hercules");
        sessionFactory.getCurrentSession().save(pelicula1);

        Usuario usuarioQueDaLikeAUnComentario = new Usuario();
        usuarioQueDaLikeAUnComentario.setId(1);
        usuarioQueDaLikeAUnComentario.setUsername("agusmd");
        sessionFactory.getCurrentSession().save(usuarioQueDaLikeAUnComentario);


        Comentario comentario1 = new Comentario();
        comentario1.setId(1);
        comentario1.setUsuario(usuarioQueComenta);
        comentario1.setMovie(pelicula1);
        comentario1.setDescripcion("buena");
        sessionFactory.getCurrentSession().save(comentario1);

        UsuarioComentario usuarioComentario = new UsuarioComentario();
        usuarioComentario.setComentario(comentario1);
        usuarioComentario.setUsuario(usuarioQueDaLikeAUnComentario);
        usuarioComentario.setLikeComentario(true);
        sessionFactory.getCurrentSession().save(usuarioComentario);

        //ejecucion
        boolean dioLike = usuarioComentarioRepository.existsByComentarioAndUsuario(comentario1.getId(), null);

        assertFalse(dioLike);
    }


    @Test
    @Transactional
    @Rollback
    public void testGuardarNuevoUsuarioComentarioOseaLike() {
        //preparacion
        Usuario usuarioQueComenta = new Usuario();
        usuarioQueComenta.setId(2);
        usuarioQueComenta.setUsername("usr1");
        sessionFactory.getCurrentSession().save(usuarioQueComenta);

        Movie pelicula1 = new Movie();
        pelicula1.setId(1);
        pelicula1.setNombre("hercules");
        sessionFactory.getCurrentSession().save(pelicula1);

        Usuario usuarioQueDaLikeAUnComentario = new Usuario();
        usuarioQueDaLikeAUnComentario.setId(1);
        usuarioQueDaLikeAUnComentario.setUsername("agusmd");
        sessionFactory.getCurrentSession().save(usuarioQueDaLikeAUnComentario);


        Comentario comentario1 = new Comentario();
        comentario1.setId(1);
        comentario1.setUsuario(usuarioQueComenta);
        comentario1.setMovie(pelicula1);
        comentario1.setDescripcion("buena");
        sessionFactory.getCurrentSession().save(comentario1);

        UsuarioComentario usuarioComentario = new UsuarioComentario();
        usuarioComentario.setComentario(comentario1);
        usuarioComentario.setUsuario(usuarioQueDaLikeAUnComentario);
        usuarioComentario.setLikeComentario(true);

        //ejecucion
        usuarioComentarioRepository.save(usuarioComentario);
        UsuarioComentario resultado = usuarioComentarioRepository.findByComentarioAndUsuario(comentario1.getId(), usuarioQueDaLikeAUnComentario.getId());


        //validacion
        assertNotNull(resultado, "se deberia haber guardado el like");
        assertEquals(comentario1.getId(), resultado.getComentario().getId(), "coincide el comentario");
        assertEquals(usuarioQueDaLikeAUnComentario.getId(), resultado.getUsuario().getId(), "coincide el usuario");
        assertTrue(resultado.getLikeComentario(), "el like deberia ser true");
    }

    @Test
    @Transactional
    @Rollback
    public void testBorrarUsuarioComentarioOseaQuitarLike() {
        //preparacion
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("agusmd");
        sessionFactory.getCurrentSession().save(usuario);

        Movie pelicula = new Movie();
        pelicula.setId(1);
        pelicula.setNombre("Hercules");
        sessionFactory.getCurrentSession().save(pelicula);

        Comentario comentario = new Comentario();
        comentario.setId(1);
        comentario.setUsuario(usuario);
        comentario.setMovie(pelicula);
        comentario.setDescripcion("Buena pelicula");
        sessionFactory.getCurrentSession().save(comentario);

        UsuarioComentario usuarioComentario = new UsuarioComentario();
        usuarioComentario.setComentario(comentario);
        usuarioComentario.setUsuario(usuario);
        usuarioComentario.setLikeComentario(true);
        sessionFactory.getCurrentSession().save(usuarioComentario);

        //ejecucion
        usuarioComentarioRepository.delete(usuarioComentario);
        UsuarioComentario resultado = usuarioComentarioRepository.findByComentarioAndUsuario(comentario.getId(), usuario.getId());

        //validacion
        assertNull(resultado, "comentario deberia ser eliminado");
    }


}
