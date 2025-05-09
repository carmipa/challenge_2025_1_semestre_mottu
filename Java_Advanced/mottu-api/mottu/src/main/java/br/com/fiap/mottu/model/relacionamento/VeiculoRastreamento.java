package br.com.fiap.mottu.model.relacionamento;

import br.com.fiap.mottu.model.Rastreamento; // Importa a classe TbRastreamento
import br.com.fiap.mottu.model.Veiculo; // Importa a classe TbVeiculo

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_VEICULORASTREAMENTO") // Mapeia para o nome renomeado e em maiúsculas no BD
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VeiculoRastreamento {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private VeiculoRastreamentoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("veiculoId")
    @JoinColumn(name = "tb_veiculo_id_veiculo", nullable = false, insertable = false, updatable = false)
    private Veiculo veiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("rastreamentoId")
    @JoinColumn(name = "tb_rastreamento_id_rastreamento", nullable = false, insertable = false, updatable = false)
    private Rastreamento rastreamento;

    public VeiculoRastreamento(Veiculo veiculo, Rastreamento rastreamento) {
        this.veiculo = veiculo;
        this.rastreamento = rastreamento;
        this.id = new VeiculoRastreamentoId(veiculo.getIdVeiculo(), rastreamento.getIdRastreamento());
    }
}