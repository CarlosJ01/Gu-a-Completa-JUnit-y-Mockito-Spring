package org.apolyon3818.springUnitTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.apolyon3818.springUnitTest.data.Datos.*;

import org.apolyon3818.springUnitTest.exceptions.DineroInsuficienteException;
import org.apolyon3818.springUnitTest.models.Banco;
import org.apolyon3818.springUnitTest.models.Cuenta;
import org.apolyon3818.springUnitTest.repositories.BancoRepository;
import org.apolyon3818.springUnitTest.repositories.CuentaRepository;
import org.apolyon3818.springUnitTest.services.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

@SpringBootTest
class SpringbootTestApplicationTests {

	@Test
	void contextLoads() {
	}
}
