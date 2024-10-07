package org.twelve.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PerfilController {

    @GetMapping("/perfil/{id}")
    public ModelAndView mostrarPerfil() {
        return new ModelAndView("perfil");
    }
}
