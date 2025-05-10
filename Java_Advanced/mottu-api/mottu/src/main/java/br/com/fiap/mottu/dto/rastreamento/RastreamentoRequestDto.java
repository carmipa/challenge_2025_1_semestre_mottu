// Caminho do arquivo: br\com\fiap\mottu\dto\rastreamento\RastreamentoRequestDto.java
package br.com.fiap.mottu.dto.rastreamento;

import lombok.Value;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * DTO for {@link br.com.fiap.mottu.model.Rastreamento}
 */
@Value
public class RastreamentoRequestDto implements Serializable {
    @NotBlank(message = "IPS não pode estar em branco.")
    String ips; // Mantido como String para simplicidade; considere SDO_GEOMETRY com Hibernate Spatial.

    @NotBlank(message = "GPRS não pode estar em branco.")
    String gprs; // Mantido como String para simplicidade; considere SDO_GEOMETRY com Hibernate Spatial.
}