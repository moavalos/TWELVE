package org.twelve.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.ServicioLogin;
import org.twelve.dominio.UsuarioService;
import org.twelve.dominio.entities.Usuario;
import org.twelve.dominio.excepcion.ContrasenasNoCoinciden;
import org.twelve.dominio.excepcion.UsuarioExistente;
import org.twelve.presentacion.dto.PerfilDTO;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UsuarioController {

    private UsuarioService usuarioService;
    private ServicioLogin servicioLogin;

    @Autowired
    public UsuarioController(UsuarioService usuarioService, ServicioLogin servicioLogin) {
        this.usuarioService = usuarioService;
        this.servicioLogin = servicioLogin;
    }

    @RequestMapping(path = "/registrarme", method = RequestMethod.GET)
    public ModelAndView mostrarFormularioRegistro() {
        ModelMap model = new ModelMap();
        model.put("usuario", new Usuario());
        return new ModelAndView("nuevo-usuario", model);
    }

    @RequestMapping(path = "/completarPerfil", method = RequestMethod.GET)
    public ModelAndView mostrarCompletarPerfil(@RequestParam("id") Integer id) {
        ModelMap model = new ModelMap();
        PerfilDTO usuario = usuarioService.buscarPorId(id);
        if (usuario == null) {
            model.put("error", "Usuario no encontrado");
            return new ModelAndView("usuario-datos", model);
        }
        model.put("usuario", usuario);
        return new ModelAndView("usuario-datos", model);
    }

    @RequestMapping(path = "/registrarme", method = RequestMethod.POST)
    public ModelAndView registrarYCompletarPerfil(@ModelAttribute("usuario") Usuario usuario,
                                                  @RequestParam("confirmPassword") String confirmPassword,
                                                  HttpServletRequest request) {
        ModelMap model = new ModelMap();

        try {
            servicioLogin.verificarUsuarioExistente(usuario);
            Usuario usuarioGuardado = servicioLogin.registrar(usuario, confirmPassword);

            if (usuarioGuardado.getNombre() == null || usuarioGuardado.getNombre().isEmpty()) {
                model.put("mensaje", "Por favor, completa tu perfil.");
                model.put("usuario", usuarioGuardado);
                return new ModelAndView("usuario-datos", model);
            }

        } catch (UsuarioExistente e) {
            model.put("error", "El usuario ya existe.");
            return new ModelAndView("usuario-datos", model);
        } catch (ContrasenasNoCoinciden e) {
            model.put("error", "Las contraseñas no coinciden.");
            return new ModelAndView("usuario-datos", model);
        } catch (Exception e) {
            model.put("error", "Error al registrar el nuevo usuario: " + e.getMessage());
            return new ModelAndView("usuario-datos", model);
        }

        return new ModelAndView("redirect:/home");
    }

    @RequestMapping(path = "/perfil/{id}", method = RequestMethod.GET)
    public ModelAndView buscarPorId(@PathVariable Integer id) {
        ModelMap model = new ModelMap();
        PerfilDTO usuario = usuarioService.buscarPorId(id);
        if (usuario == null) {
            model.put("error", "Usuario no encontrado");
            return new ModelAndView("usuario", model);
        }

        model.put("usuario", usuario);
        return new ModelAndView("usuario", model);
    }
}



