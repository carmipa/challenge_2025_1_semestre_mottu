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
public class FvId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "id_fv")
    private Long idFv;

    // Parte da FK Composta para Filial
    @Column(name = "tb_filial_id_fil")
    private Long filialIdFil;

    @Column(name = "tb_filial_tb_end_f_id_endf")
    private Long filialEnderecoFilialId;

    // FK Simples para Veiculo
    @Column(name = "tb_veiculo_id_veiculo")
    private Long veiculoId;
}