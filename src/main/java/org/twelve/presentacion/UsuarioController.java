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

        model.put("usuario", usuario);
        model.put("cantidadPeliculasVistas", usuario.getCantidadPeliculasVistas());
        model.put("cantidadPeliculasVistasEsteAno", usuario.getCantidadPeliculasVistasEsteAno());
        model.put("peliculasFavoritas", usuario.getPeliculasFavoritas());

        return new ModelAndView("perfil", model);
    }

///

@PostMapping("/upload-profile-picture")
public ModelAndView uploadProfilePicture(@RequestParam("profilePicture") MultipartFile file,
                                         @RequestParam("userId") Integer userId,
                                         RedirectAttributes redirectAttributes) {
    ModelMap model = new ModelMap();

    // Buscar el usuario por ID y verificar que existe
    PerfilDTO usuario = usuarioService.buscarPorId(userId);
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
        usuarioService.actualizarFotoPerfil(userId, destinationFile.getPath());

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


}



