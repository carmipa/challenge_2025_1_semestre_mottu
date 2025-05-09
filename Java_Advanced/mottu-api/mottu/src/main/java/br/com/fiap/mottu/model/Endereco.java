package br.com.fiap.mottu.model;

import jakarta.persistence.*;
import lombok.*;
// Importe java.util.Set se for adicionar relacionamentos inversos com EnderecoPatio ou Cliente

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TB_ENDERECO") // Mapeia para o nome em maiúsculas no BD
public class Endereco { // Nome mantido

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_endereco") // Nome da coluna em minúsculas no BD
    private Long idEndereco; // Renomeado para clareza

    @Column(name = "cep", nullable = false, length = 9) // DDL é CHAR(9)
    private String cep; // CHAR(9) pode ser mapeado como String

    @Column(name = "numero", nullable = false, precision = 7, scale = 0) // DDL é NUMBER(7) NOT NULL
    private Integer numero; // Use Integer para NUMBER sem casas decimais e com precisão pequena

    @Column(name = "logradouro", nullable = false, length = 50) // DDL é VARCHAR2(50)
    private String logradouro;

    @Column(name = "bairro", nullable = false, length = 50) // DDL é VARCHAR2(50)
    private String bairro;

    @Column(name = "cidade", nullable = false, length = 50) // DDL é VARCHAR2(50)
    private String cidade;

    @Column(name = "estado", nullable = false, length = 2) // DDL é CHAR(2)
    private String estado; // CHAR(2) pode ser mapeado como String

    @Column(name = "pais", nullable = false, length = 50) // DDL é VARCHAR2(50) NOT NULL
    private String pais;

    @Column(name = "complemento", length = 60) // DDL é VARCHAR2(60). Nullable por padrão.
    private String complemento;

    @Column(name = "observacao", length = 200) // DDL é VARCHAR2(200). Nullable por padrão.
    private String observacao;

    // Relacionamentos inversos (opcional, se precisar navegar)
    // @OneToMany(mappedBy = "endereco")
    // private Set<Cliente> clientes;

    // @OneToMany(mappedBy = "endereco")
    // private Set<EnderecoPatio> enderecosPatio;
}