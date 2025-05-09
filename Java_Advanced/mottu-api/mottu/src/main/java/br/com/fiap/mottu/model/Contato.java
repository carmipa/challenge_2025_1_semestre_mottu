package br.com.fiap.mottu.model;

import jakarta.persistence.*;
import lombok.*;
// Importe java.util.Set se for adicionar relacionamentos inversos com ContatoPatio ou Cliente

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TB_CONTATO") // Mapeia para o nome em maiúsculas no BD
public class Contato { // Nome mantido

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contato") // Nome da coluna em minúsculas no BD
    private Long idContato; // Renomeado para clareza

    @Column(name = "email", nullable = false, length = 100) // DDL é VARCHAR2(100)
    private String email; // UNIQUE no DDL final

    @Column(name = "ddd", nullable = false, precision = 4, scale = 0) // DDL é NUMBER(4) com CHECK range
    private Integer ddd; // Integer ou Short

    @Column(name = "ddi", nullable = false, precision = 4, scale = 0) // DDL é NUMBER(4)
    private Integer ddi; // Integer ou Short

    @Column(name = "telefone1", nullable = false, length = 20) // DDL é VARCHAR2(20)
    private String telefone1;

    @Column(name = "telefone2", length = 20) // DDL é VARCHAR2(20). Nullable por padrão.
    private String telefone2;

    @Column(name = "telefone3", length = 20) // DDL é VARCHAR2(20). Nullable por padrão.
    private String telefone3;

    @Column(name = "celular", nullable = false, length = 20) // DDL é VARCHAR2(20)
    private String celular;

    @Column(name = "outro", length = 100) // DDL é VARCHAR2(100). Nullable por padrão.
    private String outro;

    @Column(name = "observacao", length = 200) // DDL é VARCHAR2(200). Nullable por padrão.
    private String observacao;

    // Relacionamentos inversos (opcional, se precisar navegar)
    // @OneToMany(mappedBy = "contato")
    // private Set<Cliente> clientes;

    // @OneToMany(mappedBy = "contato")
    // private Set<ContatoPatio> patioContatos;
}