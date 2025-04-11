package org.apolyon.unitest.ejemplos.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ParametrizarTest {

    //    Repite un test un numero de4 veces se puede poner un titulo a las repeticiones con la repeticion actual y
//    el total de repeticiones
    @RepeatedTest(value = 3, name = "{displayName} | #{currentRepetition} - {totalRepetitions}")
    @DisplayName("Test Debito en Cuenta Repetir")
//    Los datos del ciclo se pueden optener con la injeccion de dependencias como parametro
    void test_Debito_Cuenta(RepetitionInfo info) {

        if (info.getCurrentRepetition() == 3) {
            System.out.println("Prueba numero 3");
        }

        Cuenta cuenta = new Cuenta(new BigDecimal("1000.00"), "Yuli");
        cuenta.debito(new BigDecimal(100));

        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.00", cuenta.getSaldo().toPlainString());
    }

//    Hace un tes parametrizado
    @ParameterizedTest(name = "# {index} -> valor {0} | {argumentsWithNames}")
//    Dar los valores para los test se pueden de varias fuentes
    @ValueSource(strings = {"100", "200", "500", "1000"})
//    Podemos optener los parametros como paramertro de la funcion
    void test_Debito_Cuenta_Parametrizado(String monto) {

        Cuenta cuenta = new Cuenta(new BigDecimal("1000.00"), "Yuli");
        cuenta.debito(new BigDecimal(monto));

        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) >= 0);
    }

    @ParameterizedTest(name = "# {index} -> valor {argumentsWithNames}")
//    Datos de entrada por CSV entran como string
    @CsvSource({"1,100", "2,500", "3,1000"})
    void test_Debito_Cuenta_Parametrizado_CSV(String index, String monto) {
        System.out.println(index);
        Cuenta cuenta = new Cuenta(new BigDecimal("1000.00"), "Yuli");
        cuenta.debito(new BigDecimal(monto));

        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) >= 0);
    }

    @ParameterizedTest(name = "# {index} -> valor {0}")
//    Datos de entrada por CSV de un archivo
    @CsvFileSource(resources = "/data.csv")
    void test_Debito_Cuenta_Parametrizado_CSV_File(String monto) {
        Cuenta cuenta = new Cuenta(new BigDecimal("1000.00"), "Yuli");
        cuenta.debito(new BigDecimal(monto));

        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) >= 0);
    }

    @ParameterizedTest(name = "# {index} -> valor {0}")
//    Datos de entrada por una funcion que los genera
    @MethodSource("montoList")
    void test_Debito_Cuenta_Parametrizado_Method(String monto) {
        Cuenta cuenta = new Cuenta(new BigDecimal("1000.00"), "Yuli");
        cuenta.debito(new BigDecimal(monto));

        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) >= 0);
    }

//    El metodo que prove los datos debe ser static
    public static List<String> montoList() {
        return Arrays.asList("100", "200", "500", "1000");
    }


    @ParameterizedTest(name = "# {index} -> valor {argumentsWithNames}")
    @CsvSource({"200,100,Carlos", "2500,500,Yuli", "30000,1000,Maria"})
    void test_Debito_Cuenta_Parametrizado_CSV_2(String saldo, String monto, String nombre) {
        Cuenta cuenta = new Cuenta(new BigDecimal(saldo), nombre);
        cuenta.debito(new BigDecimal(monto));

        System.out.println(cuenta.getPersona());

        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) >= 0);
    }

}
