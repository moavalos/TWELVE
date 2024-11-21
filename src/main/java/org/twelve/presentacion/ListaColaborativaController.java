package org.twelve.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.*;
import org.twelve.dominio.entities.ListaMovie;
import org.twelve.presentacion.dto.ListaColaborativaDTO;
import org.twelve.presentacion.dto.PerfilDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ListaColaborativaController {

    private final UsuarioService usuarioService;
    private final ListaColaborativaService listaColaborativaService;
    private final MovieService movieService;

    @Autowired
    public ListaColaborativaController(UsuarioService usuarioService, ListaColaborativaService listaColaborativaService, MovieService movieService) {
        this.usuarioService = usuarioService;
        this.listaColaborativaService = listaColaborativaService;
        this.movieService = movieService;
    }

    @RequestMapping(path = "/crearListaColaborativa", method = RequestMethod.GET)
    public ModelAndView mostrarFormularioCrearLista(HttpServletRequest request) {
        ModelMap model = new ModelMap();

        Integer usuarioLogueadoId = (Integer) request.getSession().getAttribute("usuarioId");

        List<PerfilDTO> amigos = usuarioService.obtenerAmigos(usuarioLogueadoId);

        model.put("usuarios", amigos);
        return new ModelAndView("crearListaColaborativa", model);
    }

    @RequestMapping(path = "/crearListaColaborativa", method = RequestMethod.POST)
    public ModelAndView crearListaColaborativa(HttpServletRequest request, String nombreLista, Integer usuarioColaborador) {
        ModelMap model = new ModelMap();
        Integer usuarioLogueadoId = (Integer) request.getSession().getAttribute("usuarioId");

        if (usuarioLogueadoId == null) {
            return new ModelAndView("redirect:/login");
        }

        // hay q verificar si los usuarios se siguen mutuamente
        boolean sonAmigos = usuarioService.sonAmigos(usuarioLogueadoId, usuarioColaborador);
        if (!sonAmigos) {
            model.put("error", "Solo puedes crear listas con usuarios que te sigan y a quienes tú sigas.");
            model.put("usuarios", usuarioService.encontrarTodos());
            return new ModelAndView("crearListaColaborativa", model);
        }

        listaColaborativaService.crearListaColaborativa(usuarioLogueadoId, usuarioColaborador, nombreLista);
        return new ModelAndView("redirect:/listas/" + usuarioLogueadoId);
    }

    @RequestMapping(path = "/agregarPelicula", method = RequestMethod.GET)
    public ModelAndView mostrarFormularioAgregarPelicula() {
        ModelMap model = new ModelMap();
        model.put("listas", listaColaborativaService.obtenerTodasLasListasColaborativas());
        model.put("peliculas", movieService.getAll());
        return new ModelAndView("agregarPeliculasLista", model);
    }

    @RequestMapping(path = "/listas/{id}", method = RequestMethod.GET)
    public ModelAndView mostrarListasUsuario(@PathVariable Integer id, HttpServletRequest request) {
        ModelMap model = new ModelMap();

        Integer usuarioLogueadoId = (Integer) request.getSession().getAttribute("usuarioId");

        if (usuarioLogueadoId == null) {
            return new ModelAndView("redirect:/login");
        }

        List<ListaColaborativaDTO> listas = listaColaborativaService.obtenerListasPorUsuario(usuarioLogueadoId);

        model.put("listas", listas);
        return new ModelAndView("listas", model);
    }

    @RequestMapping(path = "/listas/detalle/{id}", method = RequestMethod.GET)
    public ModelAndView mostrarDetalleLista(@PathVariable Integer id) {
        ModelMap model = new ModelMap();

        ListaColaborativaDTO lista = listaColaborativaService.obtenerDetalleLista(id);

        if (lista == null) {
            model.put("error", "La lista solicitada no existe.");
            return new ModelAndView("error", model);
        }

        List<ListaMovie> peliculas = listaColaborativaService.obtenerPeliculasPorListaId(id);
        model.put("peliculas", peliculas);
        model.put("lista", lista);

        return new ModelAndView("detalleLista", model);
    }
}
