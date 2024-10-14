package org.twelve.dominio.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@EqualsAndHashCode
@Table(name = "Usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer id;

    private String email;

    private String password;

    private String nombre;

    private String username;

    private String pais;

    private String descripcion;

    private String rol;

    private Boolean activo = false;

    @OneToMany(mappedBy = "usuario")
    private List<UsuarioMovie> peliculasVistas;

}
