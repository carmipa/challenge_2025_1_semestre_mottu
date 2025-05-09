package br.com.fiap.mottu.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
// Importe java.util.Set se for adicionar relacionamentos inversos com VeiculoZona, ZonaBox, ZonaPatio

@Entity
@Table(name = "TB_ZONA") // Mapeia para o nome em maiúsculas no BD
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(callSuper = false) // Cuidado em entidades JPA
public class Zona { // Renomeado de TbZona para Zona

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zona") // Nome da coluna em minúsculas no BD
    private Long idZona;

    @Column(name = "nome", nullable = false, length = 50) // DDL é VARCHAR2(50)
    private String nome;

    @Column(name = "data_entrada", nullable = false) // DDL é DATE
    private LocalDate dataEntrada; // Use LocalDate para DATE sem hora

    @Column(name = "data_saida", nullable = false) // DDL é DATE
    private LocalDate dataSaida; // Use LocalDate para DATE sem hora

    @Column(name = "observacao", length = 100) // DDL é VARCHAR2(100). Nullable por padrão.
    private String observacao;

    // Relacionamentos inversos (opcional, se precisar navegar)
    // @OneToMany(mappedBy = "zona")
    // private Set<VeiculoZona> veiculosNestaZona;

    // @OneToMany(mappedBy = "zona")
    // private Set<ZonaBox> boxesNestaZona; // Uma zona pode ter vários boxes

    // @OneToMany(mappedBy = "zona")
    // private Set<ZonaPatio> patiosDestaZona; // Uma zona pode estar associada a vários pátios? Conforme o modelo permite.
}