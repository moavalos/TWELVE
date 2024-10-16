package org.twelve.presentacion.dto;

import org.twelve.dominio.entities.Movie;

import java.util.List;

public class PerfilDTO {
    private Integer id;
    private String nombre;
    private String email;
    private String descripcion;
    private String password;
    private String username;
    private String pais;
    private String rol;
    private Boolean activo;

    private Integer cantidadPeliculasVistas;
    private Integer cantidadPeliculasVistasEsteAno;
    private List<Movie> peliculasFavoritas;

    public PerfilDTO() {
    }

    public PerfilDTO(Integer id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantidadPeliculasVistas() {
        return cantidadPeliculasVistas;
    }

    public void setCantidadPeliculasVistas(int cantidadPeliculasVistas) {
        this.cantidadPeliculasVistas = cantidadPeliculasVistas;
    }

    public Integer getCantidadPeliculasVistasEsteAno() {
        return cantidadPeliculasVistasEsteAno;
    }

    public void setCantidadPeliculasVistasEsteAno(Integer cantidadPeliculasVistasEsteAno) {
        this.cantidadPeliculasVistasEsteAno = cantidadPeliculasVistasEsteAno;
    }

    public List<Movie> getPeliculasFavoritas() {
        return peliculasFavoritas;
    }

    public void setPeliculasFavoritas(List<Movie> peliculasFavoritas) {
        this.peliculasFavoritas = peliculasFavoritas;
    }


}
