package br.com.fiap.mattu.model.patio;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_CONTATO_PATIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContatoPatio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CONTATO_PATIO")
    private long idContatoPatio;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "TELEFONE1", length = 15)
    private String telefone1;

    @Column(name = "TELEFONE2", length = 15)
    private String telefone2;

    @Column(name = "OUTRO", length = 15)
    private String outro;
}
