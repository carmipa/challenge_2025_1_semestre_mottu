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
public class ZonaBoxId implements Serializable {

    @Column(name = "tb_zona_id_zona", nullable = false) // Nome da coluna FK no BD
    private Long zonaId; // Tipo do campo PK referenciado em TbZona

    @Column(name = "tb_box_id_box", nullable = false) // Nome da coluna FK no BD
    private Long boxId; // Tipo do campo PK referenciado em TbBox
}