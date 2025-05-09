package br.com.fiap.mottu.model;

import jakarta.persistence.*;
import lombok.*;
// Importe java.util.Set se for adicionar relacionamentos inversos com ClienteVeiculo, VeiculoBox, VeiculoPatio, VeiculoRastreamento, VeiculoZona

@Entity
@Table(name = "TB_VEICULO") // Mapeia para o nome em maiúsculas no BD
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(callSuper = false) // Cuidado em entidades JPA
public class Veiculo { // Renomeado de TbVeiculo para Veiculo

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_veiculo") // Nome da coluna em minúsculas no BD
    private Long idVeiculo;

    @Column(name = "placa", nullable = false, length = 10) // DDL é VARCHAR2(10) com UNIQUE
    private String placa; // Validar formato. Unicidade garantida pelo BD.

    @Column(name = "renavam", nullable = false, length = 11) // DDL é CHAR(11) com UNIQUE
    private String renavam; // CHAR(11) pode ser String. Validar formato. Unicidade garantida pelo BD.

    @Column(name = "chassi", nullable = false, length = 17) // DDL é CHAR(17) com UNIQUE
    private String chassi; // CHAR(17) pode ser String. Validar formato. Unicidade garantida pelo BD.

    @Column(name = "fabricante", nullable = false, length = 50) // DDL é VARCHAR2(50)
    private String fabricante;

    @Column(name = "modelo", nullable = false, length = 60) // DDL é VARCHAR2(60)
    private String modelo;

    @Column(name = "motor", length = 30) // DDL é VARCHAR2(30). Nullable por padrão.
    private String motor;

    @Column(name = "ano", nullable = false) // DDL é NUMBER
    private Integer ano; // Use Integer ou Short dependendo do range de anos

    @Column(name = "combustivel", nullable = false, length = 20) // DDL é VARCHAR2(20)
    private String combustivel; // Validar lista de valores no Backend/Frontend/BD

    // Relacionamentos inversos (opcional, se precisar navegar)
    // @OneToMany(mappedBy = "veiculo")
    // private Set<ClienteVeiculo> clientesAssociados;

    // @OneToMany(mappedBy = "veiculo")
    // private Set<VeiculoBox> boxesAssociados;

    // @OneToMany(mappedBy = "veiculo")
    // private Set<VeiculoPatio> patiosAssociados;

    // @OneToMany(mappedBy = "veiculo")
    // private Set<VeiculoRastreamento> rastreamentosAssociados; // Vários registros de rastreamento para um veículo

    // @OneToMany(mappedBy = "veiculo")
    // private Set<VeiculoZona> zonasAssociadas;
}