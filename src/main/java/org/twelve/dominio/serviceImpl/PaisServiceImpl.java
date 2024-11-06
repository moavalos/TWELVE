package org.twelve.dominio.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.twelve.dominio.PaisRepository;
import org.twelve.dominio.PaisService;
import org.twelve.dominio.entities.Pais;
import org.twelve.presentacion.dto.PaisDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service("paisService")
public class PaisServiceImpl implements PaisService {

    private final PaisRepository paisRepository;

    @Autowired
    public PaisServiceImpl(PaisRepository paisRepository) {
        this.paisRepository = paisRepository;
    }

    @Override
    public List<PaisDTO> findAll() {
        List<Pais> paises = paisRepository.findAll();
        return paises.stream().map(PaisDTO::convertToDTO).collect(Collectors.toList());
    }

}
