package org.twelve.presentacion.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PerfilDTO {
    // Getters y Setters
    private Long id;
    private String nombre;
    private String email;
    private String descripcion;
    private String username;


    public PerfilDTO(Long id, String username) {
        this.id = id;
        this.username = username;

    }

    public PerfilDTO() { //constructor vacio

    }


}
