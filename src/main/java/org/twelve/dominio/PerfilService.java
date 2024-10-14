package org.twelve.dominio;

import org.springframework.stereotype.Service;
import org.twelve.presentacion.dto.PerfilDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class PerfilService {

    private List<PerfilDTO> perfiles = new ArrayList<>();
    private long nextId = 1; // Simulación de ID autoincrementable

    public List<PerfilDTO> getAll() {
        return perfiles;
    }

    public PerfilDTO getById(Long id) {
        return perfiles.stream()
                .filter(perfil -> perfil.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public PerfilDTO create(PerfilDTO perfil) {
        perfil.setId(nextId++);
        perfiles.add(perfil);
        return perfil;
    }

    public PerfilDTO update(PerfilDTO perfil) {
        long index = perfiles.indexOf(getById(perfil.getId()));
        if (index >= 0) {
            perfiles.set((int) index, perfil);
            return perfil;
        }
        return null;
    }

    public List<PerfilDTO> searchByNombre(String nombre) {
        List<PerfilDTO> result = new ArrayList<>();
        for (PerfilDTO perfil : perfiles) {
            if (perfil.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                result.add(perfil);
            }
        }
        return result;
    }
}
