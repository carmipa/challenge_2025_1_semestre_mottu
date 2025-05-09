package br.com.fiap.mottu.model.relacionamento;

import br.com.fiap.mottu.model.Veiculo; // Importa a classe TbVeiculo
import br.com.fiap.mottu.model.Zona; // Importa a classe TbZona

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_VEICULOZONA") // Mapeia para o nome renomeado e em maiúsculas no BD
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VeiculoZona {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private VeiculoZonaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("veiculoId")
    @JoinColumn(name = "tb_veiculo_id_veiculo", nullable = false, insertable = false, updatable = false)
    private Veiculo veiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("zonaId")
    @JoinColumn(name = "tb_zona_id_zona", nullable = false, insertable = false, updatable = false)
    private Zona zona;

    public VeiculoZona(Veiculo veiculo, Zona zona) {
        this.veiculo = veiculo;
        this.zona = zona;
        this.id = new VeiculoZonaId(veiculo.getIdVeiculo(), zona.getIdZona());
    }
}