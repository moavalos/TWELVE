package org.twelve.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(path = "/comentario/{id}/like", method = RequestMethod.POST)
    public String likeComentario(@PathVariable("id") Integer comentarioId, HttpServletRequest request) {
        PerfilDTO usuarioDTO = (PerfilDTO) request.getSession().getAttribute("usuario");

        if (usuarioDTO == null) {
            return "redirect:/login";
        }

        ComentarioDTO comentarioDTO = comentarioService.buscarPorId(comentarioId);
        if (comentarioDTO == null) {
            return "redirect:/error";
        }

        usuarioService.darMegustaComentario(usuarioDTO, comentarioDTO);

        return "redirect:/detalle-pelicula/" + comentarioDTO.getIdMovie();
    }
}
