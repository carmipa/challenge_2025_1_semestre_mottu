package br.com.fiap.mottu.dto.veiculo;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link br.com.fiap.mottu.model.Veiculo}
 */
@Value
public class VeiculoRequestDto implements Serializable {
    String placa;
    String renavam;
    String chassi;
    String fabricante;
    String modelo;
    String motor;
    Integer ano;
    String combustivel;
}