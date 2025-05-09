package br.com.fiap.mottu.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
// Importe java.util.Set se for adicionar relacionamentos inversos com VeiculoBox e ZonaBox

@Entity
@Table(name = "TB_BOX") // Mapeia para o nome em maiúsculas no BD
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(callSuper = false) // Cuidado em entidades JPA
public class Box { // Renomeado de TbBox para Box conforme sua convenção

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_box") // Nome da coluna em minúsculas no BD
    private Long idBox;

    @Column(name = "nome", nullable = false, length = 50) // DDL é VARCHAR2(50)
    private String nome;

    @Column(name = "status", nullable = false, length = 1) // DDL é CHAR(1)
    private String status; // 'L' (Livre), 'O' (Ocupado), etc. Validar no Backend/Frontend/BD.

    @Column(name = "data_entrada", nullable = false) // DDL é DATE
    private LocalDate dataEntrada; // Use LocalDate para DATE sem hora

    @Column(name = "data_saida", nullable = false) // DDL é DATE
    private LocalDate dataSaida; // Use LocalDate para DATE sem hora

    @Column(name = "observacao", length = 100) // DDL é VARCHAR2(100). Nullable por padrão.
    private String observacao;

    // Relacionamentos inversos para tabelas de junção (opcional, se precisar navegar)
    // @OneToMany(mappedBy = "box")
    // private Set<VeiculoBox> veiculosNoBox;

    // @OneToMany(mappedBy = "box")
    // private Set<ZonaBox> zonasDoBox; // Uma box pode estar associada a várias zonas? Conforme o modelo permite.
}