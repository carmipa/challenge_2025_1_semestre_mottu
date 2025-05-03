package br.com.fiap.mattu.model.relacionamento;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class VzId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "id_vz")
    private Long idVz;

    @Column(name = "tb_veiculo_id_veiculo") // FK Simples para Veiculo
    private Long veiculoId;

    @Column(name = "tb_zona_id_zona") // FK Simples para Zona
    private Long zonaId;
}