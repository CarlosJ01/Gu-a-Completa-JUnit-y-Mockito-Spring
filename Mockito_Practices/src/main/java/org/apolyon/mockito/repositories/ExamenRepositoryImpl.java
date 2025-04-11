package org.apolyon.mockito.repositories;

import org.apolyon.mockito.models.Examen;

import java.util.List;

public class ExamenRepositoryImpl implements ExamenRepository{
    @Override
    public List<Examen> findAll() {
        System.out.println("ExamenRepositoryImpl.findAll");
        return List.of(
                new Examen(5L, "Matematicas"),
                new Examen(6L, "Lenguajes"),
                new Examen(7L, "Historia")
        );
    }

    @Override
    public Examen save(Examen examen) {
        System.out.println("ExamenRepositoryImpl.save");
        examen.setId(100L);
        return examen;
    }
}
