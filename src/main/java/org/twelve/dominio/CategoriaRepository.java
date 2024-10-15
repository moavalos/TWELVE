package org.twelve.dominio;

import org.twelve.dominio.entities.Categoria;
import org.twelve.dominio.entities.Movie;

import java.util.List;

public interface CategoriaRepository {

    List<Categoria> findAll();

    Categoria save(Categoria categoria);
}
