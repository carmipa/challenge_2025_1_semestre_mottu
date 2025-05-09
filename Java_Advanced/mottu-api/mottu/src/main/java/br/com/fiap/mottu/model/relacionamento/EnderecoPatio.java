package br.com.fiap.mottu.model.relacionamento;

import br.com.fiap.mottu.model.Endereco; // Importa a classe Endereco
import br.com.fiap.mottu.model.Patio; // Importa a classe TbPatio

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_ENDERECIOPATIO") // Mapeia para o nome renomeado e em maiúsculas no BD
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EnderecoPatio {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private EnderecoPatioId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("enderecoId")
    @JoinColumn(name = "tb_endereco_id_endereco", nullable = false, insertable = false, updatable = false)
    private Endereco endereco;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("patioId")
    @JoinColumn(name = "tb_patio_id_patio", nullable = false, insertable = false, updatable = false)
    private Patio patio;

    public EnderecoPatio(Endereco endereco, Patio patio) {
        this.endereco = endereco;
        this.patio = patio;
        this.id = new EnderecoPatioId(endereco.getIdEndereco(), patio.getIdPatio());
    }
}