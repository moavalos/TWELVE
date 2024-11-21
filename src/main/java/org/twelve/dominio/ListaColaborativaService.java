package org.twelve.dominio;

import org.twelve.dominio.entities.ListaMovie;
import org.twelve.presentacion.dto.ListaColaborativaDTO;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ListaColaborativaService {

    List<ListaColaborativaDTO> obtenerTodasLasListasColaborativas();

    List<ListaColaborativaDTO> obtenerListasPorUsuario(Integer usuarioId);

    ListaColaborativaDTO crearListaColaborativa(Integer usuario1Id, Integer usuario2Id, String nombreLista);

    ListaColaborativaDTO agregarPeliculaALista(Integer listaId, Integer movieId, Integer usuarioId);

    ListaColaborativaDTO obtenerDetalleLista(Integer id);

    List<ListaMovie> obtenerPeliculasPorListaId(Integer listaId);

    void eliminarListaColaborativa(Integer listaId, Integer usuarioId);

}
