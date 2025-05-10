// Caminho do arquivo: br\com\fiap\mottu\mapper\PatioMapper.java
package br.com.fiap.mottu.mapper;

import br.com.fiap.mottu.dto.patio.PatioRequestDto;
import br.com.fiap.mottu.dto.patio.PatioResponseDto;
import br.com.fiap.mottu.model.Patio;

import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingConstants;

// Importe os mappers das entidades relacionadas se PatioRequestDto ou PatioResponseDto tiverem DTOs aninhados ou coleções de DTOs delas
// import br.com.fiap.mottu.mapper.relacionamento.EnderecoPatioMapper;
// import br.com.fiap.mottu.mapper.relacionamento.ContatoPatioMapper;
// import br.com.fiap.mottu.mapper.relacionamento.ZonaPatioMapper;
// import br.com.fiap.mottu.mapper.relacionamento.VeiculoPatioMapper;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
        // Adicione aqui o 'uses' se PatioRequestDto ou PatioResponseDto incluirem DTOs aninhados ou coleções
        // uses = { EnderecoPatioMapper.class, ContatoPatioMapper.class, ZonaPatioMapper.class, VeiculoPatioMapper.class }
)
public interface PatioMapper {

    // Método 1: Converte de Request DTO para Entidade (para CRIAR uma nova)
    @Mapping(target = "idPatio", ignore = true) // ID é gerado pelo BD
    @Mapping(target = "contatoPatios", ignore = true) // Relacionamento inverso não é mapeado na criação
    @Mapping(target = "enderecoPatios", ignore = true) // Relacionamento inverso não é mapeado na criação
    @Mapping(target = "veiculoPatios", ignore = true) // Relacionamento inverso não é mapeado na criação
    @Mapping(target = "zonaPatios", ignore = true) // Relacionamento inverso não é mapeado na criação
    Patio toEntity(PatioRequestDto patioRequestDto);

    // Método 2: Atualização de uma Entidade existente a partir de Request DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idPatio", ignore = true) // ID não é atualizado
    @Mapping(target = "contatoPatios", ignore = true) // Relacionamento inverso não é atualizado
    @Mapping(target = "enderecoPatios", ignore = true) // Relacionamento inverso não é atualizado
    @Mapping(target = "veiculoPatios", ignore = true) // Relacionamento inverso não é atualizado
    @Mapping(target = "zonaPatios", ignore = true) // Relacionamento inverso não é atualizado
    Patio partialUpdate(PatioRequestDto patioRequestDto, @MappingTarget Patio patio);

    // Método 3: Converte de Entidade para DTO de Resposta (ESSENCIAL para consultas)
    PatioResponseDto toResponseDto(Patio patio);

    // --- Métodos para mapear coleções (opcional) ---
    // List<PatioResponseDto> toResponseDtoList(List<Patio> patios);
    // Set<PatioResponseDto> toResponseDtoSet(Set<Patio> patios);
}