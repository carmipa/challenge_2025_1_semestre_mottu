package br.com.fiap.mattu.model.relacionamento;

import br.com.fiap.mattu.model.filial.Filial; // Ajuste o import
import br.com.fiap.mattu.model.veiculo.Veiculo; // Ajuste o import
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_fv")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Fv {

    @EmbeddedId
    private FvId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("filialIdFil") // Mapeia a PARTE 'filialIdFil' do FvId
    @JoinColumns({
            @JoinColumn(name = "tb_filial_id_fil", referencedColumnName = "id_fil", insertable = false, updatable = false),
            @JoinColumn(name = "tb_filial_tb_end_f_id_endf", referencedColumnName = "tb_end_f_id_endf", insertable = false, updatable = false)
    })
    private Filial filial;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("veiculoId") // Mapeia a PARTE 'veiculoId' do FvId
    @JoinColumn(name = "tb_veiculo_id_veiculo", referencedColumnName = "id_veiculo", insertable = false, updatable = false)
    private Veiculo veiculo;

    // Outros campos se houver...
}