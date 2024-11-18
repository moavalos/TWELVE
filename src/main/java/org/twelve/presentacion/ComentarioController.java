package org.twelve.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.ComentarioService;
import org.twelve.dominio.UsuarioService;
import org.twelve.presentacion.dto.ComentarioDTO;
import org.twelve.presentacion.dto.PerfilDTO;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ComentarioController {

    private final ComentarioService comentarioService;
    private final UsuarioService usuarioService;

    @Autowired
    public ComentarioController(ComentarioService comentarioService, UsuarioService usuarioService) {
        this.comentarioService = comentarioService;
        this.usuarioService = usuarioService;
    }

    @RequestMapping(path = "/guardar-comentario", method = RequestMethod.POST)
    public String guardarComentario(@ModelAttribute ComentarioDTO comentarioDTO, HttpServletRequest requestMock) {
        this.comentarioService.agregarComentario(comentarioDTO);
        return "redirect:/detalle-pelicula/" + comentarioDTO.getIdMovie();
    }

    @RequestMapping(path = "/comentario/{comentarioId}/like", method = RequestMethod.POST)
    public String darLikeComentario(@PathVariable Integer comentarioId, HttpServletRequest request) {
        Integer usuarioLogueadoId = (Integer) request.getSession().getAttribute("usuarioId");
        PerfilDTO usuario = usuarioService.buscarPorId(usuarioLogueadoId);

        ComentarioDTO comentarioDTO = comentarioService.buscarPorId(comentarioId);
        usuarioService.darMegustaComentario(usuario, comentarioDTO);

        return "redirect:/detalle-pelicula/" + comentarioDTO.getIdMovie();
    }
    

}
