package org.twelve.servicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.twelve.dominio.ComentarioRepository;
import org.twelve.dominio.ComentarioService;
import org.twelve.dominio.MovieRepository;
import org.twelve.dominio.RepositorioUsuario;
import org.twelve.dominio.entities.Comentario;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.Usuario;
import org.twelve.dominio.serviceImpl.ComentarioServiceImpl;
import org.twelve.presentacion.dto.ComentarioDTO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;


public class ComentarioServiceImplTest {

    private ComentarioService comentarioServiceImpl;
    private MovieRepository movieRepository;
    private RepositorioUsuario repositorioUsuario;
    private ComentarioRepository comentarioRepository;

    private Movie movie1;
    private Movie movie2;
    private Usuario usuario1;
    private Usuario usuario2;
    private Comentario comentario1;
    private Comentario comentario2;

    @BeforeEach
    public void init() {
        movieRepository = mock(MovieRepository.class);
        repositorioUsuario = mock(RepositorioUsuario.class);
        comentarioRepository = mock(ComentarioRepository.class);
        this.comentarioServiceImpl = new ComentarioServiceImpl(comentarioRepository, movieRepository, repositorioUsuario);
        this.movie1 = mock(Movie.class);
        this.movie2 = mock(Movie.class);
        this.usuario1 = mock(Usuario.class);
        this.usuario2 = mock(Usuario.class);
        this.comentario1 = mock(Comentario.class);
        this.comentario2 = mock(Comentario.class);


    }

    @Test
    public void testSiNoHayComentarioPorPeliculaDeberiaDevolverUnaListaVacia() {
        //preparacion
        when(comentarioRepository.findByIdMovie(1)).thenReturn(Collections.emptyList());

        //ejecucion
        List<ComentarioDTO> listaComentario = comentarioServiceImpl.obtenerComentariosPorPelicula(1);

        //validacion
        assertEquals(0, listaComentario.size(), "La lista debería estar vacía cuando no hay comentarios.");
        verify(comentarioRepository, times(1)).findByIdMovie(1);
    }


    @Test
    public void testHacerComentario() {
        //preparacion
        //validacion
        //ejecucion
    }


    @Test
    public void testSiUnUsuarioHizoUnComentarioEnUnaPeliculaDeberiaTraerElComentario() {

        //preparacion
        when(movie1.getId()).thenReturn(1);

        when(usuario1.getId()).thenReturn(1);
        when(usuario1.getUsername()).thenReturn("userTest");

        when(comentario1.getDescripcion()).thenReturn("muy buena");
        when(comentario1.getValoracion()).thenReturn(9.0);
        when(comentario1.getUsuario()).thenReturn(usuario1);
        when(comentario1.getMovie()).thenReturn(movie1);

        when(comentarioRepository.findByIdMovie(1)).thenReturn(Collections.singletonList(comentario1));
        //ejecucion
        List<ComentarioDTO> listaComentario = comentarioServiceImpl.obtenerComentariosPorPelicula(1);

        //validacion

        assertEquals(1, listaComentario.size());
        assertEquals("muy buena", listaComentario.get(0).getDescripcion());
        assertEquals(9.0, listaComentario.get(0).getValoracion());
        assertEquals("userTest", listaComentario.get(0).getUsuario().getUsername());

        verify(comentarioRepository, times(1)).findByIdMovie(1);
    }

    @Test
    public void testQueSiLaMismaPeliculaTieneDosComentariosPorDosUsuariosDiferentesTeTraeLosDosComentarios() {

        //preparacion
        when(movie1.getId()).thenReturn(1);

        when(usuario1.getId()).thenReturn(1);
        when(usuario1.getUsername()).thenReturn("userTest1");
        when(usuario2.getId()).thenReturn(2);
        when(usuario2.getUsername()).thenReturn("userTest2");

        when(comentario1.getDescripcion()).thenReturn("comentario1");
        when(comentario1.getValoracion()).thenReturn(7.0);
        when(comentario1.getUsuario()).thenReturn(usuario1);
        when(comentario1.getMovie()).thenReturn(movie1);

        when(comentario2.getDescripcion()).thenReturn("comentario2");
        when(comentario2.getValoracion()).thenReturn(6.0);
        when(comentario2.getUsuario()).thenReturn(usuario2);
        when(comentario2.getMovie()).thenReturn(movie1);

        List<Comentario> comentarios = Arrays.asList(comentario1, comentario2);
        when(comentarioRepository.findByIdMovie(1)).thenReturn(comentarios);

        //ejecucion
        List<ComentarioDTO> listaComentario = comentarioServiceImpl.obtenerComentariosPorPelicula(1);

        //validacion
        assertEquals(2, listaComentario.size());

        assertEquals("comentario1", listaComentario.get(0).getDescripcion());
        assertEquals(7.0, listaComentario.get(0).getValoracion());
        assertEquals("userTest1", listaComentario.get(0).getUsuario().getUsername());

        assertEquals("comentario2", listaComentario.get(1).getDescripcion());
        assertEquals(6.0, listaComentario.get(1).getValoracion());
        assertEquals("userTest2", listaComentario.get(1).getUsuario().getUsername());

        verify(comentarioRepository, times(1)).findByIdMovie(1);


    }

    @Test
    public void testQueAlHacerDosComentariosEnLaMismaPeliculaElPuntajeSePromedie() {

    }

    @Test
    public void testQueUnMismoUsuarioTengaComentariosEnDosPeliculasDiferentes() {


    }

}