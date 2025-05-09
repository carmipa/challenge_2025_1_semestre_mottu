package br.com.fiap.mottu.dto.box;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link br.com.fiap.mottu.model.Box}
 */
@Value
public class BoxRequestDto implements Serializable {
    String nome;
    String status;
    LocalDate dataEntrada;
    LocalDate dataSaida;
    String observacao;
}