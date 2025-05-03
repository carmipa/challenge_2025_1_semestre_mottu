package br.com.fiap.mattu.model.filial;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_ENDERECO_FILIAL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnderecoFilial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ENDERECO_FILIAL")
    private long idEnderecoFilial;

    @Column(name = "CEP", nullable = false, length = 9)
    private String cep;

    @Column(name = "NUMERO", nullable = false)
    private int numero;

    @Column(name = "LOGRADOURO", nullable = false, length = 100)
    private String logradouro;

    @Column(name = "BAIRRO", nullable = false, length = 60)
    private String bairro;

    @Column(name = "CIDADE", nullable = false, length = 60)
    private String cidade;

    @Column(name = "ESTADO", nullable = false, length = 2)
    private String estado;

    @Column(name = "PAIS", nullable = false, length = 60)
    private String pais;

    @Column(name = "COMPLEMENTO", length = 100)
    private String complemento;
}
