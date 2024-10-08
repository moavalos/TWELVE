package org.twelve.dominio.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.twelve.dominio.RepositorioUsuario;
import org.twelve.dominio.UsuarioService;
import org.twelve.dominio.entities.Usuario;
import org.twelve.presentacion.dto.PerfilDTO;

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
    public List<PerfilDTO> getAll() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public PerfilDTO getById(Long id) {
        Usuario usuario = usuarioRepository.findById(Math.toIntExact(id));
        return convertToDTO(usuario);
    }

    @Override
    public PerfilDTO create(PerfilDTO perfilDTO) {
        Usuario usuario = convertToEntity(perfilDTO);
        Usuario savedUsuario = usuarioRepository.save(usuario);
        return convertToDTO(savedUsuario);
    }

    @Override
    public List<PerfilDTO> findByUserName(String username) {
        List<Usuario> usuarios = usuarioRepository.findByUserName(username);
        return usuarios.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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

