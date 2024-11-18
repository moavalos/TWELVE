package org.twelve.presentacion.dto;

import org.twelve.dominio.entities.ListaColaborativa;
import org.twelve.dominio.entities.ListaMovie;
import org.twelve.dominio.entities.Usuario;

import java.time.LocalDate;
import java.util.List;

public class ListaColaborativaDTO {

    private Integer id;

    private String nombre;

    private Usuario creador; // usuario que inició sesion

    private Usuario colaborador;

    private List<ListaMovie> peliculas;

    private LocalDate fechaCreacion;

    public ListaColaborativaDTO() {}

    public ListaColaborativaDTO(Integer id, String nombre, Usuario creador, Usuario colaborador, List<ListaMovie> peliculas, LocalDate fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.creador = creador;
        this.colaborador = colaborador;
        this.peliculas = peliculas;
        this.fechaCreacion = fechaCreacion;
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

    public Usuario getCreador() {
        return creador;
    }

    public void setCreador(Usuario creador) {
        this.creador = creador;
    }

    public Usuario getColaborador() {
        return colaborador;
    }

    public void setColaborador(Usuario colaborador) {
        this.colaborador = colaborador;
    }

    public List<ListaMovie> getPeliculas() {
        return peliculas;
    }

    public void setPeliculas(List<ListaMovie> peliculas) {
        this.peliculas = peliculas;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public static ListaColaborativa convertToEntity(ListaColaborativaDTO dto) {
        ListaColaborativa listaColaborativa = new ListaColaborativa();
        listaColaborativa.setId(dto.getId());
        listaColaborativa.setNombre(dto.getNombre());
        listaColaborativa.setCreador(dto.getCreador());
        listaColaborativa.setColaborador(dto.getColaborador());
        listaColaborativa.setPeliculas(dto.getPeliculas());
        listaColaborativa.setFechaCreacion(dto.getFechaCreacion());
        return listaColaborativa;
    }

    public static ListaColaborativaDTO convertToDTO(ListaColaborativa entity) {
        ListaColaborativaDTO dto = new ListaColaborativaDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setCreador(entity.getCreador());
        dto.setColaborador(entity.getColaborador());
        dto.setPeliculas(entity.getPeliculas());
        dto.setFechaCreacion(entity.getFechaCreacion());
        return dto;
    }

}
