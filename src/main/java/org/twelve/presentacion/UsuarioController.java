package org.twelve.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.UsuarioService;
import org.twelve.dominio.entities.Usuario;

@Controller
public class UsuarioController {


    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @RequestMapping(path = "/completarPerfil", method = RequestMethod.GET)
    public ModelAndView mostrarCompletarPerfil(@RequestParam("id") Long id) {
        ModelMap model = new ModelMap();
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario == null) {
            model.put("error", "Usuario no encontrado");
            return new ModelAndView("usuario-datos", model);
        }
        model.put("usuario", usuario);
        System.out.println(usuario);
        return new ModelAndView("usuario-datos", model);
    }

    @RequestMapping(path = "/completarPerfil", method = RequestMethod.POST)
    public ModelAndView completarPerfil(@ModelAttribute("usuario") Usuario usuario) {
        ModelMap model = new ModelMap();
        Usuario usuarioExistente = usuarioService.buscarPorId(usuario.getId());
        try {
            usuarioExistente.setNombre(usuario.getNombre());
            usuarioExistente.setDescripcion(usuario.getDescripcion());
            usuarioExistente.setPais(usuario.getPais());
            
            usuarioService.actualizarPerfil(usuarioExistente);
        } catch (Exception e) {
            model.put("error", "Error al completar el perfil");
            return new ModelAndView("usuario-datos", model);
        }
        return new ModelAndView("redirect:/login");
    }
}
