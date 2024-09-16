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
    private Integer id;

    private String descripcion;

    //@ManyToOne // definir una relacion de muchos a uno entre dos entidades con JPA
    //private Usuario usuario; // usuario que hizo el comentario
    // comentario esta asociado a un solo usuario pero usuario puede tener muchos comentarios

    private Integer likes;
}
