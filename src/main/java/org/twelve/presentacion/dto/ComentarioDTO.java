package org.twelve.presentacion.dto;

import org.twelve.dominio.MovieRepository;
import org.twelve.dominio.RepositorioUsuario;
import org.twelve.dominio.entities.Comentario;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.Usuario;

public class ComentarioDTO {

    private Integer id;
    private Integer idMovie;
    private Integer idUsuario;
    private String descripcion;
    private Integer likes = 0;
    private Double valoracion = 0.0;
    private PerfilDTO usuario;

    private static RepositorioUsuario usuarioRepository;
    private static MovieRepository movieRepository;

    public ComentarioDTO(Integer idUsuario, Integer idMovie, String descripcion, Double valoracion, Integer likes, PerfilDTO usuario) {
        this.idUsuario = idUsuario;
        this.idMovie = idMovie;
        this.descripcion = descripcion;
        this.valoracion = valoracion;
        this.likes = likes;
        this.usuario = usuario;
    }

    public ComentarioDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(Integer idMovie) {
        this.idMovie = idMovie;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Double getValoracion() {
        return valoracion;
    }

    public void setValoracion(Double valoracion) {
        this.valoracion = valoracion;
    }

    public PerfilDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(PerfilDTO usuario) {
        this.usuario = usuario;
    }

    public static Comentario convertToEntity(ComentarioDTO comentarioDTO) {
        Comentario comentario = new Comentario();
        comentario.setDescripcion(comentarioDTO.getDescripcion());
        comentario.setValoracion(comentarioDTO.getValoracion());

        Usuario usuario = usuarioRepository.buscarPorId(comentarioDTO.getIdUsuario());
        comentario.setUsuario(usuario);

        Movie movie = movieRepository.findById(comentarioDTO.getIdMovie());
        comentario.setMovie(movie);
        return comentario;
    }

    public static ComentarioDTO convertToDTO(Comentario comentario) {
        ComentarioDTO comentarioDTO = new ComentarioDTO();
        comentarioDTO.setDescripcion(comentario.getDescripcion());
        comentarioDTO.setValoracion(comentario.getValoracion());
        return comentarioDTO;
    }
}
