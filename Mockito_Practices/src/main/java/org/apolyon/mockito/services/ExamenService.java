package org.apolyon.mockito.services;

import org.apolyon.mockito.models.Examen;

import java.util.Optional;

public interface ExamenService {
    Optional<Examen> findExamenPorNombre(String nombre);
    Examen findExamenByNombreWithPreguntas(String nombre);
    Examen save(Examen examen);
}
