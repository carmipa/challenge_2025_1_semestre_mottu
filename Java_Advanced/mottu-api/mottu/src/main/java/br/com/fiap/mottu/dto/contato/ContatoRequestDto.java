// Caminho do arquivo: br\com\fiap\mottu\dto\contato\ContatoRequestDto.java
package br.com.fiap.mottu.dto.contato;

import lombok.Value;

import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * DTO for {@link br.com.fiap.mottu.model.Contato}
 */
@Value
public class ContatoRequestDto implements Serializable {
    @NotBlank(message = "O email não pode estar em branco.")
    @Email(message = "O email deve ser válido.")
    @Size(max = 100, message = "O email deve ter no máximo 100 caracteres.")
    String email;

    @NotNull(message = "O DDD não pode ser nulo.")
    @Min(value = 11, message = "O DDD deve ser no mínimo 11.")
    @Max(value = 99, message = "O DDD deve ser no máximo 99.")
    Integer ddd;

    @NotNull(message = "O DDI não pode ser nulo.")
    @Min(value = 1, message = "O DDI deve ser no mínimo 1.")
    @Max(value = 999, message = "O DDI deve ser no máximo 999.") // Assumindo DDI de 3 dígitos, ajuste se necessário
    Integer ddi;

    @NotBlank(message = "O telefone 1 não pode estar em branco.")
    @Size(max = 20, message = "O telefone 1 deve ter no máximo 20 caracteres.")
    String telefone1;

    @Size(max = 20, message = "O telefone 2 deve ter no máximo 20 caracteres.")
    String telefone2;

    @Size(max = 20, message = "O telefone 3 deve ter no máximo 20 caracteres.")
    String telefone3;

    @NotBlank(message = "O celular não pode estar em branco.")
    @Size(max = 20, message = "O celular deve ter no máximo 20 caracteres.")
    String celular;

    @Size(max = 100, message = "Outras informações de contato devem ter no máximo 100 caracteres.")
    String outro;

    @Size(max = 200, message = "A observação deve ter no máximo 200 caracteres.")
    String observacao;
}