package br.com.fiap.mottu.dto.rastreamento;

import lombok.Value;
import java.io.Serializable;
import java.math.BigDecimal; // Importe BigDecimal!

/**
 * DTO for {@link br.com.fiap.mottu.model.Rastreamento}
 */
@Value
public class RastreamentoResponseDto implements Serializable {
    Long idRastreamento;

    private BigDecimal ipsX;
    private BigDecimal ipsY;
    private BigDecimal ipsZ;

    private BigDecimal gprsLatitude;
    private BigDecimal gprsLongitude;
    private BigDecimal gprsAltitude;
}