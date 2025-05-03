package br.com.fiap.mattu.model.relacionamento;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CvId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "id_cv")
    private Long idCv;

    // Parte da FK Composta para Cliente
    @Column(name = "tb_cli_id_cli")
    private Long clienteIdCli;

    @Column(name = "tb_cli_tb_end_c_id_endc")
    private Long clienteEnderecoClienteId;

    @Column(name = "tb_cli_tb_cont_c_id_contc")
    private Long clienteContatoClienteId;

    // FK Simples para Veiculo
    @Column(name = "tb_veiculo_id_veiculo")
    private Long veiculoId;
}