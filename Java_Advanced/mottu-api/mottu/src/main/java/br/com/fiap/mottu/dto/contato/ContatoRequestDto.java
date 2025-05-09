package br.com.fiap.mottu.dto.contato;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link br.com.fiap.mottu.model.Contato}
 */
@Value
public class ContatoRequestDto implements Serializable {
    String email;
    Integer ddd;
    Integer ddi;
    String telefone1;
    String telefone2;
    String telefone3;
    String celular;
    String outro;
    String observacao;
}