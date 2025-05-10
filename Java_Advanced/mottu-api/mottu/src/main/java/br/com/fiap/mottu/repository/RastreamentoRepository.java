// Caminho do arquivo: br\com\fiap\mottu\repository\RastreamentoRepository.java
package br.com.fiap.mottu.repository;

import br.com.fiap.mottu.model.Rastreamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
// Para buscar por campos string (ips, gprs) que são de SDO_GEOMETRY e você mapeou como String
import java.util.List;

@Repository
public interface RastreamentoRepository extends JpaRepository<Rastreamento, Long>, JpaSpecificationExecutor<Rastreamento> {
    // Métodos de pesquisa avançada (derivados):
    // Rastreamento: id
    Optional<Rastreamento> findById(Long idRastreamento); // Já disponível, mas explicitando

    // Se os campos IPS e GPRS são string (para busca simplificada):
    List<Rastreamento> findByIpsContainingIgnoreCase(String ips);
    List<Rastreamento> findByGprsContainingIgnoreCase(String gprs);
}