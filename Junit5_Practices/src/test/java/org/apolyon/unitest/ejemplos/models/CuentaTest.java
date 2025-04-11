package org.apolyon.unitest.ejemplos.models;

import org.apolyon.unitest.ejemplos.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

//Cambiar el ciclo de vida de la clase por defecto es por metodo, en clase usa una sola instancia de la clase
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuentaTest {

    Cuenta cuenta;

//    Metodo que se ejectura antes de cada prueba
    @BeforeEach
    void initMetodoTest(){
        System.out.println("Iniciando el metodo.");
//        Un segmento de codigo que es comun entre todos pero siempre se tiene que limpiar
        this.cuenta = new Cuenta(new BigDecimal("1000.00"), "Carlos");
    }

//    Metodo que se ejecuta después de cada prueba
    @AfterEach
    void tearDown() {
        System.out.println("Terminando metodo");
    }

//    Se ejecuta antes de todas las pruebas y antes de la instancia de la clase
//    Siempre es estatico
    @BeforeAll
    static void beforeAll() {
        System.out.println("Iniciando el Test de Cuentas");
    }

//    Se eejcuta al final de todas las pruebas y después de destruir la instancia
//    Siempre es estatico
    @AfterAll
    static void afterAll() {
        System.out.println("Terminando el Test de Cuentas");
    }

    //    Selecciona un metodo como un test
    @Test
//    Display name para darle un nombre personalizado en cada metodo y en el reporte mostrarlo
    @DisplayName("Probando nombre de la cuenta")
    void test_Nombre_Cuenta() {
        Cuenta cuenta = new Cuenta(new BigDecimal("100.234"), "Carlos");

//        cuenta.setPersona("Carlos");
        String valorEsperado = "Carlos";
        String valorReal = cuenta.getPersona();

//        Verifica que dos valores sean iguales
        assertEquals(valorEsperado, valorReal);
//       Valida que es un valor true
        assertTrue(valorReal.equals("Carlos"));
    }

    @Test
    @DisplayName("Testiando el saldo de la cuenta 😊😊😊😂")
    void test_saldo_cuenta() {
        Cuenta cuenta = new Cuenta(new BigDecimal("1000.12345"), "Andres");
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
//        Valida que una condicion sea falsa
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);

        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void test_Referencia_Cuenta() {
        Cuenta cuenta1 = new Cuenta(new BigDecimal("100.00"), "John Snow");
        Cuenta cuenta2 = new Cuenta(new BigDecimal("100.00"), "John Snow");

//        Compara que dos objetos no son iguales !=
//        assertNotEquals(cuenta1, cuenta2);

        assertEquals(cuenta1, cuenta2);
    }

    @Test
    void test_Debito_Cuenta() {
        Cuenta cuenta = new Cuenta(new BigDecimal("1000.00"), "Yuli");
        cuenta.debito(new BigDecimal(100));

//      Validar que no sea un valor null
        assertNotNull(cuenta.getSaldo());

        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.00", cuenta.getSaldo().toPlainString());
    }

    @Test
    void test_Crebito_Cuenta() {
        Cuenta cuenta = new Cuenta(new BigDecimal("1000.00"), "Yuli");
        cuenta.credito(new BigDecimal(100));

//        Segundo o tercer argumento es un mensaje personalizado del error
        assertNotNull(cuenta.getSaldo(), "El saldo no puede ser null");
        assertEquals(1100, cuenta.getSaldo().intValue(), "El saldo no sumo el monto");
//        Tambien se pueden usar lambdas para crear el mensaje y ahorrar recursos de memoria y procesesamiento
        assertEquals("1100.00", cuenta.getSaldo().toPlainString(), () -> "El saldo en string no coincide");
    }

    @Test
    void testDineroInsuficienteException() {
        Cuenta cuenta = new Cuenta(new BigDecimal("1000.00"), "Eren");

//        Valida que una funcion landa regrese la exception que se le indico
//        podemos recuperar el objeto de la exception
        Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal(1000.01));
        });

        String actual = exception.getMessage();
        String esperado = "Dinero insuficiente";
        assertEquals(esperado, actual);
    }

    @Test
    void test_TransferirDineroCuentas() {
        Cuenta cuentaOrigen = new Cuenta(new BigDecimal("1000.00"), "Bruce Wane");
        Cuenta cuentaDestino = new Cuenta(new BigDecimal("1000.00"), "Tony Stark");
        Banco banco = new Banco();
        banco.setNombre("Gothan Bank");

        banco.transferir(cuentaOrigen, cuentaDestino, new BigDecimal(500));

        assertEquals("500.00", cuentaOrigen.getSaldo().toPlainString());
        assertEquals(1500.00, cuentaDestino.getSaldo().floatValue());
    }

    @Test
    @DisplayName("Probando relaciones entre Bancos y Cuentas con Assert All")
    //    Desactiva una prueba para que no se evalue
//    @Disabled
    void test_Relacion_Banco_Cuentas() {
//        Inducimos a que la prueba falle
//        fail();
        Cuenta cuentaOrigen = new Cuenta(new BigDecimal("1000.00"), "Bruce Wane");
        Cuenta cuentaDestino = new Cuenta(new BigDecimal("1000.00"), "Tony Stark");

        Banco banco = new Banco();
        banco.setNombre("Gothan Bank");
        banco.addCuenta(cuentaOrigen);
        banco.addCuenta(cuentaDestino);

//        Evalua todos los assert, si uno o mas fallan, muestra todas las fallas en el reporte
        assertAll(
                () -> assertEquals(2, banco.getCuentas().size()),
                () -> assertEquals("Gothan Bank", cuentaOrigen.getBanco().getNombre()),
                () -> assertTrue(banco.getCuentas().stream().anyMatch(cuenta -> cuenta.getPersona().equals("Bruce Wane"))),
                () -> assertEquals("Bruce Wane", banco.getCuentas().stream().filter(cuenta -> cuenta.getPersona().equals("Bruce Wane")).findFirst().get().getPersona())
        );

    }
}