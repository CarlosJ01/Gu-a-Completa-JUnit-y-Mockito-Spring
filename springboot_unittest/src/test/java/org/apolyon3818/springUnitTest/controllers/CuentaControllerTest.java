package org.apolyon3818.springUnitTest.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apolyon3818.springUnitTest.data.Datos;
import org.apolyon3818.springUnitTest.models.Cuenta;
import org.apolyon3818.springUnitTest.models.DTO.TransaccionDTO;
import org.apolyon3818.springUnitTest.services.CuentaService;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Implementacion para test de controller REST
@WebMvcTest(CuentaController.class)
class CuentaControllerTest {

//    Mock para probar en MVC, servidor http simulado y todo lo demas
//    Podemos simular peticiones http
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CuentaService cuentaService;

//  Para escribir y leer objetos a json
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
            objectMapper = new ObjectMapper();
    }

    @Test
    void test_Detalle() throws Exception {
//         Given
        when(cuentaService.findById(1L)).thenReturn(Datos.crearCuenta001().get());

//        When
//        Simulamos una peticion REST HTTP, con su url y el tipo de la peticion
        mvc.perform(
                MockMvcRequestBuilders.get("/api/cuentas/1").contentType(MediaType.APPLICATION_JSON))
//          Then
//       Escribimos lo que esperamos de la respuesta como tipo, status y validar el body
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.persona").value("Bruce"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.saldo").value("1000"));

        verify(cuentaService).findById(1L);
    }

    @Test
    void test_Transferir() throws Exception, JsonProcessingException {
//        Given
        TransaccionDTO dto = new TransaccionDTO();
        dto.setCuentaOrigenId(1L);
        dto.setCuentaDestinoId(2L);
        dto.setMonto(new BigDecimal(100));
        dto.setBancoId(1L);

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("message", "Tranferencia relaizado con exito");
        response.put("transaccion", dto);


//        When
        ResultActions request = mvc.perform(
                MockMvcRequestBuilders.post("/api/cuentas/transferir")
                        .contentType(MediaType.APPLICATION_JSON)
//                        objectMapper -> Para convertir objetos a json
                        .content(objectMapper.writeValueAsString(dto))
        );

//        Then
//        $.a.b para validar valores que estan por niveles
        request.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Tranferencia relaizado con exito"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transaccion.cuentaOrigenId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transaccion.cuentaDestinoId").value(2L))
//                Comparar todo el body
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void testListar() throws Exception {
//        Given
        List<Cuenta> cuentas = Arrays.asList(
                Datos.crearCuenta001().get(),
                Datos.crearCuenta002().get()
        );
        when(cuentaService.findAll()).thenReturn(cuentas);

//        When
        ResultActions request = mvc.perform(
                MockMvcRequestBuilders.get("/api/cuentas").contentType(MediaType.APPLICATION_JSON)
        );

//       Then
        request.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                Hacer validaciones con arreglos
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].persona").value("Bruce"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].persona").value("Tony"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].saldo").value("1000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].saldo").value("2000"))
//                Validar el tamaño del arreglo
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(cuentas)));
    }

    @Test
    void test_Guardar() throws Exception {
//        Given
        Cuenta cuenta = new Cuenta(null, "Peter", new BigDecimal(10));
        when(cuentaService.save(any())).then(invocation -> {
            Cuenta cuentaGuardar = invocation.getArgument(0);
            cuentaGuardar.setId(10L);
            return  cuentaGuardar;
        });

//        When
        ResultActions request = mvc.perform(
                MockMvcRequestBuilders.post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cuenta))
        );

//        Then
        request.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                Is otra forma de comparar valores
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.persona", Matchers.is("Peter")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.saldo", Matchers.is(10)));

        verify(cuentaService).save(any());
    }
}