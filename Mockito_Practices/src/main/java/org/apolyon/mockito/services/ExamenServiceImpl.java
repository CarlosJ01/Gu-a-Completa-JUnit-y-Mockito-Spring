package org.apolyon.mockito.services;

import org.apolyon.mockito.models.Examen;
import org.apolyon.mockito.repositories.ExamenRepository;
import org.apolyon.mockito.repositories.PreguntaRepository;

import java.util.List;
import java.util.Optional;

public class ExamenServiceImpl implements  ExamenService{

    private ExamenRepository examenRepository;
    private PreguntaRepository preguntaRepository;

    public ExamenServiceImpl(ExamenRepository repository, PreguntaRepository preguntaRepository) {
        this.examenRepository = repository;
        this.preguntaRepository = preguntaRepository;
    }

    @Override
    public Optional<Examen> findExamenPorNombre(String nombre) {
        return examenRepository.findAll()
                .stream()
                .filter(e -> e.getNombre().contains(nombre))
                .findFirst();
    }

    @Override
    public Examen findExamenByNombreWithPreguntas(String nombre) {
        Optional<Examen> examenOptional = findExamenPorNombre(nombre);
        Examen examen = null;
        if (examenOptional.isPresent()){
            examen = examenOptional.get();
            List<String> preguntasByExamen = preguntaRepository.findPreguntasByExamenId(examen.getId());
            examen.setPreguntas(preguntasByExamen);
        }
        return examen;
    }

    @Override
    public Examen save(Examen examen) {
        if (!examen.getPreguntas().isEmpty()){
            preguntaRepository.saveMany(examen.getPreguntas());
        }
        return examenRepository.save(examen);
    }

}
