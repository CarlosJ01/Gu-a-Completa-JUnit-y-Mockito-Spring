package org.apolyon3818.springUnitTest.services;

import org.apolyon3818.springUnitTest.models.Cuenta;

import java.math.BigDecimal;
import java.util.List;

public interface CuentaService {
    List<Cuenta> findAll();

    Cuenta findById(Long id);

    int revisarTotalTransferencias(Long bancoId);

    BigDecimal revisarSaldo(Long cuentaId);

    void transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto,
                    Long bancoId);

    Cuenta save(Cuenta cuenta);

    void deleteById(Long id);
}
