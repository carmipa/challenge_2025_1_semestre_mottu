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

    // Componente da PK que referencia a PK simplificada de TB_CLIENTE
    @Column(name = "tb_cliente_id_cliente", nullable = false) // Nome da coluna FK no BD
    private Long clienteId; // Tipo do campo PK referenciado em Cliente

    // Componentes da antiga PK composta de TB_CLIENTE, mantidos na PK DESSA tabela de junção
    // Note que estes campos NÃO são @MapsId para a entidade Cliente, apenas fazem parte da PK AQUI
    @Column(name = "tb_cliente_tb_endereco_id_endereco", nullable = false)
    private Long clienteEnderecoId;

    @Column(name = "tb_cliente_tb_contato_id_contato", nullable = false)
    private Long clienteContatoId;

    // Componente da PK que referencia a PK de TB_VEICULO
    @Column(name = "tb_veiculo_id_veiculo", nullable = false) // Nome da coluna FK no BD
    private Long veiculoId; // Tipo do campo PK referenciado em Veiculo
}