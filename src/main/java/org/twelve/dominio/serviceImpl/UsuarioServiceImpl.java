package org.twelve.dominio.serviceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.twelve.dominio.RepositorioUsuario;
import org.twelve.dominio.UsuarioService;
import org.twelve.dominio.entities.Usuario;
import org.springframework.stereotype.Service;




import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final RepositorioUsuario usuarioRepository;

    public UsuarioServiceImpl(RepositorioUsuario usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    @Override
    public Usuario findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return null;
    }

    @Override
    public void actualizarPerfil(Usuario usuarioExistente) {

    }

    @Override
    public Usuario getCurrentUser() {
        return null;
    }
}

