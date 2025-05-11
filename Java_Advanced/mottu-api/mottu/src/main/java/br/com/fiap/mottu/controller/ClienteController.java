package br.com.fiap.mottu.controller;

import br.com.fiap.mottu.dto.cliente.ClienteRequestDto;
import br.com.fiap.mottu.dto.cliente.ClienteResponseDto;
import br.com.fiap.mottu.dto.veiculo.VeiculoResponseDto;
import br.com.fiap.mottu.filter.ClienteFilter;
import br.com.fiap.mottu.service.ClienteService;
import br.com.fiap.mottu.mapper.ClienteMapper;
import br.com.fiap.mottu.mapper.VeiculoMapper; // Adicione o VeiculoMapper
import br.com.fiap.mottu.exception.DuplicatedResourceException;
import br.com.fiap.mottu.exception.ResourceNotFoundException;
import br.com.fiap.mottu.exception.InvalidInputException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono; // Importe Mono

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Gerenciamento de Clientes")
public class ClienteController {

    private static final Logger log = LoggerFactory.getLogger(ClienteController.class);
    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;
    private final VeiculoMapper veiculoMapper; // Injetar VeiculoMapper

    @Autowired
    public ClienteController(ClienteService clienteService, ClienteMapper clienteMapper, VeiculoMapper veiculoMapper) {
        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;
        this.veiculoMapper = veiculoMapper;
    }

    @Operation(
            summary = "Listar todos os clientes",
            description = "Retorna uma lista de todos os clientes cadastrados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClienteResponseDto.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> listarTodosClientes() {
        log.info("Buscando todos os clientes.");
        List<ClienteResponseDto> clientes = clienteService.listarTodosClientes().stream()
                .map(clienteMapper::toResponseDto)
                .collect(Collectors.toList());
        log.info("Retornando {} clientes.", clientes.size());
        return ResponseEntity.ok(clientes);
    }

    @Operation(
            summary = "Buscar cliente por ID",
            description = "Retorna um cliente específico com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Cliente com ID 1 não encontrado(a).\",\"path\":\"/api/clientes/1\"}")))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> buscarClientePorId(@PathVariable Long id) {
        log.info("Buscando cliente com ID: {}", id);
        try {
            ClienteResponseDto cliente = clienteMapper.toResponseDto(clienteService.buscarClientePorId(id));
            log.info("Cliente com ID {} encontrado com sucesso.", id);
            return ResponseEntity.ok(cliente);
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao buscar cliente com ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Operation(
            summary = "Buscar clientes por filtro",
            description = "Retorna uma lista de clientes que correspondem aos critérios de filtro fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de clientes filtrada retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClienteResponseDto.class)))
            }
    )
    @GetMapping("/search")
    public ResponseEntity<List<ClienteResponseDto>> buscarClientesPorFiltro(ClienteFilter filter) {
        log.info("Buscando clientes com filtro: {}", filter);
        List<ClienteResponseDto> clientes = clienteService.buscarClientesPorFiltro(filter).stream()
                .map(clienteMapper::toResponseDto)
                .collect(Collectors.toList());
        log.info("Retornando {} clientes filtrados.", clientes.size());
        return ResponseEntity.ok(clientes);
    }

    @Operation(
            summary = "Criar novo cliente",
            description = "Cria um novo cliente com os dados fornecidos, incluindo endereço e contato. Pode criar novos ou associar existentes.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":400,\"error\":\"Requisição Inválida\",\"message\":\"Mensagem de validação ou erro de input.\",\"path\":\"/api/clientes\"}"))),
                    @ApiResponse(responseCode = "404", description = "Recurso aninhado não encontrado (Ex: Endereço/Contato com ID fornecido não existe)",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Endereço com ID 100 não encontrado(a).\",\"path\":\"/api/clientes\"}"))),
                    @ApiResponse(responseCode = "409", description = "Conflito de recurso (CPF duplicado)",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":409,\"error\":\"Conflito de Dados\",\"message\":\"Cliente com CPF '12345678901' já existe.\",\"path\":\"/api/clientes\"}")))
            }
    )
    @PostMapping
    public Mono<ResponseEntity<ClienteResponseDto>> criarCliente(@Valid @RequestBody ClienteRequestDto clienteRequestDto) {
        log.info("Recebida requisição para criar cliente: {}", clienteRequestDto);
        return clienteService.criarCliente(clienteRequestDto)
                .map(clienteCriado -> {
                    log.info("Cliente criado com sucesso com ID: {}", clienteCriado.getIdCliente());
                    return ResponseEntity.status(HttpStatus.CREATED).body(clienteMapper.toResponseDto(clienteCriado));
                })
                .onErrorResume(ResourceNotFoundException.class, e -> {
                    log.error("Erro ao criar cliente - Recurso aninhado não encontrado: {}", e.getMessage());
                    return Mono.error(e);
                })
                .onErrorResume(DuplicatedResourceException.class, e -> {
                    log.error("Erro ao criar cliente - Conflito: {}", e.getMessage());
                    return Mono.error(e);
                })
                .onErrorResume(InvalidInputException.class, e -> {
                    log.error("Erro ao criar cliente - Requisição inválida: {}", e.getMessage());
                    return Mono.error(e);
                })
                .onErrorResume(e -> {
                    log.error("Erro inesperado ao criar cliente: {}", e.getMessage(), e);
                    return Mono.error(e);
                });
    }

    @Operation(
            summary = "Atualizar cliente existente",
            description = "Atualiza um cliente existente com base no ID e nos dados fornecidos. Permite atualizar endereço e contato.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":400,\"error\":\"Requisição Inválida\",\"message\":\"Mensagem de validação ou erro de input.\",\"path\":\"/api/clientes/1\"}"))),
                    @ApiResponse(responseCode = "404", description = "Cliente ou recurso aninhado não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Cliente com ID 1 não encontrado(a).\",\"path\":\"/api/clientes/1\"}"))),
                    @ApiResponse(responseCode = "409", description = "Conflito de recurso (CPF duplicado)",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":409,\"error\":\"Conflito de Dados\",\"message\":\"Cliente com CPF '12345678901' já existe.\",\"path\":\"/api/clientes/1\"}")))
            }
    )
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ClienteResponseDto>> atualizarCliente(@PathVariable Long id, @Valid @RequestBody ClienteRequestDto clienteRequestDto) {
        log.info("Recebida requisição para atualizar cliente com ID {}: {}", id, clienteRequestDto);
        return clienteService.atualizarCliente(id, clienteRequestDto)
                .map(clienteAtualizado -> {
                    log.info("Cliente com ID {} atualizado com sucesso.", id);
                    return ResponseEntity.ok(clienteMapper.toResponseDto(clienteAtualizado));
                })
                .onErrorResume(ResourceNotFoundException.class, e -> {
                    log.error("Erro ao atualizar cliente - Não encontrado: {}", e.getMessage());
                    return Mono.error(e);
                })
                .onErrorResume(DuplicatedResourceException.class, e -> {
                    log.error("Erro ao atualizar cliente - Conflito: {}", e.getMessage());
                    return Mono.error(e);
                })
                .onErrorResume(InvalidInputException.class, e -> {
                    log.error("Erro ao atualizar cliente - Requisição inválida: {}", e.getMessage());
                    return Mono.error(e);
                })
                .onErrorResume(e -> {
                    log.error("Erro inesperado ao atualizar cliente com ID {}: {}", id, e.getMessage(), e);
                    return Mono.error(e);
                });
    }

    @Operation(
            summary = "Deletar cliente",
            description = "Exclui um cliente com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Cliente deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Cliente com ID 1 não encontrado(a).\",\"path\":\"/api/clientes/1\"}")))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        log.info("Recebida requisição para deletar cliente com ID: {}", id);
        try {
            clienteService.deletarCliente(id);
            log.info("Cliente com ID {} deletado com sucesso.", id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao deletar cliente - Não encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao deletar cliente com ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Associar veículo a um cliente",
            description = "Associa um veículo existente a um cliente existente.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Associação criada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Cliente ou Veículo não encontrado"),
                    @ApiResponse(responseCode = "409", description = "Associação já existe")
            }
    )
    @PostMapping("/{clienteId}/enderecos/{enderecoId}/contatos/{contatoId}/veiculos/{veiculoId}/associar")
    public ResponseEntity<String> associarClienteVeiculo(
            @PathVariable Long clienteId,
            @PathVariable Long enderecoId,
            @PathVariable Long contatoId,
            @PathVariable Long veiculoId) {
        log.info("Associando veículo ID {} ao cliente ID {} (Endereco: {}, Contato: {}).", veiculoId, clienteId, enderecoId, contatoId);
        try {
            clienteService.associarClienteVeiculo(clienteId, enderecoId, contatoId, veiculoId);
            log.info("Associação entre Cliente {} e Veículo {} (End: {}, Cont: {}) criada com sucesso.", clienteId, veiculoId, enderecoId, contatoId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Associação criada com sucesso.");
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao associar - Recurso não encontrado: {}", e.getMessage());
            throw e;
        } catch (DuplicatedResourceException e) {
            log.error("Erro ao associar - Conflito: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao associar Cliente {} e Veículo {}: {}", clienteId, veiculoId, e.getMessage(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Desassociar veículo de um cliente",
            description = "Remove a associação entre um veículo e um cliente.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Associação removida com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Associação não encontrada")
            }
    )
    @DeleteMapping("/{clienteId}/enderecos/{enderecoId}/contatos/{contatoId}/veiculos/{veiculoId}/desassociar")
    public ResponseEntity<Void> desassociarClienteVeiculo(
            @PathVariable Long clienteId,
            @PathVariable Long enderecoId,
            @PathVariable Long contatoId,
            @PathVariable Long veiculoId) {
        log.info("Desassociando veículo ID {} do cliente ID {} (Endereco: {}, Contato: {}).", veiculoId, clienteId, enderecoId, contatoId);
        try {
            clienteService.desassociarClienteVeiculo(clienteId, enderecoId, contatoId, veiculoId);
            log.info("Associação entre Cliente {} e Veículo {} (End: {}, Cont: {}) removida com sucesso.", clienteId, veiculoId, enderecoId, contatoId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao desassociar - Associação não encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao desassociar Cliente {} e Veículo {}: {}", clienteId, veiculoId, e.getMessage(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Listar veículos de um cliente",
            description = "Retorna todos os veículos associados a um cliente específico.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Veículos do cliente retornados com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = VeiculoResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
            }
    )
    @GetMapping("/{clienteId}/veiculos")
    public ResponseEntity<Set<VeiculoResponseDto>> getVeiculosByClienteId(@PathVariable Long clienteId) {
        log.info("Buscando veículos associados ao cliente com ID: {}", clienteId);
        try {
            Set<VeiculoResponseDto> veiculos = clienteService.getVeiculosByClienteId(clienteId).stream()
                    .map(veiculoMapper::toResponseDto)
                    .collect(Collectors.toSet());
            log.info("Retornando {} veículos para o cliente com ID {}.", veiculos.size(), clienteId);
            return ResponseEntity.ok(veiculos);
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao buscar veículos do cliente com ID {}: {}", clienteId, e.getMessage());
            throw e;
        }
    }
}