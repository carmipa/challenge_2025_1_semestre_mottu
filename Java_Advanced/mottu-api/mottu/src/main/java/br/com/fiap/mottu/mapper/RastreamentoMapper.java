// Caminho do arquivo: br\com\fiap\mottu\mapper\RastreamentoMapper.java
package br.com.fiap.mottu.mapper;

import br.com.fiap.mottu.dto.rastreamento.RastreamentoRequestDto;
import br.com.fiap.mottu.dto.rastreamento.RastreamentoResponseDto;
import br.com.fiap.mottu.model.Rastreamento;

import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingConstants;

// Importe os mappers das entidades relacionadas se RastreamentoRequestDto ou RastreamentoResponseDto tiverem DTOs aninhados ou coleções de DTOs delas
// import br.com.fiap.mottu.mapper.relacionamento.VeiculoRastreamentoMapper;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
        // Adicione aqui o 'uses' se RastreamentoRequestDto ou RastreamentoResponseDto incluirem DTOs aninhados ou coleções
        // uses = { VeiculoRastreamentoMapper.class }
)
public interface RastreamentoMapper {

    // Método 1: Converte de Request DTO para Entidade (para CRIAR uma nova)
    @Mapping(target = "idRastreamento", ignore = true) // ID é gerado pelo BD
    @Mapping(target = "veiculoRastreamentos", ignore = true) // Relacionamento inverso não é mapeado na criação
    Rastreamento toEntity(RastreamentoRequestDto rastreamentoRequestDto);

    // Método 2: Atualização de uma Entidade existente a partir de Request DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idRastreamento", ignore = true) // ID não é atualizado
    @Mapping(target = "veiculoRastreamentos", ignore = true) // Relacionamento inverso não é atualizado
    Rastreamento partialUpdate(RastreamentoRequestDto rastreamentoRequestDto, @MappingTarget Rastreamento rastreamento);

    // Método 3: Converte de Entidade para DTO de Resposta (ESSENCIAL para consultas)
    RastreamentoResponseDto toResponseDto(Rastreamento rastreamento);

    // --- Métodos para mapear coleções (opcional) ---
    // List<RastreamentoResponseDto> toResponseDtoList(List<Rastreamento> rastreamentos);
    // Set<RastreamentoResponseDto> toResponseDtoSet(Set<Rastreamento> rastreamentos);
}