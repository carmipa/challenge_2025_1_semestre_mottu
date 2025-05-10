// Caminho do arquivo: br\com\fiap\mottu\model\Contato.java
package br.com.fiap.mottu.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TB_CONTATO", schema = "CHALLENGE") // Adicionado schema
@ToString(exclude = {"clienteContatos", "contatoPatios"}) // Excluir relacionamentos
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Usar apenas campos incluídos para equals/hashCode
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CONTATO") // Nome da coluna em MAIÚSCULAS no BD
    @EqualsAndHashCode.Include // Incluir apenas o ID no cálculo de hash/equals
    private Long idContato;

    @Column(name = "EMAIL", nullable = false, length = 100) // DDL é VARCHAR2(100) NOT NULL
    private String email;

    @Column(name = "DDD", nullable = false, precision = 4, scale = 0) // DDL é NUMBER(4) NOT NULL
    private Integer ddd;

    @Column(name = "DDI", nullable = false, precision = 4, scale = 0) // DDL é NUMBER(4) NOT NULL
    private Integer ddi;

    @Column(name = "TELEFONE1", nullable = false, length = 20) // DDL é VARCHAR2(20) NOT NULL
    private String telefone1;

    @Column(name = "TELEFONE2", length = 20) // DDL é VARCHAR2(20)
    private String telefone2;

    @Column(name = "TELEFONE3", length = 20) // DDL é VARCHAR2(20)
    private String telefone3;

    @Column(name = "CELULAR", nullable = false, length = 20) // DDL é VARCHAR2(20) NOT NULL
    private String celular;

    @Column(name = "OUTRO", length = 100) // DDL é VARCHAR2(100)
    private String outro;

    @Column(name = "OBSERVACAO", length = 200) // DDL é VARCHAR2(200)
    private String observacao;

    // Relacionamentos inversos
    // Assumindo que TB_CLIENTE.TB_CONTATO_ID_CONTATO é uma FK e Contato pode ter vários Clientes
    @OneToMany(mappedBy = "contato", cascade = CascadeType.ALL) // Ajuste CascadeType conforme sua necessidade
    @ToString.Exclude
    private Set<Cliente> clienteContatos = new HashSet<>();

    @OneToMany(mappedBy = "contato", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<br.com.fiap.mottu.model.relacionamento.ContatoPatio> contatoPatios = new HashSet<>();
}