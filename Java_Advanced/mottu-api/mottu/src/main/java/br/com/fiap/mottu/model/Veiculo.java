// Caminho do arquivo: br\com\fiap\mottu\model\Veiculo.java
package br.com.fiap.mottu.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TB_VEICULO", schema = "CHALLENGE") // Adicionado schema
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"clienteVeiculos", "veiculoBoxes", "veiculoPatios", "veiculoRastreamentos", "veiculoZonas"}) // Excluir relacionamentos
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Usar apenas campos incluídos para equals/hashCode
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VEICULO") // Nome da coluna em MAIÚSCULAS no BD
    @EqualsAndHashCode.Include // Incluir apenas o ID no cálculo de hash/equals
    private Long idVeiculo;

    @Column(name = "PLACA", nullable = false, unique = true, length = 10) // DDL é VARCHAR2(10) NOT NULL, UNIQUE
    private String placa;

    @Column(name = "RENAVAM", nullable = false, unique = true, length = 11) // DDL é CHAR(11) NOT NULL, UNIQUE
    private String renavam;

    @Column(name = "CHASSI", nullable = false, unique = true, length = 17) // DDL é CHAR(17) NOT NULL, UNIQUE
    private String chassi;

    @Column(name = "FABRICANTE", nullable = false, length = 50) // DDL é VARCHAR2(50) NOT NULL
    private String fabricante;

    @Column(name = "MODELO", nullable = false, length = 60) // DDL é VARCHAR2(60) NOT NULL
    private String modelo;

    @Column(name = "MOTOR", length = 30) // DDL é VARCHAR2(30). Nullable por padrão.
    private String motor;

    @Column(name = "ANO", nullable = false) // DDL é NUMBER NOT NULL
    private Integer ano;

    @Column(name = "COMBUSTIVEL", nullable = false, length = 20) // DDL é VARCHAR2(20) NOT NULL
    private String combustivel;

    // Relacionamentos inversos
    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<br.com.fiap.mottu.model.relacionamento.ClienteVeiculo> clienteVeiculos = new HashSet<>();

    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<br.com.fiap.mottu.model.relacionamento.VeiculoBox> veiculoBoxes = new HashSet<>();

    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<br.com.fiap.mottu.model.relacionamento.VeiculoPatio> veiculoPatios = new HashSet<>();

    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<br.com.fiap.mottu.model.relacionamento.VeiculoRastreamento> veiculoRastreamentos = new HashSet<>();

    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<br.com.fiap.mottu.model.relacionamento.VeiculoZona> veiculoZonas = new HashSet<>();
}