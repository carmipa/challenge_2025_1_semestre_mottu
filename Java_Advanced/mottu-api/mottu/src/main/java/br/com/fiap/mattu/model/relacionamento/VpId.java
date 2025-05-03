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
public class VpId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "id_vp")
    private Long idVp;

    @Column(name = "tb_veiculo_id_veiculo") // FK Simples para Veiculo
    private Long veiculoId;

    // Parte da FK Composta para Patio
    @Column(name = "tb_patio_id_patio")
    private Long patioIdPatio;

    @Column(name = "tb_patio_tb_cont_p_id_contp")
    private Long patioContatoPatioId;

    @Column(name = "tb_patio_status", length = 1)
    private String patioStatus;

    @Column(name = "tb_patio_tb_end_p_id_endp")
    private Long patioEnderecoPatioId;
}