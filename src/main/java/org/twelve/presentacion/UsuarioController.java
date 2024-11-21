package org.twelve.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.ComentarioService;
import org.twelve.dominio.UsuarioService;
import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.UsuarioMovie;
import org.twelve.presentacion.dto.ComentarioDTO;
import org.twelve.presentacion.dto.PerfilDTO;
import org.twelve.presentacion.dto.UsuarioMovieDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Controller
public class UsuarioController {

    private final ComentarioService comentarioService;
    private final UsuarioService usuarioService;
    private HttpSession session;

    @Autowired
    public UsuarioController(UsuarioService usuarioService, HttpSession session, ComentarioService comentarioService) {
        this.usuarioService = usuarioService;
        this.session = session;
        this.comentarioService = comentarioService;
    }

    @RequestMapping(path = "/perfil/{id}", method = RequestMethod.GET)
    public ModelAndView verPerfil(@PathVariable Integer id, HttpServletRequest request) {
        ModelMap model = new ModelMap();

        // verifico si hay un usuario logueado
        Integer usuarioLogueadoId = (Integer) request.getSession().getAttribute("usuarioId");
        System.out.println("usuario logueado; " + usuarioLogueadoId);

        if (usuarioLogueadoId == null)
            return new ModelAndView("redirect:/login");

        boolean esPerfilPropio = usuarioLogueadoId.equals(id);

        PerfilDTO usuario = usuarioService.buscarPorId(id);
        if (usuario == null) {
            model.put("error", "Usuario no encontrado");
            return new ModelAndView("perfil", model);
        }

        Boolean estaSiguiendo = usuarioService.estaSiguiendo(usuarioLogueadoId, id);
        String seguirODejarUrl = estaSiguiendo ? "/dejarDeSeguir/" + id : "/seguir/" + id;

        List<UsuarioMovieDTO> historial = usuarioService.obtenerHistorialDePeliculasVistas(usuario.getId());
        List<UsuarioMovieDTO> verMasTarde = usuarioService.obtenerListaVerMasTarde(usuario.getId());

        //agrego comentarios
        List<ComentarioDTO> comentariosRecientes = comentarioService.obtenerUltimosTresComentarios(id);

        model.put("usuario", usuario);
        model.put("estaSiguiendo", estaSiguiendo);
        model.put("seguirODejarUrl", seguirODejarUrl);
        model.put("cantidadPeliculasVistas", usuario.getCantidadPeliculasVistas());
        model.put("cantidadPeliculasVistasEsteAno", usuario.getCantidadPeliculasVistasEsteAno());
        model.put("peliculasFavoritas", usuario.getPeliculasFavoritas());
        model.put("historial", historial);
        model.put("verMasTarde", verMasTarde);
        model.put("seguidores", usuario.getSeguidores());
        model.put("siguiendo", usuario.getSeguidos());
        model.put("esPerfilPropio", esPerfilPropio);
        // model.put("actividadReciente", ultimosComentarios);
        model.put("comentariosRecientes", comentariosRecientes);
        return new ModelAndView("perfil", model);
    }

    @RequestMapping(path = "/seguir/{idSeguido}", method = RequestMethod.POST)
    public String seguirUsuario(@PathVariable Integer idSeguido, HttpServletRequest request) {
        Integer usuarioLogueadoId = (Integer) request.getSession().getAttribute("usuarioId");

        if (usuarioLogueadoId.equals(idSeguido)) {
            return "redirect:/error";
        }

        try {
            usuarioService.seguirUsuario(usuarioLogueadoId, idSeguido);
        } catch (Exception e) {
            return "redirect:/error";
        }
        return "redirect:/perfil/" + idSeguido;
    }

    @RequestMapping(path = "/dejarDeSeguir/{idSeguido}", method = RequestMethod.POST)
    public String dejarDeSeguirUsuario(@PathVariable Integer idSeguido, HttpServletRequest request) {
        Integer usuarioLogueadoId = (Integer) request.getSession().getAttribute("usuarioId");
        usuarioService.dejarDeSeguirUsuario(usuarioLogueadoId, idSeguido);
        return "redirect:/perfil/" + idSeguido;
    }

    @RequestMapping(path = "/favoritos", method = RequestMethod.GET)
    public ModelAndView verFavoritos(HttpServletRequest request) {
        Integer usuarioLogueadoId = (Integer) request.getSession().getAttribute("usuarioId");

        if (usuarioLogueadoId == null) {
            return new ModelAndView("redirect:/login");
        }

        PerfilDTO usuario = usuarioService.buscarPorId(usuarioLogueadoId);
        List<Movie> peliculasFavoritas = usuarioService.obtenerPeliculasFavoritas(usuario.getId());

        ModelMap model = new ModelMap();
        model.put("peliculasFavoritas", peliculasFavoritas != null ? peliculasFavoritas : Collections.emptyList());

        return new ModelAndView("favoritos", model);
    }

    @RequestMapping(path = "/editarPerfil", method = RequestMethod.POST)
    public String editarPerfil(
            @RequestParam("username") String username,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("nombre") String nombre,
            @RequestParam("pais") String pais,
            @RequestParam(value = "fotoPerfil", required = false) MultipartFile fotoPerfil,
            HttpServletRequest request) {

        Integer usuarioLogueadoId = (Integer) request.getSession().getAttribute("usuarioId");

        if (usuarioLogueadoId == null) {
            return "redirect:/login";
        }

        if (username.isEmpty() || nombre.isEmpty() || pais.isEmpty()) {
            return "redirect:/perfil/" + usuarioLogueadoId + "?error=Campos obligatorios";
        }

        try {
            usuarioService.actualizarPerfil(usuarioLogueadoId, username, descripcion, nombre, pais, fotoPerfil);
        } catch (DataIntegrityViolationException e) {
            return "redirect:/perfil/" + usuarioLogueadoId + "?error=Error de datos";
        } catch (Exception e) {
            return "redirect:/error";
        }

        return "redirect:/perfil/" + usuarioLogueadoId;
    }

    @RequestMapping(path = "/historial", method = RequestMethod.GET)
    public ModelAndView verHistorial(HttpServletRequest request) {
        Integer usuarioLogueadoId = (Integer) request.getSession().getAttribute("usuarioId");

        if (usuarioLogueadoId == null) {
            return new ModelAndView("redirect:/login");
        }

        PerfilDTO usuario = usuarioService.buscarPorId(usuarioLogueadoId);
        List<UsuarioMovieDTO> historial = usuarioService.obtenerHistorialDePeliculasVistas(usuario.getId());

        ModelMap model = new ModelMap();
        model.put("historial", historial);

        return new ModelAndView("historial", model);
    }

    @RequestMapping(path = "/verMasTarde", method = RequestMethod.GET)
    public ModelAndView verMasTarde(HttpServletRequest request) {
        Integer usuarioLogueadoId = (Integer) request.getSession().getAttribute("usuarioId");

        if (usuarioLogueadoId == null) {
            return new ModelAndView("redirect:/login");
        }

        PerfilDTO usuario = usuarioService.buscarPorId(usuarioLogueadoId);
        List<UsuarioMovieDTO> verMasTarde = usuarioService.obtenerListaVerMasTarde(usuario.getId());

        ModelMap model = new ModelMap();
        model.put("verMasTarde", verMasTarde);

        return new ModelAndView("verMasTarde", model);
    }

}



