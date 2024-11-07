package org.twelve.presentacion.dto;

import org.twelve.dominio.MovieRepository;
import org.twelve.dominio.RepositorioUsuario;
import org.twelve.dominio.entities.Comentario;

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

    public ComentarioDTO(Integer idUsuario, Integer idMovie, String descripcion, Double valoracion, Integer likes,
                         PerfilDTO usuario, String nombrePelicula, String imagenPelicula) {
        this.idUsuario = idUsuario;
        this.idMovie = idMovie;
        this.descripcion = descripcion;
        this.valoracion = valoracion;
        this.likes = likes;
        this.usuario = usuario;
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

    public static Comentario convertToEntity(ComentarioDTO comentarioDTO) {
        Comentario comentario = new Comentario();
        comentario.setDescripcion(comentarioDTO.getDescripcion());
        comentario.setValoracion(comentarioDTO.getValoracion());

        return comentario;
    }

    public static ComentarioDTO convertToDTO(Comentario comentario) {
        ComentarioDTO comentarioDTO = new ComentarioDTO();
        comentarioDTO.setDescripcion(comentario.getDescripcion());
        comentarioDTO.setValoracion(comentario.getValoracion());
        return comentarioDTO;
    }
}
