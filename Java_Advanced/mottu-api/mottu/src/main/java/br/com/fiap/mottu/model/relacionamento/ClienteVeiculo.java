package br.com.fiap.mottu.model.relacionamento;

import br.com.fiap.mottu.model.Cliente; // Importa a classe Cliente
import br.com.fiap.mottu.model.Veiculo; // Importa a classe TbVeiculo

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_CLIENTEVEICULO") // Mapeia para o nome renomeado e em maiúsculas no BD
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Use onlyExplicitlyIncluded com @EmbeddedId
public class ClienteVeiculo {

    @EmbeddedId // Usa a classe Embeddable para a PK composta
    @EqualsAndHashCode.Include // Inclui apenas o ID no cálculo de hash/equals
    private ClienteVeiculoId id;

    // Mapeamento dos relacionamentos Many-to-One para as entidades que compõem a PK
    // @MapsId indica que este Many-to-One mapeia parte da chave primária embutida
    // Mapeamento para TB_CLIENTE (agora com PK simplificada id_cliente)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("clienteId") // Mapeia apenas o componente clienteId do EmbeddedId
    @JoinColumn(name = "tb_cliente_id_cliente", nullable = false, insertable = false, updatable = false) // Coluna FK real no BD
    private Cliente cliente; // Relacionamento Many-to-One com Cliente

    // Mapeamento para TB_VEICULO
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("veiculoId") // Mapeia o componente veiculoId do EmbeddedId
    @JoinColumn(name = "tb_veiculo_id_veiculo", nullable = false, insertable = false, updatable = false) // Coluna FK real no BD
    private Veiculo veiculo; // Relacionamento Many-to-One com TbVeiculo

    // Os campos clienteEnderecoId e clienteContatoId na PK composta ClienteVeiculoId
    // não são usados aqui para mapear para a entidade Cliente (pois a PK de Cliente foi simplificada).
    // Eles existem APENAS na PK da tabela TB_CLIENTEVEICULO no BD.
    // Para criar um objeto ClienteVeiculo, você precisará fornecer todos os componentes do ClienteVeiculoId.

    // Construtor útil para criar a entidade a partir das entidades relacionadas
    // Note que este construtor exige que as entidades Cliente e Veiculo já tenham seus IDs preenchidos.
    // Além disso, exige os IDs de Endereco e Contato associados ao cliente no momento da criação desta associação.
    public ClienteVeiculo(Cliente cliente, Veiculo veiculo) {
        this.cliente = cliente;
        this.veiculo = veiculo;
        // Construir o ID requer o ID do cliente (PK simplificada) e os IDs de endereco/contato
        // que FAZIAM parte da PK original de Cliente, mas agora só compõem a PK DESSA tabela de junção.
        // Isso pode ser um pouco estranho na lógica, pois os IDs de Endereco/Contato do Cliente
        // são fixados na PK desta tabela associativa no momento da criação.
        this.id = new ClienteVeiculoId(
                cliente.getIdCliente(),
                cliente.getEndereco() != null ? cliente.getEndereco().getIdEndereco() : null, // Precisa ter Endereco associado
                cliente.getContato() != null ? cliente.getContato().getIdContato() : null,   // Precisa ter Contato associado
                veiculo.getIdVeiculo()
        );
        // Adicionado null check para Endereco/Contato caso a associação Cliente -> Endereco/Contato
        // ainda não esteja estabelecida (o que não deveria acontecer se nullable=false nas FKs de Cliente).
    }
}