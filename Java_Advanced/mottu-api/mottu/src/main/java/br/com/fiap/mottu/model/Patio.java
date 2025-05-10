// Caminho do arquivo: br\com\fiap\mottu\model\Patio.java
package br.com.fiap.mottu.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TB_PATIO", schema = "CHALLENGE") // Adicionado schema
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"contatoPatios", "enderecoPatios", "veiculoPatios", "zonaPatios"}) // Excluir relacionamentos
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Usar apenas campos incluídos para equals/hashCode
public class Patio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PATIO") // Nome da coluna em MAIÚSCULAS no BD
    @EqualsAndHashCode.Include // Incluir apenas o ID no cálculo de hash/equals
    private Long idPatio;

    @Column(name = "NOME_PATIO", nullable = false, length = 50) // DDL é VARCHAR2(50) NOT NULL
    private String nomePatio;

    @Column(name = "DATA_ENTRADA", nullable = false) // DDL é DATE NOT NULL
    private LocalDate dataEntrada;

    @Column(name = "DATA_SAIDA", nullable = false) // DDL é DATE NOT NULL
    private LocalDate dataSaida;

    @Column(name = "OBSERVACAO", length = 100) // DDL é VARCHAR2(100)
    private String observacao;

    // Relacionamentos inversos
    @OneToMany(mappedBy = "patio", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<br.com.fiap.mottu.model.relacionamento.ContatoPatio> contatoPatios = new HashSet<>();

    @OneToMany(mappedBy = "patio", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<br.com.fiap.mottu.model.relacionamento.EnderecoPatio> enderecoPatios = new HashSet<>();

    @OneToMany(mappedBy = "patio", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<br.com.fiap.mottu.model.relacionamento.VeiculoPatio> veiculoPatios = new HashSet<>();

    @OneToMany(mappedBy = "patio", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<br.com.fiap.mottu.model.relacionamento.ZonaPatio> zonaPatios = new HashSet<>();
}