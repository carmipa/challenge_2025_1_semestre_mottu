// Caminho do arquivo: br\com\fiap\mottu\mapper\BoxMapper.java
package br.com.fiap.mottu.mapper;

import br.com.fiap.mottu.dto.box.BoxRequestDto;
import br.com.fiap.mottu.dto.box.BoxResponseDto;
import br.com.fiap.mottu.model.Box;

import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingConstants;

// Importe os mappers das entidades relacionadas se BoxResponseDto tiver DTOs aninhados ou coleções de DTOs delas
// import br.com.fiap.mottu.mapper.relacionamento.VeiculoBoxMapper; // Exemplo
// import br.com.fiap.mottu.mapper.relacionamento.ZonaBoxMapper; // Exemplo

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
        // Adicione aqui o 'uses' se BoxResponseDto incluir DTOs aninhados ou coleções
        // uses = { VeiculoBoxMapper.class, ZonaBoxMapper.class }
)
public interface BoxMapper {

    // Método 1: Converte de Request DTO para Entidade (para CRIAR uma nova)
    @Mapping(target = "idBox", ignore = true) // ID é gerado pelo BD
    Box toEntity(BoxRequestDto boxRequestDto);

    // Método 2: Atualização de uma Entidade existente a partir de um Request DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    // O idBox já está ignorado por padrão em partialUpdate se não for um campo do DTO
    Box partialUpdate(BoxRequestDto boxRequestDto, @MappingTarget Box box);

    // Método 3: Converte de Entidade para DTO de Resposta (ESSENCIAL para consultas)
    BoxResponseDto toResponseDto(Box box);

    // --- Métodos para mapear coleções (opcional) ---
    // List<BoxResponseDto> toResponseDtoList(List<Box> boxes);
    // Set<BoxResponseDto> toResponseDtoSet(Set<Box> boxes);
}