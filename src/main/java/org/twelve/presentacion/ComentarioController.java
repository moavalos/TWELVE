package org.twelve.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.twelve.dominio.ComentarioService;
import org.twelve.presentacion.dto.ComentarioDTO;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ComentarioController {

    private ComentarioService comentarioService;

    @Autowired
    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @RequestMapping(path = "/guardar-comentario", method = RequestMethod.POST)
    public String guardarComentario(@ModelAttribute ComentarioDTO comentarioDTO, HttpServletRequest requestMock) {
        this.comentarioService.agregarComentario(comentarioDTO);
        return "redirect:/detalle-pelicula/" + comentarioDTO.getIdMovie();
    }
}
