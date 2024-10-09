package org.twelve.dominio.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//@Data
@AllArgsConstructor
@NoArgsConstructor
//@ToString(exclude = "categorias")
@Entity
//@EqualsAndHashCode
@Table(name = "Movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer id;

    private String nombre;

    private String descripcion;

    private String frase;

    private Double duracion;

    private String pais;

    private Integer cantVistas;
//
//    @Column(nullable = false)
//    private Integer idCategoria;
@ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "movie_categoria",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categorias = new HashSet<>();

    private String añoLanzamiento;

    private String imagen;

    //private Cast cast;

    //private Director director;

    private Integer idComentario;

    private Integer likes;

    @Column(length = 5)
    private Double valoracion;

    private String director;

    private String escritor;

    private String idioma;

    private String tambienConocidaComo;

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

    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public Set<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(Set<Categoria> categorias) {
        this.categorias = categorias;
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

    public Integer getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(Integer idComentario) {
        this.idComentario = idComentario;
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

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getEscritor() {
        return escritor;
    }

    public void setEscritor(String escritor) {
        this.escritor = escritor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getTambienConocidaComo() {
        return tambienConocidaComo;
    }

    public void setTambienConocidaComo(String tambienConocidaComo) {
        this.tambienConocidaComo = tambienConocidaComo;
    }
}
