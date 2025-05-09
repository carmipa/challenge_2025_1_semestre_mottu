package br.com.fiap.mottu.model.relacionamento;

import br.com.fiap.mottu.model.Patio; // Importa a classe TbPatio
import br.com.fiap.mottu.model.Veiculo; // Importa a classe TbVeiculo

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_VEICULOPATIO") // Mapeia para o nome renomeado e em maiúsculas no BD
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VeiculoPatio {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private VeiculoPatioId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("veiculoId")
    @JoinColumn(name = "tb_veiculo_id_veiculo", nullable = false, insertable = false, updatable = false)
    private Veiculo veiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("patioId")
    @JoinColumn(name = "tb_patio_id_patio", nullable = false, insertable = false, updatable = false)
    private Patio patio;

    public VeiculoPatio(Veiculo veiculo, Patio patio) {
        this.veiculo = veiculo;
        this.patio = patio;
        this.id = new VeiculoPatioId(veiculo.getIdVeiculo(), patio.getIdPatio());
    }
}