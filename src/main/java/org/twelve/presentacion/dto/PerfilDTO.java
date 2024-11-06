package org.twelve.presentacion.dto;

import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.Seguidor;

import java.util.List;


import org.twelve.dominio.entities.Usuario;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
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
    private String rutaFotoPerfil;

    private Integer cantidadPeliculasVistas;
    private Integer cantidadPeliculasVistasEsteAno;
    private List<Movie> peliculasFavoritas;

    private List<Seguidor> seguidores;
    private List<Seguidor> seguidos;

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

    public List<Seguidor> getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(List<Seguidor> seguidores) {
        this.seguidores = seguidores;
    }

    public List<Seguidor> getSeguidos() {
        return seguidos;
    }

    public void setSeguidos(List<Seguidor> seguidos) {
        this.seguidos = seguidos;
    }

    public static Usuario convertToEntity(PerfilDTO perfilDTO) {
        Usuario usuario = new Usuario();
        usuario.setId(perfilDTO.getId());
        usuario.setUsername(perfilDTO.getUsername());
        usuario.setPassword(perfilDTO.getPassword());
        usuario.setNombre(perfilDTO.getNombre());
        usuario.setEmail(perfilDTO.getEmail());
        usuario.setPais(perfilDTO.getPais());
        usuario.setDescripcion(perfilDTO.getDescripcion());
        usuario.setRol(perfilDTO.getRol());
        usuario.setActivo(perfilDTO.getActivo());
        usuario.setSeguidos(perfilDTO.getSeguidores());
        usuario.setSeguidores(perfilDTO.getSeguidores());
        return usuario;
    }

    public static PerfilDTO convertToDTO(Usuario usuario) {
        PerfilDTO perfilDTO = new PerfilDTO();
        perfilDTO.setId(usuario.getId());
        perfilDTO.setUsername(usuario.getUsername());
        perfilDTO.setPassword(usuario.getPassword());
        perfilDTO.setNombre(usuario.getNombre());
        perfilDTO.setEmail(usuario.getEmail());
        perfilDTO.setPais(usuario.getPais());
        perfilDTO.setDescripcion(usuario.getDescripcion());
        perfilDTO.setRol(usuario.getRol());
        perfilDTO.setActivo(usuario.getActivo());
        perfilDTO.setSeguidores(usuario.getSeguidores());
        perfilDTO.setSeguidos(usuario.getSeguidos());

        return perfilDTO;
    }


    // Getters y setters
    public String getRutaFotoPerfil() {
        return rutaFotoPerfil;
    }

    public void setRutaFotoPerfil(String rutaFotoPerfil) {
        this.rutaFotoPerfil = rutaFotoPerfil;
    }
}





