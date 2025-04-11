package org.apolyon.unitest.ejemplos.models;

import org.junit.jupiter.api.*;

public class InjeccionDependenciasTest {

    @BeforeEach
//    Infeccion de objetos con informacion extra del Test
//    TestInfo es diferente por test ya que es su info
    void setUp(TestInfo testInfo, TestReporter testReporter) {
        System.out.println("Ejecutando "+testInfo.getDisplayName()
                + " | " + testInfo.getTestMethod().get().getName());

        System.out.println("Etiquetas: "+testInfo.getTags());

//        testInfo -> Informacion del test
//        testReporter -> un logeer para Junit
    }

    @Test
    @Tag("Injeccion")
    void injeccion_test(TestInfo testInfo, TestReporter testReporter) {
        testReporter.publishEntry("1");
    }

    @Test
    @Tag("Dependencias")
    void injeccion_test_2(TestInfo testInfo, TestReporter testReporter) {
        testReporter.publishEntry("2");
    }

    @Test
    @Tag("Junit")
    void injeccion_test_3(TestInfo testInfo, TestReporter testReporter) {
        testReporter.publishEntry("3");
    }
}
