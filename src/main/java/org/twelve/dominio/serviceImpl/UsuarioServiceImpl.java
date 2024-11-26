package org.twelve.dominio.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.twelve.dominio.MovieRepository;
import org.twelve.dominio.RepositorioUsuario;
import org.twelve.dominio.UsuarioMovieRepository;
import org.twelve.dominio.UsuarioService;
import org.twelve.dominio.entities.*;
import org.twelve.presentacion.dto.MovieDTO;
import org.twelve.presentacion.dto.PaisDTO;
import org.twelve.presentacion.dto.PerfilDTO;
import org.twelve.presentacion.dto.UsuarioMovieDTO;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

        if (usuarioDTO == null || movieDTO == null) {
            throw new IllegalArgumentException("Usuario o película no puede ser nulo");
        }

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
            nuevoLike.setFechaVista(LocalDate.now());
            nuevoLike.setVistaPorUsuario(Boolean.TRUE);
            usuarioMovieRepository.guardar(nuevoLike);
        }
        movieRepository.actualizar(movie);
    }

    @Override
    public long obtenerCantidadDeLikes(MovieDTO movieDTO) {
        if (movieDTO == null)
            throw new IllegalArgumentException("MovieDTO no puede ser nulo");

        if (movieDTO.getId() < 0)
            throw new IllegalArgumentException("ID de película no puede ser negativo");

        Movie movie = MovieDTO.convertToEntity(movieDTO);
        return this.usuarioMovieRepository.obtenerCantidadDeLikes(movie);
    }

    @Override
    public boolean haDadoLike(PerfilDTO usuarioDTO, MovieDTO movieDTO) {
        if (usuarioDTO == null || movieDTO == null)
            throw new IllegalArgumentException("Usuario o película no pueden ser nulos");

        if (usuarioDTO.getId() < 0 || movieDTO.getId() < 0)
            throw new IllegalArgumentException("ID de usuario o película no pueden ser negativos");

        Usuario usuario = convertToEntity(usuarioDTO);
        Movie movie = MovieDTO.convertToEntity(movieDTO);
        return usuarioMovieRepository.buscarMeGustaPorUsuario(usuario, movie).isPresent();
    }

    @Override
    public List<Movie> obtenerPeliculasFavoritas(Integer usuarioId) {
        if (usuarioId == null)
            throw new IllegalArgumentException("El ID de usuario no puede ser nulo");

        if (usuarioId < 0)
            throw new IllegalArgumentException("El ID de usuario no puede ser negativo");

        return usuarioMovieRepository.obtenerPeliculasFavoritas(usuarioId);
    }

    @Override
    public void actualizarPerfil(Integer userId, String username, String descripcion, String nombre, PaisDTO pais, MultipartFile fotoPerfil) {
        Usuario usuario = repositorioUsuario.buscarPorId(userId);

        usuario.setUsername(username);
        usuario.setDescripcion(descripcion);
        usuario.setNombre(nombre);
        usuario.setPais(PaisDTO.convertToEntity(pais));

        if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
            String nombreArchivo = guardarFoto(fotoPerfil);
            usuario.setFotoDePerfil(nombreArchivo);
        }

        repositorioUsuario.guardar(usuario);
    }

    @Override
    public String guardarFoto(MultipartFile fotoPerfil) {
        String tipoArchivo = fotoPerfil.getContentType();
        if (!tipoArchivo.startsWith("images/")) {
            throw new RuntimeException("El archivo no es una imagen válida");
        }

        String nombreArchivo = UUID.randomUUID() + "_" + fotoPerfil.getOriginalFilename();
        Path rutaArchivo = Paths.get("uploads", "user", nombreArchivo);

        try {
            Files.copy(fotoPerfil.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la foto de perfil", e);
        }

        return nombreArchivo;
    }

    @Override
    public List<UsuarioMovieDTO> obtenerHistorialDePeliculasVistas(Integer usuarioId) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("El ID de usuario no puede ser nulo");
        }

        Usuario usuario = repositorioUsuario.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new EntityNotFoundException("Usuario no encontrado");
        }

        List<Object[]> results = usuarioMovieRepository.buscarPeliculasDondeElUsuarioTuvoInteraccion(usuarioId);
        List<UsuarioMovieDTO> historialDTO = new ArrayList<>();

        for (Object[] row : results) {
            UsuarioMovie usuarioMovie = (UsuarioMovie) row[0];
            Double valoracion = (Double) row[1];

            UsuarioMovieDTO dto = new UsuarioMovieDTO();
            dto.setId(usuarioMovie.getId());
            dto.setUsuario(usuarioMovie.getUsuario());
            dto.setPelicula(usuarioMovie.getPelicula());
            dto.setEsLike(usuarioMovie.getEsLike());
            dto.setFechaVista(usuarioMovie.getFechaVista());
            dto.setFechaLike(usuarioMovie.getFechaLike());
            dto.setValoracion(valoracion);
            dto.setVistaPorUsuario(usuarioMovie.getVistaPorUsuario());

            historialDTO.add(dto);
        }

        return historialDTO;
    }

    @Override
    public boolean estaEnListaVerMasTarde(PerfilDTO usuarioDTO, MovieDTO movieDTO) {
        if (usuarioDTO == null || movieDTO == null) {
            throw new IllegalArgumentException("Usuario o película no pueden ser nulos");
        }

        Usuario usuario = convertToEntity(usuarioDTO);
        Movie movie = MovieDTO.convertToEntity(movieDTO);

        return usuarioMovieRepository.buscarVerMasTardePorUsuario(usuario, movie).isPresent();
    }

    @Override
    public void agregarEnVerMasTarde(PerfilDTO usuarioDTO, MovieDTO movieDTO) {
        if (usuarioDTO == null || movieDTO == null) {
            throw new IllegalArgumentException("Usuario o película no pueden ser nulos");
        }

        Usuario usuario = convertToEntity(usuarioDTO);
        Movie movie = MovieDTO.convertToEntity(movieDTO);

        Optional<UsuarioMovie> verMasTarde = usuarioMovieRepository.buscarVerMasTardePorUsuario(usuario, movie);

        if (verMasTarde.isPresent()) {
            usuarioMovieRepository.borrarVerMasTarde(verMasTarde.get());
        } else {
            UsuarioMovie usuarioMovie = new UsuarioMovie();
            usuarioMovie.setUsuario(usuario);
            usuarioMovie.setPelicula(movie);
            usuarioMovie.setEsVerMasTarde(Boolean.TRUE);
            usuarioMovieRepository.guardar(usuarioMovie);
        }
    }

    @Override
    public List<UsuarioMovieDTO> obtenerListaVerMasTarde(Integer usuarioId) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("El ID de usuario no puede ser nulo");
        }

        List<UsuarioMovie> verMasTardeMovies = usuarioMovieRepository.obtenerPeliculasVerMasTarde(usuarioId);

        return verMasTardeMovies.stream()
                .map(UsuarioMovieDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean sonAmigos(Integer usuario1Id, Integer usuario2Id) {
        return repositorioUsuario.existeRelacion(usuario1Id, usuario2Id)
                && repositorioUsuario.existeRelacion(usuario2Id, usuario1Id);
    }

    @Override
    public List<PerfilDTO> obtenerAmigos(Integer usuarioId) {
        List<Usuario> seguidos = repositorioUsuario.obtenerUsuariosSeguidos(usuarioId);

        return seguidos.stream()
                .filter(seguido -> repositorioUsuario.existeRelacion(seguido.getId(), usuarioId))
                .map(usuario -> new PerfilDTO(usuario.getId(), usuario.getUsername()))
                .collect(Collectors.toList());
    }

    @Override
    public List<PerfilDTO> buscarPorUsername(String username) {
        List<Usuario> users = repositorioUsuario.buscarPorUsername(username);
        return users.stream()
                .map(PerfilDTO::convertToDTO)
                .collect(Collectors.toList());
    }


}

