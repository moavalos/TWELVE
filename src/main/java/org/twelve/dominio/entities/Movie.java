package org.twelve.dominio.entities;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(exclude = "categorias")
@ToString(exclude = "categorias")
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

    @ManyToOne
    @JoinColumn(name = "pais")
    private Pais pais;

    private Integer cantVistas;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
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

    private Integer idComentario;

    private Integer likes;

    @Column(length = 5)
    private Double valoracion;

    // Nuevos campos agregados
    private String director;

    private String escritor;

    private String idioma;

    private String tambienConocidaComo;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comentario> comentarios;

    @OneToMany(mappedBy = "pelicula")
    private List<UsuarioMovie> usuariosQueLaVieron;

}
