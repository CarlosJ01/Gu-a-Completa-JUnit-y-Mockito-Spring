package org.apolyon.mockito.services;

import org.apolyon.mockito.models.Examen;
import org.apolyon.mockito.repositories.ExamenRepository;
import org.apolyon.mockito.repositories.ExamenRepositoryImpl;
import org.apolyon.mockito.repositories.PreguntaRepository;
import org.apolyon.mockito.repositories.PreguntaRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessorKt;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import javax.swing.text.html.Option;
import javax.xml.crypto.Data;

import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

// Habilitar las anotaciones y extienda el ejecutor de Junit con Mockito (Integra ambos)
@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {

    //    Injecta un mock de la clase que se desea, como hacer la instancia con mock()
//    Se pueden generar de interfaces o clases implementadas
    @Mock
    private ExamenRepositoryImpl repository;
    @Mock
    private PreguntaRepositoryImpl preguntaRepository;

    //    Injecta los mock en el constructor del objeto que se requieren, como injectar los mock en el constructor
//    Si es interface se necesita que sea la clase que lo implementa
    @InjectMocks
    private ExamenServiceImpl service;

    //    Genera un Argument Captor del tipo deseado
    @Captor
    private ArgumentCaptor<Long> captor;

//    @BeforeEach
//    void setUp() {
////        Habilitamos el uso de anotaciones
//        MockitoAnnotations.openMocks(this);
    ////        Creando un Mock de la interface Repository
    ////        repository = mock(ExamenRepository.class);
    ////        preguntaRepository = mock(PreguntaRepository.class);
    ////        service = new ExamenServiceImpl(repository, preguntaRepository);
//    }

    @Test
    void findExamenPorNombre() {
//      Simulamos que cuando se llame una funcion entonces colocamos el resultado
        when(repository.findAll()).thenReturn(Datos.EXAMENES);

        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");

        assertTrue(examen.isPresent());
        assertEquals(5L, examen.orElseThrow().getId());
        assertEquals("Matematicas", examen.get().getNombre());
    }

    @Test
    void findExamenPorNombre_VoidList() {
        when(repository.findAll())
//                Colocamos el objeto o valor que queremos que regrese
                .thenReturn(Collections.emptyList());

        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");

        assertFalse(examen.isPresent());
    }

    @Test
    void testPreguntasExamen() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
//        anyLong() -> Es para sustitur los parametros de entrada de un metodo simulado asegurandonos que hace
//        hace match con un tipo de dato hay para los diferentes tipos
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.findExamenByNombreWithPreguntas("Matematicas");

        assertNotNull(examen);
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("integrales"));
        assertFalse(examen.getPreguntas().contains("historia del pais"));
    }

    @Test
    void testPreguntasExamen_Verify() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.findExamenByNombreWithPreguntas("Matematicas");

//        assertNotNull(examen);
//        assertEquals(5, examen.getPreguntas().size());
//        assertTrue(examen.getPreguntas().contains("integrales"));
//        assertFalse(examen.getPreguntas().contains("historia del pais"));

//        Verifica que en objeto mock se alla invocado el metodo que se desea con los parametros que se desean
//        Si no se verifica que se llamaron a los metodos entoces falla la prueba
        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasByExamenId(5L);
    }

    @Test
    void testNoExisteExamen_Verify() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
//        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.findExamenByNombreWithPreguntas("Matematicas");

        assertNull(examen);

        verify(repository).findAll();
//        verify(preguntaRepository).findPreguntasByExamenId(anyLong());
    }

    @Test
    void guardarExamen() {
//        GIVEN
        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);

//        any para cualquier objeto de una clase
        when(repository.save(any(Examen.class)))
//              Se peude usar then para programar una respuesta aqui se usaria una clase anonima de Anwser
                .then(new Answer<Examen>() {
                    Long secuencia = 100L;

                    //                    En el metodo answer podemos optener los parametros del mock en InvocationOnMock
                    @Override
                    public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
//                        Optener el primer parametro
                        Examen examenAnswer = invocationOnMock.getArgument(0);
                        examenAnswer.setId(secuencia++);
                        return examenAnswer;
                    }
                });

//        WHEN
        examen = service.save(examen);

//        THEN
        assertNotNull(examen);
        assertEquals(100L, examen.getId());
        assertEquals("Programacion", examen.getNombre());

        verify(repository).save(any(Examen.class));
//        Any para una lista
        verify(preguntaRepository).saveMany(anyList());
    }

    @Test
    void test_Exceptions() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES_ID_NULL);
//        isNull() como un any pero para validar que es null
        when(preguntaRepository.findPreguntasByExamenId(isNull()))
//                Con thenThrow simulamos que lanza una exception
                .thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> service.findExamenByNombreWithPreguntas("Matematicas"));

        assertEquals(IllegalArgumentException.class, exception.getClass());
        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasByExamenId(isNull());
    }

    @Test
    void testArgumentMatchers() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        service.findExamenByNombreWithPreguntas("Matematicas");

        verify(repository).findAll();
//        argThat -> podemos hacer una implementacion personalizada para los matchers de los parametros
//        necesita una expresion lambda que regrese un boolean
        verify(preguntaRepository).findPreguntasByExamenId(argThat( arg -> arg.equals(5l) ));
        verify(preguntaRepository).findPreguntasByExamenId(argThat( arg -> arg != null && arg >= 5l ));
//        Equals un matcher para comparar objetos como string.equals
        verify(preguntaRepository).findPreguntasByExamenId(eq(5l));
    }

    //    Clase para crear un matcher mas complejo
    public static class MiArgsMatchers implements ArgumentMatcher<Long> {

        private Long argument;

        //        Matcher personalizado
        @Override
        public boolean matches(Long argument) {
            this.argument = argument;
            return argument != null && argument > 0;
        }

        //        Mensaje personalizado
        @Override
        public String toString() {
            return "Es para un mensaje personalizado de erro " +
                    "que imprime mockito en caso de fallo del test " +
                    +argument+"debe ser un entero positivo";
        }
    }

    @Test
    void testArgumentMatchers_PersonalizadoClass() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        service.findExamenByNombreWithPreguntas("Matematicas");

        verify(repository).findAll();
//        Tambien podemos pasar una implementacion de ArgumentMatcher
        verify(preguntaRepository).findPreguntasByExamenId(argThat(new MiArgsMatchers()));
    }

    @Test
    void testArgumentCaptor() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        service.findExamenByNombreWithPreguntas("Matematicas");

//        Objeto generico para capturar los argumentos de entrada, el tipo generico debe ser el mismo que el del
//        dato que se decea
//        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
//        capture -> para mandar a capturar el valor como argumento del metodo que se va a validar
        verify(preguntaRepository).findPreguntasByExamenId(captor.capture());

//        getValue -> para optener el valor
        assertEquals(5L, captor.getValue());
    }

    @Test
    void testDoThrow() {
//        When no funciona para void como regreso
//        Para funciones void es al rebes se usa do y luego when y la funcion con sus matches
        doThrow(IllegalArgumentException.class).when(preguntaRepository).saveMany(anyList());

        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);

        assertThrows(IllegalArgumentException.class, ()-> {
            service.save(examen);
        });
    }

    @Test
    void testDoAnswer() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);

//        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

//        Eventos mas personalizados para los return es como then pero al revez
        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return id == 5l ? Datos.PREGUNTAS : Collections.emptyList();
        }).when(preguntaRepository).findPreguntasByExamenId(anyLong());


        Examen examen = service.findExamenByNombreWithPreguntas("Lenguaje");

        assertEquals(0, examen.getPreguntas().size());
        assertEquals(6l, examen.getId());
        assertEquals("Lenguajes", examen.getNombre());

        verify(preguntaRepository).findPreguntasByExamenId(anyLong());
    }

    @Test
    void testDoCallRealMethod() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
//        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

//        Llama el metodo real cuando se invoca una funcion o se lanza un evento como los demas Do
//        No funciona con interfaces si no deben de ser de la implementacion de la interface
        doCallRealMethod().when(preguntaRepository).findPreguntasByExamenId(anyLong());
        Examen examen = service.findExamenByNombreWithPreguntas("Matematicas");

        assertEquals(5l, examen.getId());
        assertEquals("Matematicas", examen.getNombre());

    }

    @Test
    void testSpy() {
//        Un spy es un hibrido entre el real y un mock. Siempre llama por defecto la funcion real
//        almenos que la simulemos con doReturn o los demas do
//        Se crean desde una clase implementada no por interfaces o clases abstractas
        ExamenRepository examenRepositorySpy = spy(ExamenRepositoryImpl.class);
        PreguntaRepository preguntaRepositorySpy = spy(PreguntaRepositoryImpl.class);

//        En el when con spy invoca una vez el metodo que se esta simulando cuando entra en la linea de line
//        pero en la ejecucion ya actual el mock
//        when(preguntaRepositorySpy.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

//        Para hacer simulaciones de Spy sin que se llame nunca el metodo real
//        Es al reves primero se coloca el dato a devolver
        doReturn(Datos.PREGUNTAS).when(preguntaRepositorySpy).findPreguntasByExamenId(anyLong());

        ExamenService examenService = new ExamenServiceImpl(examenRepositorySpy, preguntaRepositorySpy);

        Examen examen = examenService.findExamenByNombreWithPreguntas("Matematicas");

        assertEquals(5l, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmetica"));

        verify(examenRepositorySpy).findAll();
        verify(preguntaRepositorySpy).findPreguntasByExamenId(anyLong());
    }

    @Test
    void testOrdenDeInvocaciones() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);

        service.findExamenByNombreWithPreguntas("Matematicas");
        service.findExamenByNombreWithPreguntas("Lenguaje");

//        inOrder para validar el orden de invocacion de un metodo colocando
//        el mock y su metodo con sus parametros, puede ser uno o mas
//        El orden en que se verifican es el que se esta validando en la logica de la pieza a testiar
        InOrder inOrder = inOrder(repository, preguntaRepository);
        inOrder.verify(repository).findAll();
        inOrder.verify(preguntaRepository).findPreguntasByExamenId(5L);
        inOrder.verify(repository).findAll();
        inOrder.verify(preguntaRepository).findPreguntasByExamenId(6L);
    }

    @Test
    void testNumeroInvocaciones() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);

        service.findExamenByNombreWithPreguntas("Matematicas");

//        Segundo parametro es el numero de veces que se espera que se llamo el metodo
//        por default es 1
        verify(preguntaRepository).findPreguntasByExamenId(5L);
        verify(preguntaRepository, times(1)).findPreguntasByExamenId(5L);

//        Se puede especificar al menos cuantas veces se invoca
        verify(preguntaRepository, atLeastOnce()).findPreguntasByExamenId(5L);
        verify(preguntaRepository, atLeast(1)).findPreguntasByExamenId(5L);

//        Tambien se puede verificar a lo mas cuantas veces
        verify(preguntaRepository, atMostOnce()).findPreguntasByExamenId(5L);
        verify(preguntaRepository, atMost(1)).findPreguntasByExamenId(5L);
    }

    @Test
    void testNumeroInvocaciones3Times() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        service.findExamenByNombreWithPreguntas("Matematicas");

//        never para validar que nuna paso por el metodo
        verify(preguntaRepository, never()).findPreguntasByExamenId(anyLong());

//        Verificar que nunca se uso algun metodo del mock
        verifyNoInteractions(preguntaRepository);
    }
}