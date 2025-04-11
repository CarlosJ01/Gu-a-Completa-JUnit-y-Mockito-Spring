package com.apolyon3818.unitTestSpring.services;

import com.apolyon3818.unitTestSpring.data.Datos;
import com.apolyon3818.unitTestSpring.exceptions.DineroInsuficienteException;
import com.apolyon3818.unitTestSpring.models.Banco;
import com.apolyon3818.unitTestSpring.models.Cuenta;
import com.apolyon3818.unitTestSpring.repositories.BancoRepository;
import com.apolyon3818.unitTestSpring.repositories.CuentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Autoconfiguracion para montar el ambiente de spring con junit
@SpringBootTest
class CuentaServiceImplTest {

//    Como mock se crean los mock de las clases pero tambien se registran como beans de spring
//    @MockBean
    @MockBean
     CuentaRepository cuentaRepository;
//    @MockBean
    @MockBean
    BancoRepository bancoRepository;

//    Trae el bean del contenedor y al mismo tiempo injecta los mock bean como en spring normal
//    @Autowired
    @Autowired
    CuentaService service;

//    @BeforeEach
//    void setUp() {
//        cuentaRepository = mock(CuentaRepository.class);
//        bancoRepository = mock(BancoRepository.class);
//
//        service = new CuentaServiceImpl(cuentaRepository, bancoRepository);
//    }

    @Test
    void test_Transferir() {
        when(cuentaRepository.findById(1L)).thenReturn(Datos.getCuenta1());
        when(cuentaRepository.findById(2L)).thenReturn(Datos.getCuenta2());
        when(bancoRepository.findById(1L)).thenReturn(Datos.getBanco());

        BigDecimal saldoOrigen = service.revisarSaldo(1L);
        BigDecimal saldoDestino = service.revisarSaldo(2L);

        assertEquals(1000, saldoOrigen.intValue());
        assertEquals("2000", saldoDestino.toPlainString());

        service.transferir(1L, 2L, new BigDecimal(100), 1L);
        saldoOrigen = service.revisarSaldo(1L);
        saldoDestino = service.revisarSaldo(2L);

        assertEquals(900, saldoOrigen.intValue());
        assertEquals("2100", saldoDestino.toPlainString());

        int total = service.revisarTotalTransferencias(1L);
        assertEquals(1, total);

        verify(cuentaRepository, times(3)).findById(1L);
        verify(cuentaRepository, times(3)).findById(2L);



        verify(bancoRepository, times(2)).findById(1L);
        verify(bancoRepository).save(any(Banco.class));
    }
    
    @Test
    void test_Tranferir_Exception() {
        when(cuentaRepository.findById(1L)).thenReturn(Datos.getCuenta1());
        when(cuentaRepository.findById(2L)).thenReturn(Datos.getCuenta2());
        when(bancoRepository.findById(1L)).thenReturn(Datos.getBanco());

        BigDecimal saldoOrigen = service.revisarSaldo(1L);
        BigDecimal saldoDestino = service.revisarSaldo(2L);

        assertEquals(1000, saldoOrigen.intValue());
        assertEquals("2000", saldoDestino.toPlainString());

        assertThrows(DineroInsuficienteException.class, () -> {
            service.transferir(1L, 2L, new BigDecimal(1200), 1L);
        });

        saldoOrigen = service.revisarSaldo(1L);
        saldoDestino = service.revisarSaldo(2L);

        assertEquals(1000, saldoOrigen.intValue());
        assertEquals("2000", saldoDestino.toPlainString());

        int total = service.revisarTotalTransferencias(1L);
        assertEquals(0, total);

        verify(cuentaRepository, times(3)).findById(1L);
        verify(cuentaRepository, times(2)).findById(2L);
        verify(cuentaRepository, never()).save(any(Cuenta.class));

        verify(bancoRepository, times(1)).findById(1L);
        verify(bancoRepository, never()).save(any(Banco.class));

        verify(cuentaRepository, times(5)).findById(anyLong());
        verify(cuentaRepository, never()).findAll();
    }

    @Test
    void test_3() {
        when(cuentaRepository.findById(1L)).thenReturn(Datos.getCuenta1());

        Cuenta cuenta1 = service.findById(1L);
        Cuenta cuenta2 =service.findById(1L);

//        Valida que dos objetos sean iguales por el metodo equals
        assertSame(cuenta1, cuenta2);
        assertTrue(cuenta1 == cuenta2);
    }
}