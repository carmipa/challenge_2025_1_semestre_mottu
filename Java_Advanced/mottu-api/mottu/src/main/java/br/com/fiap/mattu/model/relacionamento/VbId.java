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
public class VbId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "id_vb")
    private Long idVb;

    @Column(name = "tb_veiculo_id_veiculo") // FK Simples para Veiculo
    private Long veiculoId;

    @Column(name = "tb_box_id_box") // FK Simples para Box
    private Long boxId;
}