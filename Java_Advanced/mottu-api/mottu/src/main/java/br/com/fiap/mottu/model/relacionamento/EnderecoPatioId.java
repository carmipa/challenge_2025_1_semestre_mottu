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
public class EnderecoPatioId implements Serializable {

    @Column(name = "tb_endereco_id_endereco", nullable = false)
    private Long enderecoId; // Tipo do campo PK referenciado em Endereco

    @Column(name = "tb_patio_id_patio", nullable = false)
    private Long patioId; // Tipo do campo PK referenciado em TbPatio
}