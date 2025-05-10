// Caminho do arquivo: br\com\fiap\mottu\dto\endereco\EnderecoRequestDto.java
package br.com.fiap.mottu.dto.endereco;

import lombok.Value;

import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * DTO for {@link br.com.fiap.mottu.model.Endereco}
 */
@Value
public class EnderecoRequestDto implements Serializable {
    @NotBlank(message = "O CEP não pode estar em branco.")
    @Size(min = 9, max = 9, message = "O CEP deve ter 9 caracteres.") // Considerando formato "XXXXX-XXX" ou similar
    @Pattern(regexp = "^\\d{5}-\\d{3}$|^\\d{8}$", message = "Formato de CEP inválido (esperado XXXXX-XXX ou XXXXXXXX).") // Exemplo para CEP com/sem hífen
    String cep;

    @NotNull(message = "O número não pode ser nulo.")
    @Positive(message = "O número deve ser positivo.")
    @Max(value = 9999999, message = "O número deve ter no máximo 7 dígitos.")
    Integer numero;

    @NotBlank(message = "O logradouro não pode estar em branco.")
    @Size(max = 50, message = "O logradouro deve ter no máximo 50 caracteres.")
    String logradouro;

    @NotBlank(message = "O bairro não pode estar em branco.")
    @Size(max = 50, message = "O bairro deve ter no máximo 50 caracteres.")
    String bairro;

    @NotBlank(message = "A cidade não pode estar em branco.")
    @Size(max = 50, message = "A cidade deve ter no máximo 50 caracteres.")
    String cidade;

    @NotBlank(message = "O estado não pode estar em branco.")
    @Size(min = 2, max = 2, message = "O estado deve ter 2 caracteres.")
    String estado;

    @NotBlank(message = "O país não pode estar em branco.")
    @Size(max = 50, message = "O país deve ter no máximo 50 caracteres.")
    String pais;

    @Size(max = 60, message = "O complemento deve ter no máximo 60 caracteres.")
    String complemento;

    @Size(max = 200, message = "A observação deve ter no máximo 200 caracteres.")
    String observacao;
}