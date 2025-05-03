package br.com.fiap.mattu.model.filial;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_CONTATO_FILIAL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContatoFilial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CONTATO_FILIAL")
    private long idContatoFilial;

    @Column(name = "EMAIL", nullable = false, length = 100)
    private String email;

    @Column(name = "TELEFONE1", length = 20)
    private String telefone1;

    @Column(name = "TELEFONE2", length = 20)
    private String telefone2;

    @Column(name = "OUTRO", length = 100)
    private String outro;
}
