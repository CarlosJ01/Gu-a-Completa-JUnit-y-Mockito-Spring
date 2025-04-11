package org.apolyon.unitest.ejemplos.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

public class AssumptionsTest {

    @Test
    @DisplayName("Solo si esta en DEV con Assumptions")
    void test_saldo_cuenta_DEV() {
        boolean isDEV = "DEV".equals(System.getProperty("ENV"));
//        Como los Assets pero estos desactivan la prueba si no pasan la validation, como los @Enable
        assumeTrue(isDEV);
        Cuenta cuenta = new Cuenta(new BigDecimal("1000.12345"), "Andres");
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Solo si esta en DEV con Assumptions por Partes")
    void test_saldo_cuenta_DEV2() {
        boolean isDEV = "DEV".equals(System.getProperty("ENV1"));

        Cuenta cuenta = new Cuenta(new BigDecimal("1000.12345"), "Andres");
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());

//        Si pasa la validacion ejecuta la prueba en la funcion lambda si no la omite
        assumingThat(isDEV, () -> {
            System.out.println("Entro en el assumptions");
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        });

    }

}
