package org.twelve.dominio.entities;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode
@ToString(exclude = "movies")
@Table(name = "Categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer id;

    private String nombre;

    @ManyToMany(mappedBy = "categorias")
    private Set<Movie> movies = new HashSet<>();

    public Categoria(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}
