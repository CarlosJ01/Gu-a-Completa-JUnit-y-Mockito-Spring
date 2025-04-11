package com.apolyon3818.unitTestSpring.data;

import com.apolyon3818.unitTestSpring.models.Banco;
import com.apolyon3818.unitTestSpring.models.Cuenta;

import java.math.BigDecimal;
import java.util.Optional;

public class Datos {
//    public static final Cuenta CUENTA_1 = new Cuenta(1L, "Bruce", new BigDecimal(1000));
//    public static final Cuenta CUENTA_2 = new Cuenta(1L, "Tony", new BigDecimal(2000));
//    public static final Banco BANCO = new Banco(1L, "Banco de Spring", 0);

    public static Optional<Cuenta> getCuenta1() {
        return Optional.of(new Cuenta(1L, "Bruce", new BigDecimal(1000)));
    }

    public static Optional<Cuenta> getCuenta2() {
        return Optional.of(new Cuenta(1L, "Tony", new BigDecimal(2000)));
    }

    public static Optional<Banco> getBanco() {
        return Optional.of(new Banco(1L, "Banco de Spring", 0));
    }
}
