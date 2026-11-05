package br.com.signalinvest.repository;

import br.com.signalinvest.model.ContaBancaria;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long> {
    List<ContaBancaria> findAllByUsuarioIdOrderByIdDesc(Long usuarioId);
    Optional<ContaBancaria> findByIdAndUsuarioId(Long id, Long usuarioId);
}
