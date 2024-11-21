package org.twelve.dominio;

import org.twelve.dominio.entities.ListaColaborativa;
import org.twelve.dominio.entities.ListaMovie;

import java.util.List;

public interface ListaColaborativaRepository {

    List<ListaColaborativa> encontrarTodos();

    ListaColaborativa guardar(ListaColaborativa listaColaborativa);

    ListaColaborativa buscarPorId(Integer id);

    void guardarListaPelicula(ListaMovie listaPelicula);

    List<ListaColaborativa> buscarPorUsuarioId(Integer usuarioId);

    List<ListaMovie> buscarPeliculasPorListaId(Integer listaId);

    boolean existePeliculaEnLista(Integer listaId, Integer movieId);

    boolean existeListaConNombreParaUsuario(Integer usuarioId, String nombreLista);
}
