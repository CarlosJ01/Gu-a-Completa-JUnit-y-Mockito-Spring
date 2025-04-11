package org.apolyon.mockito.repositories;

import java.util.List;

public interface PreguntaRepository {
    List<String> findPreguntasByExamenId(Long id);
    void saveMany(List<String> preguntas);
}
