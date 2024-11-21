package org.twelve.repositoriesImpl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.twelve.dominio.entities.ListaColaborativa;
import org.twelve.dominio.entities.ListaMovie;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.Usuario;
import org.twelve.infraestructura.ListaColaborativaRepositoryImpl;
import org.twelve.integracion.config.HibernateTestConfig;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestConfig.class})
public class ListaColaborativaRepositoryImplTest {

    @Autowired
    private SessionFactory sessionFactory;

    private ListaColaborativaRepositoryImpl listaColaborativaRepositoryImpl;

    @BeforeEach
    public void setUp() {
        this.listaColaborativaRepositoryImpl = new ListaColaborativaRepositoryImpl(sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    public void testEncontrarTodosCuandoHayListasDevuelveListaCorrecta() throws Exception {
        Usuario creador = new Usuario();
        creador.setEmail("creador@unlam.com");
        creador.setPassword("password1");
        sessionFactory.getCurrentSession().save(creador);

        Usuario colaborador = new Usuario();
        colaborador.setEmail("colaborador@unlam.com");
        colaborador.setPassword("password2");
        sessionFactory.getCurrentSession().save(colaborador);

        ListaColaborativa lista1 = new ListaColaborativa();
        lista1.setNombre("Lista 1");
        lista1.setCreador(creador);
        lista1.setColaborador(colaborador);
        lista1.setFechaCreacion(LocalDate.now());
        sessionFactory.getCurrentSession().save(lista1);

        ListaColaborativa lista2 = new ListaColaborativa();
        lista2.setNombre("Lista 2");
        lista2.setCreador(colaborador);
        lista2.setColaborador(creador);
        lista2.setFechaCreacion(LocalDate.now());
        sessionFactory.getCurrentSession().save(lista2);

        List<ListaColaborativa> listas = listaColaborativaRepositoryImpl.encontrarTodos();

        assertNotNull(listas);
        assertEquals(2, listas.size());
        assertTrue(listas.stream().anyMatch(l -> l.getNombre().equals("Lista 1")));
        assertTrue(listas.stream().anyMatch(l -> l.getNombre().equals("Lista 2")));
    }

    @Test
    @Transactional
    @Rollback
    public void testEncontrarTodosCuandoNoHayListasDevuelveListaVacia() throws Exception {
        List<ListaColaborativa> listas = listaColaborativaRepositoryImpl.encontrarTodos();

        assertNotNull(listas);
        assertTrue(listas.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testEncontrarTodosConListasCreadorYColaboradorCorrectos() throws Exception {
        Usuario creador = new Usuario();
        creador.setEmail("creador@unlam.com");
        creador.setPassword("password1");
        sessionFactory.getCurrentSession().save(creador);

        Usuario colaborador = new Usuario();
        colaborador.setEmail("colaborador@unlam.com");
        colaborador.setPassword("password2");
        sessionFactory.getCurrentSession().save(colaborador);

        ListaColaborativa lista = new ListaColaborativa();
        lista.setNombre("Lista Colaborativa");
        lista.setCreador(creador);
        lista.setColaborador(colaborador);
        lista.setFechaCreacion(LocalDate.now());
        sessionFactory.getCurrentSession().save(lista);

        List<ListaColaborativa> listas = listaColaborativaRepositoryImpl.encontrarTodos();

        assertNotNull(listas);
        assertEquals(1, listas.size());
        ListaColaborativa listaEncontrada = listas.get(0);
        assertEquals("Lista Colaborativa", listaEncontrada.getNombre());
        assertEquals("creador@unlam.com", listaEncontrada.getCreador().getEmail());
        assertEquals("colaborador@unlam.com", listaEncontrada.getColaborador().getEmail());
    }

    @Test
    @Transactional
    @Rollback
    public void testGuardarListaColaborativaCuandoEsNueva() throws Exception {
        Usuario creador = new Usuario();
        creador.setEmail("creador@unlam.com");
        creador.setPassword("password1");
        sessionFactory.getCurrentSession().save(creador);

        Usuario colaborador = new Usuario();
        colaborador.setEmail("colaborador@unlam.com");
        colaborador.setPassword("password2");
        sessionFactory.getCurrentSession().save(colaborador);

        ListaColaborativa lista = new ListaColaborativa();
        lista.setNombre("Nueva Lista");
        lista.setCreador(creador);
        lista.setColaborador(colaborador);
        lista.setFechaCreacion(LocalDate.now());

        ListaColaborativa listaGuardada = listaColaborativaRepositoryImpl.guardar(lista);

        assertNotNull(listaGuardada);
        assertNotNull(listaGuardada.getId());
        assertEquals("Nueva Lista", listaGuardada.getNombre());
        assertEquals(creador.getId(), listaGuardada.getCreador().getId());
        assertEquals(colaborador.getId(), listaGuardada.getColaborador().getId());
    }

    @Test
    @Transactional
    @Rollback
    public void testGuardarListaColaborativaCuandoYaExiste() throws Exception {
        Usuario creador = new Usuario();
        creador.setEmail("creador@unlam.com");
        creador.setPassword("password1");
        sessionFactory.getCurrentSession().save(creador);

        Usuario colaborador = new Usuario();
        colaborador.setEmail("colaborador@unlam.com");
        colaborador.setPassword("password2");
        sessionFactory.getCurrentSession().save(colaborador);

        ListaColaborativa lista = new ListaColaborativa();
        lista.setNombre("Lista Existente");
        lista.setCreador(creador);
        lista.setColaborador(colaborador);
        lista.setFechaCreacion(LocalDate.now());
        sessionFactory.getCurrentSession().save(lista);

        lista.setNombre("Lista Actualizada");
        ListaColaborativa listaActualizada = listaColaborativaRepositoryImpl.guardar(lista);

        assertNotNull(listaActualizada);
        assertEquals(lista.getId(), listaActualizada.getId());
        assertEquals("Lista Actualizada", listaActualizada.getNombre());
        assertEquals(creador.getId(), listaActualizada.getCreador().getId());
        assertEquals(colaborador.getId(), listaActualizada.getColaborador().getId());
    }

    @Test
    @Transactional
    @Rollback
    public void testGuardarListaColaborativaConCamposNulosLanzaExcepcion() {
        ListaColaborativa lista = new ListaColaborativa();
        lista.setNombre("Lista Sin Creador o Colaborador");
        lista.setFechaCreacion(LocalDate.now());

        assertThrows(Exception.class, () -> listaColaborativaRepositoryImpl.guardar(lista));
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPorIdCuandoLaListaExiste() throws Exception {
        Usuario creador = new Usuario();
        creador.setEmail("creador@unlam.com");
        creador.setPassword("password1");
        sessionFactory.getCurrentSession().save(creador);

        Usuario colaborador = new Usuario();
        colaborador.setEmail("colaborador@unlam.com");
        colaborador.setPassword("password2");
        sessionFactory.getCurrentSession().save(colaborador);

        ListaColaborativa lista = new ListaColaborativa();
        lista.setNombre("Lista a Buscar");
        lista.setCreador(creador);
        lista.setColaborador(colaborador);
        lista.setFechaCreacion(LocalDate.now());
        sessionFactory.getCurrentSession().save(lista);

        ListaColaborativa listaEncontrada = listaColaborativaRepositoryImpl.buscarPorId(lista.getId());

        assertNotNull(listaEncontrada);
        assertEquals(lista.getId(), listaEncontrada.getId());
        assertEquals("Lista a Buscar", listaEncontrada.getNombre());
        assertEquals(creador.getId(), listaEncontrada.getCreador().getId());
        assertEquals(colaborador.getId(), listaEncontrada.getColaborador().getId());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPorIdCuandoLaListaNoExisteDevuelveNull() {
        ListaColaborativa listaEncontrada = listaColaborativaRepositoryImpl.buscarPorId(999);

        assertNull(listaEncontrada);
    }

    @Test
    @Transactional
    @Rollback
    public void testGuardarListaPeliculaGuardaCorrectamente() throws Exception {
        Usuario creador = new Usuario();
        creador.setEmail("creador@unlam.com");
        creador.setPassword("password1");
        sessionFactory.getCurrentSession().save(creador);

        Usuario colaborador = new Usuario();
        colaborador.setEmail("colaborador@unlam.com");
        colaborador.setPassword("password2");
        sessionFactory.getCurrentSession().save(colaborador);

        Movie pelicula = new Movie();
        pelicula.setNombre("Pelicula de Prueba");
        sessionFactory.getCurrentSession().save(pelicula);

        ListaColaborativa lista = new ListaColaborativa();
        lista.setNombre("Lista con Película");
        lista.setCreador(creador);
        lista.setColaborador(colaborador);
        lista.setFechaCreacion(LocalDate.now());
        sessionFactory.getCurrentSession().save(lista);

        ListaMovie listaPelicula = new ListaMovie();
        listaPelicula.setLista(lista);
        listaPelicula.setPelicula(pelicula);
        listaPelicula.setUsuarioAgregador(creador);
        listaPelicula.setFechaAgregada(LocalDate.now());

        listaColaborativaRepositoryImpl.guardarListaPelicula(listaPelicula);

        String hql = "FROM ListaMovie WHERE lista.id = :listaId AND pelicula.id = :peliculaId";
        ListaMovie listaPeliculaGuardada = (ListaMovie) sessionFactory.getCurrentSession()
                .createQuery(hql)
                .setParameter("listaId", lista.getId())
                .setParameter("peliculaId", pelicula.getId())
                .getSingleResult();

        assertNotNull(listaPeliculaGuardada);
        assertEquals(lista.getId(), listaPeliculaGuardada.getLista().getId());
        assertEquals(pelicula.getId(), listaPeliculaGuardada.getPelicula().getId());
        assertEquals(creador.getId(), listaPeliculaGuardada.getUsuarioAgregador().getId());
    }

    @Test
    @Transactional
    @Rollback
    public void testGuardarListaPeliculaCuandoYaExisteActualizaCorrectamente() throws Exception {
        Usuario creador = new Usuario();
        Usuario colaborador = new Usuario();
        creador.setEmail("creador@unlam.com");
        creador.setPassword("password1");
        sessionFactory.getCurrentSession().save(creador);
        sessionFactory.getCurrentSession().save(colaborador);

        Movie pelicula = new Movie();
        pelicula.setNombre("Pelicula de Prueba");
        sessionFactory.getCurrentSession().save(pelicula);

        ListaColaborativa lista = new ListaColaborativa();
        lista.setNombre("Lista con Película");
        lista.setCreador(creador);
        lista.setFechaCreacion(LocalDate.now());
        lista.setColaborador(colaborador);
        sessionFactory.getCurrentSession().save(lista);

        ListaMovie listaPelicula = new ListaMovie();
        listaPelicula.setLista(lista);
        listaPelicula.setPelicula(pelicula);
        listaPelicula.setUsuarioAgregador(creador);
        listaPelicula.setFechaAgregada(LocalDate.now());
        sessionFactory.getCurrentSession().save(listaPelicula);

        listaPelicula.setFechaAgregada(LocalDate.now().plusDays(1));
        listaColaborativaRepositoryImpl.guardarListaPelicula(listaPelicula);

        ListaMovie listaPeliculaActualizada = (ListaMovie) sessionFactory.getCurrentSession()
                .createQuery("FROM ListaMovie WHERE id = :id")
                .setParameter("id", listaPelicula.getId())
                .getSingleResult();

        assertNotNull(listaPeliculaActualizada);
        assertEquals(listaPelicula.getId(), listaPeliculaActualizada.getId());
        assertEquals(LocalDate.now().plusDays(1), listaPeliculaActualizada.getFechaAgregada());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPorUsuarioIdCuandoElUsuarioEsCreador() throws Exception {
        Usuario creador = new Usuario();
        creador.setEmail("creador@unlam.com");
        creador.setPassword("password1");
        sessionFactory.getCurrentSession().save(creador);

        Usuario colaborador = new Usuario();
        colaborador.setEmail("colaborador@unlam.com");
        colaborador.setPassword("password2");
        sessionFactory.getCurrentSession().save(colaborador);

        ListaColaborativa lista1 = new ListaColaborativa();
        lista1.setNombre("Lista 1");
        lista1.setCreador(creador);
        lista1.setColaborador(colaborador);
        lista1.setFechaCreacion(LocalDate.now());
        sessionFactory.getCurrentSession().save(lista1);

        ListaColaborativa lista2 = new ListaColaborativa();
        lista2.setNombre("Lista 2");
        lista2.setCreador(creador);
        lista2.setColaborador(colaborador);
        lista2.setFechaCreacion(LocalDate.now());
        sessionFactory.getCurrentSession().save(lista2);

        List<ListaColaborativa> listas = listaColaborativaRepositoryImpl.buscarPorUsuarioId(creador.getId());

        assertNotNull(listas);
        assertEquals(2, listas.size());
        assertTrue(listas.stream().anyMatch(lista -> lista.getNombre().equals("Lista 1")));
        assertTrue(listas.stream().anyMatch(lista -> lista.getNombre().equals("Lista 2")));
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPorUsuarioIdCuandoElUsuarioEsColaborador() throws Exception {
        Usuario creador = new Usuario();
        creador.setEmail("creador@unlam.com");
        creador.setPassword("password1");
        sessionFactory.getCurrentSession().save(creador);

        Usuario colaborador = new Usuario();
        colaborador.setEmail("colaborador@unlam.com");
        colaborador.setPassword("password2");
        sessionFactory.getCurrentSession().save(colaborador);

        ListaColaborativa lista1 = new ListaColaborativa();
        lista1.setNombre("Lista 1");
        lista1.setCreador(creador);
        lista1.setColaborador(colaborador);
        lista1.setFechaCreacion(LocalDate.now());
        sessionFactory.getCurrentSession().save(lista1);

        ListaColaborativa lista2 = new ListaColaborativa();
        lista2.setNombre("Lista 2");
        lista2.setCreador(creador);
        lista2.setColaborador(colaborador);
        lista2.setFechaCreacion(LocalDate.now());
        sessionFactory.getCurrentSession().save(lista2);

        List<ListaColaborativa> listas = listaColaborativaRepositoryImpl.buscarPorUsuarioId(colaborador.getId());

        assertNotNull(listas);
        assertEquals(2, listas.size());
        assertTrue(listas.stream().anyMatch(lista -> lista.getNombre().equals("Lista 1")));
        assertTrue(listas.stream().anyMatch(lista -> lista.getNombre().equals("Lista 2")));
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPorUsuarioIdCuandoNoTieneListas() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("usuario@unlam.com");
        usuario.setPassword("password");
        sessionFactory.getCurrentSession().save(usuario);

        List<ListaColaborativa> listas = listaColaborativaRepositoryImpl.buscarPorUsuarioId(usuario.getId());

        assertNotNull(listas);
        assertTrue(listas.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPorUsuarioIdConUsuarioInexistente() throws Exception {
        List<ListaColaborativa> listas = listaColaborativaRepositoryImpl.buscarPorUsuarioId(999);

        assertNotNull(listas);
        assertTrue(listas.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPeliculasPorListaIdConPeliculasAsociadas() throws Exception {
        Usuario creador = new Usuario();
        creador.setEmail("creador@unlam.com");
        creador.setPassword("password1");
        sessionFactory.getCurrentSession().save(creador);

        Usuario colaborador = new Usuario();
        colaborador.setEmail("colaborador@unlam.com");
        colaborador.setPassword("password2");
        sessionFactory.getCurrentSession().save(colaborador);

        ListaColaborativa lista = new ListaColaborativa();
        lista.setNombre("Lista de Prueba");
        lista.setCreador(creador);
        lista.setColaborador(colaborador);
        lista.setFechaCreacion(LocalDate.now());
        sessionFactory.getCurrentSession().save(lista);

        Movie pelicula1 = new Movie();
        pelicula1.setNombre("Pelicula 1");
        sessionFactory.getCurrentSession().save(pelicula1);

        Movie pelicula2 = new Movie();
        pelicula2.setNombre("Pelicula 2");
        sessionFactory.getCurrentSession().save(pelicula2);

        ListaMovie listaMovie1 = new ListaMovie();
        listaMovie1.setLista(lista);
        listaMovie1.setPelicula(pelicula1);
        listaMovie1.setFechaAgregada(LocalDate.now());
        listaMovie1.setUsuarioAgregador(creador);
        sessionFactory.getCurrentSession().save(listaMovie1);

        ListaMovie listaMovie2 = new ListaMovie();
        listaMovie2.setLista(lista);
        listaMovie2.setPelicula(pelicula2);
        listaMovie2.setFechaAgregada(LocalDate.now());
        listaMovie2.setUsuarioAgregador(creador);
        sessionFactory.getCurrentSession().save(listaMovie2);

        List<ListaMovie> peliculasEnLista = listaColaborativaRepositoryImpl.buscarPeliculasPorListaId(lista.getId());

        assertNotNull(peliculasEnLista);
        assertEquals(2, peliculasEnLista.size());
        assertTrue(peliculasEnLista.stream().anyMatch(lm -> lm.getPelicula().getNombre().equals("Pelicula 1")));
        assertTrue(peliculasEnLista.stream().anyMatch(lm -> lm.getPelicula().getNombre().equals("Pelicula 2")));
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPeliculasPorListaIdConListaSinPeliculas() throws Exception {
        Usuario creador = new Usuario();
        creador.setEmail("creador@unlam.com");
        creador.setPassword("password1");
        sessionFactory.getCurrentSession().save(creador);

        Usuario colaborador = new Usuario();
        colaborador.setEmail("colaborador@unlam.com");
        colaborador.setPassword("password2");
        sessionFactory.getCurrentSession().save(colaborador);

        ListaColaborativa lista = new ListaColaborativa();
        lista.setNombre("Lista Vacía");
        lista.setCreador(creador);
        lista.setColaborador(colaborador);
        lista.setFechaCreacion(LocalDate.now());
        sessionFactory.getCurrentSession().save(lista);

        List<ListaMovie> peliculasEnLista = listaColaborativaRepositoryImpl.buscarPeliculasPorListaId(lista.getId());

        assertNotNull(peliculasEnLista);
        assertTrue(peliculasEnLista.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testBuscarPeliculasPorListaIdConListaInexistente() throws Exception {
        List<ListaMovie> peliculasEnLista = listaColaborativaRepositoryImpl.buscarPeliculasPorListaId(999);

        assertNotNull(peliculasEnLista);
        assertTrue(peliculasEnLista.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testExistePeliculaEnListaCuandoPeliculaEstaEnLista() throws Exception {
        Usuario creador = new Usuario();
        creador.setEmail("creador@unlam.com");
        creador.setPassword("password1");
        sessionFactory.getCurrentSession().save(creador);

        Usuario colaborador = new Usuario();
        colaborador.setEmail("colaborador@unlam.com");
        colaborador.setPassword("password2");
        sessionFactory.getCurrentSession().save(colaborador);

        ListaColaborativa lista = new ListaColaborativa();
        lista.setNombre("Lista de Prueba");
        lista.setCreador(creador);
        lista.setColaborador(colaborador);
        lista.setFechaCreacion(LocalDate.now());
        sessionFactory.getCurrentSession().save(lista);

        Movie pelicula = new Movie();
        pelicula.setNombre("Pelicula 1");
        sessionFactory.getCurrentSession().save(pelicula);

        ListaMovie listaMovie = new ListaMovie();
        listaMovie.setLista(lista);
        listaMovie.setPelicula(pelicula);
        listaMovie.setFechaAgregada(LocalDate.now());
        listaMovie.setUsuarioAgregador(creador);
        sessionFactory.getCurrentSession().save(listaMovie);

        boolean resultado = listaColaborativaRepositoryImpl.existePeliculaEnLista(lista.getId(), pelicula.getId());

        assertTrue(resultado);
    }

    @Test
    @Transactional
    @Rollback
    public void testExistePeliculaEnListaCuandoPeliculaNoEstaEnLista() throws Exception {
        Usuario creador = new Usuario();
        creador.setEmail("creador@unlam.com");
        creador.setPassword("password1");
        sessionFactory.getCurrentSession().save(creador);

        Usuario colaborador = new Usuario();
        colaborador.setEmail("colaborador@unlam.com");
        colaborador.setPassword("password2");
        sessionFactory.getCurrentSession().save(colaborador);

        ListaColaborativa lista = new ListaColaborativa();
        lista.setNombre("Lista de Prueba");
        lista.setCreador(creador);
        lista.setColaborador(colaborador);
        lista.setFechaCreacion(LocalDate.now());
        sessionFactory.getCurrentSession().save(lista);

        Movie pelicula = new Movie();
        pelicula.setNombre("Pelicula 2");
        sessionFactory.getCurrentSession().save(pelicula);

        boolean resultado = listaColaborativaRepositoryImpl.existePeliculaEnLista(lista.getId(), pelicula.getId());

        assertFalse(resultado);
    }

    @Test
    @Transactional
    @Rollback
    public void testExistePeliculaEnListaCuandoListaNoExiste() throws Exception {
        Movie pelicula = new Movie();
        pelicula.setNombre("Pelicula Inexistente");
        sessionFactory.getCurrentSession().save(pelicula);

        boolean resultado = listaColaborativaRepositoryImpl.existePeliculaEnLista(999, pelicula.getId());

        assertFalse(resultado);
    }

    @Test
    @Transactional
    @Rollback
    public void testExistePeliculaEnListaCuandoPeliculaNoExiste() throws Exception {
        Usuario creador = new Usuario();
        creador.setEmail("creador@unlam.com");
        creador.setPassword("password1");
        sessionFactory.getCurrentSession().save(creador);

        Usuario colaborador = new Usuario();
        colaborador.setEmail("colaborador@unlam.com");
        colaborador.setPassword("password2");
        sessionFactory.getCurrentSession().save(colaborador);

        ListaColaborativa lista = new ListaColaborativa();
        lista.setNombre("Lista de Prueba");
        lista.setCreador(creador);
        lista.setColaborador(colaborador);
        lista.setFechaCreacion(LocalDate.now());
        sessionFactory.getCurrentSession().save(lista);

        boolean resultado = listaColaborativaRepositoryImpl.existePeliculaEnLista(lista.getId(), 999);

        assertFalse(resultado);
    }

    @Test
    @Transactional
    @Rollback
    public void testExistePeliculaEnListaCuandoListaYPeliculaNoExisten() throws Exception {
        boolean resultado = listaColaborativaRepositoryImpl.existePeliculaEnLista(999, 888);

        assertFalse(resultado);
    }
}
