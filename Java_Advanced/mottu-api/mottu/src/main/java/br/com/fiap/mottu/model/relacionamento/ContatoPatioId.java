// Caminho do arquivo: br\com\fiap\mottu\model\relacionamento\ContatoPatioId.java
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

    @Column(name = "TB_PATIO_ID_PATIO", nullable = false) // Nome da coluna FK em MAIÚSCULAS
    private Long patioId;

    @Column(name = "TB_CONTATO_ID_CONTATO", nullable = false) // Nome da coluna FK em MAIÚSCULAS
    private Long contatoId;
}