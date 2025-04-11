package org.apolyon.mockito.services;

import org.apolyon.mockito.models.Examen;

import java.util.Arrays;
import java.util.List;

public class Datos {

    public final static  List<Examen> EXAMENES = List.of(
            new Examen(5L, "Matematicas"),
            new Examen(6L, "Lenguajes"),
            new Examen(7L, "Historia")
    );

    public final static  List<Examen> EXAMENES_ID_NULL = List.of(
            new Examen(null, "Matematicas"),
            new Examen(null, "Lenguajes"),
            new Examen(null, "Historia")
    );

    public final static  List<Examen> EXAMENES_ID_NEGATIVOS = List.of(
            new Examen(-5L, "Matematicas"),
            new Examen(-6L, "Lenguajes"),
            new Examen(-7L, "Historia")
    );

    public final static List<String> PREGUNTAS = Arrays.asList(
            "aritmetica",
            "integrales",
            "derivadas",
            "trigonometria",
            "geometria"
    );

    public final static Examen EXAMEN = new Examen(null, "Programacion");
}
