package br.com.fiap.mattu.model.relacionamento;

import br.com.fiap.mattu.model.rastreamento.Rastreamento;
import br.com.fiap.mattu.model.veiculo.Veiculo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_vr")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Vr {

    @EmbeddedId
    private VrId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("veiculoId")
    @JoinColumn(name = "tb_veiculo_id_veiculo", referencedColumnName = "ID_VEICULO", insertable = false, updatable = false) // <-- CORRIGIDO referencedColumnName (assumindo PK de Veiculo é ID_VEICULO)
    private Veiculo veiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("rastreamentoId")
    @JoinColumn(
            name = "tb_rastreamento_id_rast",
            // CORREÇÃO: referencedColumnName deve bater com a PK de Rastreamento
            referencedColumnName = "ID_RASTREAMENTO", // <-- CORRIGIDO de "id_rast"
            insertable = false, updatable = false
    )
    private Rastreamento rastreamento;

    // Outros campos se houver...
}