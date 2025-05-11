// main\java\br\com\fiap\mottu\model\Cliente.java
package br.com.fiap.mottu.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TB_CLIENTE", schema = "CHALLENGE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CLIENTE")
    @EqualsAndHashCode.Include
    private Long idCliente;
    @Column(name = "DATA_CADASTRO", nullable = false, updatable = false)
    private LocalDate dataCadastro;

    @Column(name = "SEXO", nullable = false, length = 2) // Linha a ser restaurada para este estado
    private String sexo;
    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;
    @Column(name = "SOBRENOME", nullable = false, length = 100)
    private String sobrenome;
    @Column(name = "DATA_NASCIMENTO", nullable = false)
    private LocalDate dataNascimento;
    @Column(name = "CPF", nullable = false, unique = true, length = 11) // Linha a ser restaurada para este estado
    private String cpf;
    @Column(name = "PROFISSAO", nullable = false, length = 50)
    private String profissao;
    @Column(name = "ESTADO_CIVIL", nullable = false, length = 50)
    private String estadoCivil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TB_ENDERECO_ID_ENDERECO", nullable = false)
    @ToString.Exclude
    private Endereco endereco;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TB_CONTATO_ID_CONTATO", nullable = false)
    @ToString.Exclude
    private Contato contato;
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    private Set<br.com.fiap.mottu.model.relacionamento.ClienteVeiculo> clienteVeiculos = new HashSet<>();
}