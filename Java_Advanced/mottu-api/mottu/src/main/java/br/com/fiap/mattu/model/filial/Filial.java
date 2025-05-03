package br.com.fiap.mattu.model.filial;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "TB_FILIAL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class Filial {

    @EmbeddedId
    private FilialId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("enderecoFilialId")
    @JoinColumn(
            name = "tb_end_f_id_endf",
            // CORREÇÃO: referencedColumnName deve bater com a PK de EnderecoFilial
            referencedColumnName = "ID_ENDERECO_FILIAL", // <-- CORRIGIDO de "id_endf"
            insertable = false, updatable = false
    )
    private EnderecoFilial enderecoFilial;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(
            name = "tb_cont_f_id_contf",
            // CORREÇÃO: referencedColumnName deve bater com a PK de ContatoFilial
            referencedColumnName = "ID_CONTATO_FILIAL", // <-- CORRIGIDO de "id_contf"
            nullable = false // Adicionado pois optional=false e a FK no DDL é NOT NULL
    )
    private ContatoFilial contatoFilial;

    @Column(name = "NOME", nullable = false, unique = true, length = 50)
    private String nome;

    @Column(name = "STATUS", nullable = false, length = 1)
    private String status;

    @Column(name = "DATA_ENTRADA", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate dataEntrada;

    @Column(name = "DATA_SAIDA")
    @Temporal(TemporalType.DATE)
    private LocalDate dataSaida;

    @Column(name = "OBSERVACAO", length = 100)
    private String observacao;

    // Construtor Opcional (mantido como estava)
    public Filial(FilialId id, String nome, String status, LocalDate dataEntrada, LocalDate dataSaida, String observacao, EnderecoFilial enderecoFilial, ContatoFilial contatoFilial) {
        this.id = id;
        this.nome = nome;
        this.status = status;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.observacao = observacao;
        this.enderecoFilial = enderecoFilial;
        this.contatoFilial = contatoFilial;
    }
}