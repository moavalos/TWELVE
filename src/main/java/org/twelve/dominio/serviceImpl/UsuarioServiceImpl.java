package org.twelve.dominio.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.twelve.dominio.RepositorioUsuario;
import org.twelve.dominio.UsuarioMovieRepository;
import org.twelve.dominio.UsuarioService;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.Usuario;
import org.twelve.presentacion.dto.PerfilDTO;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private RepositorioUsuario repositorioUsuario;
    private UsuarioMovieRepository usuarioMovieRepository;

    @Autowired
    public UsuarioServiceImpl(RepositorioUsuario repositorioUsuario, UsuarioMovieRepository usuarioMovieRepository) {
        this.repositorioUsuario = repositorioUsuario;
        this.usuarioMovieRepository = usuarioMovieRepository;
    }

    @Override
    public List<PerfilDTO> encontrarTodos() {
        List<Usuario> usuarios = repositorioUsuario.encontrarTodos();
        return usuarios.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PerfilDTO buscarPorId(Integer id) {
        Usuario usuario = repositorioUsuario.buscarPorId(id);

        if (usuario == null) {
            throw new EntityNotFoundException("Usuario no encontrado");
        }

        int cantidadPeliculasVistas = usuarioMovieRepository.obtenerCantidadPeliculasVistas(id);
        int cantidadPeliculasVistasEsteAno = usuarioMovieRepository.obtenerCantidadPeliculasVistasEsteAno(id);
        List<Movie> peliculasFavoritas = usuarioMovieRepository.obtenerPeliculasFavoritas(id);

        PerfilDTO perfilDTO = convertToDTO(usuario);
        perfilDTO.setCantidadPeliculasVistas(cantidadPeliculasVistas);
        perfilDTO.setCantidadPeliculasVistasEsteAno(cantidadPeliculasVistasEsteAno);
        perfilDTO.setPeliculasFavoritas(peliculasFavoritas);

        return perfilDTO;
    }

    @Override
    public PerfilDTO crear(PerfilDTO perfilDTO) {
        Usuario usuario = convertToEntity(perfilDTO);
        Usuario savedUsuario = repositorioUsuario.guardar(usuario);
        return convertToDTO(savedUsuario);
    }

    @Override
    public List<PerfilDTO> buscarPorUsername(String username) {
        List<Usuario> usuarios = (List<Usuario>) repositorioUsuario.buscarPorUsername(username);
        return usuarios.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Usuario convertToEntity(PerfilDTO perfilDTO) {
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

    public PerfilDTO convertToDTO(Usuario usuario) {
        PerfilDTO perfilDTO = new PerfilDTO();
        perfilDTO.setId(usuario.getId());
        perfilDTO.setNombre(usuario.getNombre());
        perfilDTO.setDescripcion(usuario.getDescripcion());
        perfilDTO.setPais(usuario.getPais());
        perfilDTO.setEmail(usuario.getEmail());
        perfilDTO.setActivo(usuario.getActivo());
        perfilDTO.setRol(usuario.getRol());
        perfilDTO.setPassword(usuario.getPassword());
        perfilDTO.setUsername(usuario.getUsername());
        return perfilDTO;
    }
}

