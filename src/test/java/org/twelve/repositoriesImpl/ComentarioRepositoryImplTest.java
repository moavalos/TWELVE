package org.twelve.repositoriesImpl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.twelve.dominio.entities.Comentario;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.Usuario;
import org.twelve.infraestructura.ComentarioRepositoryImpl;
import org.twelve.integracion.config.HibernateTestConfig;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestConfig.class})
public class ComentarioRepositoryImplTest {

    @Autowired
    private SessionFactory sessionFactory;
    private ComentarioRepositoryImpl comentarioRepository;

    @BeforeEach
    public void init() {
        this.comentarioRepository = new ComentarioRepositoryImpl(sessionFactory);
    }


    @Test
    @Transactional
    public void testDadoQueExisteUnRepositorioComentarioCuandoGuardoComentariosEntoncesLoEncuentroEnLaBaseDeDatos() {

        //preparacion
        Movie movie = new Movie();
        movie.setId(1);
        movie.setNombre("Matrix");
        sessionFactory.getCurrentSession().save(movie);

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Jose");
        sessionFactory.getCurrentSession().save(usuario);


        Comentario comentario = new Comentario();
        comentario.setDescripcion("buenarda");
        comentario.setMovie(movie);
        comentario.setUsuario(usuario);


        //ejecucion
        this.comentarioRepository.save(comentario);

        //validacion
        String hql = "FROM Comentario  WHERE descripcion = :descripcion";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("descripcion", "buenarda");
        Comentario resultado = (Comentario) query.getSingleResult();

        assertNotNull(resultado);
        assertThat(resultado, equalTo(comentario));
    }


    @Test
    @Transactional
    public void testDadoQueBuscoUnaPeliculaPorIdMeTraeTodosLosComentariosQueTenga() {
        //preparacion
        Movie movie = new Movie();
        movie.setId(1);
        movie.setNombre("matrix");
        sessionFactory.getCurrentSession().save(movie);

        Usuario usuario1 = new Usuario();
        usuario1.setId(1);
        usuario1.setNombre("usuario1");
        sessionFactory.getCurrentSession().save(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setId(2);
        usuario2.setNombre("usuario2");
        sessionFactory.getCurrentSession().save(usuario2);

        Comentario comentario1 = new Comentario();
        comentario1.setDescripcion("comentario1");
        comentario1.setMovie(movie);
        comentario1.setUsuario(usuario1);
        sessionFactory.getCurrentSession().save(comentario1);

        Comentario comentario2 = new Comentario();
        comentario2.setDescripcion("comentario2");
        comentario2.setMovie(movie);
        comentario2.setUsuario(usuario2);
        sessionFactory.getCurrentSession().save(comentario2);

        //ejecucion
        List<Comentario> comentarios = comentarioRepository.findByIdMovie(movie.getId());

        //validacion
        assertNotNull(comentarios);
        assertThat(comentarios.size(), equalTo(2));
        assertThat(comentarios.get(0).getDescripcion(), equalTo("comentario1"));
        assertThat(comentarios.get(1).getDescripcion(), equalTo("comentario2"));
    }

    @Test
    @Transactional
    public void testDadoQueBuscoLosUltimosTresComentariosPorUsuarioCuandoExistenComentariosEntoncesDevuelveLosTresUltimos() {
        //preparacion
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Jose");
        sessionFactory.getCurrentSession().save(usuario);
        //peliculas
        Movie movie1 = new Movie();
        movie1.setId(1);
        movie1.setNombre("pelicula1");
        sessionFactory.getCurrentSession().save(movie1);

        Movie movie2 = new Movie();
        movie2.setId(2);
        movie2.setNombre("pelicula2");
        sessionFactory.getCurrentSession().save(movie2);

        Movie movie3 = new Movie();
        movie3.setId(3);
        movie3.setNombre("pelicula3");
        sessionFactory.getCurrentSession().save(movie3);

        Movie movie4 = new Movie();
        movie4.setId(4);
        movie4.setNombre("pelicula4");
        sessionFactory.getCurrentSession().save(movie4);

        //comentarios
        Comentario comentario1 = new Comentario();
        comentario1.setDescripcion("comentario1");
        comentario1.setUsuario(usuario);
        comentario1.setMovie(movie1);
        sessionFactory.getCurrentSession().save(comentario1);

        Comentario comentario2 = new Comentario();
        comentario2.setDescripcion("comentario2");
        comentario2.setUsuario(usuario);
        comentario2.setMovie(movie2);
        sessionFactory.getCurrentSession().save(comentario2);

        Comentario comentario3 = new Comentario();
        comentario3.setDescripcion("comentario3");
        comentario3.setUsuario(usuario);
        comentario3.setMovie(movie3);
        sessionFactory.getCurrentSession().save(comentario3);

        Comentario comentario4 = new Comentario();
        comentario4.setDescripcion("comentario4");
        comentario4.setUsuario(usuario);
        comentario4.setMovie(movie4);
        sessionFactory.getCurrentSession().save(comentario4);

        //ejecucion
        List<Comentario> comentarios = comentarioRepository.findTop3ByUsuarioId(usuario.getId());

        //validacion
        assertNotNull(comentarios);
        assertThat(comentarios.size(), equalTo(3));
        assertThat(comentarios.get(0).getDescripcion(), equalTo("comentario4"));
        assertThat(comentarios.get(1).getDescripcion(), equalTo("comentario3"));
        assertThat(comentarios.get(2).getDescripcion(), equalTo("comentario2"));
    }


}
