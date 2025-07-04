package org.apolyon3818.springUnitTest.repositories;

import org.apolyon3818.springUnitTest.models.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    @Query("select c from Cuenta c where c.persona=?1")
    Optional<Cuenta> findByPersona(String persona);

//    List<Cuenta> findAll();
//    Cuenta findById(Long id);
//    void update(Cuenta cuenta);
}
