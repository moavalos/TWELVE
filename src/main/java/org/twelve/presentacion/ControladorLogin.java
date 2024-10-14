package org.twelve.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.ServicioLogin;
import org.twelve.dominio.entities.Usuario;
import org.twelve.dominio.excepcion.ContrasenasNoCoinciden;
import org.twelve.dominio.excepcion.UsuarioExistente;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorLogin {

    private ServicioLogin servicioLogin;

    @Autowired
    public ControladorLogin(ServicioLogin servicioLogin) {
        this.servicioLogin = servicioLogin;
    }

    @RequestMapping("/login")
    public ModelAndView irALogin() {

        ModelMap modelo = new ModelMap();
        modelo.put("datosLogin", new DatosLogin());
        return new ModelAndView("login", modelo);
    }

    @RequestMapping(path = "/validar-login", method = RequestMethod.POST)
    public ModelAndView validarLogin(@ModelAttribute("datosLogin") DatosLogin datosLogin, HttpServletRequest request) {
        ModelMap model = new ModelMap();

        Usuario usuarioBuscado = servicioLogin.consultarUsuario(datosLogin.getEmail(), datosLogin.getPassword());
        if (usuarioBuscado != null) {
            request.getSession().setAttribute("ROL", usuarioBuscado.getRol());
            request.getSession().setAttribute("usuario", usuarioBuscado);
            return new ModelAndView("redirect:/home");
        } else {
            model.put("error", "Usuario o clave incorrecta");
        }
        return new ModelAndView("login", model);
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

        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(path = "/salir", method = RequestMethod.GET)
    public ModelAndView salir(HttpServletRequest request) {
        request.getSession().invalidate(); // cierra sesion
        return new ModelAndView("redirect:/home");
    }

    @RequestMapping(path = "/nuevo-usuario", method = RequestMethod.GET)
    public ModelAndView nuevoUsuario() {
        ModelMap model = new ModelMap();
        model.put("usuario", new Usuario());
        return new ModelAndView("usuario-datos", model);
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView inicio() {
        return new ModelAndView("redirect:/login");
    }
}

