package org.apolyon.mockito.repositories;

import org.apolyon.mockito.models.Examen;

import java.util.List;

public interface ExamenRepository {
    List<Examen> findAll();
    Examen save(Examen examen);
}
