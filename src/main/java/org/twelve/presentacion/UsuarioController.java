package org.twelve.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.PaisRepository;
import org.twelve.dominio.ServicioLogin;
import org.twelve.dominio.UsuarioService;
import org.twelve.dominio.entities.Pais;
import org.twelve.dominio.entities.Usuario;
import org.twelve.presentacion.dto.PerfilDTO;

import javax.transaction.Transactional;
import java.util.List;

@Controller
public class UsuarioController {

    private UsuarioService usuarioService;

     private final PaisRepository paisRepository;

    @Autowired
    public UsuarioController(UsuarioService usuarioService,PaisRepository paisRepository, ServicioLogin servicioLogin) {
        this.usuarioService = usuarioService;
        this.paisRepository = paisRepository;
    }



    @RequestMapping(path = "/perfil/{id}", method = RequestMethod.GET)
    public ModelAndView buscarPorId(@PathVariable Integer id) {
        ModelMap model = new ModelMap();

        PerfilDTO usuario = usuarioService.buscarPorId(id);
        if (usuario == null) {
            model.put("error", "Usuario no encontrado");
            return new ModelAndView("perfil", model);
        }

        model.put("usuario", usuario);
        model.put("cantidadPeliculasVistas", usuario.getCantidadPeliculasVistas());
        model.put("cantidadPeliculasVistasEsteAno", usuario.getCantidadPeliculasVistasEsteAno());
        model.put("peliculasFavoritas", usuario.getPeliculasFavoritas());

        return new ModelAndView("perfil", model);
    }
}



