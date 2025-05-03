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
public class FpId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "id_fp")
    private Long idFp;

    // Parte da FK Composta para Filial
    @Column(name = "tb_filial_id_fil")
    private Long filialIdFil;

    @Column(name = "tb_filial_tb_end_f_id_endf")
    private Long filialEnderecoFilialId;

    // Parte da FK Composta para Patio
    @Column(name = "tb_patio_id_patio")
    private Long patioIdPatio;

    @Column(name = "tb_patio_id_contp")
    private Long patioContatoPatioId;

    @Column(name = "tb_patio_status", length = 1)
    private String patioStatus;

    @Column(name = "tb_patio_id_endp")
    private Long patioEnderecoPatioId;
}