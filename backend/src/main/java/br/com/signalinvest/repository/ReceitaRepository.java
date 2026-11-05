package br.com.signalinvest.repository;

import br.com.signalinvest.model.Receita;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReceitaRepository extends JpaRepository<Receita, Long> {
    List<Receita> findAllByUsuarioIdOrderByDataDescIdDesc(Long usuarioId);
    Optional<Receita> findByIdAndUsuarioId(Long id, Long usuarioId);
    boolean existsByContaId(Long contaId);

    @Query("select coalesce(sum(r.valor), 0) from Receita r where r.usuario.id = :usuarioId")
    BigDecimal somarPorUsuario(@Param("usuarioId") Long usuarioId);
}
