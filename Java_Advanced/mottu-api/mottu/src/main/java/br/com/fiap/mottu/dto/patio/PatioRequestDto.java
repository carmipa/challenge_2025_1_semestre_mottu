package br.com.fiap.mottu.dto.patio;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link br.com.fiap.mottu.model.Patio}
 */
@Value
public class PatioRequestDto implements Serializable {
    String nomePatio;
    LocalDate dataEntrada;
    LocalDate dataSaida;
    String observacao;
}