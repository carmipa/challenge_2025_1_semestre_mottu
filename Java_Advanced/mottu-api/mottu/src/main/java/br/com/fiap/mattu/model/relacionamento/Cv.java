package br.com.fiap.mattu.model.relacionamento;

import br.com.fiap.mattu.model.cliente.Cliente; // Ajuste o import se necessário
import br.com.fiap.mattu.model.veiculo.Veiculo; // Ajuste o import se necessário
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_cv")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id") // Baseado no ID embutido
public class Cv {

    @EmbeddedId
    private CvId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("clienteIdCli") // Mapeia a PARTE 'clienteIdCli' do CvId para a entidade Cliente
    @JoinColumns({
            @JoinColumn(name = "tb_cli_id_cli", referencedColumnName = "id_cli", insertable = false, updatable = false),
            @JoinColumn(name = "tb_cli_tb_end_c_id_endc", referencedColumnName = "tb_end_c_id_endc", insertable = false, updatable = false),
            @JoinColumn(name = "tb_cli_tb_cont_c_id_contc", referencedColumnName = "tb_cont_c_id_contc", insertable = false, updatable = false)
    })
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("veiculoId") // Mapeia a PARTE 'veiculoId' do CvId para a entidade Veiculo
    @JoinColumn(name = "tb_veiculo_id_veiculo", referencedColumnName = "id_veiculo", insertable = false, updatable = false)
    private Veiculo veiculo;

    // Adicionar outros campos se a tabela tb_cv tiver colunas além das chaves
    // Ex: @Column(name="data_locacao") private LocalDate dataLocacao;
}