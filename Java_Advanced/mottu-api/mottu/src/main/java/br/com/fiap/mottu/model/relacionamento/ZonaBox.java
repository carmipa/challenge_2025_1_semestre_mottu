package br.com.fiap.mottu.model.relacionamento;

import br.com.fiap.mottu.model.Box; // Importa a classe TbBox
import br.com.fiap.mottu.model.Zona; // Importa a classe TbZona

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_ZONABOX") // Mapeia para o nome renomeado e em maiúsculas no BD
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ZonaBox {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private ZonaBoxId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("zonaId")
    @JoinColumn(name = "tb_zona_id_zona", nullable = false, insertable = false, updatable = false)
    private Zona zona;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("boxId")
    @JoinColumn(name = "tb_box_id_box", nullable = false, insertable = false, updatable = false)
    private Box box;

    public ZonaBox(Zona zona, Box box) {
        this.zona = zona;
        this.box = box;
        this.id = new ZonaBoxId(zona.getIdZona(), box.getIdBox());
    }
}