package br.com.fiap.mottu.mapper;

import br.com.fiap.mottu.dto.veiculo.VeiculoRequestDto;
import br.com.fiap.mottu.dto.veiculo.VeiculoResponseDto;
import br.com.fiap.mottu.model.Veiculo;

import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingConstants;

// Importe os mappers das entidades relacionadas se VeiculoRequestDto ou VeiculoResponseDto tiverem DTOs aninhados ou coleções de DTOs delas
// import br.com.fiap.mottu.mapper.ClienteMapper; // Exemplo se VeiculoResponseDto incluir Cliente associado
// import br.com.fiap.mottu.mapper.BoxMapper; // Exemplo se VeiculoResponseDto incluir Box associado
// import br.com.fiap.mottu.mapper.ZonaMapper; // Exemplo se VeiculoResponseDto incluir Zona associada
// import br.com.fiap.mottu.mapper.PatioMapper; // Exemplo se VeiculoResponseDto incluir Patio associado
// import br.com.fiap.mottu.mapper.RastreamentoMapper; // Exemplo se VeiculoResponseDto incluir Rastreamento associado


@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
        // Adicione aqui o 'uses' se VeiculoRequestDto ou VeiculoResponseDto incluirem DTOs aninhados ou coleções
        // uses = { ClienteMapper.class, BoxMapper.class, ZonaMapper.class, PatioMapper.class, RastreamentoMapper.class } // Exemplo
)
public interface VeiculoMapper {

    // Método 1: Converte de Request DTO para Entidade (para CRIAR uma nova)
    // Mapeia VeiculoRequestDto -> Veiculo
    @Mapping(target = "idVeiculo", ignore = true) // ID é gerado pelo BD
    // Adicionar aqui @Mapping para campos de relacionamento (FKs) no Request DTO
    // Ex: @Mapping(target = "cliente", source = "idCliente") // Exemplo: se RequestDto tiver 'idCliente' e Entidade Veiculo tiver '@ManyToOne Cliente cliente'
    Veiculo toEntity(VeiculoRequestDto veiculoRequestDto);

    // Método 2: Atualização de uma Entidade existente a partir de Request DTO
    // Mapeia VeiculoRequestDto -> Veiculo (existente)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    // @Mapping(target = "idVeiculo", ignore = true) // ID não é atualizado
    // Adicionar aqui @Mapping para campos de relacionamento (FKs) no Request DTO
    // Ex: @Mapping(target = "cliente", source = "idCliente") // Exemplo: se RequestDto tiver 'idCliente' para atualizar associação
    Veiculo partialUpdate(VeiculoRequestDto veiculoRequestDto, @MappingTarget Veiculo veiculo);

    // Método 3: Converte de Entidade para DTO de Resposta (ESSENCIAL para consultas)
    // Mapeia Veiculo -> VeiculoResponseDto
    // MapStruct mapeia campos com nomes iguais automaticamente (idVeiculo, placa, etc.)
    // Adicionar aqui @Mapping para campos de relacionamentos no ResponseDto
    // Ex: @Mapping(target = "clienteAssociado", source = "relacionamentoVeiculoCliente") // Se na entidade Veiculo tiver um @OneToMany para a tabela de junção ClienteVeiculo
    // Ex: @Mapping(target = "boxAtual", source = "relacionamentoVeiculoBox") // Se na entidade Veiculo tiver um @OneToMany para a tabela de junção VeiculoBox
    // Ex: @Mapping(target = "ultimoRastreamento", source = "relacionamentoVeiculoRastreamento") // Se na entidade Veiculo tiver um @OneToMany para a tabela de junção VeiculoRastreamento
    VeiculoResponseDto toResponseDto(Veiculo veiculo); // Renomeado de toDto1

    // --- Métodos para mapear coleções (opcional) ---
    // List<VeiculoResponseDto> toResponseDtoList(List<Veiculo> veiculos);
    // Set<VeiculoResponseDto> toResponseDtoSet(Set<Veiculo> veiculos);
}
