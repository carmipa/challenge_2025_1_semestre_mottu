package br.com.fiap.mattu.model.relacionamento;

import br.com.fiap.mattu.model.filial.Filial; // Ajuste o import
import br.com.fiap.mattu.model.patio.Patio;   // Ajuste o import
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_fp")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Fp {

    @EmbeddedId
    private FpId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("filialIdFil") // Mapeia a PARTE 'filialIdFil' do FpId
    @JoinColumns({
            @JoinColumn(name = "tb_filial_id_fil", referencedColumnName = "id_fil", insertable = false, updatable = false),
            @JoinColumn(name = "tb_filial_tb_end_f_id_endf", referencedColumnName = "tb_end_f_id_endf", insertable = false, updatable = false)
    })
    private Filial filial;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("patioIdPatio") // Mapeia a PARTE 'patioIdPatio' do FpId
    @JoinColumns({
            @JoinColumn(name = "tb_patio_id_patio", referencedColumnName = "id_patio", insertable = false, updatable = false),
            @JoinColumn(name = "tb_patio_id_contp", referencedColumnName = "tb_cont_p_id_contp", insertable = false, updatable = false),
            @JoinColumn(name = "tb_patio_status", referencedColumnName = "status", insertable = false, updatable = false),
            @JoinColumn(name = "tb_patio_id_endp", referencedColumnName = "tb_end_p_id_endp", insertable = false, updatable = false)
    })
    private Patio patio;

    // Outros campos se houver...
}