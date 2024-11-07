package org.twelve.dominio;

import org.twelve.presentacion.dto.PaisDTO;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface PaisService {

    List<PaisDTO> findAll();
    PaisDTO findById(Integer id);

}
