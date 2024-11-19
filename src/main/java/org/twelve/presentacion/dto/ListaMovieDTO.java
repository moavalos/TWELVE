package org.twelve.presentacion.dto;

import org.twelve.dominio.entities.ListaColaborativa;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.Usuario;

import java.time.LocalDate;

public class ListaMovieDTO {

    private Integer id;

    private ListaColaborativa lista;

    private Movie pelicula;

    private Usuario usuarioAgregador;

    private LocalDate fechaAgregada;

    public ListaMovieDTO() {}

    public ListaMovieDTO(Integer id, ListaColaborativa lista, Movie pelicula, Usuario usuarioAgregador, LocalDate fechaAgregada) {
        this.id = id;
        this.lista = lista;
        this.pelicula = pelicula;
        this.usuarioAgregador = usuarioAgregador;
        this.fechaAgregada = fechaAgregada;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ListaColaborativa getLista() {
        return lista;
    }

    public void setLista(ListaColaborativa lista) {
        this.lista = lista;
    }

    public Movie getPelicula() {
        return pelicula;
    }

    public void setPelicula(Movie pelicula) {
        this.pelicula = pelicula;
    }

    public Usuario getUsuarioAgregador() {
        return usuarioAgregador;
    }

    public void setUsuarioAgregador(Usuario usuarioAgregador) {
        this.usuarioAgregador = usuarioAgregador;
    }

    public LocalDate getFechaAgregada() {
        return fechaAgregada;
    }

    public void setFechaAgregada(LocalDate fechaAgregada) {
        this.fechaAgregada = fechaAgregada;
    }
}
