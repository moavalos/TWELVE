package org.twelve.dominio.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.twelve.dominio.ComentarioRepository;
import org.twelve.dominio.ComentarioService;
import org.twelve.dominio.MovieRepository;
import org.twelve.dominio.RepositorioUsuario;
import org.twelve.dominio.entities.Comentario;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.Usuario;
import org.twelve.presentacion.dto.ComentarioDTO;
import org.twelve.presentacion.dto.PerfilDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service("comentarioService")
public class ComentarioServiceImpl implements ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final MovieRepository movieRepository;
    private final RepositorioUsuario usuarioRepository;

    @Autowired
    public ComentarioServiceImpl(ComentarioRepository comentarioRepository, MovieRepository movieRepository, RepositorioUsuario usuarioRepository) {
        this.comentarioRepository = comentarioRepository;
        this.movieRepository = movieRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<ComentarioDTO> obtenerComentariosPorPelicula(Integer idMovie) {
        List<Comentario> comentarios = comentarioRepository.findByIdMovie(idMovie);

        //lo vuelve a convertir en dto
        return comentarios.stream().map(comentario -> {
            Usuario usuario = comentario.getUsuario();
            Movie movie = comentario.getMovie();

            return new ComentarioDTO(usuario.getId(), movie.getId(), comentario.getDescripcion(), comentario.getValoracion(), comentario.getLikes(), new PerfilDTO(usuario.getId(), usuario.getUsername()) // para q diga el nombre de usuario
            );
        }).collect(Collectors.toList());
    }

    @Override
    public void agregarComentario(ComentarioDTO comentarioDTO) {
        Usuario usuario = usuarioRepository.buscarPorId(comentarioDTO.getIdUsuario());
        Movie movie = movieRepository.findById(comentarioDTO.getIdMovie());
        Comentario comentario = ComentarioDTO.convertToEntity(comentarioDTO);

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

}
