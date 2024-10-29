package org.twelve.dominio;

import org.twelve.dominio.entities.Pais;

import java.util.List;

public interface PaisRepository {

    List<Pais> findAll();

    Pais findPaisById(Integer id);
}
