package org.twelve.dominio;

import org.twelve.dominio.entities.Categoria;
import org.twelve.presentacion.dto.CategoriaDTO;

import java.util.List;

public interface CategoriaService {

    List<CategoriaDTO> getAll();

    CategoriaDTO convertToDTO(Categoria categoria);
}
