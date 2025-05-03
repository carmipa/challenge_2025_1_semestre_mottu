package br.com.fiap.mattu.model.filial;

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
public class FilialId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "id_fil", nullable = false) // Coluna id_fil da PK composta
    private Long idFil;

    @Column(name = "tb_end_f_id_endf", nullable = false) // Coluna tb_end_f_id_endf da PK composta (FK para EnderecoFilial)
    private Long enderecoFilialId; // Armazena o ID do EnderecoFilial
}