package br.com.fiap.mattu.model.relacionamento;

import br.com.fiap.mattu.model.patio.Patio;   // Ajuste o import
import br.com.fiap.mattu.model.veiculo.Veiculo; // Ajuste o import
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_vp")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Vp {

    @EmbeddedId
    private VpId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("veiculoId") // Mapeia a PARTE 'veiculoId' do VpId
    @JoinColumn(name = "tb_veiculo_id_veiculo", referencedColumnName = "id_veiculo", insertable = false, updatable = false)
    private Veiculo veiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("patioIdPatio") // Mapeia a PARTE 'patioIdPatio' do VpId
    @JoinColumns({
            @JoinColumn(name = "tb_patio_id_patio", referencedColumnName = "id_patio", insertable = false, updatable = false),
            @JoinColumn(name = "tb_patio_tb_cont_p_id_contp", referencedColumnName = "tb_cont_p_id_contp", insertable = false, updatable = false),
            @JoinColumn(name = "tb_patio_status", referencedColumnName = "status", insertable = false, updatable = false),
            @JoinColumn(name = "tb_patio_tb_end_p_id_endp", referencedColumnName = "tb_end_p_id_endp", insertable = false, updatable = false)
    })
    private Patio patio;

    // Outros campos se houver...
}