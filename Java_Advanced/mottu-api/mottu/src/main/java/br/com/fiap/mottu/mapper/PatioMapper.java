package br.com.fiap.mottu.mapper;

import br.com.fiap.mottu.dto.patio.PatioRequestDto;
import br.com.fiap.mottu.dto.patio.PatioResponseDto;
import br.com.fiap.mottu.model.Patio;

import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingConstants;

// Importe os mappers das entidades relacionadas se PatioRequestDto ou PatioResponseDto tiverem DTOs aninhados ou coleções de DTOs delas
// import br.com.fiap.mottu.mapper.EnderecoMapper;
// import br.com.fiap.mottu.mapper.ContatoMapper;
// import br.com.fiap.mottu.mapper.ZonaMapper;
// import br.com.fiap.mottu.mapper.VeiculoMapper;


@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
        // Adicione aqui o 'uses' se PatioRequestDto ou PatioResponseDto incluirem DTOs aninhados ou coleções
        // uses = { EnderecoMapper.class, ContatoMapper.class, ZonaMapper.class, VeiculoMapper.class } // Exemplo
)
public interface PatioMapper {

    // Método 1: Converte de Request DTO para Entidade (para CRIAR uma nova)
    // Mapeia PatioRequestDto -> Patio
    @Mapping(target = "idPatio", ignore = true) // ID é gerado pelo BD
    // Adicionar aqui @Mapping para campos de relacionamento (FKs) no Request DTO se houver
    // Ex: @Mapping(target = "enderecos", source = "enderecosRequestDto") // Se PatioRequestDto tiver Set<EnderecoRequestDto> enderecosRequestDto
    Patio toEntity(PatioRequestDto patioRequestDto);

    // Método 2: Atualização de uma Entidade existente a partir de Request DTO
    // Mapeia PatioRequestDto -> Patio (existente)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    // @Mapping(target = "idPatio", ignore = true) // ID não é atualizado
    // Adicionar aqui @Mapping para campos de relacionamento (FKs) no Request DTO se houver
    // Ex: @Mapping(target = "enderecos", source = "enderecosRequestDto") // Se PatioRequestDto tiver Set<EnderecoRequestDto> enderecosRequestDto
    Patio partialUpdate(PatioRequestDto patioRequestDto, @MappingTarget Patio patio);

    // Método 3: Converte de Entidade para DTO de Resposta (ESSENCIAL para consultas)
    // Mapeia Patio -> PatioResponseDto
    // MapStruct mapeia campos com nomes iguais automaticamente (idPatio, nomePatio, etc.)
    // Adicionar aqui @Mapping para campos de relacionamentos no ResponseDto
    // Ex: @Mapping(target = "enderecos", source = "relacionamentoPatioEndereco") // Se na entidade Patio tiver um @OneToMany para a tabela de junção T_EP
    // Ex: @Mapping(target = "contatos", source = "relacionamentoPatioContato") // Se na entidade Patio tiver um @OneToMany para a tabela de junção T_CP
    // Ex: @Mapping(target = "zonas", source = "relacionamentoPatioZona") // Se na entidade Patio tiver um @OneToMany para a tabela de junção TB_ZP
    // Ex: @Mapping(target = "veiculos", source = "relacionamentoPatioVeiculo") // Se na entidade Patio tiver um @OneToMany para a tabela de junção TB_VP
    PatioResponseDto toResponseDto(Patio patio); // Renomeado de toDto1

    // --- Métodos para mapear coleções (opcional) ---
    // List<PatioResponseDto> toResponseDtoList(List<Patio> patios);
    // Set<PatioResponseDto> toResponseDtoSet(Set<Patio> patios);
}
