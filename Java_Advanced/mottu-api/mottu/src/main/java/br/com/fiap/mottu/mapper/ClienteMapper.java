package br.com.fiap.mottu.mapper;

import br.com.fiap.mottu.dto.cliente.ClienteRequestDto;
import br.com.fiap.mottu.dto.cliente.ClienteResponseDto;
import br.com.fiap.mottu.model.Cliente;

// Importe os mappers para os DTOs aninhados
import br.com.fiap.mottu.mapper.EnderecoMapper;
import br.com.fiap.mottu.mapper.ContatoMapper;

import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingConstants;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        // Adiciona os mappers para os DTOs aninhados (Endereco e Contato)
        uses = { EnderecoMapper.class, ContatoMapper.class }
)
public interface ClienteMapper {

    // Método 1: Converte de Request DTO para Entidade (para CRIAR uma nova)
    // Mapeia ClienteRequestDto -> Cliente
    @Mapping(target = "idCliente", ignore = true) // ID é gerado pelo BD, ignore ao mapear Request para Entidade
    @Mapping(target = "dataCadastro", ignore = true) // dataCadastro é gerada pelo BD, ignore ao mapear Request para Entidade
    // Mapeia o DTO de Endereco aninhado no Request para a Entidade Endereco no Cliente
    // @Mapping(target = "endereco", source = "endereco") // Removido: Redundante, MapStruct mapeia por nome
    // Mapeia o DTO de Contato aninhado no Request para a Entidade Contato no Cliente
    // @Mapping(target = "contato", source = "contato") // Removido: Redundante, MapStruct mapeia por nome
    Cliente toEntity(ClienteRequestDto clienteRequestDto);

    // Método 2: Atualização de uma Entidade existente a partir de um Request DTO
    // Mapeia ClienteRequestDto -> Cliente (existente)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idCliente", ignore = true) // ID não é atualizado pelo DTO
    @Mapping(target = "dataCadastro", ignore = true) // Data de cadastro não é atualizada pelo DTO
    // Mapeia o DTO de Endereco aninhado no Request para a Entidade Endereco existente no Cliente
    // @Mapping(target = "endereco", source = "endereco") // Removido: Redundante, MapStruct mapeia por nome
    // Mapeia o DTO de Contato aninhado no Request para a Entidade Contato existente no Cliente
    // @Mapping(target = "contato", source = "contato") // Removido: Redundante, MapStruct mapeia por nome
    Cliente partialUpdate(ClienteRequestDto clienteRequestDto, @MappingTarget Cliente cliente);

    // Método 3: Converte de Entidade para DTO de Resposta (ESSENCIAL para consultas)
    // Mapeia Cliente -> ClienteResponseDto
    // MapStruct mapeia campos com nomes iguais automaticamente (idCliente, nome, etc.)
    // Mapeia a Entidade Endereco no Cliente para o DTO de Endereco aninhado no Response
    // @Mapping(target = "endereco", source = "endereco") // Removido: Redundante, MapStruct mapeia por nome
    // Mapeia a Entidade Contato no Cliente para o DTO de Contato aninhado no Response
    // @Mapping(target = "contato", source = "contato") // Removido: Redundante, MapStruct mapeia por nome
    // Se ClienteResponseDto tiver campos de coleções (ex: Set<VeiculoResponseDto> veiculos)
    // @Mapping(target = "veiculos", source = "relacionamentoVeiculoCliente") // Exemplo: mapear de um @OneToMany VeiculoCliente na Entidade
    ClienteResponseDto toResponseDto(Cliente cliente); // Renomeado de toDto1

    // --- Métodos para mapear coleções (opcional) ---
    // List<ClienteResponseDto> toResponseDtoList(List<Cliente> clientes);
    // Set<ClienteResponseDto> toResponseDtoSet(Set<Cliente> clientes);
}
