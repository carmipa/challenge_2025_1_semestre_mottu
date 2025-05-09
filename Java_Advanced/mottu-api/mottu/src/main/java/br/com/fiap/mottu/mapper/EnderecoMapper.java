package br.com.fiap.mottu.mapper;

import br.com.fiap.mottu.dto.endereco.EnderecoRequestDto;
import br.com.fiap.mottu.dto.endereco.EnderecoResponseDto;
import br.com.fiap.mottu.model.Endereco;

import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingConstants;

// Importe os mappers das entidades relacionadas se EnderecoResponseDto tiver DTOs aninhados ou coleções de DTOs delas
// import br.com.fiap.mottu.mapper.ClienteMapper; // Exemplo se EnderecoResponseDto incluir Cliente associado
// import br.com.fiap.mottu.mapper.PatioMapper; // Exemplo se EnderecoResponseDto incluir Patio associado

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
        // Adicione aqui o 'uses' se EnderecoResponseDto incluir DTOs aninhados ou coleções
        // uses = { ClienteMapper.class, PatioMapper.class } // Exemplo
)
public interface EnderecoMapper {

    // Método 1: Converte de Request DTO para Entidade (para CRIAR uma nova)
    // Mapeia EnderecoRequestDto -> Endereco
    @Mapping(target = "idEndereco", ignore = true) // ID é gerado pelo BD
    // Adicionar aqui @Mapping para campos de relacionamento (FKs) no Request DTO se houver
    Endereco toEntity(EnderecoRequestDto enderecoRequestDto);

    // Método 2: Atualização de uma Entidade existente a partir de Request DTO
    // Mapeia EnderecoRequestDto -> Endereco (existente)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    // @Mapping(target = "idEndereco", ignore = true) // ID não é atualizado
    // Adicionar aqui @Mapping para campos de relacionamento (FKs) no Request DTO se houver
    Endereco partialUpdate(EnderecoRequestDto enderecoRequestDto, @MappingTarget Endereco endereco);

    // Método 3: Converte de Entidade para DTO de Resposta (ESSENCIAL para consultas)
    // Mapeia Endereco -> EnderecoResponseDto
    // MapStruct mapeia campos com nomes iguais automaticamente (idEndereco, cep, etc.)
    // Adicionar aqui @Mapping para campos de relacionamentos no ResponseDto
    // Ex: @Mapping(target = "clienteAssociado", source = "relacionamentoClienteEndereco") // Se na entidade Endereco tiver um @OneToMany para a tabela de junção ClienteEndereco
    // Ex: @Mapping(target = "patioAssociado", source = "relacionamentoPatioEndereco") // Se na entidade Endereco tiver um @OneToMany para a tabela de junção PatioEndereco
    EnderecoResponseDto toResponseDto(Endereco endereco); // Renomeado de toDto1

    // --- Métodos para mapear coleções (opcional) ---
    // List<EnderecoResponseDto> toResponseDtoList(List<Endereco> enderecos);
    // Set<EnderecoResponseDto> toResponseDtoSet(Set<Endereco> enderecos);
}
