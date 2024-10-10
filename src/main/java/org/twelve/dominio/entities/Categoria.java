package org.twelve.dominio.entities;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//@Data
@AllArgsConstructor
@NoArgsConstructor
//@ToString(exclude = "movies")
@Entity
//@EqualsAndHashCode
@Table(name = "Categoria")


public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer id;

    private String nombre;

    @ManyToMany(mappedBy = "categorias", fetch = FetchType.LAZY)
    private Set<Movie> movies = new HashSet<>();

    public Categoria(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
