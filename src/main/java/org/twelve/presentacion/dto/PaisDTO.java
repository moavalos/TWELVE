package org.twelve.presentacion.dto;

import org.twelve.dominio.entities.Pais;

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

    public static PaisDTO convertToDTO(Pais pais) {
        return new PaisDTO(
                pais.getId(),
                pais.getNombre()
        );
    }

    public static Pais convertToEntity(PaisDTO paisDTO) {
        Pais pais = new Pais();
        pais.setId(paisDTO.getId());
        pais.setNombre(paisDTO.getNombre());
        return pais;
    }

}
