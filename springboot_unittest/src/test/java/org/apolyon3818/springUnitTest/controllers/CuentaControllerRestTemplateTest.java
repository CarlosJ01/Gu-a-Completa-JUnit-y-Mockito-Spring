package org.apolyon3818.springUnitTest.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apolyon3818.springUnitTest.models.Cuenta;
import org.apolyon3818.springUnitTest.models.DTO.TransaccionDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("integration_trt")
class CuentaControllerRestTemplateTest {

//    Otro cliente web para REST
    @Autowired
    private TestRestTemplate client;

    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

//    private final String URL_TEST = "/api/cuentas";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    private String crearUri(String uri) {
        return "http://localhost:"+ port + "/api/cuentas" + uri;
    }

    @Test
    @Order(1)
    void test_Transferir() throws JsonProcessingException {

        TransaccionDTO dto = new TransaccionDTO();
        dto.setCuentaOrigenId(1L);
        dto.setCuentaDestinoId(2L);
        dto.setBancoId(1L);
        dto.setMonto(new BigDecimal(100));

        Map<String, Object> responseExpected = new HashMap<>();
        responseExpected.put("date", LocalDate.now().toString());
        responseExpected.put("status", "OK");
        responseExpected.put("message", "Tranferencia relaizado con exito");
        responseExpected.put("transaccion", dto);

//        Envinando una peticion POST real, (url, body, tipo_body_return.class)
        ResponseEntity<String> response = client.postForEntity(crearUri("/transferir"), dto, String.class);
//        Obtener el contenido del body
        String json = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(json);
        assertTrue(json.contains("Tranferencia relaizado con exito"));
        assertTrue(json.contains(objectMapper.writeValueAsString(responseExpected)));

//        JSON Node
        JsonNode jsonNode = objectMapper.readTree(json);
        assertEquals("Tranferencia relaizado con exito", jsonNode.path("message").asText());
        assertEquals(LocalDate.now().toString(), jsonNode.path("date").asText());
        assertEquals("100", jsonNode.path("transaccion").path("monto").asText());
        assertEquals(1L, jsonNode.path("transaccion").path("cuentaOrigenId").asLong());

        assertEquals(objectMapper.writeValueAsString(responseExpected), json);
    }

    @Test
    @Order(2)
    void test_detalle() {
//        Peticion GET  (Uri, tipo de retorno)
        ResponseEntity<Cuenta> response = client.getForEntity(crearUri("/1"), Cuenta.class);

        Cuenta cuenta = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        assertNotNull(cuenta);
        assertEquals("Bruce", cuenta.getPersona());
        assertEquals("900.00", cuenta.getSaldo().toPlainString());
//        assertEquals(new Cuenta(1L, "Bruce", new BigDecimal(900)), cuenta);
    }

    @Test
    @Order(3)
    void test_Listar() throws JsonProcessingException {
//        Peticion GET para arreglos o listas como respuestas
        ResponseEntity<Cuenta[]> response = client.getForEntity(crearUri(""), Cuenta[].class);

        List<Cuenta> cuentas = Arrays.asList(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        assertEquals(3, cuentas.size());
        assertEquals(1L, cuentas.get(0).getId());
        assertEquals("Bruce", cuentas.get(0).getPersona());
        assertEquals("900.00", cuentas.get(0).getSaldo().toPlainString());
        assertEquals(2L, cuentas.get(1).getId());
        assertEquals("Tony", cuentas.get(1).getPersona());
        assertEquals("2100.00", cuentas.get(1).getSaldo().toPlainString());

        JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(cuentas));
        assertEquals(1L, json.get(0).path("id").asLong());
        assertEquals("Bruce", json.get(0).path("persona").asText());
        assertEquals("900.0", json.get(0).path("saldo").asText());
        assertEquals(2L, json.get(1).path("id").asLong());
        assertEquals("Tony", json.get(1).path("persona").asText());
        assertEquals("2100.0", json.get(1).path("saldo").asText());

    }

    @Test
    @Order(4)
    void test_Guardar() {
        Cuenta cuenta = new Cuenta(null, "Jose", new BigDecimal(3800));

        ResponseEntity<Cuenta> response = client.postForEntity(crearUri(""), cuenta, Cuenta.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        Cuenta nuevaCuenta = response.getBody();
        assertNotNull(nuevaCuenta);
        assertEquals(4L, nuevaCuenta.getId());
        assertEquals(cuenta.getPersona(), nuevaCuenta.getPersona());
        assertEquals(cuenta.getSaldo(), nuevaCuenta.getSaldo());
    }

    @Test
    @Order(5)
    void test_Eliminar() {
        ResponseEntity<Cuenta[]> response = client.getForEntity(crearUri(""), Cuenta[].class);
        List<Cuenta> cuentas = Arrays.asList(response.getBody());
        assertEquals(4, cuentas.size());

//        Peticion DELETE
//        client.delete(crearUri("/4"));

//        Otra maneja de consruir un request
        Map<String, Long> pathVariables = new HashMap<>();
        pathVariables.put("id", 4l);
        ResponseEntity<Void> exchange = client.exchange(
                crearUri("/{id}"),
                HttpMethod.DELETE,
                null,
                Void.class,
                pathVariables);
        assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode());
        assertFalse(exchange.hasBody());


        response = client.getForEntity(crearUri(""), Cuenta[].class);
        cuentas = Arrays.asList(response.getBody());
        assertEquals(3, cuentas.size());

        ResponseEntity<Cuenta> responseCuenta = client.getForEntity(crearUri("/4"), Cuenta.class);
        assertEquals(HttpStatus.NOT_FOUND, responseCuenta.getStatusCode());
        assertFalse(responseCuenta.hasBody());
    }
}