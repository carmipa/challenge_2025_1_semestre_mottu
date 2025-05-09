package br.com.fiap.mottu.dto.zona;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link br.com.fiap.mottu.model.Zona}
 */
@Value
public class ZonaRequestDto implements Serializable {
    String nome;
    LocalDate dataEntrada;
    LocalDate dataSaida;
    String observacao;
}