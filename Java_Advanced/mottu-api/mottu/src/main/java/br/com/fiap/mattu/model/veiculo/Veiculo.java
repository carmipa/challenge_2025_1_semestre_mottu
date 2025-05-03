package br.com.fiap.mattu.model.veiculo;

import br.com.fiap.mattu.model.cliente.Cliente;
import br.com.fiap.mattu.model.filial.Filial;
import br.com.fiap.mattu.model.patio.Patio;
import br.com.fiap.mattu.model.zona.Zona;
import br.com.fiap.mattu.model.box.Box;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_VEICULO")
@Data // Pode manter @Data ou usar @Getter/@Setter se preferir
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VEICULO")
    private long idVeiculo;

    @Column(name = "PLACA", length = 10, nullable = false, unique = true)
    private String placa;

    @Column(name = "RENAVAM", length = 20, nullable = false, unique = true)
    private String renavam;

    @Column(name = "CHASSI", length = 25, nullable = false, unique = true)
    private String chassi;

    @Column(name = "FABRICANTE", length = 50)
    private String fabricante;

    @Column(name = "MODELO", length = 50)
    private String modelo;

    @Column(name = "MOTOR", length = 30)
    private String motor;

    @Column(name = "ANO")
    private int ano;

    @Column(name = "COMBUSTIVEL", length = 20)
    private String combustivel;

    @Column(name = "STATUS")
    private boolean status;

    // --- CORREÇÕES ABAIXO ---
    // Relacionamentos com entidades de CHAVE COMPOSTA precisam de @JoinColumns

    @ManyToOne(fetch = FetchType.LAZY)
    // CORREÇÃO: Usar @JoinColumns para chave composta de Cliente
    @JoinColumns({
            // ATENÇÃO: Verifique se os nomes das colunas FK ('name=...') existem na sua tabela TB_VEICULO
            @JoinColumn(name = "TB_CLI_ID_CLI", referencedColumnName = "id_cli"), // Mapeia para id_cli em ClienteId
            @JoinColumn(name = "TB_CLI_TB_END_C_ID_ENDC", referencedColumnName = "tb_end_c_id_endc"), // Mapeia para tb_end_c_id_endc em ClienteId
            @JoinColumn(name = "TB_CLI_TB_CONT_C_ID_CONTC", referencedColumnName = "tb_cont_c_id_contc") // Mapeia para tb_cont_c_id_contc em ClienteId
    })
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    // CORREÇÃO: Usar @JoinColumns para chave composta de Filial
    @JoinColumns({
            // ATENÇÃO: Verifique se os nomes das colunas FK ('name=...') existem na sua tabela TB_VEICULO
            @JoinColumn(name = "TB_FILIAL_ID_FIL", referencedColumnName = "id_fil"), // Mapeia para id_fil em FilialId
            @JoinColumn(name = "TB_FILIAL_TB_END_F_ID_ENDF", referencedColumnName = "tb_end_f_id_endf") // Mapeia para tb_end_f_id_endf em FilialId
    })
    private Filial filial;

    @ManyToOne(fetch = FetchType.LAZY)
    // CORREÇÃO: Usar @JoinColumns para chave composta de Patio
    @JoinColumns({
            // ATENÇÃO: Verifique se os nomes das colunas FK ('name=...') existem na sua tabela TB_VEICULO
            @JoinColumn(name = "TB_PATIO_ID_PATIO", referencedColumnName = "id_patio"),               // Mapeia para id_patio em PatioId
            @JoinColumn(name = "TB_PATIO_TB_CONT_P_ID_CONTP", referencedColumnName = "tb_cont_p_id_contp"), // Mapeia para tb_cont_p_id_contp em PatioId
            @JoinColumn(name = "TB_PATIO_STATUS", referencedColumnName = "status"),                 // Mapeia para status em PatioId
            @JoinColumn(name = "TB_PATIO_TB_END_P_ID_ENDP", referencedColumnName = "tb_end_p_id_endp")   // Mapeia para tb_end_p_id_endp em PatioId
    })
    private Patio patio;

    // Relacionamentos com entidades de CHAVE SIMPLES podem usar @JoinColumn

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TB_ZONA_ID_ZONA", referencedColumnName = "ID_ZONA") // Assumindo PK de Zona é ID_ZONA
    private Zona zona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TB_BOX_ID_BOX", referencedColumnName = "ID_BOX") // Assumindo PK de Box é ID_BOX
    private Box box;
}