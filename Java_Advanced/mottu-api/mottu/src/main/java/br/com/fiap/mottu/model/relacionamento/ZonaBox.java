// Caminho do arquivo: br\com\fiap\mottu\model\relacionamento\ZonaBox.java
package br.com.fiap.mottu.model.relacionamento;

import br.com.fiap.mottu.model.Box;
import br.com.fiap.mottu.model.Zona;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_ZONABOX", schema = "CHALLENGE") // Adicionado schema
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"zona", "box"}) // Excluir relacionamentos
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ZonaBox {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private ZonaBoxId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("zonaId")
    @JoinColumn(name = "TB_ZONA_ID_ZONA", nullable = false, insertable = false, updatable = false) // Nome da coluna em MAIÚSCULAS
    @ToString.Exclude
    private Zona zona;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("boxId")
    @JoinColumn(name = "TB_BOX_ID_BOX", nullable = false, insertable = false, updatable = false) // Nome da coluna em MAIÚSCULAS
    @ToString.Exclude
    private Box box;

    public ZonaBox(Zona zona, Box box) {
        this.zona = zona;
        this.box = box;
        this.id = new ZonaBoxId(zona.getIdZona(), box.getIdBox());
    }
}