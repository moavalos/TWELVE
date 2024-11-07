package org.twelve.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.ComentarioService;
import org.twelve.dominio.UsuarioService;
import org.twelve.dominio.entities.Movie;
import org.twelve.presentacion.dto.ComentarioDTO;
import org.twelve.presentacion.dto.PerfilDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

        //agrego comentarios
        List<ComentarioDTO> comentariosRecientes = comentarioService.obtenerUltimosTresComentarios(id);

        model.put("usuario", usuario);
        model.put("estaSiguiendo", estaSiguiendo);
        model.put("seguirODejarUrl", seguirODejarUrl);
        model.put("cantidadPeliculasVistas", usuario.getCantidadPeliculasVistas());
        model.put("cantidadPeliculasVistasEsteAno", usuario.getCantidadPeliculasVistasEsteAno());
        model.put("peliculasFavoritas", usuario.getPeliculasFavoritas());
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

}



