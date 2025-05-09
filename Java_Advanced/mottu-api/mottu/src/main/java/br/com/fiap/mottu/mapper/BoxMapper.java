package br.com.fiap.mottu.mapper;

import br.com.fiap.mottu.dto.box.BoxRequestDto;
import br.com.fiap.mottu.dto.box.BoxResponseDto;
import br.com.fiap.mottu.model.Box;

import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingConstants;

// Importe os mappers das entidades relacionadas se BoxResponseDto tiver DTOs aninhados ou coleções de DTOs delas
// import br.com.fiap.mottu.mapper.VeiculoMapper;
// import br.com.fiap.mottu.mapper.ZonaMapper;


@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
        // Adicione aqui o 'uses' se BoxResponseDto incluir DTOs aninhados ou coleções
        // uses = { VeiculoMapper.class, ZonaMapper.class } // Exemplo
)
public interface BoxMapper {

    // Método 1: Converte de Request DTO para Entidade (para CRIAR uma nova)
    @Mapping(target = "idBox", ignore = true) // ID é gerado pelo BD
    // @Mapping(target = "zona", source = "idZona") // Exemplo: se RequestDto tiver 'idZona' e Entidade Box tiver '@ManyToOne Zona zona'
    Box toEntity(BoxRequestDto boxRequestDto);

    // Método 2: Atualização de uma Entidade existente a partir de um Request DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    // @Mapping(target = "idBox", ignore = true) // ID não é atualizado
    // @Mapping(target = "zona", source = "idZona") // Exemplo: se RequestDto tiver 'idZona' para atualizar associação
    Box partialUpdate(BoxRequestDto boxRequestDto, @MappingTarget Box box);

    // Método 3: Converte de Entidade para DTO de Resposta (ESSENCIAL para consultas)
    // Mapeia Box -> BoxResponseDto
    // MapStruct mapeia campos com nomes iguais automaticamente (idBox, nome, etc.)
    // Adicionar aqui @Mapping para campos de relacionamentos no ResponseDto
    // Ex: @Mapping(target = "veiculos", source = "relacionamentoVeiculoBox") // Se na entidade Box tiver um @OneToMany VeiculoBox, mapeie para uma lista de VeiculoResponseDto (requer VeiculoMapper e 'uses')
    // Ex: @Mapping(target = "zona", source = "relacionamentoZonaBox") // Se na entidade Box tiver um @OneToMany ZonaBox, mapeie para ZonaResponseDto (requer ZonaMapper e 'uses')
    BoxResponseDto toResponseDto(Box box);

    // --- Métodos para mapear coleções (opcional) ---
    // List<BoxResponseDto> toResponseDtoList(List<Box> boxes);
    // Set<BoxResponseDto> toResponseDtoSet(Set<Box> boxes);
}