package org.twelve.dominio.entities;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "movies")
@Entity
@EqualsAndHashCode
@Table(name = "Categoria")


public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer id;

    private String nombre;

    @ManyToMany(mappedBy = "categorias", fetch = FetchType.LAZY)
    private Set<Movie> movies = new HashSet<>();
}
