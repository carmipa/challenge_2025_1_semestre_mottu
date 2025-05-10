// Caminho do arquivo: br\com\fiap\mottu\model\Box.java
package br.com.fiap.mottu.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TB_BOX", schema = "CHALLENGE") // Adicionado schema
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"veiculoBoxes", "zonaBoxes"}) // Excluir relacionamentos no toString
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Usar apenas campos incluídos para equals/hashCode
public class Box {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_BOX") // Nome da coluna em MAIÚSCULAS no BD
    @EqualsAndHashCode.Include // Incluir apenas o ID no cálculo de hash/equals
    private Long idBox;

    @Column(name = "NOME", nullable = false, length = 50) // DDL é VARCHAR2(50) NOT NULL
    private String nome;

    @Column(name = "STATUS", nullable = false, length = 1) // DDL é CHAR(1) NOT NULL
    private String status;

    @Column(name = "DATA_ENTRADA", nullable = false) // DDL é DATE NOT NULL
    private LocalDate dataEntrada;

    @Column(name = "DATA_SAIDA", nullable = false) // DDL é DATE NOT NULL
    private LocalDate dataSaida;

    @Column(name = "OBSERVACAO", length = 100) // DDL é VARCHAR2(100). Nullable por padrão.
    private String observacao;

    // Relacionamentos inversos para tabelas de junção
    @OneToMany(mappedBy = "box", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<br.com.fiap.mottu.model.relacionamento.VeiculoBox> veiculoBoxes = new HashSet<>();

    @OneToMany(mappedBy = "box", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<br.com.fiap.mottu.model.relacionamento.ZonaBox> zonaBoxes = new HashSet<>();
}