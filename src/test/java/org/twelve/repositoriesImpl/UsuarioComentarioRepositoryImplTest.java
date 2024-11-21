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

    @Test
    @Transactional
    @Rollback
    @Disabled
    public void testFindComentarioIdsByUsuarioId() {
        //preparacion
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("agusmd");
        sessionFactory.getCurrentSession().save(usuario);

        Movie pelicula = new Movie();
        pelicula.setId(1);
        pelicula.setNombre("Hercules");
        sessionFactory.getCurrentSession().save(pelicula);

        Comentario comentario1 = new Comentario();
        comentario1.setId(1);
        comentario1.setUsuario(usuario);
        comentario1.setMovie(pelicula);
        comentario1.setDescripcion("Comentario 1");
        sessionFactory.getCurrentSession().save(comentario1);

        Comentario comentario2 = new Comentario();
        comentario2.setId(2);
        comentario2.setUsuario(usuario);
        comentario2.setMovie(pelicula);
        comentario2.setDescripcion("Comentario 2");
        sessionFactory.getCurrentSession().save(comentario2);

        UsuarioComentario usuarioComentario1 = new UsuarioComentario();
        usuarioComentario1.setComentario(comentario1);
        usuarioComentario1.setUsuario(usuario);
        usuarioComentario1.setLikeComentario(true);
        sessionFactory.getCurrentSession().save(usuarioComentario1);

        UsuarioComentario usuarioComentario2 = new UsuarioComentario();
        usuarioComentario2.setComentario(comentario2);
        usuarioComentario2.setUsuario(usuario);
        usuarioComentario2.setLikeComentario(true);
        sessionFactory.getCurrentSession().save(usuarioComentario2);

        //ejecucion
        List<Integer> comentarioIds = usuarioComentarioRepository.findComentarioIdsByUsuarioId(usuario.getId());

        //valiracion
        assertEquals(2, comentarioIds.size(), "el user deberia tener 2 comentarios likeados");
        assertTrue(comentarioIds.contains(1), "comentario 1");
        assertTrue(comentarioIds.contains(2), "comentario 2");
    }


}
