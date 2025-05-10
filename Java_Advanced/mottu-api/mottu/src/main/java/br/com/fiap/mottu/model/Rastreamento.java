// Caminho do arquivo: br\com\fiap\mottu\model\Rastreamento.java
package br.com.fiap.mottu.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TB_RASTREAMENTO", schema = "CHALLENGE") // Adicionado schema
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"veiculoRastreamentos"}) // Excluir relacionamentos
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Usar apenas campos incluídos para equals/hashCode
public class Rastreamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RASTREAMENTO") // Nome da coluna em MAIÚSCULAS no BD
    @EqualsAndHashCode.Include // Incluir apenas o ID no cálculo de hash/equals
    private Long idRastreamento;

    @Column(name = "IPS", nullable = false) // DDL é mdsys.sdo_geometry. Mapeado como String por simplicidade.
    private String ips; // Representação String dos dados IPS (ex: WKT). NOT NULL no BD.

    @Column(name = "GPRS", nullable = false) // DDL é mdsys.sdo_geometry. Mapeado como String por simplicidade.
    private String gprs; // Representação String dos dados GPRS (ex: WKT). NOT NULL no BD.

    // Nota: Para usar a funcionalidade espacial, considere a biblioteca `hibernate-spatial`
    // e um UserType customizado para mapear SDO_GEOMETRY.

    // Relacionamento inverso com a tabela de junção TB_VEICULORASTREAMENTO
    @OneToMany(mappedBy = "rastreamento", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<br.com.fiap.mottu.model.relacionamento.VeiculoRastreamento> veiculoRastreamentos = new HashSet<>();
}