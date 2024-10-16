package org.twelve.dominio;

import org.twelve.presentacion.dto.PaisDTO;

import java.util.List;

public interface PaisService {

    List<PaisDTO> findAll();

}
