package org.twelve.presentacion.dto;

public class CategoriaDTO {


    public CategoriaDTO(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public CategoriaDTO() {
    }

    private Integer id;

    private String nombre;


    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
