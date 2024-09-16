package org.twelve.dominio;

import org.twelve.dominio.entities.Categoria;

import java.util.List;

public interface CategoriaRepository {

    List<Categoria> findAll();
}
