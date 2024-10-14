package org.twelve.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
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

    // private final UsuarioService usuarioService;
    // private final PaisRepository paisRepository;

    // public UsuarioController(UsuarioService usuarioService, PaisRepository paisRepository) {
    @Autowired
    public UsuarioController(UsuarioService usuarioService, ServicioLogin servicioLogin) {
        this.usuarioService = usuarioService;
        this.paisRepository = paisRepository;
    }

    // @Transactional
    // @RequestMapping(path = "/completarPerfil", method = RequestMethod.GET)
    // public ModelAndView mostrarCompletarPerfil(@RequestParam("id") Long id) {
    //     ModelMap model = new ModelMap();
    //     PerfilDTO usuario = usuarioService.buscarPorId(id);
    //     if (usuario == null) {
    //         model.put("error", "Usuario no encontrado");
    //         return new ModelAndView("usuario-datos", model);
    //     }
    //     model.put("usuario", usuario);

    //     List<Pais> paises = paisRepository.findAll();
    //     model.put("usuario", usuario);
    //     model.put("paises", paises);


    //     return new ModelAndView("usuario-datos", model);
    // }


    // @RequestMapping(path = "/completarPerfil", method = RequestMethod.POST)
    // public ModelAndView completarPerfil(@ModelAttribute("usuario") Usuario usuario) {
    //     ModelMap model = new ModelMap();
    //     PerfilDTO usuarioExistente = usuarioService.buscarPorId(usuario.getId());
    //     if (usuarioExistente == null) {
    //         model.put("error", "Usuario no encontrado");
    //         return new ModelAndView("usuario-datos", model);
    //     }
    //     try {
    //         usuarioExistente.setNombre(usuario.getNombre());
    //         usuarioExistente.setUsername(usuario.getUsername());
    //         usuarioExistente.setDescripcion(usuario.getDescripcion());
    //         usuarioExistente.setPais(usuario.getPais());

    //         usuarioService.actualizarPerfil(usuarioExistente);
    //     } catch (Exception e) {
    //         model.put("error", "Error al completar el perfil");
    //         return new ModelAndView("usuario-datos", model);
    //     }
    //     return new ModelAndView("redirect:/login");
    // }

    // @RequestMapping(path = "/editar/{id}", method = RequestMethod.PUT)
    // public ResponseEntity<PerfilDTO> actualizarPerfil(@PathVariable Long id, @RequestBody PerfilDTO perfilDTO) {
    //     try {
    //         PerfilDTO usuario = usuarioService.buscarPorId(id);
    //         if (usuario == null) {
    //             return ResponseEntity.notFound().build();
    //         }
    //         // Suponiendo que tienes un método para convertir de PerfilDTO a Usuario
    //         usuario.setNombre(perfilDTO.getNombre());
    //         usuario.setUsername(perfilDTO.getUsername());
    //         usuario.setDescripcion(perfilDTO.getDescripcion());
    //         usuario.setPais(perfilDTO.getPais());

    //         usuarioService.actualizarPerfil(usuario);
    //         return ResponseEntity.ok(perfilDTO); // Devuelve el perfil actualizado
    //     } catch (Exception e) {
    //         return ResponseEntity.status(500).build(); // Error interno del servidor
    //     }
    @RequestMapping(path = "/registrarme", method = RequestMethod.GET)
    public ModelAndView mostrarFormularioRegistro() {
        ModelMap model = new ModelMap();
        model.put("usuario", new Usuario());
        return new ModelAndView("nuevo-usuario", model);
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



