package br.com.fiap.mattu.model.cliente;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate; // Usando java.time consistentemente

@Entity
@Table(name = "TB_CLI")
@Getter // Preferir @Getter/@Setter em vez de @Data para entidades JPA
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id") // Basear equals/hashCode na chave composta
@Builder
public class Cliente {

    // Chave Primária Composta - usa a classe ClienteId
    @EmbeddedId
    private ClienteId id;

    // Relacionamento mapeado para a parte 'enderecoClienteId' do EmbeddedId
    @ManyToOne(fetch = FetchType.LAZY) // Usar ManyToOne e LAZY
    @MapsId("enderecoClienteId") // Vincula ao atributo 'enderecoClienteId' em ClienteId
    @JoinColumn(
            name = "tb_end_c_id_endc", // Coluna FK em TB_CLI
            referencedColumnName = "id_endc", // Coluna PK em TB_END_C (assumindo que é 'id_endc')
            insertable = false, updatable = false // Obrigatório com @MapsId
    )
    private EnderecoCliente enderecoCliente;

    // Relacionamento mapeado para a parte 'contatoClienteId' do EmbeddedId
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("contatoClienteId") // Vincula ao atributo 'contatoClienteId' em ClienteId
    @JoinColumn(
            name = "tb_cont_c_id_contc", // Coluna FK em TB_CLI
            referencedColumnName = "id_contc", // Coluna PK em TB_CONT_C (assumindo que é 'id_contc')
            insertable = false, updatable = false // Obrigatório com @MapsId
    )
    private ContatoCliente contatoCliente;

    // --- Outros campos da tabela ---

    // O campo 'id_cli' já está mapeado DENTRO do ClienteId

    // Os campos 'tb_end_c_id_endc' e 'tb_cont_c_id_contc' também estão no ClienteId
    // e são preenchidos/gerenciados através dos relacionamentos com @MapsId.
    // Não precisamos mais dos atributos 'enderecoId' e 'contatoId'.

    @Column(name = "DATA_CADASTRO", nullable = false, updatable = false)
    @Temporal(TemporalType.DATE) // Mapeando para DATE
    private LocalDate dataCadastro; // Usando LocalDate

    // 🔹 Método automático para definir a data de cadastro ao criar um objeto
    @PrePersist
    protected void onCreate() {
        this.dataCadastro = LocalDate.now();
    }

    @Column(name = "SEXO", length = 1, nullable = false)
    private String sexo; // CHAR(1) pode ser mapeado como String

    @Column(name = "NOME", length = 50, nullable = false)
    private String nome;

    @Column(name = "SOBRENOME", length = 50, nullable = false)
    private String sobrenome;

    @Column(name = "DATA_NASCIMENTO", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate dataNascimento; // Usando LocalDate

    @Column(name = "CPF", length = 11, nullable = false, unique = true)
    private String cpf;

    @Column(name = "PROFISSAO", length = 50) // Corrigido para 'profisao' conforme DDL
    private String profissao;

    @Column(name = "ESTADO_CIVIL", length = 50)
    private String estadoCivil;

    @Column(name = "STATUS", length = 1, nullable = false)
    private String status; // CHAR(1) pode ser mapeado como String

    // Construtor (opcional, dependendo do uso de Lombok @Builder/@AllArgsConstructor)
    public Cliente(ClienteId id, LocalDate dataCadastro, String sexo, String nome, String sobrenome, LocalDate dataNascimento, String cpf, String profissao, String estadoCivil, String status, EnderecoCliente enderecoCliente, ContatoCliente contatoCliente) {
        this.id = id;
        this.dataCadastro = dataCadastro;
        this.sexo = sexo;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.profissao = profissao;
        this.estadoCivil = estadoCivil;
        this.status = status;
        this.enderecoCliente = enderecoCliente;
        this.contatoCliente = contatoCliente;
    }
}