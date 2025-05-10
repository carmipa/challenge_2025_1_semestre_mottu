// Caminho do arquivo: br\com\fiap\mottu\model\Cliente.java
package br.com.fiap.mottu.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TB_CLIENTE", schema = "CHALLENGE") // Adicionado schema
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"endereco", "contato", "clienteVeiculos"}) // Excluir relacionamentos
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Usar apenas campos incluídos para equals/hashCode
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CLIENTE") // Nome da coluna em MAIÚSCULAS no BD
    @EqualsAndHashCode.Include // Incluir apenas o ID no cálculo de hash/equals
    private Long idCliente;

    @Column(name = "DATA_CADASTRO", nullable = false, updatable = false) // DDL é DATE com DEFAULT SYSDATE. updatable = false
    private LocalDate dataCadastro;

    @Column(name = "SEXO", nullable = false, length = 2) // DDL é CHAR(2) NOT NULL
    private String sexo;

    @Column(name = "NOME", nullable = false, length = 100) // DDL é VARCHAR2(100) NOT NULL
    private String nome;

    @Column(name = "SOBRENOME", nullable = false, length = 100) // DDL é VARCHAR2(100) NOT NULL
    private String sobrenome;

    @Column(name = "DATA_NASCIMENTO", nullable = false) // DDL é DATE NOT NULL
    private LocalDate dataNascimento;

    @Column(name = "CPF", nullable = false, unique = true, length = 11) // DDL é CHAR(11) NOT NULL, UNIQUE
    private String cpf;

    @Column(name = "PROFISSAO", nullable = false, length = 50) // DDL é VARCHAR2(50) NOT NULL
    private String profissao;

    @Column(name = "ESTADO_CIVIL", nullable = false, length = 50) // DDL é VARCHAR2(50) NOT NULL
    private String estadoCivil;

    // Mapeamento da Chave Estrangeira para TB_ENDERECO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TB_ENDERECO_ID_ENDERECO", nullable = false) // Coluna FK real no BD
    @ToString.Exclude
    private Endereco endereco;

    // Mapeamento da Chave Estrangeira para TB_CONTATO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TB_CONTATO_ID_CONTATO", nullable = false) // Coluna FK real no BD
    @ToString.Exclude
    private Contato contato;

    // Relacionamento inverso com a tabela de junção TB_CLIENTEVEICULO
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<br.com.fiap.mottu.model.relacionamento.ClienteVeiculo> clienteVeiculos = new HashSet<>();
}