package org.twelve.dominio.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode
@Table(name = "UsuarioMovie")
public class UsuarioMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "pelicula_id")
    private Movie pelicula;

    private Boolean esLike = Boolean.FALSE;

    private LocalDate fechaLike;

    private Boolean vistaPorUsuario = Boolean.FALSE;

    private LocalDate fechaVista;

    private Boolean esVerMasTarde = Boolean.FALSE;
}
