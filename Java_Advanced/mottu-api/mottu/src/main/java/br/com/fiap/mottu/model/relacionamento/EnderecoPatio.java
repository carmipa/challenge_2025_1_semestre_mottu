// Caminho do arquivo: br\com\fiap\mottu\model\relacionamento\EnderecoPatio.java
package br.com.fiap.mottu.model.relacionamento;

import br.com.fiap.mottu.model.Endereco;
import br.com.fiap.mottu.model.Patio;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_ENDERECIOPATIO", schema = "CHALLENGE") // Adicionado schema
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"endereco", "patio"}) // Excluir relacionamentos
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EnderecoPatio {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private EnderecoPatioId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("enderecoId")
    @JoinColumn(name = "TB_ENDERECO_ID_ENDERECO", nullable = false, insertable = false, updatable = false) // Nome da coluna em MAIÚSCULAS
    @ToString.Exclude
    private Endereco endereco;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("patioId")
    @JoinColumn(name = "TB_PATIO_ID_PATIO", nullable = false, insertable = false, updatable = false) // Nome da coluna em MAIÚSCULAS
    @ToString.Exclude
    private Patio patio;

    public EnderecoPatio(Endereco endereco, Patio patio) {
        this.endereco = endereco;
        this.patio = patio;
        this.id = new EnderecoPatioId(endereco.getIdEndereco(), patio.getIdPatio());
    }
}