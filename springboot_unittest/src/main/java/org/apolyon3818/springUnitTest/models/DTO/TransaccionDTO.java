package org.apolyon3818.springUnitTest.models.DTO;

import java.math.BigDecimal;

public class TransaccionDTO {
        private Long cuentaOrigenId;
        private Long cuentaDestinoId;
        private BigDecimal monto;
        private Long bancoId;

        public Long getCuentaOrigenId() {
            return cuentaOrigenId;
        }

        public void setCuentaOrigenId(Long cuentaOrigenId) {
            this.cuentaOrigenId = cuentaOrigenId;
        }

        public Long getCuentaDestinoId() {
            return cuentaDestinoId;
        }

        public void setCuentaDestinoId(Long cuentaDestinoId) {
            this.cuentaDestinoId = cuentaDestinoId;
        }

        public BigDecimal getMonto() {
            return monto;
        }

        public void setMonto(BigDecimal monto) {
            this.monto = monto;
        }

    public Long getBancoId() {
        return bancoId;
    }

    public void setBancoId(Long bancoId) {
        this.bancoId = bancoId;
    }
}
