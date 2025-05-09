package br.com.fiap.mottu.mapper;

import br.com.fiap.mottu.dto.rastreamento.RastreamentoRequestDto;
import br.com.fiap.mottu.dto.rastreamento.RastreamentoResponseDto;
import br.com.fiap.mottu.model.Rastreamento;

import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingConstants;

// Importe os mappers das entidades relacionadas se RastreamentoRequestDto ou RastreamentoResponseDto tiverem DTOs aninhados ou coleções de DTOs delas
// import br.com.fiap.mottu.mapper.VeiculoMapper; // Exemplo se RastreamentoResponseDto incluir Veiculo associado


@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
        // Adicione aqui o 'uses' se RastreamentoRequestDto ou RastreamentoResponseDto incluirem DTOs aninhados ou coleções
        // uses = { VeiculoMapper.class } // Exemplo
)
public interface RastreamentoMapper {

    // Método 1: Converte de Request DTO para Entidade (para CRIAR uma nova)
    // Mapeia RastreamentoRequestDto -> Rastreamento
    @Mapping(target = "idRastreamento", ignore = true) // ID é gerado pelo BD
    // Adicionar aqui @Mapping para campos de relacionamento (FKs) no Request DTO
    // Ex: @Mapping(target = "veiculo", source = "idVeiculo") // Exemplo: se RequestDto tiver 'idVeiculo' e Entidade Rastreamento tiver '@ManyToOne Veiculo veiculo'
    Rastreamento toEntity(RastreamentoRequestDto rastreamentoRequestDto);

    // Método 2: Atualização de uma Entidade existente a partir de Request DTO
    // Mapeia RastreamentoRequestDto -> Rastreamento (existente)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    // @Mapping(target = "idRastreamento", ignore = true) // ID não é atualizado
    // Adicionar aqui @Mapping para campos de relacionamento (FKs) no Request DTO
    // Ex: @Mapping(target = "veiculo", source = "idVeiculo") // Exemplo: se RequestDto tiver 'idVeiculo' para atualizar associação
    Rastreamento partialUpdate(RastreamentoRequestDto rastreamentoRequestDto, @MappingTarget Rastreamento rastreamento);

    // Método 3: Converte de Entidade para DTO de Resposta (ESSENCIAL para consultas)
    // Mapeia Rastreamento -> RastreamentoResponseDto
    // MapStruct mapeia campos com nomes iguais automaticamente (idRastreamento, ips, gprs)
    // Adicionar aqui @Mapping para campos de relacionamentos no ResponseDto
    // Ex: @Mapping(target = "veiculoAssociado", source = "relacionamentoVeiculoRastreamento") // Se na entidade Rastreamento tiver um @OneToMany para a tabela de junção VeiculoRastreamento
    RastreamentoResponseDto toResponseDto(Rastreamento rastreamento); // Renomeado de toDto1

    // --- Métodos para mapear coleções (opcional) ---
    // List<RastreamentoResponseDto> toResponseDtoList(List<Rastreamento> rastreamentos);
    // Set<RastreamentoResponseDto> toResponseDtoSet(Set<Rastreamento> rastreamentos);
}
