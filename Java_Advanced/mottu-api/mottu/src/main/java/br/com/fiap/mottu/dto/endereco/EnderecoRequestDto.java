package br.com.fiap.mottu.dto.endereco;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link br.com.fiap.mottu.model.Endereco}
 */
@Value
public class EnderecoRequestDto implements Serializable {
    String cep;
    Integer numero;
    String logradouro;
    String bairro;
    String cidade;
    String estado;
    String pais;
    String complemento;
    String observacao;
}