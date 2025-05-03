package br.com.fiap.mattu.model.relacionamento;

import br.com.fiap.mattu.model.box.Box;         // Ajuste o import
import br.com.fiap.mattu.model.veiculo.Veiculo; // Ajuste o import
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_vb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Vb {

    @EmbeddedId
    private VbId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("veiculoId") // Mapeia a PARTE 'veiculoId' do VbId
    @JoinColumn(name = "tb_veiculo_id_veiculo", referencedColumnName = "id_veiculo", insertable = false, updatable = false)
    private Veiculo veiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("boxId") // Mapeia a PARTE 'boxId' do VbId
    @JoinColumn(name = "tb_box_id_box", referencedColumnName = "id_box", insertable = false, updatable = false)
    private Box box;

    // Outros campos se houver...
}