package br.com.fiap.mattu.model.cliente;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable // Indica que esta classe pode ser embutida em uma entidade
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode // Essencial para chaves compostas
public class ClienteId implements Serializable {
    // Serializable é uma boa prática para IDs
    private static final long serialVersionUID = 1L;

    @Column(name = "id_cli", nullable = false) // Mapeia a coluna id_cli da PK composta
    private Long idCli;

    @Column(name = "tb_end_c_id_endc", nullable = false) // Mapeia a coluna tb_end_c_id_endc da PK composta (FK para EnderecoCliente)
    private Long enderecoClienteId; // Armazena o ID do EnderecoCliente relacionado

    @Column(name = "tb_cont_c_id_contc", nullable = false) // Mapeia a coluna tb_cont_c_id_contc da PK composta (FK para ContatoCliente)
    private Long contatoClienteId; // Armazena o ID do ContatoCliente relacionado
}