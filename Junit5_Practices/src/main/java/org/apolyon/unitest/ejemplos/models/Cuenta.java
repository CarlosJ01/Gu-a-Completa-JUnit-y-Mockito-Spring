package org.apolyon.unitest.ejemplos.models;

import org.apolyon.unitest.ejemplos.exceptions.DineroInsuficienteException;

import java.math.BigDecimal;

public class Cuenta {
    private String persona;
    private BigDecimal saldo;

    private Banco banco;

    public Cuenta(BigDecimal saldo, String persona) {
        this.saldo = saldo;
        this.persona = persona;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public void debito(BigDecimal monto) {
        BigDecimal nuevoSaldo = this.saldo.subtract(monto);
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0)
            throw new DineroInsuficienteException("Dinero insuficiente");
        this.saldo = nuevoSaldo;
    }

    public void credito(BigDecimal monto) {
        this.saldo = this.saldo.add(monto);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof  Cuenta))
            return false;

        Cuenta cuenta2 = (Cuenta) obj;

        if (this.persona == null || this.saldo == null)
            return false;

        return (this.persona.equals(cuenta2.getPersona()) && this.saldo.equals(cuenta2.getSaldo()));
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }
}
