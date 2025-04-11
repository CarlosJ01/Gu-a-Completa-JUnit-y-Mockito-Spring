package org.apolyon.unitest.ejemplos.models;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

import java.util.Map;
import java.util.Properties;

public class CondicionalesTest {


    //    Test Condicionales
    @Test
//    El test se ejecutara dependiendo del SO o se ignorara
    @EnabledOnOs(OS.WINDOWS)
    void testSoloWindows() {
    }

    @EnabledOnOs({OS.LINUX, OS.MAC})
    void testSoloLinux() {
    }

    //    El test solo se ejecuta si no esta en un SO
    @DisabledOnOs(OS.WINDOWS)
    void testNoWindows() {
    }

    @Test
//    Ejecutar dependiendo de la version de JAVA
    @EnabledOnJre(JRE.JAVA_8)
    void soloJDK8(){
    }

    @EnabledOnJre(JRE.JAVA_14)
    void soloJDK14(){
    }

    @Test
//    Lo ejecuta si no es una version de JAVA
    @DisabledOnJre(JRE.JAVA_10)
    void siNoEsJava10() {
    }

    @Test
    @Disabled
    void imprimirSystemProperties() {
        Properties properties = System.getProperties();
        properties.forEach((key, value) -> System.out.println(key+" : "+value));
    }

    @Test
//    Ejecuta el test si existe una propiedad con su valor o con una expresion regular
    @EnabledIfSystemProperty(named = "java.specification.version", matches = "14")
    void  testJavaVersion(){
    }

    @Test
    @EnabledIfSystemProperty(named = "sun.cpu.isalist", matches = ".*64.*")
    void  testSolo64(){
    }

    @Test
//    Desactiva si se cumple una condicion
    @DisabledIfSystemProperty(named = "sun.cpu.isalist", matches = ".*64.*")
    void  testNoSolo64(){
    }

    @Test
    @Disabled
    void imprimirVariblesAmbiente() {
        Map<String, String> variables = System.getenv();
        variables.forEach((k, v ) -> System.out.println(k+" = "+v));
    }

    @Test
//      Ejecuta si entuentra un match con una varible de ambiente
    @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk-14.0.2")
    void testJavaHome() {
    }

    @Test
//      Desactiva el test si entuentra un match con una varible de ambiente
    @DisabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk-14.0.1")
    void testNotJavaHome() {
    }

}
