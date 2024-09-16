package org.twelve.presentacion.dto;

public class MovieDTO {

    private String nombre;
    private String descripcion;
    private String frase;
    private Double duracion;
    private String pais;
    private Integer cantVistas;
    private Integer idCategoria;
    private String añoLanzamiento;
    private String imagen;
    private Integer likes;
    private Double valoracion;

    public MovieDTO() {
    }

    public MovieDTO(String nombre, String descripcion, String frase, Double duracion, String pais, Integer cantVistas, Integer idCategoria, String añoLanzamiento, String imagen, Integer likes, Double valoracion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.frase = frase;
        this.duracion = duracion;
        this.pais = pais;
        this.cantVistas = cantVistas;
        this.idCategoria = idCategoria;
        this.añoLanzamiento = añoLanzamiento;
        this.imagen = imagen;
        this.likes = likes;
        this.valoracion = valoracion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    public Double getDuracion() {
        return duracion;
    }

    public void setDuracion(Double duracion) {
        this.duracion = duracion;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Integer getCantVistas() {
        return cantVistas;
    }

    public void setCantVistas(Integer cantVistas) {
        this.cantVistas = cantVistas;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getAñoLanzamiento() {
        return añoLanzamiento;
    }

    public void setAñoLanzamiento(String añoLanzamiento) {
        this.añoLanzamiento = añoLanzamiento;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
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
}
