package br.com.fiap.mottu.model.relacionamento;

import br.com.fiap.mottu.model.Contato; // Importa a classe Contato
import br.com.fiap.mottu.model.Patio; // Importa a classe TbPatio

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_CONTATOPATIO") // Mapeia para o nome renomeado e em maiúsculas no BD
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Use onlyExplicitlyIncluded com @EmbeddedId
public class ContatoPatio {

    @EmbeddedId // Usa a classe Embeddable para a PK composta
    @EqualsAndHashCode.Include // Inclui apenas o ID no cálculo de hash/equals
    private ContatoPatioId id;

    // Mapeamento dos relacionamentos Many-to-One para as entidades que compõem a PK
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("patioId") // Mapeia a parte 'patioId' do EmbeddedId
    @JoinColumn(name = "tb_patio_id_patio", nullable = false, insertable = false, updatable = false) // Coluna real no BD, não pode ser alterada por este lado
    private Patio patio;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("contatoId") // Mapeia a parte 'contatoId' do EmbeddedId
    @JoinColumn(name = "tb_contato_id_contato", nullable = false, insertable = false, updatable = false) // Coluna real no BD, não pode ser alterada por este lado
    private Contato contato;

    // Construtor útil para criar a entidade a partir das entidades relacionadas
    public ContatoPatio(Patio patio, Contato contato) {
        this.patio = patio;
        this.contato = contato;
        this.id = new ContatoPatioId(patio.getIdPatio(), contato.getIdContato());
    }
}