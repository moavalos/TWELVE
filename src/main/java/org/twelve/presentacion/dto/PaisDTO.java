package org.twelve.presentacion.dto;

public class PaisDTO {

    private Integer id;
    private String nombre;

    public PaisDTO(Integer id, String nombre) {
        this.nombre = nombre;
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getId() {
        return id;
    }
}
