package br.com.fiap.mottu.model.relacionamento;

import br.com.fiap.mottu.model.Box; // Importa a classe TbBox
import br.com.fiap.mottu.model.Veiculo; // Importa a classe TbVeiculo

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_VEICULOBOX") // Mapeia para o nome renomeado e em maiúsculas no BD
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VeiculoBox {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private VeiculoBoxId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("veiculoId")
    @JoinColumn(name = "tb_veiculo_id_veiculo", nullable = false, insertable = false, updatable = false)
    private Veiculo veiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("boxId")
    @JoinColumn(name = "tb_box_id_box", nullable = false, insertable = false, updatable = false)
    private Box box;

    public VeiculoBox(Veiculo veiculo, Box box) {
        this.veiculo = veiculo;
        this.box = box;
        this.id = new VeiculoBoxId(veiculo.getIdVeiculo(), box.getIdBox());
    }
}