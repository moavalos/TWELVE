package org.twelve.dominio.entities;

import lombok.*;

import javax.persistence.*;

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
    private Integer id;

    private String nombre;

    private String descripcion;

    private String frase;

    private Double duracion;

    private String pais;

    private Integer cantVistas;

    private Integer idCategoria;

    private String añoLanzamiento;

    private String imagen;

    private Integer idComentario;

    private Integer likes;

    @Column(length = 5)
    private Double valoracion;

    // Nuevos campos agregados
    private String director;

    private String escritor;

    private String idioma;

    private String tambienConocidaComo;
}
