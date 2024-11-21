package org.twelve.servicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.twelve.dominio.ListaColaborativaRepository;
import org.twelve.dominio.MovieRepository;
import org.twelve.dominio.RepositorioUsuario;
import org.twelve.dominio.entities.ListaColaborativa;
import org.twelve.dominio.entities.ListaMovie;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.Usuario;
import org.twelve.dominio.serviceImpl.ListaColaborativaServiceImpl;
import org.twelve.presentacion.dto.ListaColaborativaDTO;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ListaColaborativaServiceImplTest {

    private ListaColaborativaServiceImpl listaColaborativaServiceImpl;
    private ListaColaborativaRepository listaColaborativaRepository;
    private RepositorioUsuario repositorioUsuario;
    private MovieRepository movieRepository;

    @BeforeEach
    public void init() {
        movieRepository = mock(MovieRepository.class);
        repositorioUsuario = mock(RepositorioUsuario.class);
        listaColaborativaRepository = mock(ListaColaborativaRepository.class);
        listaColaborativaServiceImpl = new ListaColaborativaServiceImpl(repositorioUsuario, listaColaborativaRepository, movieRepository);
    }

    @Test
    public void testObtenerTodasLasListasColaborativasCuandoExistenListas() {
        ListaColaborativa listaColaborativa1 = new ListaColaborativa();
        listaColaborativa1.setId(1);
        listaColaborativa1.setNombre("Lista 1");

        ListaColaborativa listaColaborativa2 = new ListaColaborativa();
        listaColaborativa2.setId(2);
        listaColaborativa2.setNombre("Lista 2");

        when(listaColaborativaRepository.encontrarTodos()).thenReturn(List.of(listaColaborativa1, listaColaborativa2));

        List<ListaColaborativaDTO> resultado = listaColaborativaServiceImpl.obtenerTodasLasListasColaborativas();

        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertEquals(2, resultado.size(), "El tamaño de la lista debe ser 2");
        assertEquals("Lista 1", resultado.get(0).getNombre(), "El nombre de la primera lista debe ser 'Lista 1'");
        assertEquals("Lista 2", resultado.get(1).getNombre(), "El nombre de la segunda lista debe ser 'Lista 2'");

        verify(listaColaborativaRepository, times(1)).encontrarTodos();
    }

    @Test
    public void testObtenerTodasLasListasColaborativasCuandoNoExistenListas() {
        when(listaColaborativaRepository.encontrarTodos()).thenReturn(Collections.emptyList());

        List<ListaColaborativaDTO> resultado = listaColaborativaServiceImpl.obtenerTodasLasListasColaborativas();

        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertTrue(resultado.isEmpty(), "El resultado debe ser una lista vacía");

        verify(listaColaborativaRepository, times(1)).encontrarTodos();
    }

    @Test
    public void testObtenerListasPorUsuarioCuandoExistenListas() {
        ListaColaborativa listaColaborativa1 = new ListaColaborativa();
        listaColaborativa1.setId(1);
        listaColaborativa1.setNombre("Lista 1");

        ListaColaborativa listaColaborativa2 = new ListaColaborativa();
        listaColaborativa2.setId(2);
        listaColaborativa2.setNombre("Lista 2");

        when(listaColaborativaRepository.buscarPorUsuarioId(1)).thenReturn(List.of(listaColaborativa1, listaColaborativa2));

        List<ListaColaborativaDTO> resultado = listaColaborativaServiceImpl.obtenerListasPorUsuario(1);

        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertEquals(2, resultado.size(), "El tamaño de la lista debe ser 2");
        assertEquals("Lista 1", resultado.get(0).getNombre(), "El nombre de la primera lista debe ser 'Lista 1'");
        assertEquals("Lista 2", resultado.get(1).getNombre(), "El nombre de la segunda lista debe ser 'Lista 2'");

        verify(listaColaborativaRepository, times(1)).buscarPorUsuarioId(1);
    }

    @Test
    public void testObtenerListasPorUsuarioCuandoNoExistenListas() {
        when(listaColaborativaRepository.buscarPorUsuarioId(1)).thenReturn(Collections.emptyList());

        List<ListaColaborativaDTO> resultado = listaColaborativaServiceImpl.obtenerListasPorUsuario(1);

        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertTrue(resultado.isEmpty(), "El resultado debe ser una lista vacía");

        verify(listaColaborativaRepository, times(1)).buscarPorUsuarioId(1);
    }

    @Test
    public void testObtenerListasPorUsuarioConUsuarioSinListas() {
        when(listaColaborativaRepository.buscarPorUsuarioId(999)).thenReturn(Collections.emptyList());

        List<ListaColaborativaDTO> resultado = listaColaborativaServiceImpl.obtenerListasPorUsuario(999);

        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertTrue(resultado.isEmpty(), "El resultado debe ser una lista vacía");

        verify(listaColaborativaRepository, times(1)).buscarPorUsuarioId(999);
    }

    @Test
    public void testCrearListaColaborativaCuandoUsuariosSonAmigos() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1);
        Usuario usuario2 = new Usuario();
        usuario2.setId(2);

        when(repositorioUsuario.existeRelacion(1, 2)).thenReturn(true);
        when(repositorioUsuario.buscarPorId(1)).thenReturn(usuario1);
        when(repositorioUsuario.buscarPorId(2)).thenReturn(usuario2);

        ListaColaborativa listaColaborativa = new ListaColaborativa();
        listaColaborativa.setId(1);
        listaColaborativa.setCreador(usuario1);
        listaColaborativa.setColaborador(usuario2);
        listaColaborativa.setNombre("Lista de prueba");
        listaColaborativa.setFechaCreacion(LocalDate.now());

        when(listaColaborativaRepository.guardar(any(ListaColaborativa.class))).thenReturn(listaColaborativa);

        ListaColaborativaDTO resultado = listaColaborativaServiceImpl.crearListaColaborativa(1, 2, "Lista de prueba");

        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertEquals(1, resultado.getId(), "El ID de la lista debe ser 1");
        assertEquals("Lista de prueba", resultado.getNombre(), "El nombre de la lista debe ser 'Lista de prueba'");
        assertEquals(1, resultado.getCreador().getId(), "El creador debe ser el usuario con ID 1");
        assertEquals(2, resultado.getColaborador().getId(), "El colaborador debe ser el usuario con ID 2");

        verify(repositorioUsuario, times(1)).existeRelacion(1, 2);
    }

    @Test
    public void testCrearListaColaborativaCuandoNoSonAmigos() {
        when(repositorioUsuario.existeRelacion(1, 2)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            listaColaborativaServiceImpl.crearListaColaborativa(1, 2, "Lista de prueba");
        });

        assertEquals("Los usuarios deben seguirse mutuamente para colaborar.", exception.getMessage(),
                "El mensaje de excepción debe ser 'Los usuarios deben seguirse mutuamente para colaborar.'");

        verify(repositorioUsuario, times(1)).existeRelacion(1, 2);
    }

    @Test
    public void testAgregarPeliculaAListaCuandoListaNoExiste() {
        when(listaColaborativaRepository.buscarPorId(1)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            listaColaborativaServiceImpl.agregarPeliculaALista(1, 10, 100);
        });

        assertEquals("Lista no encontrada", exception.getMessage(), "El mensaje de excepción debe ser 'Lista no encontrada'");

        verify(listaColaborativaRepository, times(1)).buscarPorId(1);
    }

    @Test
    public void testAgregarPeliculaAListaCuandoPeliculaNoExiste() {
        ListaColaborativa lista = new ListaColaborativa();
        lista.setId(1);

        when(listaColaborativaRepository.buscarPorId(1)).thenReturn(lista);
        when(movieRepository.findById(10)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            listaColaborativaServiceImpl.agregarPeliculaALista(1, 10, 100);
        });

        assertEquals("Película no encontrada", exception.getMessage(), "El mensaje de excepción debe ser 'Película no encontrada'");
    }

    @Test
    public void testAgregarPeliculaAListaCuandoPeliculaYaEstaEnLista() {
        ListaColaborativa lista = new ListaColaborativa();
        lista.setId(1);

        Movie pelicula = new Movie();
        pelicula.setId(10);

        when(listaColaborativaRepository.buscarPorId(1)).thenReturn(lista);
        when(movieRepository.findById(10)).thenReturn(pelicula);
        when(listaColaborativaRepository.existePeliculaEnLista(1, 10)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            listaColaborativaServiceImpl.agregarPeliculaALista(1, 10, 100);
        });

        assertEquals("La película ya está en esta lista.", exception.getMessage(),
                "El mensaje de excepción debe ser 'La película ya está en esta lista.'");

    }

    @Test
    public void testObtenerDetalleListaExitoso() {
        Usuario creador = new Usuario();
        creador.setId(1);
        creador.setNombre("Usuario Creador");

        ListaColaborativa lista = new ListaColaborativa();
        lista.setId(1);
        lista.setNombre("Lista de prueba");
        lista.setCreador(creador);
        lista.setFechaCreacion(LocalDate.now());

        when(listaColaborativaRepository.buscarPorId(1)).thenReturn(lista);

        ListaColaborativaDTO resultado = listaColaborativaServiceImpl.obtenerDetalleLista(1);

        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertEquals(1, resultado.getId(), "El ID de la lista debe ser 1");
        assertEquals("Lista de prueba", resultado.getNombre(), "El nombre de la lista debe ser 'Lista de prueba'");
        assertEquals(1, resultado.getCreador().getId(), "El ID del creador debe ser 1");

        verify(listaColaborativaRepository, times(1)).buscarPorId(1);
    }

    @Test
    public void testObtenerDetalleListaSinCreador() {
        ListaColaborativa lista = new ListaColaborativa();
        lista.setId(1);
        lista.setNombre("Lista sin creador");

        when(listaColaborativaRepository.buscarPorId(1)).thenReturn(lista);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            listaColaborativaServiceImpl.obtenerDetalleLista(1);
        });

        assertEquals("La lista no tiene un creador asignado.", exception.getMessage(),
                "El mensaje de excepción debe ser 'La lista no tiene un creador asignado.'");

        verify(listaColaborativaRepository, times(1)).buscarPorId(1);
    }

    @Test
    public void testObtenerPeliculasPorListaIdExitoso() {
        ListaColaborativa lista = new ListaColaborativa();
        lista.setId(1);
        lista.setNombre("Lista de prueba");

        Movie pelicula1 = new Movie();
        pelicula1.setId(10);
        pelicula1.setNombre("Película 10");

        Movie pelicula2 = new Movie();
        pelicula2.setId(20);
        pelicula2.setNombre("Película 20");

        ListaMovie listaMovie1 = new ListaMovie();
        listaMovie1.setLista(lista);
        listaMovie1.setPelicula(pelicula1);

        ListaMovie listaMovie2 = new ListaMovie();
        listaMovie2.setLista(lista);
        listaMovie2.setPelicula(pelicula2);

        when(listaColaborativaRepository.buscarPeliculasPorListaId(1)).thenReturn(List.of(listaMovie1, listaMovie2));

        List<ListaMovie> resultado = listaColaborativaServiceImpl.obtenerPeliculasPorListaId(1);

        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertEquals(2, resultado.size(), "El tamaño de la lista debe ser 2");
        assertEquals(10, resultado.get(0).getPelicula().getId(), "El ID de la primera película debe ser 10");
        assertEquals(20, resultado.get(1).getPelicula().getId(), "El ID de la segunda película debe ser 20");

        verify(listaColaborativaRepository, times(1)).buscarPeliculasPorListaId(1);
    }

    @Test
    public void testObtenerPeliculasPorListaIdSinPeliculas() {
        when(listaColaborativaRepository.buscarPeliculasPorListaId(1)).thenReturn(Collections.emptyList());

        List<ListaMovie> resultado = listaColaborativaServiceImpl.obtenerPeliculasPorListaId(1);

        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertTrue(resultado.isEmpty(), "El resultado debe ser una lista vacía");

        verify(listaColaborativaRepository, times(1)).buscarPeliculasPorListaId(1);
    }

    @Test
    public void testObtenerPeliculasPorListaIdExitoso2() {
        ListaColaborativa lista = new ListaColaborativa();
        lista.setId(1);
        lista.setNombre("Lista de prueba");

        Movie pelicula1 = new Movie();
        pelicula1.setId(10);
        pelicula1.setNombre("Película 10");

        Movie pelicula2 = new Movie();
        pelicula2.setId(20);
        pelicula2.setNombre("Película 20");

        ListaMovie listaMovie1 = new ListaMovie();
        listaMovie1.setLista(lista);
        listaMovie1.setPelicula(pelicula1);

        ListaMovie listaMovie2 = new ListaMovie();
        listaMovie2.setLista(lista);
        listaMovie2.setPelicula(pelicula2);

        when(listaColaborativaRepository.buscarPeliculasPorListaId(1)).thenReturn(List.of(listaMovie1, listaMovie2));

        List<ListaMovie> resultado = listaColaborativaServiceImpl.obtenerPeliculasPorListaId(1);

        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertEquals(2, resultado.size(), "El tamaño de la lista debe ser 2");
        assertEquals(10, resultado.get(0).getPelicula().getId(), "El ID de la primera película debe ser 10");
        assertEquals("Película 10", resultado.get(0).getPelicula().getNombre(), "El nombre de la primera película debe ser 'Película 10'");
        assertEquals(20, resultado.get(1).getPelicula().getId(), "El ID de la segunda película debe ser 20");
        assertEquals("Película 20", resultado.get(1).getPelicula().getNombre(), "El nombre de la segunda película debe ser 'Película 20'");

        verify(listaColaborativaRepository, times(1)).buscarPeliculasPorListaId(1);
    }

    @Test
    public void testObtenerPeliculasPorListaIdSinPeliculas2() {
        when(listaColaborativaRepository.buscarPeliculasPorListaId(1)).thenReturn(Collections.emptyList());

        List<ListaMovie> resultado = listaColaborativaServiceImpl.obtenerPeliculasPorListaId(1);

        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertTrue(resultado.isEmpty(), "El resultado debe ser una lista vacía");

        verify(listaColaborativaRepository, times(1)).buscarPeliculasPorListaId(1);
    }

    @Test
    public void testObtenerPeliculasPorListaIdListaNoEncontrada() {
        when(listaColaborativaRepository.buscarPeliculasPorListaId(999)).thenReturn(Collections.emptyList());

        List<ListaMovie> resultado = listaColaborativaServiceImpl.obtenerPeliculasPorListaId(999);

        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertTrue(resultado.isEmpty(), "El resultado debe ser una lista vacía");

        verify(listaColaborativaRepository, times(1)).buscarPeliculasPorListaId(999);
    }

    @Test
    public void testCrearListaColaborativaExitoso() {
        Integer usuario1Id = 1;
        Integer usuario2Id = 2;
        String nombreLista = "Lista Nueva";

        Usuario usuario1 = new Usuario();
        usuario1.setId(usuario1Id);

        Usuario usuario2 = new Usuario();
        usuario2.setId(usuario2Id);

        ListaColaborativa lista = new ListaColaborativa();
        lista.setId(1);
        lista.setNombre(nombreLista);
        lista.setCreador(usuario1);
        lista.setColaborador(usuario2);
        lista.setFechaCreacion(LocalDate.now());

        when(repositorioUsuario.existeRelacion(usuario1Id, usuario2Id)).thenReturn(true);
        when(repositorioUsuario.buscarPorId(usuario1Id)).thenReturn(usuario1);
        when(repositorioUsuario.buscarPorId(usuario2Id)).thenReturn(usuario2);
        when(listaColaborativaRepository.existeListaConNombreParaUsuario(usuario1Id, nombreLista)).thenReturn(false);
        when(listaColaborativaRepository.guardar(any(ListaColaborativa.class))).thenReturn(lista);

        ListaColaborativaDTO resultado = listaColaborativaServiceImpl.crearListaColaborativa(usuario1Id, usuario2Id, nombreLista);

        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertEquals(1, resultado.getId(), "El ID de la lista debe ser 1");
        assertEquals(nombreLista, resultado.getNombre(), "El nombre de la lista debe coincidir");
        assertEquals(usuario1Id, resultado.getCreador().getId(), "El creador debe ser el usuario con ID 1");
        assertEquals(usuario2Id, resultado.getColaborador().getId(), "El colaborador debe ser el usuario con ID 2");
    }

    @Test
    public void testCrearListaColaborativaUsuariosNoAmigos() {
        Integer usuario1Id = 1;
        Integer usuario2Id = 2;
        String nombreLista = "Lista Nueva";

        when(repositorioUsuario.existeRelacion(usuario1Id, usuario2Id)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            listaColaborativaServiceImpl.crearListaColaborativa(usuario1Id, usuario2Id, nombreLista);
        });

        assertEquals("Los usuarios deben seguirse mutuamente para colaborar.", exception.getMessage(),
                "El mensaje de excepción debe ser 'Los usuarios deben seguirse mutuamente para colaborar.'");
    }

    @Test
    public void testCrearListaColaborativaNombreDuplicado() {
        Integer usuario1Id = 1;
        Integer usuario2Id = 2;
        String nombreLista = "Lista Duplicada";

        when(repositorioUsuario.existeRelacion(usuario1Id, usuario2Id)).thenReturn(true);
        when(listaColaborativaRepository.existeListaConNombreParaUsuario(usuario1Id, nombreLista)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            listaColaborativaServiceImpl.crearListaColaborativa(usuario1Id, usuario2Id, nombreLista);
        });

        assertEquals("Ya existe una lista con este nombre para el usuario.", exception.getMessage(),
                "El mensaje de excepción debe ser 'Ya existe una lista con este nombre para el usuario.'");
    }

    @Test
    public void testCrearListaColaborativaErrorAlGuardar() {
        Integer usuario1Id = 1;
        Integer usuario2Id = 2;
        String nombreLista = "Lista Nueva";

        Usuario usuario1 = new Usuario();
        usuario1.setId(usuario1Id);

        Usuario usuario2 = new Usuario();
        usuario2.setId(usuario2Id);

        when(repositorioUsuario.existeRelacion(usuario1Id, usuario2Id)).thenReturn(true);
        when(repositorioUsuario.buscarPorId(usuario1Id)).thenReturn(usuario1);
        when(repositorioUsuario.buscarPorId(usuario2Id)).thenReturn(usuario2);
        when(listaColaborativaRepository.existeListaConNombreParaUsuario(usuario1Id, nombreLista)).thenReturn(false);
        when(listaColaborativaRepository.guardar(any(ListaColaborativa.class))).thenThrow(new RuntimeException("Error al guardar"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            listaColaborativaServiceImpl.crearListaColaborativa(usuario1Id, usuario2Id, nombreLista);
        });

        assertEquals("Error al guardar", exception.getMessage(), "El mensaje de excepción debe ser 'Error al guardar'");
    }

}
