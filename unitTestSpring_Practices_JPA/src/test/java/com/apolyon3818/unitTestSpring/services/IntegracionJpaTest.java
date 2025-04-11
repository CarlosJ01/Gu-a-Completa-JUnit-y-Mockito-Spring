package com.apolyon3818.unitTestSpring.services;

import com.apolyon3818.unitTestSpring.models.Cuenta;
import com.apolyon3818.unitTestSpring.repositories.CuentaRepository;
import static  org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

// Pruebas de integracion con JPA, spring con juinit y DB
// Tambien por cada prueba unitaria limpia la tabla
// Creando pruebas unitarias sin dependencias entre ellos
@DataJpaTest
public class IntegracionJpaTest {

    @Autowired
    CuentaRepository cuentaRepository;

    @Test
    void testFindById() {
        Optional<Cuenta> cuenta = cuentaRepository.findById(1L);

        assertTrue(cuenta.isPresent());
        assertEquals("Bruce", cuenta.orElseThrow().getNombre());
    }

    @Test
    void testFindByNombre() {
        Optional<Cuenta> cuenta = cuentaRepository.findByNombre("Bruce");
        assertTrue(cuenta.isPresent());
        assertEquals("Bruce", cuenta.get().getNombre());
        assertEquals("1000.00", cuenta.get().getSaldo().toPlainString());
    }

    @Test
    void testFindByNombreException() {
        Optional<Cuenta> cuenta = cuentaRepository.findByNombre("Peter");
        assertThrows(NoSuchElementException.class, cuenta::orElseThrow);
        assertFalse(cuenta.isPresent());
    }

    @Test
    void testFindAll() {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        System.out.println(cuentas.size());

        assertFalse(cuentas.isEmpty());
        assertEquals(3, cuentas.size());
    }

    @Test
    void testSave() {
        Cuenta cuenta = new Cuenta(null, "Peter", new BigDecimal(1));
        cuentaRepository.save(cuenta);

        Cuenta cuentaPeter = cuentaRepository.findByNombre("Peter").orElseThrow();
        assertEquals("Peter", cuentaPeter.getNombre());
        assertEquals("1", cuentaPeter.getSaldo().toPlainString());
    }

    @Test
    void testUpdate() {
        Cuenta cuenta = new Cuenta(null, "Peter", new BigDecimal(1));

        cuenta = cuentaRepository.save(cuenta);

        assertEquals("Peter", cuenta.getNombre());
        assertEquals("1", cuenta.getSaldo().toPlainString());

        cuenta.setSaldo(new BigDecimal("1000"));
        cuenta = cuentaRepository.save(cuenta);

        assertEquals("1000", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testDelete() {
        Cuenta cuenta = cuentaRepository.findById(2L).orElseThrow();
        assertEquals("Tony", cuenta.getNombre());

        cuentaRepository.delete(cuenta);

        assertThrows(NoSuchElementException.class, () -> {
            cuentaRepository.findById(cuenta.getId()).orElseThrow();
        });
    }
}
