package br.com.fiap.mottu.model.relacionamento;

import br.com.fiap.mottu.model.Patio; // Importa a classe TbPatio
import br.com.fiap.mottu.model.Zona; // Importa a classe TbZona

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_ZONAPATIO") // Mapeia para o nome renomeado e em maiúsculas no BD
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ZonaPatio {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private ZonaPatioId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("patioId")
    @JoinColumn(name = "tb_patio_id_patio", nullable = false, insertable = false, updatable = false)
    private Patio patio;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("zonaId")
    @JoinColumn(name = "tb_zona_id_zona", nullable = false, insertable = false, updatable = false)
    private Zona zona;

    public ZonaPatio(Patio patio, Zona zona) {
        this.patio = patio;
        this.zona = zona;
        this.id = new ZonaPatioId(patio.getIdPatio(), zona.getIdZona());
    }
}