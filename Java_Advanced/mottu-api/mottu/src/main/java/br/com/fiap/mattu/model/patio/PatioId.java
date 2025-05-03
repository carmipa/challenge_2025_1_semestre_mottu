package br.com.fiap.mattu.model.patio; // Ou onde preferir colocar as classes Id

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode // Lombok gera equals/hashCode baseado nos campos
public class PatioId implements Serializable {
    private static final long serialVersionUID = 1L; // Boa prática para Serializable

    @Column(name = "id_patio")
    private Long idPatio;

    @Column(name = "tb_cont_p_id_contp") // Coluna FK que faz parte da PK composta
    private Long contatoPatioId; // Referencia o ID de ContatoPatio

    @Column(name = "status", length = 1) // Coluna status que faz parte da PK composta
    private String status; // Mapeado para String (CHAR(1) no DB)

    @Column(name = "tb_end_p_id_endp") // Coluna FK que faz parte da PK composta
    private Long enderecoPatioId; // Referencia o ID de EnderecoPatio
}