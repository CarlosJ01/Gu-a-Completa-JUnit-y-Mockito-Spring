package org.apolyon.mockito.services;

import org.apolyon.mockito.models.Examen;
import org.apolyon.mockito.repositories.ExamenRepositoryImpl;
import org.apolyon.mockito.repositories.PreguntaRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

// Habilitar las anotaciones y extienda el ejecutor de Junit con Mockito (Integra ambos)
@ExtendWith(MockitoExtension.class)
class ExamenServiceImplSpyTest {
    //    Crear un spy hibrido entre los reales y se puden simular sus metodos
//    Solo pueden ser clases con implementaciones
//    InjectMocks tambien funciona con spy
    @Spy
    ExamenRepositoryImpl examenRepositorySpy;
    @Spy
    PreguntaRepositoryImpl preguntaRepositorySpy;

    @InjectMocks
    private ExamenServiceImpl service;

    @Test
    void testSpy() {
//        Un spy es un hibrido entre el real y un mock. Siempre llama por defecto la funcion real
//        almenos que la simulemos con doReturn o los demas do
//        Se crean desde una clase implementada no por interfaces o clases abstractas
//        ExamenRepository examenRepositorySpy = spy(ExamenRepositoryImpl.class);
//        PreguntaRepository preguntaRepositorySpy = spy(PreguntaRepositoryImpl.class);

//        En el when con spy invoca una vez el metodo que se esta simulando cuando entra en la linea de line
//        pero en la ejecucion ya actual el mock
//        when(preguntaRepositorySpy.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

//        Para hacer simulaciones de Spy sin que se llame nunca el metodo real
//        Es al reves primero se coloca el dato a devolver
        doReturn(Datos.PREGUNTAS).when(preguntaRepositorySpy).findPreguntasByExamenId(anyLong());

//        ExamenService examenService = new ExamenServiceImpl(examenRepositorySpy, preguntaRepositorySpy);

        Examen examen = service.findExamenByNombreWithPreguntas("Matematicas");

        assertEquals(5l, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmetica"));

        verify(examenRepositorySpy).findAll();
        verify(preguntaRepositorySpy).findPreguntasByExamenId(anyLong());
    }
    
}