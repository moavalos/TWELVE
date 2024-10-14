package org.twelve.presentacion.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioDTO {
    private Integer id;
    private Integer idMovie;
    private Long idUsuario;
    private String descripcion;
    private Integer likes = 0;
    private Double valoracion = 0.0;
    private PerfilDTO usuario;


    public ComentarioDTO(Long idUsuario, Integer idMovie, String descripcion, Double valoracion, Integer likes, PerfilDTO usuario) {
        this.idUsuario = idUsuario;
        this.idMovie = idMovie;
        this.descripcion = descripcion;
        this.valoracion = valoracion;
        this.likes = likes;
        this.usuario = usuario;

    }
}
