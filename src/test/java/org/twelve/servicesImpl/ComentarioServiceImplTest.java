package org.twelve.servicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.twelve.dominio.*;
import org.twelve.dominio.entities.Comentario;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.Usuario;
import org.twelve.dominio.entities.UsuarioComentario;
import org.twelve.dominio.serviceImpl.ComentarioServiceImpl;
import org.twelve.presentacion.dto.ComentarioDTO;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class ComentarioServiceImplTest {

    private ComentarioService comentarioServiceImpl;
    private MovieRepository movieRepository;
    private RepositorioUsuario repositorioUsuario;
    private ComentarioRepository comentarioRepository;
    private UsuarioComentarioRepository usuarioComentarioRepository;

    private Movie movie1;
    private Movie movie2;
    private Movie movie3;
    private Usuario usuario1;
    private Usuario usuario2;
    private Comentario comentario1;
    private Comentario comentario2;
    private Comentario comentario3;

    @BeforeEach
    public void init() {
        movieRepository = mock(MovieRepository.class);
        repositorioUsuario = mock(RepositorioUsuario.class);
        comentarioRepository = mock(ComentarioRepository.class);
        usuarioComentarioRepository = mock(UsuarioComentarioRepository.class);
        this.comentarioServiceImpl = new ComentarioServiceImpl(comentarioRepository, movieRepository, repositorioUsuario, usuarioComentarioRepository);
        this.movie1 = mock(Movie.class);
        this.movie2 = mock(Movie.class);
        this.movie3 = mock(Movie.class);
        this.usuario1 = mock(Usuario.class);
        this.usuario2 = mock(Usuario.class);
        this.comentario1 = mock(Comentario.class);
        this.comentario2 = mock(Comentario.class);
        this.comentario3 = mock(Comentario.class);

    }

    @Test
    public void testSiNoHayComentarioPorPeliculaDeberiaDevolverUnaListaVacia() {
        //preparacion
        when(comentarioRepository.findByIdMovie(1)).thenReturn(Collections.emptyList());

        //ejecucion
        List<ComentarioDTO> listaComentario = comentarioServiceImpl.obtenerComentariosPorPelicula(1);

        //validacion
        assertEquals(0, listaComentario.size());
        verify(comentarioRepository, times(1)).findByIdMovie(1);
    }


    @Test
    @Disabled
    public void testQueUnUsuarioPuedaAgregarUnComentario() {
        //preparacion
        ComentarioDTO comentarioDTO = new ComentarioDTO();
        comentarioDTO.setDescripcion("Comentario de prueba");
        comentarioDTO.setValoracion(8.0);
        comentarioDTO.setIdUsuario(1);
        comentarioDTO.setIdMovie(1);

        when(repositorioUsuario.buscarPorId(1)).thenReturn(usuario1);
        when(movieRepository.findById(1)).thenReturn(movie1);

        //ejecucion
        comentarioServiceImpl.agregarComentario(comentarioDTO);

        //validacion
        verify(comentarioRepository, times(1)).save(any(Comentario.class));
        verify(movieRepository, times(1)).guardar(movie1);
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
    public void testActualizarValoracionPeliculaSinComentariosDeberiaEstablecerValoracionACero() {
        //preparacion
        when(movie1.getId()).thenReturn(1);
        when(comentarioRepository.findByIdMovie(1)).thenReturn(Collections.emptyList());

        //ejecucion
        comentarioServiceImpl.actualizarValoracionPelicula(movie1);

        //validacion
        verify(movie1).setValoracion(0.0);
        verify(movieRepository, times(1)).guardar(movie1);
    }

    @Test
    public void testActualizarValoracionPeliculaDeberiaCalcularPromedioCorrectamente() {
        //preparacion
        when(movie1.getId()).thenReturn(1);
        when(comentarioRepository.findByIdMovie(1)).thenReturn(Arrays.asList(comentario1, comentario2));

        when(comentario1.getValoracion()).thenReturn(7.0);
        when(comentario2.getValoracion()).thenReturn(9.0);

        //ejecucion
        comentarioServiceImpl.actualizarValoracionPelicula(movie1);

        //validacion
        verify(movie1).setValoracion(8.0);
        verify(movieRepository, times(1)).guardar(movie1);
    }


    @Test
    public void testObtenerUltimosTresComentarios() {
        //preparacion
        Integer usuarioId = 1;


        when(comentarioRepository.findTop3ByUsuarioId(usuarioId)).thenReturn(Arrays.asList(comentario1, comentario2, comentario3));

        when(comentario1.getDescripcion()).thenReturn("comentario1");
        when(comentario1.getValoracion()).thenReturn(8.0);
        when(comentario1.getLikes()).thenReturn(5);
        when(comentario1.getUsuario()).thenReturn(usuario1);
        when(comentario1.getMovie()).thenReturn(movie1);

        when(comentario2.getDescripcion()).thenReturn("comentario2");
        when(comentario2.getValoracion()).thenReturn(9.0);
        when(comentario2.getLikes()).thenReturn(3);
        when(comentario2.getUsuario()).thenReturn(usuario1);
        when(comentario2.getMovie()).thenReturn(movie2);

        when(comentario3.getDescripcion()).thenReturn("comentario3");
        when(comentario3.getValoracion()).thenReturn(7.0);
        when(comentario3.getLikes()).thenReturn(4);
        when(comentario3.getUsuario()).thenReturn(usuario1);
        when(comentario3.getMovie()).thenReturn(movie3);

        when(movie1.getId()).thenReturn(1);
        when(movie2.getId()).thenReturn(2);
        when(movie3.getId()).thenReturn(3);
        when(movie1.getNombre()).thenReturn("pelicula1");
        when(movie2.getNombre()).thenReturn("pelicula2");
        when(movie3.getNombre()).thenReturn("pelicula3");
        when(movie1.getImagen()).thenReturn("pelicula1.jpg");
        when(movie2.getImagen()).thenReturn("pelicula2.jpg");
        when(movie3.getImagen()).thenReturn("pelicula3.jpg");

        //ejecucion
        List<ComentarioDTO> listaComentarioDTO = comentarioServiceImpl.obtenerUltimosTresComentarios(usuarioId);

        //validacion
        assertEquals(3, listaComentarioDTO.size());
        assertEquals("comentario1", listaComentarioDTO.get(0).getDescripcion());
        assertEquals("comentario2", listaComentarioDTO.get(1).getDescripcion());
        assertEquals("comentario3", listaComentarioDTO.get(2).getDescripcion());

        assertEquals("pelicula1", listaComentarioDTO.get(0).getNombrePelicula());
        assertEquals("pelicula2", listaComentarioDTO.get(1).getNombrePelicula());
        assertEquals("pelicula3", listaComentarioDTO.get(2).getNombrePelicula());

        assertEquals("pelicula1.jpg", listaComentarioDTO.get(0).getImagenPelicula());
        assertEquals("pelicula2.jpg", listaComentarioDTO.get(1).getImagenPelicula());
        assertEquals("pelicula3.jpg", listaComentarioDTO.get(2).getImagenPelicula());

        verify(comentarioRepository, times(1)).findTop3ByUsuarioId(usuarioId);
    }

    ///////////////////////


    @Test
    public void testObtenerLikesPorUsuario() {
        //preparacion
        Integer idUsuario = 1;
        Set<Integer> comentariosLikes = new HashSet<>(Arrays.asList(1, 2, 3));

        when(usuarioComentarioRepository.findComentarioIdsByUsuarioId(idUsuario)).thenReturn(Arrays.asList(1, 2, 3));

        //ejecucion
        Set<Integer> result = comentarioServiceImpl.obtenerLikesPorUsuario(idUsuario);

        //validacion
        assertEquals(comentariosLikes, result);
        verify(usuarioComentarioRepository, times(1)).findComentarioIdsByUsuarioId(idUsuario);
    }

    @Test
    public void testDarMeGustaComentario() {
        //preparacion
        Integer idComentario = 1;
        Integer idUsuario = 1;


        Comentario comentario1 = new Comentario();
        comentario1.setLikes(0);

        when(comentarioRepository.findById(idComentario)).thenReturn(Optional.of(comentario1));
        when(repositorioUsuario.buscarPorId(idUsuario)).thenReturn(usuario1);
        when(usuarioComentarioRepository.existsByComentarioAndUsuario(idComentario, idUsuario)).thenReturn(false);

        //ejecucion
        comentarioServiceImpl.darMeGustaComentario(idComentario, idUsuario);

        //validacion
        verify(usuarioComentarioRepository, times(1)).save(any(UsuarioComentario.class));
        verify(comentarioRepository, times(1)).save(comentario1);
        assertEquals(1, comentario1.getLikes());
    }


    @Test
    public void testQuitarMeGustaComentario() {
        //preparacion
        Integer idComentario = 1;
        Integer idUsuario = 1;

        Comentario comentario1 = new Comentario();
        comentario1.setLikes(1);

        UsuarioComentario usuarioComentario = new UsuarioComentario();
        usuarioComentario.setComentario(comentario1);
        usuarioComentario.setUsuario(usuario1);
        usuarioComentario.setLikeComentario(true);

        when(comentarioRepository.findById(idComentario)).thenReturn(Optional.of(comentario1));
        when(repositorioUsuario.buscarPorId(idUsuario)).thenReturn(usuario1);
        when(usuarioComentarioRepository.existsByComentarioAndUsuario(idComentario, idUsuario)).thenReturn(true);
        when(usuarioComentarioRepository.findByComentarioAndUsuario(idComentario, idUsuario)).thenReturn(usuarioComentario);

        //ejecucion
        comentarioServiceImpl.quitarMeGustaComentario(idComentario, idUsuario);

        //validacion
        verify(usuarioComentarioRepository, times(1)).delete(usuarioComentario);
        verify(comentarioRepository, times(1)).save(comentario1);
        assertEquals(0, comentario1.getLikes());
    }
}