// Caminho do arquivo: br\com\fiap\mottu\model\relacionamento\VeiculoZona.java
package br.com.fiap.mottu.model.relacionamento;

import br.com.fiap.mottu.model.Veiculo;
import br.com.fiap.mottu.model.Zona;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_VEICULOZONA", schema = "CHALLENGE") // Adicionado schema
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"veiculo", "zona"}) // Excluir relacionamentos
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VeiculoZona {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private VeiculoZonaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("veiculoId")
    @JoinColumn(name = "TB_VEICULO_ID_VEICULO", nullable = false, insertable = false, updatable = false) // Nome da coluna em MAIÚSCULAS
    @ToString.Exclude
    private Veiculo veiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("zonaId")
    @JoinColumn(name = "TB_ZONA_ID_ZONA", nullable = false, insertable = false, updatable = false) // Nome da coluna em MAIÚSCULAS
    @ToString.Exclude
    private Zona zona;

    public VeiculoZona(Veiculo veiculo, Zona zona) {
        this.veiculo = veiculo;
        this.zona = zona;
        this.id = new VeiculoZonaId(veiculo.getIdVeiculo(), zona.getIdZona());
    }
}