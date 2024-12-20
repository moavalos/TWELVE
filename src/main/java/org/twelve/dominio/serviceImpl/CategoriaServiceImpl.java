package org.twelve.dominio.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.twelve.dominio.CategoriaRepository;
import org.twelve.dominio.CategoriaService;
import org.twelve.dominio.entities.Categoria;
import org.twelve.presentacion.dto.CategoriaDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service("categoriaService")
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<CategoriaDTO> getAll() {
        List<Categoria> categorias = categoriaRepository.findAll();
        return categorias.stream()
                .map(CategoriaDTO::convertToDTO)
                .collect(Collectors.toList());
    }

}