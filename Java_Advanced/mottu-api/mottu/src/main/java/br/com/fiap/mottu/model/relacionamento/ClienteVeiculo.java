// Caminho do arquivo: br\com\fiap\mottu\model\relacionamento\ClienteVeiculo.java
package br.com.fiap.mottu.model.relacionamento;

import br.com.fiap.mottu.model.Cliente;
import br.com.fiap.mottu.model.Veiculo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_CLIENTEVEICULO", schema = "CHALLENGE") // Adicionado schema
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"cliente", "veiculo"}) // Excluir relacionamentos para evitar StackOverflowError
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Usar apenas campos incluídos para equals/hashCode
public class ClienteVeiculo {

    @EmbeddedId // Usa a classe Embeddable para a PK composta
    @EqualsAndHashCode.Include // Inclui apenas o ID no cálculo de hash/equals
    private ClienteVeiculoId id;

    // Mapeamento dos relacionamentos Many-to-One para as entidades que compõem a PK
    // @MapsId indica que este Many-to-One mapeia parte da chave primária embutida
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("clienteId") // Mapeia o componente clienteId do EmbeddedId
    // As colunas da chave composta da associação (TB_CLIENTE_ID_CLIENTE, TB_CLIENTE_TB_ENDERECO_ID_ENDERECO, TB_CLIENTE_TB_CONTATO_ID_CONTATO)
    // são parte da PK da tabela de junção. A entidade Cliente tem apenas ID_CLIENTE como PK.
    // Apenas a coluna ID_CLIENTE é a FK para a entidade Cliente.
    @JoinColumn(name = "TB_CLIENTE_ID_CLIENTE", referencedColumnName = "ID_CLIENTE", nullable = false, insertable = false, updatable = false)
    @ToString.Exclude
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("veiculoId") // Mapeia o componente veiculoId do EmbeddedId
    @JoinColumn(name = "TB_VEICULO_ID_VEICULO", referencedColumnName = "ID_VEICULO", nullable = false, insertable = false, updatable = false)
    @ToString.Exclude
    private Veiculo veiculo;

    // Construtor útil para criar a entidade a partir das entidades relacionadas
    // Note que este construtor exige que as entidades Cliente e Veiculo já tenham seus IDs preenchidos.
    // Além disso, exige os IDs de Endereco e Contato associados ao cliente no momento da criação desta associação,
    // pois eles fazem parte da chave composta da tabela de junção.
    public ClienteVeiculo(Cliente cliente, Veiculo veiculo) {
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.id = new ClienteVeiculoId(
                cliente.getIdCliente(),
                cliente.getEndereco() != null ? cliente.getEndereco().getIdEndereco() : null,
                cliente.getContato() != null ? cliente.getContato().getIdContato() : null,
                veiculo.getIdVeiculo()
        );
    }
}