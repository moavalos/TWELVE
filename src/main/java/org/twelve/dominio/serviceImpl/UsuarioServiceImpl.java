package org.twelve.dominio.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.twelve.dominio.MovieRepository;
import org.twelve.dominio.RepositorioUsuario;
import org.twelve.dominio.UsuarioMovieRepository;
import org.twelve.dominio.UsuarioService;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.Seguidor;
import org.twelve.dominio.entities.Usuario;
import org.twelve.dominio.entities.UsuarioMovie;
import org.twelve.presentacion.dto.MovieDTO;
import org.twelve.presentacion.dto.PerfilDTO;
import org.twelve.presentacion.dto.UsuarioMovieDTO;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.twelve.presentacion.dto.PerfilDTO.convertToDTO;
import static org.twelve.presentacion.dto.PerfilDTO.convertToEntity;

@Service("usuarioService")
public class UsuarioServiceImpl implements UsuarioService {

    private final RepositorioUsuario repositorioUsuario;
    private final UsuarioMovieRepository usuarioMovieRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public UsuarioServiceImpl(RepositorioUsuario repositorioUsuario, UsuarioMovieRepository usuarioMovieRepository, MovieRepository movieRepository) {
        this.repositorioUsuario = repositorioUsuario;
        this.usuarioMovieRepository = usuarioMovieRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public List<PerfilDTO> encontrarTodos() {
        List<Usuario> usuarios = repositorioUsuario.encontrarTodos();
        return usuarios.stream().map(PerfilDTO::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PerfilDTO buscarPorId(Integer id) {
        Usuario usuario = repositorioUsuario.buscarPorId(id);

        if (usuario == null) {
            throw new EntityNotFoundException("Usuario no encontrado");
        }

        int cantidadPeliculasVistas = usuarioMovieRepository.obtenerCantidadPeliculasVistas(id);
        int cantidadPeliculasVistasEsteAno = usuarioMovieRepository.obtenerCantidadPeliculasVistasEsteAno(id);
        List<Movie> peliculasFavoritas = usuarioMovieRepository.obtenerPeliculasFavoritas(id);

        List<Seguidor> seguidores = repositorioUsuario.obtenerSeguidores(id);
        List<Seguidor> seguidos = repositorioUsuario.obtenerSeguidos(id);

        PerfilDTO perfilDTO = convertToDTO(usuario);
        perfilDTO.setCantidadPeliculasVistas(cantidadPeliculasVistas);
        perfilDTO.setCantidadPeliculasVistasEsteAno(cantidadPeliculasVistasEsteAno);
        perfilDTO.setPeliculasFavoritas(peliculasFavoritas);
        perfilDTO.setSeguidores(seguidores);
        perfilDTO.setSeguidos(seguidos);

        return perfilDTO;
    }

    @Override
    public PerfilDTO crear(PerfilDTO perfilDTO) {
        Usuario usuario = convertToEntity(perfilDTO);
        Usuario savedUsuario = repositorioUsuario.guardar(usuario);
        return convertToDTO(savedUsuario);
    }

    @Override
    public void seguirUsuario(Integer usuarioId, Integer seguidoId) {
        Usuario usuario = repositorioUsuario.buscarPorId(usuarioId);
        Usuario seguido = repositorioUsuario.buscarPorId(seguidoId);

        if (usuario == null || seguido == null)
            throw new IllegalArgumentException("Usuario o seguido no encontrado");

        if (!repositorioUsuario.estaSiguiendo(usuario, seguido))
            repositorioUsuario.seguirUsuario(usuario, seguido);
    }

    @Override
    public void dejarDeSeguirUsuario(Integer usuarioId, Integer seguidoId) {
        Usuario usuario = repositorioUsuario.buscarPorId(usuarioId);
        Usuario seguido = repositorioUsuario.buscarPorId(seguidoId);

        if (usuario == null || seguido == null)
            throw new IllegalArgumentException("Usuario o seguido no encontrado");

        if (repositorioUsuario.estaSiguiendo(usuario, seguido))
            repositorioUsuario.dejarDeSeguirUsuario(usuario, seguido);
    }

    @Override
    public Boolean estaSiguiendo(Integer usuarioId, Integer seguidoId) {
        Usuario usuario = repositorioUsuario.buscarPorId(usuarioId);
        Usuario seguido = repositorioUsuario.buscarPorId(seguidoId);
        return repositorioUsuario.estaSiguiendo(usuario, seguido);
    }

    @Override
    public void guardarMeGusta(PerfilDTO usuarioDTO, MovieDTO movieDTO) {
        Usuario usuario = convertToEntity(usuarioDTO);
        Movie movie = MovieDTO.convertToEntity(movieDTO);

        Optional<UsuarioMovie> like = usuarioMovieRepository.buscarMeGustaPorUsuario(usuario, movie);

        if (like.isPresent()) {
            usuarioMovieRepository.borrarMeGusta(like.get());
        } else {
            UsuarioMovie nuevoLike = new UsuarioMovie();
            nuevoLike.setUsuario(usuario);
            nuevoLike.setPelicula(movie);
            nuevoLike.setEsLike(Boolean.TRUE);
            nuevoLike.setFechaLike(LocalDate.now());
            usuarioMovieRepository.guardar(nuevoLike);
        }
        movieRepository.actualizar(movie);
    }

    @Override
    public long obtenerCantidadDeLikes(MovieDTO movieDTO){
        Movie movie = MovieDTO.convertToEntity(movieDTO);
        return this.usuarioMovieRepository.obtenerCantidadDeLikes(movie);
    }

    @Override
    public boolean haDadoLike(PerfilDTO usuarioDTO, MovieDTO movieDTO) {
        Usuario usuario = convertToEntity(usuarioDTO);
        Movie movie = MovieDTO.convertToEntity(movieDTO);
        return usuarioMovieRepository.buscarMeGustaPorUsuario(usuario, movie).isPresent();
    }

    @Override
    public List<Movie> obtenerPeliculasFavoritas(Integer usuarioId){
        return usuarioMovieRepository.obtenerPeliculasFavoritas(usuarioId);
    }


}

