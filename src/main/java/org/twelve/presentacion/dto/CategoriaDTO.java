package org.twelve.presentacion.dto;

import org.twelve.dominio.entities.Categoria;

public class CategoriaDTO {

    private Integer id;
    private String nombre;

    public CategoriaDTO(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public CategoriaDTO() {
    }

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

    public static CategoriaDTO convertToDTO(Categoria categoria) {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        return dto;
    }

    public static Categoria convertToEntity(CategoriaDTO categoriaDTO) {
        Categoria entidad = new Categoria();
        entidad.setId(categoriaDTO.getId());
        entidad.setNombre(categoriaDTO.getNombre());
        return entidad;
    }
}
