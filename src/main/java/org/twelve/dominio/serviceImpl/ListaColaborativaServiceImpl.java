package org.twelve.dominio.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.twelve.dominio.*;
import org.twelve.dominio.entities.ListaColaborativa;
import org.twelve.dominio.entities.ListaMovie;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.Usuario;
import org.twelve.presentacion.dto.ListaColaborativaDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service("listaColaborativaService")
public class ListaColaborativaServiceImpl implements ListaColaborativaService {

    private final RepositorioUsuario repositorioUsuario;
    private final ListaColaborativaRepository listaColaborativaRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public ListaColaborativaServiceImpl(RepositorioUsuario repositorioUsuario, ListaColaborativaRepository listaColaborativaRepository, MovieRepository movieRepository) {
        this.repositorioUsuario = repositorioUsuario;
        this.listaColaborativaRepository = listaColaborativaRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public List<ListaColaborativaDTO> obtenerTodasLasListasColaborativas() {
        List<ListaColaborativa> listas = listaColaborativaRepository.encontrarTodos();
        return listas.stream()
                .map(ListaColaborativaDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ListaColaborativaDTO> obtenerListasPorUsuario(Integer usuarioId) {
        List<ListaColaborativa> listas = listaColaborativaRepository.buscarPorUsuarioId(usuarioId);
        return listas.stream()
                .map(ListaColaborativaDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ListaColaborativaDTO crearListaColaborativa(Integer usuario1Id, Integer usuario2Id, String nombreLista) {
        if (!repositorioUsuario.existeRelacion(usuario1Id, usuario2Id)) {
            throw new RuntimeException("Los usuarios deben seguirse mutuamente para colaborar.");
        }

        boolean existeListaConNombre = listaColaborativaRepository.existeListaConNombreParaUsuario(usuario1Id, nombreLista);
        if (existeListaConNombre) {
            throw new RuntimeException("Ya existe una lista con este nombre para el usuario.");
        }

        ListaColaborativa listaColaborativa = new ListaColaborativa();
        listaColaborativa.setCreador(repositorioUsuario.buscarPorId(usuario1Id));
        listaColaborativa.setColaborador(repositorioUsuario.buscarPorId(usuario2Id));
        listaColaborativa.setNombre(nombreLista);
        listaColaborativa.setFechaCreacion(LocalDate.now());

        ListaColaborativa savedEntity = listaColaborativaRepository.guardar(listaColaborativa);

        return ListaColaborativaDTO.convertToDTO(savedEntity);
    }

    @Override
    public ListaColaborativaDTO agregarPeliculaALista(Integer listaId, Integer movieId, Integer usuarioId) {

        ListaColaborativa lista = listaColaborativaRepository.buscarPorId(listaId);
        if (lista == null)
            throw new RuntimeException("Lista no encontrada");

        Movie pelicula = movieRepository.findById(movieId);
        if (pelicula == null)
            throw new RuntimeException("Película no encontrada");

        boolean peliculaYaAgregada = listaColaborativaRepository.existePeliculaEnLista(listaId, movieId);
        if (peliculaYaAgregada)
            throw new RuntimeException("La película ya está en esta lista.");

        Usuario usuario = repositorioUsuario.buscarPorId(usuarioId);

        ListaMovie listaPelicula = new ListaMovie();
        listaPelicula.setLista(lista);
        listaPelicula.setPelicula(pelicula);
        listaPelicula.setUsuarioAgregador(usuario);
        listaPelicula.setFechaAgregada(LocalDate.now());

        listaColaborativaRepository.guardarListaPelicula(listaPelicula);

        ListaColaborativa listaActualizada = listaColaborativaRepository.buscarPorId(listaId);
        if (listaActualizada == null)
            throw new RuntimeException("Error al recuperar la lista actualizada");

        return ListaColaborativaDTO.convertToDTO(listaActualizada);
    }

    @Override
    public ListaColaborativaDTO obtenerDetalleLista(Integer id) {
        ListaColaborativa lista = listaColaborativaRepository.buscarPorId(id);

        if (lista.getCreador() == null) {
            throw new IllegalArgumentException("La lista no tiene un creador asignado.");
        }

        List<ListaMovie> peliculas = lista.getPeliculas() != null ? lista.getPeliculas() : List.of();

        return new ListaColaborativaDTO(lista.getId(), lista.getNombre(),
                lista.getCreador(), lista.getColaborador(), peliculas, lista.getFechaCreacion());
    }

    @Override
    public List<ListaMovie> obtenerPeliculasPorListaId(Integer listaId) {
        return listaColaborativaRepository.buscarPeliculasPorListaId(listaId);
    }

    @Override
    public void eliminarListaColaborativa(Integer listaId, Integer usuarioId) {
        ListaColaborativa lista = listaColaborativaRepository.buscarPorId(listaId);

        if (lista == null) {
            throw new RuntimeException("La lista no existe.");
        }

        if (!lista.getCreador().getId().equals(usuarioId) && !lista.getColaborador().getId().equals(usuarioId)) {
            throw new RuntimeException("No tienes permiso para eliminar esta lista.");
        }

        List<ListaMovie> peliculas = listaColaborativaRepository.buscarPeliculasPorListaId(listaId);
        for (ListaMovie pelicula : peliculas) {
            listaColaborativaRepository.eliminarPelicula(pelicula);
        }

        listaColaborativaRepository.eliminar(listaId);
    }

}
