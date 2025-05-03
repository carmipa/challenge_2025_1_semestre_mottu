package br.com.fiap.mattu.model.patio;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "TB_PATIO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class Patio {

    @EmbeddedId
    private PatioId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("contatoPatioId")
    @JoinColumn(
            name = "tb_cont_p_id_contp",
            // CORREÇÃO: referencedColumnName deve bater com a PK de ContatoPatio
            referencedColumnName = "ID_CONTATO_PATIO", // <-- CORRIGIDO de "id_contp"
            insertable = false, updatable = false
    )
    private ContatoPatio contatoPatio;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("enderecoPatioId")
    @JoinColumn(
            name = "tb_end_p_id_endp",
            // CORREÇÃO: referencedColumnName deve bater com a PK de EnderecoPatio
            referencedColumnName = "ID_ENDERECO_PATIO", // <-- CORRIGIDO de "id_endp"
            insertable = false, updatable = false
    )
    private EnderecoPatio enderecoPatio;

    @Column(name = "NOME_PATIO", nullable = false, unique = true, length = 50)
    private String nomePatio;

    // 'status' está no PatioId
    // 'id_patio' está no PatioId

    @Column(name = "DATA_ENTRADA", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate dataEntrada;

    @Column(name = "DATA_SAIDA")
    @Temporal(TemporalType.DATE)
    private LocalDate dataSaida;

    @Column(name = "OBSERVACAO", length = 100)
    private String observacao;

    // Construtor Opcional (mantido como estava)
    public Patio(PatioId id, String nomePatio, LocalDate dataEntrada, LocalDate dataSaida, String observacao, ContatoPatio contatoPatio, EnderecoPatio enderecoPatio) {
        this.id = id;
        this.nomePatio = nomePatio;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.observacao = observacao;
        this.contatoPatio = contatoPatio;
        this.enderecoPatio = enderecoPatio;
    }
}