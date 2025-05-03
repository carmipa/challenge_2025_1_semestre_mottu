package br.com.fiap.mattu.model.relacionamento;

import br.com.fiap.mattu.model.patio.Patio; // Ajuste o import
import br.com.fiap.mattu.model.zona.Zona;   // Ajuste o import
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_zp")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Zp {

    @EmbeddedId
    private ZpId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("patioIdPatio") // Mapeia a PARTE 'patioIdPatio' do ZpId
    @JoinColumns({
            @JoinColumn(name = "tb_patio_id_patio", referencedColumnName = "id_patio", insertable = false, updatable = false),
            @JoinColumn(name = "tb_patio_tb_cont_p_id_contp", referencedColumnName = "tb_cont_p_id_contp", insertable = false, updatable = false),
            @JoinColumn(name = "tb_patio_status", referencedColumnName = "status", insertable = false, updatable = false),
            @JoinColumn(name = "tb_patio_tb_end_p_id_endp", referencedColumnName = "tb_end_p_id_endp", insertable = false, updatable = false)
    })
    private Patio patio;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("zonaId") // Mapeia a PARTE 'zonaId' do ZpId
    @JoinColumn(name = "tb_zona_id_zona", referencedColumnName = "id_zona", insertable = false, updatable = false)
    private Zona zona;

    // Outros campos se houver...
}