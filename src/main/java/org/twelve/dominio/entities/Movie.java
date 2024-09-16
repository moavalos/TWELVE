package org.twelve.dominio.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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

    private Integer idCategoria;

    private String añoLanzamiento;

    private String imagen;

    //private Cast cast;

    //private Director director;

    //@OneToMany(mappedBy = "movie") // una pelicula puede tener n comentarios
    //private List<Comentario> comentarios;

    private Integer likes;

    private Double valoracion;
}
