package org.apolyon.unitest.ejemplos.models;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class TimeOutTest {

    @Test
//    Despues de un tiempo en segundos falla
    @Timeout(1)
    void timeout_test() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
    }

    @Test
//    Se puede cambiar la unidad de segundos a la que se necesite
    @Timeout(value = 5000, unit = TimeUnit.MILLISECONDS)
    void timeout_test2() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    void test_tiemout_Assertions() {
//        Valida que la funcion no dure mas de lo indicado
        assertTimeout(Duration.ofSeconds(5), () -> {
            TimeUnit.MILLISECONDS.sleep(500);
        });
    }
}
