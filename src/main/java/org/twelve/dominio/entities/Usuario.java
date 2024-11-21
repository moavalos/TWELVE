package org.twelve.dominio.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(name = "foto_de_perfil")
    private String fotoDePerfil;

    @ManyToOne
    @JoinColumn(name = "pais_id")
    private Pais pais;

    private String descripcion;

    private String rol;

    private Boolean activo = false;

    @OneToMany(mappedBy = "usuario")
    private List<UsuarioMovie> peliculasVistas;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Seguidor> seguidores;

    @OneToMany(mappedBy = "seguido", cascade = CascadeType.ALL)
    private List<Seguidor> seguidos;


}
