package br.com.fiap.mattu.model.cliente;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_CONT_C")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContatoCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CONTC")
    private Long idContato;

    @Column(name = "EMAIL", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "TELEFONE", length = 20)
    private String telefone;

    @Column(name = "CELULAR", length = 20, nullable = false)
    private String celular;

    @Column(name = "OUTRO", length = 100)
    private String outro;
}
