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
public class ZbId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "id_zb")
    private Long idZb;

    @Column(name = "tb_zona_id_zona") // FK Simples para Zona
    private Long zonaId;

    @Column(name = "tb_box_id_box") // FK Simples para Box
    private Long boxId;
}