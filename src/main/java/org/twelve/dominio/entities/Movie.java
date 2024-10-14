package org.twelve.dominio.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode
@Table(name = "Movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer id;

    private String nombre;

    private String descripcion;

    private String frase;

    private Double duracion;

    private String pais;

    private Integer cantVistas;


    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "movie_categoria",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categorias = new HashSet<>();

    private String añoLanzamiento;

    private String imagen;

    //private Cast cast;

    private Integer idComentario;

    // favorita
    private Integer likes;

    @Column(length = 5)
    private Double valoracion;

    private String director;

    private String escritor;

    private String idioma;

    private String tambienConocidaComo;

    @OneToMany(mappedBy = "pelicula")
    private List<UsuarioMovie> usuariosQueLaVieron;

}
