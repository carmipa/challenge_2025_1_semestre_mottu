package br.com.fiap.mottu.controller;

import br.com.fiap.mottu.dto.rastreamento.RastreamentoRequestDto;
import br.com.fiap.mottu.dto.rastreamento.RastreamentoResponseDto;
import br.com.fiap.mottu.filter.RastreamentoFilter;
import br.com.fiap.mottu.service.RastreamentoService;
import br.com.fiap.mottu.mapper.RastreamentoMapper;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rastreamentos")
@Tag(name = "Rastreamentos", description = "Gerenciamento de Rastreamentos")
public class RastreamentoController {

    private static final Logger log = LoggerFactory.getLogger(RastreamentoController.class);
    private final RastreamentoService rastreamentoService;
    private final RastreamentoMapper rastreamentoMapper;

    @Autowired
    public RastreamentoController(RastreamentoService rastreamentoService, RastreamentoMapper rastreamentoMapper) {
        this.rastreamentoService = rastreamentoService;
        this.rastreamentoMapper = rastreamentoMapper;
    }

    @Operation(
            summary = "Listar todos os rastreamentos",
            description = "Retorna uma lista de todos os rastreamentos cadastrados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de rastreamentos retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RastreamentoResponseDto.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<RastreamentoResponseDto>> listarTodosRastreamentos() {
        log.info("Buscando todos os rastreamentos.");
        List<RastreamentoResponseDto> rastreamentos = rastreamentoService.listarTodosRastreamentos().stream()
                .map(rastreamentoMapper::toResponseDto)
                .collect(Collectors.toList());
        log.info("Retornando {} rastreamentos.", rastreamentos.size());
        return ResponseEntity.ok(rastreamentos);
    }

    @Operation(
            summary = "Buscar rastreamento por ID",
            description = "Retorna um rastreamento específico com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rastreamento encontrado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RastreamentoResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Rastreamento não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Rastreamento com ID 1 não encontrado(a).\",\"path\":\"/api/rastreamentos/1\"}")))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<RastreamentoResponseDto> buscarRastreamentoPorId(@PathVariable Long id) {
        log.info("Buscando rastreamento com ID: {}", id);
        try {
            RastreamentoResponseDto rastreamento = rastreamentoMapper.toResponseDto(rastreamentoService.buscarRastreamentoPorId(id));
            log.info("Rastreamento com ID {} encontrado com sucesso.", id);
            return ResponseEntity.ok(rastreamento);
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao buscar rastreamento com ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Operation(
            summary = "Buscar rastreamentos por filtro",
            description = "Retorna uma lista de rastreamentos que correspondem aos critérios de filtro fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de rastreamentos filtrada retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RastreamentoResponseDto.class)))
            }
    )
    @GetMapping("/search")
    public ResponseEntity<List<RastreamentoResponseDto>> buscarRastreamentosPorFiltro(RastreamentoFilter filter) {
        log.info("Buscando rastreamentos com filtro: {}", filter);
        List<RastreamentoResponseDto> rastreamentos = rastreamentoService.buscarRastreamentosPorFiltro(filter).stream()
                .map(rastreamentoMapper::toResponseDto)
                .collect(Collectors.toList());
        log.info("Retornando {} rastreamentos filtrados.", rastreamentos.size());
        return ResponseEntity.ok(rastreamentos);
    }

    @Operation(
            summary = "Criar novo rastreamento",
            description = "Cria um novo rastreamento com os dados fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Rastreamento criado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RastreamentoResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos (coordenadas ausentes)",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":400,\"error\":\"Requisição Inválida\",\"message\":\"Todas as coordenadas são obrigatórias.\",\"path\":\"/api/rastreamentos\"}")))
            }
    )
    @PostMapping
    public ResponseEntity<RastreamentoResponseDto> criarRastreamento(@Valid @RequestBody RastreamentoRequestDto rastreamentoRequestDto) {
        log.info("Recebida requisição para criar rastreamento: {}", rastreamentoRequestDto);
        try {
            RastreamentoResponseDto novoRastreamento = rastreamentoMapper.toResponseDto(rastreamentoService.criarRastreamento(rastreamentoRequestDto));
            log.info("Rastreamento criado com sucesso com ID: {}", novoRastreamento.getIdRastreamento());
            return ResponseEntity.status(HttpStatus.CREATED).body(novoRastreamento);
        } catch (InvalidInputException e) {
            log.error("Erro ao criar rastreamento - Requisição inválida: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao criar rastreamento: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Atualizar rastreamento existente",
            description = "Atualiza um rastreamento existente com base no ID e nos dados fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rastreamento atualizado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RastreamentoResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":400,\"error\":\"Requisição Inválida\",\"message\":\"Mensagem de validação ou erro de input.\",\"path\":\"/api/rastreamentos/1\"}"))),
                    @ApiResponse(responseCode = "404", description = "Rastreamento não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Rastreamento com ID 1 não encontrado(a).\",\"path\":\"/api/rastreamentos/1\"}")))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<RastreamentoResponseDto> atualizarRastreamento(@PathVariable Long id, @Valid @RequestBody RastreamentoRequestDto rastreamentoRequestDto) {
        log.info("Recebida requisição para atualizar rastreamento com ID {}: {}", id, rastreamentoRequestDto);
        try {
            RastreamentoResponseDto rastreamentoAtualizado = rastreamentoMapper.toResponseDto(rastreamentoService.atualizarRastreamento(id, rastreamentoRequestDto));
            log.info("Rastreamento com ID {} atualizado com sucesso.", id);
            return ResponseEntity.ok(rastreamentoAtualizado);
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao atualizar rastreamento - Não encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao atualizar rastreamento com ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Deletar rastreamento",
            description = "Exclui um rastreamento com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Rastreamento deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Rastreamento não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Rastreamento com ID 1 não encontrado(a).\",\"path\":\"/api/rastreamentos/1\"}")))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRastreamento(@PathVariable Long id) {
        log.info("Recebida requisição para deletar rastreamento com ID: {}", id);
        try {
            rastreamentoService.deletarRastreamento(id);
            log.info("Rastreamento com ID {} deletado com sucesso.", id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao deletar rastreamento - Não encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao deletar rastreamento com ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}