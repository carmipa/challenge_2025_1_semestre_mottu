package br.com.fiap.mottu.dto.cliente;

import br.com.fiap.mottu.dto.contato.ContatoRequestDto;
import br.com.fiap.mottu.dto.endereco.EnderecoRequestDto;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link br.com.fiap.mottu.model.Cliente}
 */
@Value
public class ClienteRequestDto implements Serializable {
    String sexo;
    String nome;
    String sobrenome;
    LocalDate dataNascimento;
    String cpf;
    String profissao;
    String estadoCivil;
    EnderecoRequestDto enderecoRequestDto;
    ContatoRequestDto contatoRequestDto;
}