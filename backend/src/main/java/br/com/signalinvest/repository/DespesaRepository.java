package br.com.signalinvest.repository;

import br.com.signalinvest.model.Despesa;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {
    List<Despesa> findAllByUsuarioIdOrderByDataDescIdDesc(Long usuarioId);
    Optional<Despesa> findByIdAndUsuarioId(Long id, Long usuarioId);
    boolean existsByContaId(Long contaId);

    @Query("select coalesce(sum(d.valor), 0) from Despesa d where d.usuario.id = :usuarioId")
    BigDecimal somarPorUsuario(@Param("usuarioId") Long usuarioId);
}
