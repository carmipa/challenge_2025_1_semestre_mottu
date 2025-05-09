package br.com.fiap.mottu.model.relacionamento;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode // Essencial para PKs compostas
public class ContatoPatioId implements Serializable {

    @Column(name = "tb_patio_id_patio", nullable = false) // Nome da coluna FK no BD
    private Long patioId; // Tipo do campo PK referenciado em TbPatio

    @Column(name = "tb_contato_id_contato", nullable = false) // Nome da coluna FK no BD
    private Long contatoId; // Tipo do campo PK referenciado em Contato
}