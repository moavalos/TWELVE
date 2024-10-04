package org.twelve.dominio.entities;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@EqualsAndHashCode
@Table(name = "Comentario")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer id;

    @Column(nullable = false)
    private Integer idMovie;

    @Column(length = 800)
    private String descripcion;

    @Column(nullable = false)
    private Integer idUsuario;

    private Integer likes;

    @Column(length = 5)
    private Double valoracion;
}
