package br.com.fiap.mottu.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
// Importe java.util.Set se for adicionar relacionamentos inversos com ClienteVeiculo

@Entity
@Table(name = "TB_CLIENTE" // Mapeia para o nome em maiúsculas no BD
        // Removida anotação uniqueConstraints uq_cliente_cpf_cnpj,
        // pois a constraint UNIQUE UK_CLIENTE_CPF foi adicionada diretamente no DDL final.
        // Você pode adicionar uniqueConstraints aqui se quiser documentar no código ou usar geração de DDL pelo JPA.
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(callSuper = false) // Cuidado com EqualsAndHashCode em entidades JPA
public class Cliente { // Nome mantido

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente") // Nome da coluna em minúsculas no BD
    private Long idCliente; // Mapeia para a PK simplificada no BD

    @Column(name = "data_cadastro", nullable = false) // DDL é DATE com DEFAULT SYSDATE
    // Para preencher automaticamente com a data do BD, confie no DEFAULT SYSDATE no DDL.
    // Se usar Hibernate, @CreationTimestamp pode funcionar (depende da configuração).
    private LocalDate dataCadastro; // Use LocalDate para DATE sem hora

    @Column(name = "sexo", nullable = false, length = 2) // DDL é CHAR(2) com CHECK ('M','H')
    private String sexo;

    @Column(name = "nome", nullable = false, length = 100) // DDL é VARCHAR2(100)
    private String nome;

    @Column(name = "sobrenome", nullable = false, length = 100) // DDL é VARCHAR2(100)
    private String sobrenome;

    @Column(name = "data_nascimento", nullable = false) // DDL é DATE com CHECK (>= 1900)
    private LocalDate dataNascimento; // Use LocalDate. Validar range no Backend/Frontend e com CHECK/Trigger no BD.

    @Column(name = "cpf", nullable = false, length = 11) // DDL é CHAR(11) com UNIQUE
    private String cpf; // Validar formato e unicidade (garantida pelo UNIQUE no BD).

    @Column(name = "profissao", nullable = false, length = 50) // DDL é VARCHAR2(50)
    private String profissao;

    @Column(name = "estado_civil", nullable = false, length = 50) // DDL é VARCHAR2(50) com CHECK (lista fixa)
    private String estadoCivil;

    // Mapeamento da Chave Estrangeira para TB_ENDERECO
    @ManyToOne(fetch = FetchType.LAZY) // Lazy Loading para performance
    @JoinColumn(name = "tb_endereco_id_endereco", nullable = false) // Coluna FK real no BD
    private Endereco endereco; // Relacionamento Many-to-One com Endereco

    // Mapeamento da Chave Estrangeira para TB_CONTATO
    @ManyToOne(fetch = FetchType.LAZY) // Lazy Loading para performance
    @JoinColumn(name = "tb_contato_id_contato", nullable = false) // Coluna FK real no BD
    private Contato contato; // Relacionamento Many-to-One com Contato

    // Relacionamento inverso com a tabela de junção TB_CLIENTEVEICULO (opcional)
    // @OneToMany(mappedBy = "cliente")
    // private Set<ClienteVeiculo> veiculosAssociados;
}