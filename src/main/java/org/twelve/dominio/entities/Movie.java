package org.twelve.dominio.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@EqualsAndHashCode
@Table(name = "Movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String descripcion;

    private String frase;

    private Double duracion;

    private String pais;

    private Integer cantVistas;

    //private Categoria categorias;

    private String añoLanzamiento;

    private String imagen;

    //private Cast cast;

    //private Director director;

    // una pelicula tiene n comentarios
    //private Comentario comentario;

    private Integer likes;

    private Double valoracion;
}
