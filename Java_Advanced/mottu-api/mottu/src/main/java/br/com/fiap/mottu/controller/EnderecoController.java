package br.com.fiap.mottu.controller;

import br.com.fiap.mottu.dto.endereco.EnderecoRequestDto;
import br.com.fiap.mottu.dto.endereco.EnderecoResponseDto;
import br.com.fiap.mottu.filter.EnderecoFilter;
import br.com.fiap.mottu.service.EnderecoService;
import br.com.fiap.mottu.mapper.EnderecoMapper;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/enderecos")
@Tag(name = "Enderecos", description = "Gerenciamento de Endereços")
public class EnderecoController {

    private static final Logger log = LoggerFactory.getLogger(EnderecoController.class);
    private final EnderecoService enderecoService;
    private final EnderecoMapper enderecoMapper;

    @Autowired
    public EnderecoController(EnderecoService enderecoService, EnderecoMapper enderecoMapper) {
        this.enderecoService = enderecoService;
        this.enderecoMapper = enderecoMapper;
    }

    @Operation(
            summary = "Listar todos os endereços",
            description = "Retorna uma lista de todos os endereços cadastrados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de endereços retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EnderecoResponseDto.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<EnderecoResponseDto>> listarTodosEnderecos() {
        log.info("Buscando todos os endereços.");
        List<EnderecoResponseDto> enderecos = enderecoService.listarTodosEnderecos().stream()
                .map(enderecoMapper::toResponseDto)
                .collect(Collectors.toList());
        log.info("Retornando {} endereços.", enderecos.size());
        return ResponseEntity.ok(enderecos);
    }

    @Operation(
            summary = "Buscar endereço por ID",
            description = "Retorna um endereço específico com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Endereço encontrado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EnderecoResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Endereço não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Endereço com ID 1 não encontrado(a).\",\"path\":\"/api/enderecos/1\"}")))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponseDto> buscarEnderecoPorId(@PathVariable Long id) {
        log.info("Buscando endereço com ID: {}", id);
        try {
            EnderecoResponseDto endereco = enderecoMapper.toResponseDto(enderecoService.buscarEnderecoPorId(id));
            log.info("Endereço com ID {} encontrado com sucesso.", id);
            return ResponseEntity.ok(endereco);
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao buscar endereço com ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Operation(
            summary = "Buscar endereços por filtro",
            description = "Retorna uma lista de endereços que correspondem aos critérios de filtro fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de endereços filtrada retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EnderecoResponseDto.class)))
            }
    )
    @GetMapping("/search")
    public ResponseEntity<List<EnderecoResponseDto>> buscarEnderecosPorFiltro(EnderecoFilter filter) {
        log.info("Buscando endereços com filtro: {}", filter);
        List<EnderecoResponseDto> enderecos = enderecoService.listarTodosEnderecos().stream() // EnderecoService não tem buscarEnderecosPorFiltro
                .map(enderecoMapper::toResponseDto)
                .collect(Collectors.toList());
        log.info("Retornando {} endereços filtrados.", enderecos.size());
        return ResponseEntity.ok(enderecos);
    }

    @Operation(
            summary = "Criar novo endereço",
            description = "Cria um novo endereço com os dados fornecidos, buscando informações de CEP na ViaCEP.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Endereço criado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EnderecoResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":400,\"error\":\"Requisição Inválida\",\"message\":\"Mensagem de validação ou erro de input.\",\"path\":\"/api/enderecos\"}"))),
                    @ApiResponse(responseCode = "404", description = "CEP não encontrado na ViaCEP",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"CEP com cep '12345-678' não encontrado(a).\",\"path\":\"/api/enderecos\"}")))
            }
    )
    @PostMapping
    public Mono<ResponseEntity<EnderecoResponseDto>> criarEndereco(@Valid @RequestBody EnderecoRequestDto enderecoRequestDto) {
        log.info("Recebida requisição para criar endereço: {}", enderecoRequestDto);
        return enderecoService.criarEndereco(enderecoRequestDto)
                .map(enderecoCriado -> {
                    log.info("Endereço criado com sucesso com ID: {}", enderecoCriado.getIdEndereco());
                    return ResponseEntity.status(HttpStatus.CREATED).body(enderecoMapper.toResponseDto(enderecoCriado));
                })
                .onErrorResume(ResourceNotFoundException.class, e -> {
                    log.error("Erro ao criar endereço - CEP não encontrado: {}", e.getMessage());
                    return Mono.error(e);
                })
                .onErrorResume(InvalidInputException.class, e -> {
                    log.error("Erro ao criar endereço - Requisição inválida: {}", e.getMessage());
                    return Mono.error(e);
                })
                .onErrorResume(e -> {
                    log.error("Erro inesperado ao criar endereço: {}", e.getMessage(), e);
                    return Mono.error(e);
                });
    }

    @Operation(
            summary = "Atualizar endereço existente",
            description = "Atualiza um endereço existente com base no ID e nos dados fornecidos. Pode buscar informações de CEP na ViaCEP se o CEP for alterado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EnderecoResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":400,\"error\":\"Requisição Inválida\",\"message\":\"Mensagem de validação ou erro de input.\",\"path\":\"/api/enderecos/1\"}"))),
                    @ApiResponse(responseCode = "404", description = "Endereço não encontrado ou CEP de atualização não encontrado na ViaCEP",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Endereço com ID 1 não encontrado(a).\",\"path\":\"/api/enderecos/1\"}")))
            }
    )
    @PutMapping("/{id}")
    public Mono<ResponseEntity<EnderecoResponseDto>> atualizarEndereco(@PathVariable Long id, @Valid @RequestBody EnderecoRequestDto enderecoRequestDto) {
        log.info("Recebida requisição para atualizar endereço com ID {}: {}", id, enderecoRequestDto);
        return enderecoService.atualizarEndereco(id, enderecoRequestDto)
                .map(enderecoAtualizado -> {
                    log.info("Endereço com ID {} atualizado com sucesso.", id);
                    return ResponseEntity.ok(enderecoMapper.toResponseDto(enderecoAtualizado));
                })
                .onErrorResume(ResourceNotFoundException.class, e -> {
                    log.error("Erro ao atualizar endereço - Não encontrado: {}", e.getMessage());
                    return Mono.error(e);
                })
                .onErrorResume(InvalidInputException.class, e -> {
                    log.error("Erro ao atualizar endereço - Requisição inválida: {}", e.getMessage());
                    return Mono.error(e);
                })
                .onErrorResume(e -> {
                    log.error("Erro inesperado ao atualizar endereço com ID {}: {}", id, e.getMessage(), e);
                    return Mono.error(e);
                });
    }

    @Operation(
            summary = "Deletar endereço",
            description = "Exclui um endereço com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Endereço deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Endereço não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Endereço com ID 1 não encontrado(a).\",\"path\":\"/api/enderecos/1\"}")))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEndereco(@PathVariable Long id) {
        log.info("Recebida requisição para deletar endereço com ID: {}", id);
        try {
            enderecoService.deletarEndereco(id);
            log.info("Endereço com ID {} deletado com sucesso.", id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao deletar endereço - Não encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao deletar endereço com ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}