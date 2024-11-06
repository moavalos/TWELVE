package org.twelve.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.twelve.dominio.PaisRepository;
import org.twelve.dominio.ServicioLogin;
import org.twelve.dominio.UsuarioService;
import org.twelve.dominio.entities.Pais;
import org.twelve.dominio.entities.Usuario;
import org.twelve.presentacion.dto.PerfilDTO;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class UsuarioController {

    private UsuarioService usuarioService;

     private final PaisRepository paisRepository;

    private final String UPLOAD_DIR = "src/main/webapp/resources/core/images/user";

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

        Boolean estaSiguiendo = usuarioService.estaSiguiendo(usuarioLogueadoId, id);
        String seguirODejarUrl = estaSiguiendo ? "/dejarDeSeguir/" + id : "/seguir/" + id;

        model.put("usuario", usuario);
        model.put("estaSiguiendo", estaSiguiendo);
        model.put("seguirODejarUrl", seguirODejarUrl);
        model.put("cantidadPeliculasVistas", usuario.getCantidadPeliculasVistas());
        model.put("cantidadPeliculasVistasEsteAno", usuario.getCantidadPeliculasVistasEsteAno());
        model.put("peliculasFavoritas", usuario.getPeliculasFavoritas());
        model.put("seguidores", usuario.getSeguidores());
        model.put("siguiendo", usuario.getSeguidos());
        model.put("esPerfilPropio", esPerfilPropio);

        return new ModelAndView("perfil", model);
    }

///

@PostMapping("/perfil/{id}")
public ModelAndView uploadProfilePicture(@RequestParam("profilePicture") MultipartFile file,
                                         @PathVariable Integer id) {
    ModelMap model = new ModelMap();

    // Buscar el usuario por ID y verificar que existe
    PerfilDTO usuario = usuarioService.buscarPorId(id);
    if (usuario == null) {
        model.put("error", "Usuario no encontrado");
        return new ModelAndView("perfil", model);
    }

    if (file.isEmpty()) {
        model.put("error", "Por favor selecciona una imagen.");
        model.put("usuario", usuario);
        return new ModelAndView("perfil", model);
    }

    try {
        String fileName = file.getOriginalFilename();
        File destinationFile = new File(UPLOAD_DIR + fileName);
        file.transferTo(destinationFile);

        // Aquí podrías guardar la ruta de la imagen en la base de datos si es necesario
        usuarioService.actualizarFotoPerfil(id, destinationFile.getPath());

        model.put("message", "Foto de perfil actualizada exitosamente.");
    } catch (IOException e) {
        model.put("error", "Error al subir la imagen.");
    }

    // Agregar información adicional al modelo
    model.put("usuario", usuario);
    model.put("cantidadPeliculasVistas", usuario.getCantidadPeliculasVistas());
    model.put("cantidadPeliculasVistasEsteAno", usuario.getCantidadPeliculasVistasEsteAno());
    model.put("peliculasFavoritas", usuario.getPeliculasFavoritas());

    return new ModelAndView("perfil", model);
}



    @RequestMapping(path = "/seguir/{idSeguido}", method = RequestMethod.POST)
    public String seguirUsuario(@PathVariable Integer idSeguido, HttpServletRequest request) {
        Integer usuarioLogueadoId = (Integer) request.getSession().getAttribute("usuarioId");

        if (usuarioLogueadoId.equals(idSeguido)) {
            return "redirect:/error";
        }

        try {
            usuarioService.seguirUsuario(usuarioLogueadoId, idSeguido);
        } catch (Exception e) {
            return "redirect:/error";
        }
        return "redirect:/perfil/" + idSeguido;
    }

    @RequestMapping(path = "/dejarDeSeguir/{idSeguido}", method = RequestMethod.POST)
    public String dejarDeSeguirUsuario(@PathVariable Integer idSeguido, HttpServletRequest request) {
        Integer usuarioLogueadoId = (Integer) request.getSession().getAttribute("usuarioId");
        usuarioService.dejarDeSeguirUsuario(usuarioLogueadoId, idSeguido);
        return "redirect:/perfil/" + idSeguido;
    }

    @RequestMapping(path = "/favoritos", method = RequestMethod.GET)
    public ModelAndView verFavoritos(HttpServletRequest request) {
        Integer usuarioLogueadoId = (Integer) request.getSession().getAttribute("usuarioId");

        if (usuarioLogueadoId == null) {
            return new ModelAndView("redirect:/login");
        }

        PerfilDTO usuario = usuarioService.buscarPorId(usuarioLogueadoId);
        List<Movie> peliculasFavoritas = usuarioService.obtenerPeliculasFavoritas(usuario.getId());

        ModelMap model = new ModelMap();
        model.put("peliculasFavoritas", peliculasFavoritas != null ? peliculasFavoritas : Collections.emptyList());

        return new ModelAndView("favoritos", model);
    }

}



