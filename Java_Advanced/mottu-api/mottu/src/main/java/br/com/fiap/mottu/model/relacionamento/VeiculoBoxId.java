package br.com.fiap.mottu.model.relacionamento;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class VeiculoBoxId implements Serializable {

    @Column(name = "tb_veiculo_id_veiculo", nullable = false) // Nome da coluna FK no BD
    private Long veiculoId; // Tipo do campo PK referenciado em TbVeiculo

    @Column(name = "tb_box_id_box", nullable = false) // Nome da coluna FK no BD
    private Long boxId; // Tipo do campo PK referenciado em TbBox
}