package org.twelve.dominio.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.twelve.dominio.RepositorioUsuario;
import org.twelve.dominio.UsuarioService;
import org.twelve.dominio.entities.Usuario;
import org.twelve.presentacion.dto.PerfilDTO;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service("usuarioService")
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final RepositorioUsuario usuarioRepository;

    @Autowired
    public UsuarioServiceImpl(RepositorioUsuario usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<PerfilDTO> encontrarTodos() {
        List<Usuario> usuarios = usuarioRepository.encontrarTodos();
        return usuarios.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public PerfilDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.buscarPorId(id);
        return convertToDTO(usuario);
    }

    @Override
    public PerfilDTO crear(PerfilDTO perfilDTO) {
        Usuario usuario = convertToEntity(perfilDTO);
        Usuario savedUsuario = usuarioRepository.guardar(usuario);
        return convertToDTO(savedUsuario);
    }

    @Override
    public List<PerfilDTO> buscarPorUsername(String username) {
        List<Usuario> usuarios = usuarioRepository.buscarPorUsername(username);
        return usuarios.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void actualizarPerfil(PerfilDTO usuario) {
        Usuario usuarioExistente = usuarioRepository.buscarPorId(usuario.getId());
        if (usuarioExistente == null) {
            throw new EntityNotFoundException("Usuario no encontrado");
        }

        // Actualiza los campos del usuario existente con los datos del perfilDTO
        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setUsername(usuario.getUsername());
        usuarioExistente.setDescripcion(usuario.getDescripcion());
        usuarioExistente.setPais(usuario.getPais());

        // Guarda los cambios en la base de datos
        usuarioRepository.guardar(usuarioExistente);

    }



    // dto a entidad en
    private Usuario convertToEntity(PerfilDTO perfilDTO) {
        Usuario usuario = new Usuario();
        usuario.setNombre(perfilDTO.getNombre());
        usuario.setDescripcion(perfilDTO.getDescripcion());
        usuario.setPais(perfilDTO.getPais());
        usuario.setEmail(perfilDTO.getEmail());
        usuario.setId(perfilDTO.getId());
        usuario.setActivo(perfilDTO.getActivo());
        usuario.setRol(perfilDTO.getRol());
        usuario.setPassword(perfilDTO.getPassword());
        usuario.setUsername(perfilDTO.getUsername());
        return usuario;
    }

    // entidad a DTO
    private PerfilDTO convertToDTO(Usuario usuario) {
        return new PerfilDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getDescripcion(),
                usuario.getPassword(),
                usuario.getUsername(),
                usuario.getPais(),
                usuario.getRol(),
                usuario.getActivo()

        );
    }
}

