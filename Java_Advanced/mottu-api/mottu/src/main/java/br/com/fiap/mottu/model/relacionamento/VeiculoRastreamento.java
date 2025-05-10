// Caminho do arquivo: br\com\fiap\mottu\model\relacionamento\VeiculoRastreamento.java
package br.com.fiap.mottu.model.relacionamento;

import br.com.fiap.mottu.model.Rastreamento;
import br.com.fiap.mottu.model.Veiculo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_VEICULORASTREAMENTO", schema = "CHALLENGE") // Adicionado schema
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"veiculo", "rastreamento"}) // Excluir relacionamentos
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VeiculoRastreamento {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private VeiculoRastreamentoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("veiculoId")
    @JoinColumn(name = "TB_VEICULO_ID_VEICULO", nullable = false, insertable = false, updatable = false) // Nome da coluna em MAIÚSCULAS
    @ToString.Exclude
    private Veiculo veiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("rastreamentoId")
    @JoinColumn(name = "TB_RASTREAMENTO_ID_RASTREAMENTO", nullable = false, insertable = false, updatable = false) // Nome da coluna em MAIÚSCULAS
    @ToString.Exclude
    private Rastreamento rastreamento;

    public VeiculoRastreamento(Veiculo veiculo, Rastreamento rastreamento) {
        this.veiculo = veiculo;
        this.rastreamento = rastreamento;
        this.id = new VeiculoRastreamentoId(veiculo.getIdVeiculo(), rastreamento.getIdRastreamento());
    }
}