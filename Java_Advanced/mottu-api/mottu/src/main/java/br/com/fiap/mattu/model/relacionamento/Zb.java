package br.com.fiap.mattu.model.relacionamento;

import br.com.fiap.mattu.model.box.Box;   // Ajuste o import
import br.com.fiap.mattu.model.zona.Zona; // Ajuste o import
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_zb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Zb {

    @EmbeddedId
    private ZbId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("zonaId") // Mapeia a PARTE 'zonaId' do ZbId
    @JoinColumn(name = "tb_zona_id_zona", referencedColumnName = "id_zona", insertable = false, updatable = false)
    private Zona zona;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("boxId") // Mapeia a PARTE 'boxId' do ZbId
    @JoinColumn(name = "tb_box_id_box", referencedColumnName = "id_box", insertable = false, updatable = false)
    private Box box;

    // Outros campos se houver...
}