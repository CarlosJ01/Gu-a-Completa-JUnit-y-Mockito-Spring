package org.apolyon.unitest.ejemplos.models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

public class NestedTest {

//    Son Inner Class en los que se agrupan los test por categorias
//    Estos entraran como subgrupos en el test grande del informe
    @Nested
//    Puedes colocar nombre al conjunto
    @DisplayName("Test por SO")
    class SistemasOperativosTest {

//        Puenden usar los eventos Each pero no los ALl
        @BeforeEach
        void setUp() {
            System.out.println("Inicio");
        }

        @AfterEach
        void afterAll() {
            System.out.println("Fin");
        }

        @Test
        @EnabledOnOs(OS.WINDOWS)
        @DisplayName("Test solo para windows")
        void testSoloWindows() {
        }

        @EnabledOnOs({OS.LINUX, OS.MAC})
        void testSoloLinux() {
        }

        //    El test solo se ejecuta si no esta en un SO
        @DisabledOnOs(OS.WINDOWS)
        void testNoWindows() {
        }

    }

    @Nested
    class JRETest {
        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void soloJDK8(){
        }

        @EnabledOnJre(JRE.JAVA_14)
        void soloJDK14(){
        }

        @Test
        @DisabledOnJre(JRE.JAVA_10)
        void siNoEsJava10() {
        }
    }
}
