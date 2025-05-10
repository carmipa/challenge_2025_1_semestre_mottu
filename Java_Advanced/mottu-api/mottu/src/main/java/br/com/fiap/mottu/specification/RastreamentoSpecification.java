// Caminho do arquivo: br\com\fiap\mottu\specification\RastreamentoSpecification.java
package br.com.fiap.mottu.specification;

import br.com.fiap.mottu.filter.RastreamentoFilter; // Importa do novo pacote
import br.com.fiap.mottu.model.Rastreamento;
import br.com.fiap.mottu.model.relacionamento.VeiculoRastreamento;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RastreamentoSpecification {

    public static Specification<Rastreamento> withFilters(RastreamentoFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.ips() != null && !filter.ips().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("ips")), "%" + filter.ips().toLowerCase() + "%"));
            }
            if (filter.gprs() != null && !filter.gprs().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("gprs")), "%" + filter.gprs().toLowerCase() + "%"));
            }

            // Filtro por relacionamento ManyToMany (VeiculoRastreamento)
            if (filter.veiculoPlaca() != null && !filter.veiculoPlaca().isBlank()) {
                Join<Rastreamento, VeiculoRastreamento> veiculoRastreamentoJoin = root.join("veiculoRastreamentos");
                predicates.add(cb.equal(veiculoRastreamentoJoin.get("veiculo").get("placa"), filter.veiculoPlaca()));
            }

            query.distinct(true); // Evitar duplicação de resultados

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}