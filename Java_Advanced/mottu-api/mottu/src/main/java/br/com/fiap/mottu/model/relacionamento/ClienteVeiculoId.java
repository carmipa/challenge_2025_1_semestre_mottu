// Caminho do arquivo: br\com\fiap\mottu\model\relacionamento\ClienteVeiculoId.java
package br.com.fiap.mottu.model.relacionamento;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode // Essencial para PKs compostas
public class ClienteVeiculoId implements Serializable {

    @Column(name = "TB_CLIENTE_ID_CLIENTE", nullable = false) // Nome da coluna FK no BD
    private Long clienteId;

    // Componentes da PK de ClienteVeiculo que incluem IDs de Endereco e Contato
    // Note que estes campos NÃO são @MapsId para a entidade Cliente, apenas fazem parte da PK AQUI
    @Column(name = "TB_CLIENTE_TB_ENDERECO_ID_ENDERECO", nullable = false)
    private Long clienteEnderecoId;

    @Column(name = "TB_CLIENTE_TB_CONTATO_ID_CONTATO", nullable = false)
    private Long clienteContatoId;

    @Column(name = "TB_VEICULO_ID_VEICULO", nullable = false) // Nome da coluna FK no BD
    private Long veiculoId;
}