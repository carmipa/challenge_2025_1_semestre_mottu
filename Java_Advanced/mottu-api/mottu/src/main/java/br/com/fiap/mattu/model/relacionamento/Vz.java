package br.com.fiap.mattu.model.relacionamento;

import br.com.fiap.mattu.model.veiculo.Veiculo; // Ajuste o import
import br.com.fiap.mattu.model.zona.Zona;       // Ajuste o import
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_vz")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Vz {

    @EmbeddedId
    private VzId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("veiculoId") // Mapeia a PARTE 'veiculoId' do VzId
    @JoinColumn(name = "tb_veiculo_id_veiculo", referencedColumnName = "id_veiculo", insertable = false, updatable = false)
    private Veiculo veiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("zonaId") // Mapeia a PARTE 'zonaId' do VzId
    @JoinColumn(name = "tb_zona_id_zona", referencedColumnName = "id_zona", insertable = false, updatable = false)
    private Zona zona;

    // Outros campos se houver...
}