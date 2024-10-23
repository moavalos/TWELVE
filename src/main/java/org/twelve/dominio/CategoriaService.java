package org.twelve.dominio;

import org.twelve.presentacion.dto.CategoriaDTO;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface CategoriaService {

    List<CategoriaDTO> getAll();
}
