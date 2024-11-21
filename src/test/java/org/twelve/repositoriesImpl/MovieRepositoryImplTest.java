package org.twelve.repositoriesImpl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.twelve.dominio.entities.Categoria;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.Pais;
import org.twelve.infraestructura.MovieRepositoryImpl;
import org.twelve.integracion.config.HibernateTestConfig;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        Categoria categoria = new Categoria();
        categoria.setNombre("CIENCIA_FICCION");
        this.sessionFactory.getCurrentSession().save(categoria);

        Movie movie = new Movie();
        movie.setNombre("The Matrix");
        movie.setAñoLanzamiento("1999");
        movie.getCategorias().add(categoria); // Asignar categoría
        movieRepository.guardar(movie);

        String hql = "FROM Movie WHERE nombre = :nombre";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("nombre", "The Matrix");

        Movie foundMovie = (Movie) query.getSingleResult();
        assertNotNull(foundMovie);
        assertEquals("The Matrix", foundMovie.getNombre());
        assertFalse(foundMovie.getCategorias().isEmpty()); // Verificar que tiene categorías
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPeliculaPorIdDevuelveCorrectamente() {
        Categoria categoria = new Categoria();
        categoria.setNombre("ACCION");
        this.sessionFactory.getCurrentSession().save(categoria);

        Movie movie = new Movie();
        movie.setNombre("Inception");
        movie.setAñoLanzamiento("2010");
        movie.getCategorias().add(categoria); // Asignar categoría
        movieRepository.guardar(movie);

        Movie peliculaEncontrada = movieRepository.findById(movie.getId());

        assertNotNull(peliculaEncontrada);
        assertEquals(movie.getId(), peliculaEncontrada.getId());
        assertEquals("Inception", peliculaEncontrada.getNombre());
        assertFalse(peliculaEncontrada.getCategorias().isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPeliculaPorTituloDevuelveCorrectamente() {
        Categoria categoria = new Categoria();
        categoria.setNombre("ACCION");
        this.sessionFactory.getCurrentSession().save(categoria);

        Movie movie = new Movie();
        movie.setNombre("Avatar");
        movie.getCategorias().add(categoria); // Asignar categoría
        movieRepository.guardar(movie);

        List<Movie> peliculasEncontradas = movieRepository.findByTitle("Avatar");
        assertFalse(peliculasEncontradas.isEmpty());
        assertEquals("Avatar", peliculasEncontradas.get(0).getNombre());
        assertFalse(peliculasEncontradas.get(0).getCategorias().isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testModificarPeliculaModificaCorrectamente() {
        Categoria categoria = new Categoria();
        categoria.setNombre("DRAMA");
        this.sessionFactory.getCurrentSession().save(categoria);

        Movie movie = new Movie();
        movie.setNombre("Titanic");
        movie.setAñoLanzamiento("1997");
        movie.getCategorias().add(categoria);
        movieRepository.guardar(movie);

        movie.setAñoLanzamiento("1998");
        movieRepository.guardar(movie);

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
        Categoria categoria1 = new Categoria();
        categoria1.setId(2);

        Movie movie1 = new Movie();
        movie1.setNombre("Coraline");
        movie1.setCantVistas(100);
        movie1.getCategorias().add(categoria1);
        movieRepository.guardar(movie1);

        Categoria categoria2 = new Categoria();
        categoria2.setId(3);

        Movie movie2 = new Movie();
        movie2.setNombre("Avatar");
        movie2.setCantVistas(200);
        movie2.getCategorias().add(categoria2);
        movieRepository.guardar(movie2);

        List<Movie> peliculasMasVistas = movieRepository.findMostViewed();
        assertFalse(peliculasMasVistas.isEmpty());
        assertEquals("Avatar", peliculasMasVistas.get(0).getNombre());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPeliculasMejorValoradasEntreDosTitulos() {
        Categoria categoria1 = new Categoria();
        categoria1.setId(3);

        Movie movie1 = new Movie();
        movie1.setId(1);
        movie1.setNombre("Coraline");
        movie1.setValoracion(4.5);
        movie1.getCategorias().add(categoria1);
        movieRepository.guardar(movie1);

        Categoria categoria2 = new Categoria();
        categoria2.setId(5);

        Movie movie2 = new Movie();
        movie2.setId(2);
        movie2.setNombre("Avatar");
        movie2.setValoracion(4.8);
        movie2.getCategorias().add(categoria2);
        movieRepository.guardar(movie2);

        List<Movie> topRatedMovies = movieRepository.findTopRated();
        assertFalse(topRatedMovies.isEmpty());
        assertEquals("Avatar", topRatedMovies.get(0).getNombre());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPeliculasMasNuevasEntreDosTitulos() {
        Categoria categoria1 = new Categoria();
        categoria1.setId(1);

        Movie movie1 = new Movie();
        movie1.setNombre("pelicula vieja");
        movie1.setAñoLanzamiento("1980");
        movie1.getCategorias().add(categoria1);
        movieRepository.guardar(movie1);

        Categoria categoria2 = new Categoria();
        categoria2.setId(2);

        Movie movie2 = new Movie();
        movie2.setNombre("pelicula nueva");
        movie2.setAñoLanzamiento("2020");
        movie2.getCategorias().add(categoria2);
        movieRepository.guardar(movie2);

        List<Movie> newestMovies = movieRepository.findNewestMovie();
        assertFalse(newestMovies.isEmpty());
        assertEquals("pelicula nueva", newestMovies.get(0).getNombre());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPeliculasPorAñoLanzamiento() {
        Categoria categoria1 = new Categoria();
        categoria1.setId(2);

        Movie movie1 = new Movie();
        movie1.setNombre("Matrix");
        movie1.setAñoLanzamiento("1999");
        movie1.getCategorias().add(categoria1);
        movieRepository.guardar(movie1);

        Categoria categoria2 = new Categoria();
        categoria2.setId(4);

        Movie movie2 = new Movie();
        movie2.setNombre("Toy Story");
        movie2.setAñoLanzamiento("1999");
        movie2.getCategorias().add(categoria2);
        movieRepository.guardar(movie2);

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
        Categoria categoria = new Categoria();
        categoria.setId(5);

        Movie movie = new Movie();
        movie.setNombre("The Godfather");
        movie.setAñoLanzamiento("1972");
        movie.getCategorias().add(categoria);
        movieRepository.guardar(movie);

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
    public void testFindSimilarMoviesDevuelvePeliculasSimilares() {
        Categoria categoria1 = new Categoria();
        categoria1.setNombre("MUSICAL");
        this.sessionFactory.getCurrentSession().save(categoria1);

        Categoria categoria2 = new Categoria();
        categoria2.setNombre("ACCIÓN");
        this.sessionFactory.getCurrentSession().save(categoria2);

        Movie movie1 = new Movie();
        movie1.setNombre("Grease");
        movie1.getCategorias().add(categoria1);
        movieRepository.guardar(movie1);

        Movie movie2 = new Movie();
        movie2.setNombre("High School Musical");
        movie2.getCategorias().add(categoria1);
        movieRepository.guardar(movie2);

        Movie movie3 = new Movie();
        movie3.setNombre("La La Land");
        movie3.getCategorias().add(categoria1);
        movieRepository.guardar(movie3);

        Movie movie4 = new Movie();
        movie4.setNombre("Star Wars");
        movie4.getCategorias().add(categoria2);
        movieRepository.guardar(movie4);

        Set<Categoria> categoriasSimilares = movie1.getCategorias();

        List<Movie> peliculasSimilares = movieRepository.findSimilarMovies(movie1.getId(), categoriasSimilares);

        assertNotNull(peliculasSimilares);
        assertEquals(2, peliculasSimilares.size());
        assertTrue(peliculasSimilares.stream().anyMatch(movie -> movie.getNombre().equals("High School Musical")));
        assertTrue(peliculasSimilares.stream().anyMatch(movie -> movie.getNombre().equals("La La Land")));
    }

    @Test
    @Transactional
    @Rollback
    public void testFindSimilarMoviesSinCoincidenciasDevuelveListaVacia() {
        Categoria categoria1 = new Categoria();
        categoria1.setNombre("MUSICAL");
        this.sessionFactory.getCurrentSession().save(categoria1);

        Categoria categoria2 = new Categoria();
        categoria2.setNombre("ACCION");
        this.sessionFactory.getCurrentSession().save(categoria2);

        Movie movie1 = new Movie();
        movie1.setNombre("Inception");
        movie1.getCategorias().add(categoria2);
        movieRepository.guardar(movie1);

        Set<Categoria> categoriasSimilares = movie1.getCategorias();

        List<Movie> peliculasSimilares = movieRepository.findSimilarMovies(movie1.getId(), categoriasSimilares);

        assertNotNull(peliculasSimilares);
        assertTrue(peliculasSimilares.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPeliculasPorCategoriaTopRated() {
        Categoria categoria = new Categoria();
        categoria.setNombre("COMEDIA");
        this.sessionFactory.getCurrentSession().save(categoria);

        Movie movie1 = new Movie();
        movie1.setNombre("Movie A");
        movie1.setValoracion(4.5);
        movie1.getCategorias().add(categoria);
        movieRepository.guardar(movie1);

        Movie movie2 = new Movie();
        movie2.setNombre("Movie B");
        movie2.setValoracion(4.8);
        movie2.getCategorias().add(categoria);
        movieRepository.guardar(movie2);

        List<Movie> topRatedMovies = movieRepository.findByCategoriaIdTopRated(categoria.getId());
        assertFalse(topRatedMovies.isEmpty());
        assertEquals("Movie B", topRatedMovies.get(0).getNombre());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPeliculasPorCategoriaMasNuevas() {
        Categoria categoria = new Categoria();
        categoria.setNombre("CIENCIA FICCION");
        this.sessionFactory.getCurrentSession().save(categoria);

        Movie movie1 = new Movie();
        movie1.setNombre("Old Movie");
        movie1.setAñoLanzamiento("1995");
        movie1.getCategorias().add(categoria);
        movieRepository.guardar(movie1);

        Movie movie2 = new Movie();
        movie2.setNombre("New Movie");
        movie2.setAñoLanzamiento("2022");
        movie2.getCategorias().add(categoria);
        movieRepository.guardar(movie2);

        List<Movie> newestMovies = movieRepository.findByCategoriaIdNewest(categoria.getId());
        assertFalse(newestMovies.isEmpty());
        assertEquals("New Movie", newestMovies.get(0).getNombre());
    }

    @Test
    @Transactional
    @Rollback
    public void testEncontrarProximosEstrenos() {
        Movie peliculaEstrenada = new Movie();
        peliculaEstrenada.setNombre("Pelicula estrenada");
        peliculaEstrenada.setFechaLanzamiento(LocalDate.now().minusDays(10));
        movieRepository.guardar(peliculaEstrenada);

        Movie peliculaNoEstrenada = new Movie();
        peliculaNoEstrenada.setNombre("Pelicula No Estrenada");
        peliculaNoEstrenada.setFechaLanzamiento(LocalDate.now().plusDays(10));
        movieRepository.guardar(peliculaNoEstrenada);

        List<Movie> upcomingMovies = movieRepository.findUpcomingMovies();

        assertEquals(1, upcomingMovies.size());
        assertEquals("Pelicula No Estrenada", upcomingMovies.get(0).getNombre());
    }


    @Test
    @Transactional
    @Rollback
    @DirtiesContext
    public void testFindByPaisIdDevuelveListaDePeliculasPorPais() {

        Pais pais = new Pais();
        pais.setNombre("EE.UU");
        this.sessionFactory.getCurrentSession().save(pais);

        Movie movie1 = new Movie();
        movie1.setNombre("Movie A");
        movie1.setPais(pais);

        Movie movie2 = new Movie();
        movie2.setNombre("Movie B");
        movie2.setPais(pais);

        this.movieRepository.guardar(movie1);
        this.movieRepository.guardar(movie2);

        List<Movie> peliculasEsperadas = new ArrayList<>();
        peliculasEsperadas.add(movie1);
        peliculasEsperadas.add(movie2);

        List<Movie> peliculasObtenidas = this.movieRepository.findByPaisId(1);

        assertEquals(peliculasEsperadas, peliculasObtenidas);
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext
    public void testFindByPaisNewestDevuelveListaDePeliculasPorPaisMasRecientesPrimero() {
        Pais pais = new Pais();
        pais.setNombre("EE.UU");
        this.sessionFactory.getCurrentSession().save(pais);

        Movie movie1 = new Movie();
        movie1.setNombre("Movie A");
        movie1.setAñoLanzamiento("1994");
        movie1.setPais(pais);
        this.movieRepository.guardar(movie1);

        Movie movie2 = new Movie();
        movie2.setNombre("Movie B");
        movie2.setAñoLanzamiento("2024");
        movie2.setPais(pais);

        this.movieRepository.guardar(movie2);

        List<Movie> peliculasObtenidas = this.movieRepository.findByPaisIdNewest(1);

        assertEquals(peliculasObtenidas.get(0), movie2);
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext
    public void testFindByPaisTopRatedDevuelveListaDePeliculasPorPaisMejorValoradasPrimero() {
        Pais pais = new Pais();
        pais.setNombre("EE.UU");
        this.sessionFactory.getCurrentSession().save(pais);

        Movie movie1 = new Movie();
        movie1.setNombre("Movie A");
        movie1.setValoracion(5.0);
        movie1.setPais(pais);

        Movie movie2 = new Movie();
        movie2.setNombre("Movie B");
        movie2.setValoracion(9.0);
        movie2.setPais(pais);

        this.movieRepository.guardar(movie1);
        this.movieRepository.guardar(movie2);

        List<Movie> peliculasObtenidas = this.movieRepository.findByPaisIdTopRated(1);

        assertEquals(peliculasObtenidas.get(0), movie2);
    }


    @Test
    @Transactional
    @Rollback
    @DirtiesContext
    public void testFindUpcomingMoviesByCategoryDevuelvesSoloPeliculasNoEstrenadas() {

        Categoria categoria = new Categoria();
        categoria.setNombre("DRAMA");
        this.sessionFactory.getCurrentSession().save(categoria);


        Movie movie1 = new Movie();
        movie1.setNombre("Movie A");
        movie1.setFechaLanzamiento(LocalDate.now().plusDays(10));
        movie1.getCategorias().add(categoria);


        Movie movie2 = new Movie();
        movie2.setNombre("Movie B");
        movie2.setFechaLanzamiento(LocalDate.now().minusDays(10));
        movie2.getCategorias().add(categoria);


        this.movieRepository.guardar(movie1);
        this.movieRepository.guardar(movie2);

        List<Movie> peliculasObtenidas = this.movieRepository.findUpcomingMoviesByCategoria(categoria.getId());

        assertEquals(peliculasObtenidas.get(0), movie1);
        assertEquals(1, peliculasObtenidas.size());

    }


}
