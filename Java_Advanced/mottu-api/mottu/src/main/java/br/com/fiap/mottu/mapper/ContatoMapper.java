package br.com.fiap.mottu.mapper;

import br.com.fiap.mottu.dto.contato.ContatoRequestDto;
import br.com.fiap.mottu.dto.contato.ContatoResponseDto;
import br.com.fiap.mottu.model.Contato;

import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingConstants;

// Importe os mappers das entidades relacionadas se ContatoResponseDto tiver DTOs aninhados ou coleções de DTOs delas
// import br.com.fiap.mottu.mapper.ClienteMapper; // Exemplo se ContatoResponseDto incluir Cliente associado
// import br.com.fiap.mottu.mapper.PatioMapper; // Exemplo se ContatoResponseDto incluir Patio associado

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
        // Adicione aqui o 'uses' se ContatoResponseDto incluir DTOs aninhados ou coleções
        // uses = { ClienteMapper.class, PatioMapper.class } // Exemplo
)
public interface ContatoMapper {

    // Método 1: Converte de Request DTO para Entidade (para CRIAR uma nova)
    // Mapeia ContatoRequestDto -> Contato
    @Mapping(target = "idContato", ignore = true) // ID é gerado pelo BD
    // Adicionar aqui @Mapping para campos de relacionamento (FKs) no Request DTO se houver
    Contato toEntity(ContatoRequestDto contatoRequestDto);

    // Método 2: Atualização de uma Entidade existente a partir de Request DTO
    // Mapeia ContatoRequestDto -> Contato (existente)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    // @Mapping(target = "idContato", ignore = true) // ID não é atualizado
    // Adicionar aqui @Mapping para campos de relacionamento (FKs) no Request DTO se houver
    Contato partialUpdate(ContatoRequestDto contatoRequestDto, @MappingTarget Contato contato);

    // Método 3: Converte de Entidade para DTO de Resposta (ESSENCIAL para consultas)
    // Mapeia Contato -> ContatoResponseDto
    // MapStruct mapeia campos com nomes iguais automaticamente (idContato, email, etc.)
    // Adicionar aqui @Mapping para campos de relacionamentos no ResponseDto
    // Ex: @Mapping(target = "clienteAssociado", source = "relacionamentoClienteContato") // Se na entidade Contato tiver um @OneToMany para a tabela de junção ClienteContato
    // Ex: @Mapping(target = "patioAssociado", source = "relacionamentoPatioContato") // Se na entidade Contato tiver um @OneToMany para a tabela de junção PatioContato
    ContatoResponseDto toResponseDto(Contato contato); // Renomeado de toDto1

    // --- Métodos para mapear coleções (opcional) ---
    // List<ContatoResponseDto> toResponseDtoList(List<Contato> contatos);
    // Set<ContatoResponseDto> toResponseDtoSet(Set<Contato> contatos);
}
