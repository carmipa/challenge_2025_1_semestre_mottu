// Caminho do arquivo: br\com\fiap\mottu\model\Endereco.java
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
@Table(name = "TB_ENDERECO", schema = "CHALLENGE") // Adicionado schema
@ToString(exclude = {"clienteEnderecos", "enderecoPatios"}) // Excluir relacionamentos
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Usar apenas campos incluídos para equals/hashCode
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ENDERECO") // Nome da coluna em MAIÚSCULAS no BD
    @EqualsAndHashCode.Include // Incluir apenas o ID no cálculo de hash/equals
    private Long idEndereco;

    @Column(name = "CEP", nullable = false, length = 9) // DDL é CHAR(9) NOT NULL
    private String cep;

    @Column(name = "NUMERO", nullable = false, precision = 7, scale = 0) // DDL é NUMBER(7) NOT NULL
    private Integer numero;

    @Column(name = "LOGRADOURO", nullable = false, length = 50) // DDL é VARCHAR2(50) NOT NULL
    private String logradouro;

    @Column(name = "BAIRRO", nullable = false, length = 50) // DDL é VARCHAR2(50) NOT NULL
    private String bairro;

    @Column(name = "CIDADE", nullable = false, length = 50) // DDL é VARCHAR2(50) NOT NULL
    private String cidade;

    @Column(name = "ESTADO", nullable = false, length = 2) // DDL é CHAR(2) NOT NULL
    private String estado;

    @Column(name = "PAIS", nullable = false, length = 50) // DDL é VARCHAR2(50) NOT NULL
    private String pais;

    @Column(name = "COMPLEMENTO", length = 60) // DDL é VARCHAR2(60)
    private String complemento;

    @Column(name = "OBSERVACAO", length = 200) // DDL é VARCHAR2(200)
    private String observacao;

    // Relacionamentos inversos
    @OneToMany(mappedBy = "endereco", cascade = CascadeType.ALL) // Ajuste CascadeType conforme sua necessidade
    @ToString.Exclude
    private Set<Cliente> clienteEnderecos = new HashSet<>();

    @OneToMany(mappedBy = "endereco", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<br.com.fiap.mottu.model.relacionamento.EnderecoPatio> enderecoPatios = new HashSet<>();
}