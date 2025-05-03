package br.com.fiap.mattu.model.cliente;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_END_C")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnderecoCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ENDC")
    private Long idEndereco;

    @Column(name = "CEP", length = 9, nullable = false)
    private String cep;

    @Column(name = "NUMERO", nullable = false)
    private Integer numero;

    @Column(name = "LOGRADOURO", length = 50, nullable = false)
    private String logradouro;

    @Column(name = "BAIRRO", length = 50, nullable = false)
    private String bairro;

    @Column(name = "CIDADE", length = 50, nullable = false)
    private String cidade;

    @Column(name = "ESTADO", length = 2, nullable = false)
    private String estado;

    @Column(name = "PAIS", length = 20, nullable = false)
    private String pais;

    @Column(name = "COMPLEMENTO", length = 60)
    private String complemento;
}
