package br.com.fiap.mottu.model;

import jakarta.persistence.*;
import lombok.*;
// Importe java.util.Set se for adicionar relacionamentos inversos com VeiculoRastreamento

@Entity
@Table(name = "TB_RASTREAMENTO") // Mapeia para o nome em maiúsculas no BD
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(callSuper = false) // Cuidado em entidades JPA
public class Rastreamento { // Renomeado de TbRastreamento para Rastreamento

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rastreamento") // Nome da coluna em minúsculas no BD
    private Long idRastreamento;

    @Column(name = "ips", nullable = false) // DDL é mdsys.sdo_geometry. Mapeado como String por simplicidade.
    private String ips; // Representação String dos dados IPS (ex: WKT). NOT NULL no BD.

    @Column(name = "gprs", nullable = false) // DDL é mdsys.sdo_geometry. Mapeado como String por simplicidade.
    private String gprs; // Representação String dos dados GPRS (ex: WKT). NOT NULL no BD.

    // Nota: Para usar a funcionalidade espacial, considere um mapeamento avançado (custom converter).

    // Relacionamento inverso com a tabela de junção TB_VEICULORASTREAMENTO (opcional)
    // @OneToMany(mappedBy = "rastreamento")
    // private Set<VeiculoRastreamento> veiculosAssociados;
}