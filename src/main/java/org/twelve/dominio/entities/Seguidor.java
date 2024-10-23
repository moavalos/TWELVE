package org.twelve.dominio.entities;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "Seguidor")
public class Seguidor { // tabla relacion usuario 1:1

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "seguido_id", nullable = false)
    private Usuario seguido;
}
