package org.twelve.infraestructura;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.twelve.dominio.RepositorioUsuario;
import org.twelve.dominio.entities.Usuario;



import org.twelve.dominio.entities.Usuario;


import java.util.ArrayList;
import java.util.List;

@Repository
public class RepositorioUsuarioImpl implements RepositorioUsuario {
    private final List<Usuario> usuarios = new ArrayList<>();
    private Long idCounter = 1L;

    @Override
    public void save(Usuario usuario) {
        if (usuario.getId() == null) {
            usuario.setId(idCounter++);
            usuarios.add(usuario);
        } else {
            // Actualizar usuario existente
            int index = usuarios.indexOf(findById(usuario.getId()));
            if (index >= 0) {
                usuarios.set(index, usuario);
            }
        }
    }

    @Override
    public Usuario findById(Long id) {
        return usuarios.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Usuario findByUsername(String username) {
        return usuarios.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
    }

    @Override
    public List<Usuario> findAll() {
        return new ArrayList<>(usuarios);
    }

    @Override
    public void guardar(Usuario usuario) {

    }

    @Override
    public Usuario buscarUsuarioPorEmail(String email) {
        return null;
    }


}
