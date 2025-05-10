// Caminho do arquivo: br\com\fiap\mottu\repository\BoxRepository.java
package br.com.fiap.mottu.repository;

import br.com.fiap.mottu.model.Box;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BoxRepository extends JpaRepository<Box, Long>, JpaSpecificationExecutor<Box> {
    // Métodos de pesquisa avançada (derivados):
    // Box: nome, data de entrada, data de saída, id
    Optional<Box> findById(Long idBox); // Já disponível, mas explicitando
    List<Box> findByNomeContainingIgnoreCase(String nome);
    List<Box> findByDataEntradaBetween(LocalDate startDate, LocalDate endDate);
    List<Box> findByDataSaidaBetween(LocalDate startDate, LocalDate endDate);
    // Você também pode combinar:
    // List<Box> findByNomeContainingIgnoreCaseAndStatus(String nome, String status);
}