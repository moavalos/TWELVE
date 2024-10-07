package org.twelve.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.ui.Model;

import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.entities.Usuario;
import org.twelve.dominio.UsuarioService;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.twelve.dominio.entities.Usuario;


@Controller
@RequestMapping("/perfil")
public class PerfilController {

    private final UsuarioService usuarioService;

    public PerfilController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @RequestMapping(path = "/completarPerfil", method = RequestMethod.POST)
    public ModelAndView completarPerfil(@ModelAttribute("usuario") Usuario usuario) {
        ModelMap model = new ModelMap();
        Usuario usuarioExistente = usuarioService.buscarPorId(usuario.getId());
        try {
            usuarioExistente.setNombre(usuario.getNombre());
            usuarioExistente.setUsername(usuario.getUsername());
            usuarioExistente.setDescripcion(usuario.getDescripcion());
            usuarioExistente.setPais(usuario.getPais());

            usuarioService.actualizarPerfil(usuarioExistente);
        } catch (Exception e) {
            model.put("error", "Error al completar el perfil");
            return new ModelAndView("usuario-datos", model);
        }
        // Redirigir al perfil del usuario después de completar el perfil
        return new ModelAndView("redirect:/perfil?id=" + usuarioExistente.getId());
    }

    @RequestMapping(path = "/perfil", method = RequestMethod.GET)
    public ModelAndView mostrarPerfil(@RequestParam("id") Long id) {
        ModelMap model = new ModelMap();
        Usuario usuario = usuarioService.buscarPorId(id);

        model.put("usuario", usuario);
        return new ModelAndView("perfil", model); // Redirige a perfil.html
    }



}


