package org.twelve.dominio.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.twelve.dominio.*;
import org.twelve.dominio.entities.Comentario;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.Usuario;
import org.twelve.dominio.entities.UsuarioComentario;
import org.twelve.infraestructura.UsuarioComentarioRepositoryImpl;
import org.twelve.presentacion.dto.ComentarioDTO;
import org.twelve.presentacion.dto.PerfilDTO;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("comentarioService")
public class ComentarioServiceImpl implements ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final MovieRepository movieRepository;
    private final RepositorioUsuario usuarioRepository;
    private final UsuarioComentarioRepository usuarioComentarioRepository;

    @Autowired
    public ComentarioServiceImpl(ComentarioRepository comentarioRepository, MovieRepository movieRepository, RepositorioUsuario usuarioRepository, UsuarioComentarioRepository usuarioComentarioRepository) {
        this.comentarioRepository = comentarioRepository;
        this.movieRepository = movieRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioComentarioRepository = usuarioComentarioRepository;
    }

    @Override
    public List<ComentarioDTO> obtenerComentariosPorPelicula(Integer idMovie) {
        List<Comentario> comentarios = comentarioRepository.findByIdMovie(idMovie);

        if (comentarios == null || comentarios.isEmpty()) {
            return Collections.emptyList();
        }

        return comentarios.stream().map(comentario -> {
            Usuario usuario = comentario.getUsuario();
            Movie movie = comentario.getMovie();

            return new ComentarioDTO(
                    comentario.getId(),
                    movie.getId(),
                    usuario.getId(),
                    comentario.getDescripcion(),
                    comentario.getLikes(),
                    new PerfilDTO(usuario.getId(), usuario.getUsername()),
                    comentario.getValoracion(),
                    movie.getNombre(),
                    movie.getImagen()
            );
        }).collect(Collectors.toList());
    }

    @Override
    public List<ComentarioDTO> obtenerUltimosTresComentarios(Integer usuarioId) {
        List<Comentario> comentarios = comentarioRepository.findTop3ByUsuarioId(usuarioId);

        if (comentarios == null || comentarios.isEmpty()) {
            return Collections.emptyList();
        }

        // Convertir a DTO
        return comentarios.stream().map(comentario -> {
            Usuario usuario = comentario.getUsuario();
            Movie movie = comentario.getMovie();

            return new ComentarioDTO(
                    comentario.getId(),
                    movie.getId(),
                    usuario.getId(),
                    comentario.getDescripcion(),
                    comentario.getLikes(),
                    new PerfilDTO(usuario.getId(), usuario.getUsername()),
                    comentario.getValoracion(),
                    movie.getNombre(),
                    movie.getImagen()
            );
        }).collect(Collectors.toList());
    }

    @Override
    public void agregarComentario(ComentarioDTO comentarioDTO) {
        Usuario usuario = usuarioRepository.buscarPorId(comentarioDTO.getIdUsuario());
        Movie movie = movieRepository.findById(comentarioDTO.getIdMovie());
        Comentario comentario = ComentarioDTO.convertToEntity(comentarioDTO, null, null);

        comentario.setUsuario(usuario);
        comentario.setMovie(movie);
        comentarioRepository.save(comentario);

        actualizarValoracionPelicula(movie);
    }

    @Override
    public void actualizarValoracionPelicula(Movie movie) {
        List<Comentario> comentarios = comentarioRepository.findByIdMovie(movie.getId());
        double promedio = comentarios.stream().mapToDouble(Comentario::getValoracion).average().orElse(0); //sino hay comentario el promedio es 0

        movie.setValoracion(promedio);
        movieRepository.guardar(movie);
    }

    public void darMeGustaComentario(Integer idComentario, Integer idUsuario) {
        Comentario comentario = comentarioRepository.findById(idComentario)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));

        Usuario usuario = usuarioRepository.buscarPorId(idUsuario);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        boolean yaDioLike = usuarioComentarioRepository.existsByComentarioAndUsuario(idComentario, idUsuario);

        if (yaDioLike) {
            throw new RuntimeException("Ya has dado like a este comentario.");
        }

        UsuarioComentario comentarioLike = new UsuarioComentario();
        comentarioLike.setComentario(comentario);
        comentarioLike.setUsuario(usuario);
        comentarioLike.setLikeComentario(true);

        usuarioComentarioRepository.save(comentarioLike);

        comentario.setLikes(comentario.getLikes() + 1);

        comentarioRepository.save(comentario);
    }

    public void quitarMeGustaComentario(Integer idComentario, Integer idUsuario) {
        Comentario comentario = comentarioRepository.findById(idComentario)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));

        Usuario usuario = usuarioRepository.buscarPorId(idUsuario);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        boolean yaDioLike = usuarioComentarioRepository.existsByComentarioAndUsuario(idComentario, idUsuario);

        if (!yaDioLike) {
            throw new RuntimeException("No has dado like a este comentario.");
        }

        UsuarioComentario comentarioLike = usuarioComentarioRepository.findByComentarioAndUsuario(idComentario, idUsuario);
        if (comentarioLike == null) {
            throw new RuntimeException("Error interno: no se encontró el registro del like.");
        }

        usuarioComentarioRepository.delete(comentarioLike);

        comentario.setLikes(Math.max(0, comentario.getLikes() - 1));

        comentarioRepository.save(comentario);
    }

    public Set<Integer> obtenerLikesPorUsuario(Integer idUsuario) {
        return new HashSet<>(usuarioComentarioRepository.findComentarioIdsByUsuarioId(idUsuario));
    }

    @Override
    public List<ComentarioDTO> obtener3ComentariosConMasLikes() {
        List<Comentario> comentarios = comentarioRepository.obtener3ComentariosConMasLikes();

        // Convertir a DTO
        return comentarios.stream().map(comentario -> {
            Usuario usuario = comentario.getUsuario();
            Movie movie = comentario.getMovie();

            return new ComentarioDTO(
                    comentario.getId(),
                    movie.getId(),
                    usuario.getId(),
                    comentario.getDescripcion(),
                    comentario.getLikes(),
                    new PerfilDTO(usuario.getId(), usuario.getUsername()),
                    comentario.getValoracion(),
                    movie.getNombre(),
                    movie.getImagen()
            );
        }).collect(Collectors.toList());
    }


}
