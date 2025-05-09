package br.com.fiap.mottu.dto.rastreamento;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link br.com.fiap.mottu.model.Rastreamento}
 */
@Value
public class RastreamentoResponseDto implements Serializable {
    Long idRastreamento;
    String ips;
    String gprs;
}