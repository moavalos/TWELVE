package org.twelve.dominio;

import org.twelve.presentacion.dto.CategoriaDTO;

import java.util.List;

public interface CategoriaService {

    List<CategoriaDTO> getAll();
}
