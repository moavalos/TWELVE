package org.twelve.dominio.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.twelve.dominio.RepositorioUsuario;
import org.twelve.dominio.UsuarioMovieRepository;
import org.twelve.dominio.UsuarioService;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.Seguidor;
import org.twelve.dominio.entities.Usuario;
import org.twelve.presentacion.dto.PerfilDTO;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.twelve.presentacion.dto.PerfilDTO.convertToDTO;
import static org.twelve.presentacion.dto.PerfilDTO.convertToEntity;

@Service("usuarioService")
public class UsuarioServiceImpl implements UsuarioService {

    private final RepositorioUsuario repositorioUsuario;
    private final UsuarioMovieRepository usuarioMovieRepository;

    @Autowired
    public UsuarioServiceImpl(RepositorioUsuario repositorioUsuario, UsuarioMovieRepository usuarioMovieRepository) {
        this.repositorioUsuario = repositorioUsuario;
        this.usuarioMovieRepository = usuarioMovieRepository;
    }

    @Override
    public List<PerfilDTO> encontrarTodos() {
        List<Usuario> usuarios = repositorioUsuario.encontrarTodos();
        return usuarios.stream().map(PerfilDTO::convertToDTO).collect(Collectors.toList());
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

        List<Seguidor> seguidores = repositorioUsuario.obtenerSeguidores(id);
        List<Seguidor> seguidos = repositorioUsuario.obtenerSeguidos(id);

        PerfilDTO perfilDTO = convertToDTO(usuario);
        perfilDTO.setCantidadPeliculasVistas(cantidadPeliculasVistas);
        perfilDTO.setCantidadPeliculasVistasEsteAno(cantidadPeliculasVistasEsteAno);
        perfilDTO.setPeliculasFavoritas(peliculasFavoritas);
        perfilDTO.setSeguidores(seguidores);
        perfilDTO.setSeguidos(seguidos);

        return perfilDTO;
    }

    @Override
    public PerfilDTO crear(PerfilDTO perfilDTO) {
        Usuario usuario = convertToEntity(perfilDTO);
        Usuario savedUsuario = repositorioUsuario.guardar(usuario);
        return convertToDTO(savedUsuario);
    }

    @Override
    public void seguirUsuario(Integer usuarioId, Integer seguidoId) {
        Usuario usuario = repositorioUsuario.buscarPorId(usuarioId);
        Usuario seguido = repositorioUsuario.buscarPorId(seguidoId);

        if (usuario == null || seguido == null)
            throw new IllegalArgumentException("Usuario o seguido no encontrado");

        if (!repositorioUsuario.estaSiguiendo(usuario, seguido))
            repositorioUsuario.seguirUsuario(usuario, seguido);
    }

    @Override
    public void dejarDeSeguirUsuario(Integer usuarioId, Integer seguidoId) {
        Usuario usuario = repositorioUsuario.buscarPorId(usuarioId);
        Usuario seguido = repositorioUsuario.buscarPorId(seguidoId);

        if (usuario == null || seguido == null)
            throw new IllegalArgumentException("Usuario o seguido no encontrado");

        if (repositorioUsuario.estaSiguiendo(usuario, seguido))
            repositorioUsuario.dejarDeSeguirUsuario(usuario, seguido);
    }

    @Override
    public Boolean estaSiguiendo(Integer usuarioId, Integer seguidoId) {
        Usuario usuario = repositorioUsuario.buscarPorId(usuarioId);
        Usuario seguido = repositorioUsuario.buscarPorId(seguidoId);
        return repositorioUsuario.estaSiguiendo(usuario, seguido);
    }

}

