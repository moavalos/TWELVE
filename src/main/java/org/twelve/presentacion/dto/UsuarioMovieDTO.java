package org.twelve.presentacion.dto;

import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.Usuario;
import org.twelve.dominio.entities.UsuarioMovie;

import java.time.LocalDate;

public class UsuarioMovieDTO {

    private Integer id;

    private Usuario usuario;

    private Movie pelicula;

    private LocalDate fechaVista;

    private Boolean esLike = Boolean.FALSE;

    private LocalDate fechaLike;

    public UsuarioMovieDTO() {
    }

    public UsuarioMovieDTO(Integer id, Usuario usuario, Movie pelicula, LocalDate fechaVista, Boolean esLike, LocalDate fechaLike) {
        this.id = id;
        this.usuario = usuario;
        this.pelicula = pelicula;
        this.fechaVista = fechaVista;
        this.esLike = esLike;
        this.fechaLike = fechaLike;
    }

    public Boolean getEsLike() {
        return esLike;
    }

    public void setEsLike(Boolean esLike) {
        this.esLike = esLike;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setPelicula(Movie pelicula) {
        this.pelicula = pelicula;
    }

    public void setFechaVista(LocalDate fechaVista) {
        this.fechaVista = fechaVista;
    }

    public void setFechaLike(LocalDate fechaLike) {
        this.fechaLike = fechaLike;
    }

    public Integer getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Movie getPelicula() {
        return pelicula;
    }

    public LocalDate getFechaVista() {
        return fechaVista;
    }

    public LocalDate getFechaLike() {
        return fechaLike;
    }

    public static UsuarioMovie dtoToEntity(UsuarioMovieDTO usuarioMovieDto) {
        UsuarioMovie usuarioMovieEntity = new UsuarioMovie();
        usuarioMovieEntity.setId(usuarioMovieDto.getId());
        usuarioMovieEntity.setUsuario(usuarioMovieDto.getUsuario());
        usuarioMovieEntity.setPelicula(usuarioMovieDto.getPelicula());
        usuarioMovieEntity.setFechaVista(usuarioMovieDto.getFechaVista());
        usuarioMovieEntity.setFechaLike(usuarioMovieDto.getFechaLike());
        usuarioMovieEntity.setEsLike(usuarioMovieDto.getEsLike());
        return usuarioMovieEntity;
    }

    public static UsuarioMovieDTO convertToDTO(UsuarioMovie usuarioMovie) {
        UsuarioMovieDTO usuarioMovieDTO = new UsuarioMovieDTO();
        usuarioMovieDTO.setId(usuarioMovie.getId());
        usuarioMovieDTO.setUsuario(usuarioMovie.getUsuario());
        usuarioMovieDTO.setPelicula(usuarioMovie.getPelicula());
        usuarioMovieDTO.setFechaVista(usuarioMovie.getFechaVista());
        usuarioMovieDTO.setFechaLike(usuarioMovie.getFechaLike());
        usuarioMovieDTO.setEsLike(usuarioMovie.getEsLike());
        return usuarioMovieDTO;
    }
}
