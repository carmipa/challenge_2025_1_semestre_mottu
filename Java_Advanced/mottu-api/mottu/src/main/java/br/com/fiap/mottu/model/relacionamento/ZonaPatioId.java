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
public class ZonaPatioId implements Serializable {

    @Column(name = "tb_patio_id_patio", nullable = false)
    private Long patioId; // Tipo do campo PK referenciado em TbPatio

    @Column(name = "tb_zona_id_zona", nullable = false)
    private Long zonaId; // Tipo do campo PK referenciado em TbZona
}