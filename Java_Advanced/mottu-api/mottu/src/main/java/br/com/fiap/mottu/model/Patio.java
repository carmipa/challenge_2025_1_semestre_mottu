package br.com.fiap.mottu.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
// Importe java.util.Set se for adicionar relacionamentos inversos com ContatoPatio, EnderecoPatio, VeiculoPatio, ZonaPatio

@Entity
@Table(name = "TB_PATIO") // Mapeia para o nome em maiúsculas no BD
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(callSuper = false) // Cuidado em entidades JPA
public class Patio { // Renomeado de TbPatio para Patio

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_patio") // Nome da coluna em minúsculas no BD
    private Long idPatio;

    @Column(name = "nome_patio", nullable = false, length = 50) // DDL é VARCHAR2(50)
    private String nomePatio;

    @Column(name = "data_entrada", nullable = false) // DDL é DATE
    private LocalDate dataEntrada; // Use LocalDate para DATE sem hora

    @Column(name = "data_saida", nullable = false) // DDL é DATE
    private LocalDate dataSaida; // Use LocalDate para DATE sem hora

    @Column(name = "observacao", length = 100) // DDL é VARCHAR2(100). Nullable por padrão.
    private String observacao;

    // Relacionamentos inversos (opcional, se precisar navegar)
    // @OneToMany(mappedBy = "patio")
    // private Set<ContatoPatio> contatosDoPatio;

    // @OneToMany(mappedBy = "patio")
    // private Set<EnderecoPatio> enderecosDoPatio;

    // @OneToMany(mappedBy = "patio")
    // private Set<VeiculoPatio> veiculosNoPatio;

    // @OneToMany(mappedBy = "patio")
    // private Set<ZonaPatio> zonasDoPatio;
}