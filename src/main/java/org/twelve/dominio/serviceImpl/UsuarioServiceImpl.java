package org.twelve.dominio.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.twelve.dominio.RepositorioUsuario;
import org.twelve.dominio.UsuarioService;
import org.twelve.dominio.entities.Usuario;

import javax.transaction.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioService {


    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public UsuarioServiceImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }


    @Transactional
    public void actualizarPerfil(Usuario usuario) {
        repositorioUsuario.modificar(usuario);
    }

    @Transactional
    public Usuario buscarPorId(Long id) {
        return repositorioUsuario.buscarPorId(id);
    }

}
