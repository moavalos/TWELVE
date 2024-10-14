package org.twelve.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.twelve.dominio.ComentarioService;
import org.twelve.presentacion.dto.ComentarioDTO;

@Controller
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @PostMapping("/guardar-comentario")
    public String guardarComentario(@ModelAttribute ComentarioDTO comentarioDTO) {

        //revisar esta conversion falopa

        comentarioService.agregarComentario(comentarioDTO);

        return "redirect:/detalle-pelicula/" + comentarioDTO.getIdMovie();
    }
}
