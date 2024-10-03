package org.twelve.presentacion.dto;

public class MovieDTO {

    private Integer id;
    private String nombre;
    private String descripcion;
    private String frase;
    private Double duracion;
    private String pais;
    private Integer cantVistas;
    private Integer idCategoria;
    private String anioLanzamiento;
    private String imagen;
    private Integer likes;
    private Double valoracion;
    private String director;
    private String escritor;
    private String idioma;
    private String tambienConocidaComo;

    public MovieDTO() {
    }

    public MovieDTO(Integer id, String nombre, String descripcion, String frase, Double duracion, String pais, Integer cantVistas, Integer idCategoria, String anioLanzamiento, String imagen, Integer likes, Double valoracion, String director, String escritor, String idioma, String tambienConocidaComo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.frase = frase;
        this.duracion = duracion;
        this.pais = pais;
        this.cantVistas = cantVistas;
        this.idCategoria = idCategoria;
        this.anioLanzamiento = anioLanzamiento;
        this.imagen = imagen;
        this.likes = likes;
        this.valoracion = valoracion;
        this.director = director;
        this.escritor = escritor;
        this.idioma = idioma;
        this.tambienConocidaComo = tambienConocidaComo;
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

    public String getAnioLanzamiento() {
        return anioLanzamiento;
    }

    public void setAnioLanzamiento(String anioLanzamiento) {
        this.anioLanzamiento = this.anioLanzamiento;
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

    public Integer getId() {
        return id;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDirector() {
        return director;
    }

    public void setEscritor(String escritor) {
        this.escritor = escritor;
    }

    public String getEscritor() {
        return escritor;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setTambienConocidaComo(String tambienConocidaComo) {
        this.tambienConocidaComo = tambienConocidaComo;
    }

    public String getTambienConocidaComo() {
        return tambienConocidaComo;
    }


}
