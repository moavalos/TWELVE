package org.twelve.dominio;

import org.springframework.stereotype.Repository;
import org.twelve.dominio.entities.Pais;

import java.util.List;


@Repository
public interface PaisRepository {

    List<Pais> findAll();
}
