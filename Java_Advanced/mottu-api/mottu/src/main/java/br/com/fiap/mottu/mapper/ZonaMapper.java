package br.com.fiap.mottu.mapper;

import br.com.fiap.mottu.dto.zona.ZonaRequestDto;
import br.com.fiap.mottu.dto.zona.ZonaResponseDto;
import br.com.fiap.mottu.model.Zona;

import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingConstants;

// Importe os mappers das entidades relacionadas se ZonaRequestDto ou ZonaResponseDto tiverem DTOs aninhados ou coleções de DTOs delas
// import br.com.fiap.mottu.mapper.BoxMapper; // Exemplo se ZonaResponseDto incluir Box associado
// import br.com.fiap.mottu.mapper.VeiculoMapper; // Exemplo se ZonaResponseDto incluir Veiculo associado
// import br.com.fiap.mottu.mapper.PatioMapper; // Exemplo se ZonaRequestDto ou ZonaResponseDto incluir Patio associado


@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
        // Adicione aqui o 'uses' se ZonaRequestDto ou ZonaResponseDto incluirem DTOs aninhados ou coleções
        // uses = { BoxMapper.class, VeiculoMapper.class, PatioMapper.class } // Exemplo
)
public interface ZonaMapper {

    // Método 1: Converte de Request DTO para Entidade (para CRIAR uma nova)
    // Mapeia ZonaRequestDto -> Zona
    @Mapping(target = "idZona", ignore = true) // ID é gerado pelo BD
    // Adicionar aqui @Mapping para campos de relacionamento (FKs) no Request DTO
    // Ex: @Mapping(target = "patio", source = "idPatio") // Exemplo: se RequestDto tiver 'idPatio' e Entidade Zona tiver '@ManyToOne Patio patio'
    Zona toEntity(ZonaRequestDto zonaRequestDto);

    // Método 2: Atualização de uma Entidade existente a partir de Request DTO
    // Mapeia ZonaRequestDto -> Zona (existente)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    // @Mapping(target = "idZona", ignore = true) // ID não é atualizado
    // Adicionar aqui @Mapping para campos de relacionamento (FKs) no Request DTO
    // Ex: @Mapping(target = "patio", source = "idPatio") // Exemplo: se RequestDto tiver 'idPatio' para atualizar associação
    Zona partialUpdate(ZonaRequestDto zonaRequestDto, @MappingTarget Zona zona);

    // Método 3: Converte de Entidade para DTO de Resposta (ESSENCIAL para consultas)
    // Mapeia Zona -> ZonaResponseDto
    // MapStruct mapeia campos com nomes iguais automaticamente (idZona, nome, etc.)
    // Adicionar aqui @Mapping para campos de relacionamentos no ResponseDto
    // Ex: @Mapping(target = "boxes", source = "relacionamentoZonaBox") // Se na entidade Zona tiver um @OneToMany para a tabela de junção ZonaBox
    // Ex: @Mapping(target = "veiculos", source = "relacionamentoZonaVeiculo") // Se na entidade Zona tiver um @OneToMany para a tabela de junção VeiculoZona
    // Ex: @Mapping(target = "patios", source = "relacionamentoZonaPatio") // Se na entidade Zona tiver um @OneToMany para a tabela de junção ZonaPatio
    ZonaResponseDto toResponseDto(Zona zona); // Renomeado de toDto1

    // --- Métodos para mapear coleções (opcional) ---
    // List<ZonaResponseDto> toResponseDtoList(List<Zona> zonas);
    // Set<ZonaResponseDto> toResponseDtoSet(Set<Zona> zonas);
}
