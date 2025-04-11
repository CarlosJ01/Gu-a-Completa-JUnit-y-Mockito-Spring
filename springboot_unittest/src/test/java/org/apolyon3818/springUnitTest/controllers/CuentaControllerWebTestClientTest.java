package org.apolyon3818.springUnitTest.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apolyon3818.springUnitTest.models.Cuenta;
import org.apolyon3818.springUnitTest.models.DTO.TransaccionDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static  org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;
import static  org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration_wtc")
// Para especificar el orden de los test por antoaciones
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// Levanta un servidor (la app entera) en otro puerto diferente al de la app
@SpringBootTest(webEnvironment = RANDOM_PORT)
class CuentaControllerWebTestClientTest {

//    Un cliente web para pruebas de integracion con peticiones HTTP
    @Autowired
    private WebTestClient webClient;


    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void test_Transferir() throws JsonProcessingException {
//        Given
        TransaccionDTO dto = new TransaccionDTO();
        dto.setCuentaOrigenId(1L);
        dto.setCuentaDestinoId(2L);
        dto.setBancoId(1L);
        dto.setMonto(new BigDecimal(100));

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("message", "Tranferencia relaizado con exito");
        response.put("transaccion", dto);

//        When
//        Enviamos una peticion post real -> exchange es para enviar
//        Si no se indica el host apunta hacia uno mismo
        WebTestClient.ResponseSpec request = webClient.post()
                .uri("/api/cuentas/transferir")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange();

        //        Then
//        Validar el status
        request.expectStatus().isOk();
//        Validar el body
//        En expect body se puede poner la clase que se desea convertir el body
        request.expectBody()
//                Otra forma de validar el Body
                .consumeWith(responseRequest -> {
                    try {
                        JsonNode json = objectMapper.readTree(responseRequest.getResponseBody());
                        assertEquals("Tranferencia relaizado con exito", json.path("message").asText());
                        assertEquals(1L, json.path("transaccion").path("cuentaOrigenId").asLong());
                        assertEquals(LocalDate.now().toString(), json.path("date").asText());
                        assertEquals(100, json.path("transaccion").path("monto").asInt());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .jsonPath("$.message").isNotEmpty()
                .jsonPath("$.message").value(is("Tranferencia relaizado con exito"))
                .jsonPath("$.message").value(message -> {
                   assertEquals("Tranferencia relaizado con exito", message);
                })
                .jsonPath("$.message").isEqualTo("Tranferencia relaizado con exito")
                .jsonPath("$.transaccion.cuentaOrigenId").isEqualTo(dto.getCuentaOrigenId())
                .jsonPath("$.date").isEqualTo(LocalDate.now().toString())
                .json(objectMapper.writeValueAsString(response));
    }

    @Test
    @Order(2)
    void test_Detalle_1() throws JsonProcessingException {
        WebTestClient.ResponseSpec request = webClient.get().uri("/api/cuentas/1").exchange();

        Cuenta cuenta = new Cuenta(1L, "Bruce", new BigDecimal(900));

        request.expectStatus().isOk()
//                Validar Headers
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.persona").isEqualTo("Bruce")
                .jsonPath("$.saldo").isEqualTo(900)
                .json(objectMapper.writeValueAsString(cuenta));
    }

    @Test
//    Especificamos el orden en que se ejecuta
    @Order(3)
    void test_Detalle_2() {
        WebTestClient.ResponseSpec request = webClient.get().uri("/api/cuentas/2").exchange();

        request.expectStatus().isOk()
//                Validar Headers
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
//                Conversion automatica del body a una clase (Cuenta.class)
                .expectBody(Cuenta.class).consumeWith(response -> {
                    Cuenta cuenta = response.getResponseBody();
                    assertEquals("Tony", cuenta.getPersona());
                    assertEquals("2100.00", cuenta.getSaldo().toPlainString());
                });
    }

    @Test
    @Order(4)
    void test_listar() {
        WebTestClient.ResponseSpec request = webClient.get().uri("/api/cuentas").exchange();

        request.expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
//                Validar Arreglos en el body
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].persona").isEqualTo("Bruce")
                .jsonPath("$[0].saldo").isEqualTo(900)
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[1].persona").isEqualTo("Tony")
                .jsonPath("$[1].saldo").isEqualTo(2100)
                .jsonPath("$").value(hasSize(3));
    }

    @Test
    @Order(5)
    void test_listar_v2() {
        WebTestClient.ResponseSpec request = webClient.get().uri("/api/cuentas").exchange();

        request.expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
//                Valdiar listas convirtiendo directamente a una lista
                .expectBodyList(Cuenta.class).consumeWith(response -> {
                    List<Cuenta> cuentas = response.getResponseBody();
                    assertNotNull(cuentas);
                    assertEquals(3, cuentas.size());
                    assertEquals(1l, cuentas.get(0).getId());
                    assertEquals("Bruce", cuentas.get(0).getPersona());
                    assertEquals("900.0", cuentas.get(0).getSaldo().toPlainString());
                    assertEquals(2l, cuentas.get(1).getId());
                    assertEquals("Tony", cuentas.get(1).getPersona());
                    assertEquals("2100.0", cuentas.get(1).getSaldo().toPlainString());
                })
                .hasSize(3)
                .value(hasSize(3));
    }

    @Test
    @Order(6)
    void test_Guardar() {
//        Given
        Cuenta cuenta = new Cuenta(null, "Peter", new BigDecimal(3000));

//        When
        WebTestClient.ResponseSpec response = webClient.post()
                .uri("/api/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cuenta)
                .exchange();

//        Then
        response.expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(4)
                .jsonPath("$.persona").isEqualTo("Peter")
                .jsonPath("$.persona").value(is("Peter"))
                .jsonPath("$.saldo").isEqualTo(3000);
    }

    @Test
    @Order(7)
    void test_Guardar_v2() {
//        Given
        Cuenta cuenta = new Cuenta(null, "Tyrion", new BigDecimal(50000));

//        When
        WebTestClient.ResponseSpec response = webClient.post()
                .uri("/api/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cuenta)
                .exchange();

//        Then
        response.expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Cuenta.class).consumeWith(res -> {
                    Cuenta cuentaResponse = res.getResponseBody();
                    assertNotNull(cuentaResponse);
                    assertEquals(5, cuentaResponse.getId());
                    assertEquals(cuenta.getPersona(), cuentaResponse.getPersona());
                    assertEquals(cuenta.getSaldo().toPlainString(), cuentaResponse.getSaldo().toPlainString());
                });
    }

    @Test
    @Order(8)
    void test_Eliminar() {

        webClient.get().uri("/api/cuentas").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cuenta.class)
                .hasSize(5);

        WebTestClient.ResponseSpec response = webClient.delete().uri("/api/cuentas/3").exchange();
        response.expectStatus().isNoContent()
                .expectBody().isEmpty();

        webClient.get().uri("/api/cuentas").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cuenta.class)
                .hasSize(4);

//        webClient.get().uri("/api/cuentas/3").exchange()
//                .expectStatus().is5xxServerError();

        webClient.get().uri("/api/cuentas/3").exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty();
    }
}