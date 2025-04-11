package org.apolyon.mockito.repositories;

import org.apolyon.mockito.data.Datos;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PreguntaRepositoryImpl implements PreguntaRepository{
    @Override
    public List<String> findPreguntasByExamenId(Long id) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("PreguntaRepositoryImpl.findPreguntasByExamenId");
        return Datos.PREGUNTAS;
    }

    @Override
    public void saveMany(List<String> preguntas) {
        System.out.println("PreguntaRepositoryImpl.saveMany");
    }
}
