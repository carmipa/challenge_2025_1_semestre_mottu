package br.com.fiap.mottu.dto.rastreamento;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link br.com.fiap.mottu.model.Rastreamento}
 */
@Value
public class RastreamentoRequestDto implements Serializable {
    String ips;
    String gprs;
}