package org.twelve.presentacion.dto;

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

    private String nombrePelicula;
    private String imagenPelicula;

    public ComentarioDTO(Integer id, Integer idMovie, Integer idUsuario, String descripcion, Integer likes, PerfilDTO usuario, Double valoracion, String nombrePelicula, String imagenPelicula) {
        this.id = id;
        this.idMovie = idMovie;
        this.idUsuario = idUsuario;
        this.descripcion = descripcion;
        this.likes = likes;
        this.usuario = usuario;
        this.valoracion = valoracion;
        this.nombrePelicula = nombrePelicula;
        this.imagenPelicula = imagenPelicula;
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

    public String getNombrePelicula() {
        return nombrePelicula;
    }

    public void setNombrePelicula(String nombrePelicula) {
        this.nombrePelicula = nombrePelicula;
    }

    public String getImagenPelicula() {
        return imagenPelicula;
    }

    public void setImagenPelicula(String imagenPelicula) {
        this.imagenPelicula = imagenPelicula;
    }

    public static Comentario convertToEntity(ComentarioDTO comentarioDTO, Movie movie, Usuario usuario) {
        Comentario comentario = new Comentario();
        comentario.setDescripcion(comentarioDTO.getDescripcion());
        comentario.setLikes(comentarioDTO.getLikes());
        comentario.setValoracion(comentarioDTO.getValoracion());
        comentario.setMovie(movie);
        comentario.setUsuario(usuario);
        return comentario;
    }

    public static ComentarioDTO convertToDTO(Comentario comentario) {
        ComentarioDTO comentarioDTO = new ComentarioDTO();
        comentarioDTO.setId(comentario.getId());
        comentarioDTO.setDescripcion(comentario.getDescripcion());
        comentarioDTO.setLikes(comentario.getLikes());
        comentarioDTO.setValoracion(comentario.getValoracion());
        comentarioDTO.setLikes(comentario.getLikes());
        comentarioDTO.setUsuario(new PerfilDTO(comentario.getUsuario().getId(), comentario.getUsuario().getUsername()));
        comentarioDTO.setNombrePelicula(comentario.getMovie().getNombre());
        comentarioDTO.setImagenPelicula(comentario.getMovie().getImagen());
        return comentarioDTO;
    }
}
