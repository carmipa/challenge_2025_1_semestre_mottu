// Caminho do arquivo: br\com\fiap\mottu\repository\PatioRepository.java
package br.com.fiap.mottu.repository;

import br.com.fiap.mottu.model.Patio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatioRepository extends JpaRepository<Patio, Long>, JpaSpecificationExecutor<Patio> {
    // Métodos de pesquisa avançada (derivados):
    // Patio: nome, data de entrada, data de saída, id
    Optional<Patio> findById(Long idPatio); // Já disponível, mas explicitando
    List<Patio> findByNomePatioContainingIgnoreCase(String nomePatio);
    List<Patio> findByDataEntradaBetween(LocalDate startDate, LocalDate endDate);
    List<Patio> findByDataSaidaBetween(LocalDate startDate, LocalDate endDate);
}
