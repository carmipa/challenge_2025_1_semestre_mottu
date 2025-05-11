package br.com.fiap.mottu.controller;

import br.com.fiap.mottu.dto.zona.ZonaRequestDto;
import br.com.fiap.mottu.dto.zona.ZonaResponseDto;
import br.com.fiap.mottu.filter.ZonaFilter;
import br.com.fiap.mottu.service.ZonaService;
import br.com.fiap.mottu.mapper.ZonaMapper;
import br.com.fiap.mottu.exception.DuplicatedResourceException;
import br.com.fiap.mottu.exception.ResourceNotFoundException;

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
@RequestMapping("/api/zonas")
@Tag(name = "Zonas", description = "Gerenciamento de Zonas")
public class ZonaController {

    private static final Logger log = LoggerFactory.getLogger(ZonaController.class);
    private final ZonaService zonaService;
    private final ZonaMapper zonaMapper;

    @Autowired
    public ZonaController(ZonaService zonaService, ZonaMapper zonaMapper) {
        this.zonaService = zonaService;
        this.zonaMapper = zonaMapper;
    }

    @Operation(
            summary = "Listar todas as zonas",
            description = "Retorna uma lista de todas as zonas cadastradas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de zonas retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ZonaResponseDto.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<ZonaResponseDto>> listarTodasZonas() {
        log.info("Buscando todas as zonas.");
        List<ZonaResponseDto> zonas = zonaService.listarTodasZonas().stream()
                .map(zonaMapper::toResponseDto)
                .collect(Collectors.toList());
        log.info("Retornando {} zonas.", zonas.size());
        return ResponseEntity.ok(zonas);
    }

    @Operation(
            summary = "Buscar zona por ID",
            description = "Retorna uma zona específica com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Zona encontrada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ZonaResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Zona não encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Zona com ID 1 não encontrado(a).\",\"path\":\"/api/zonas/1\"}")))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ZonaResponseDto> buscarZonaPorId(@PathVariable Long id) {
        log.info("Buscando zona com ID: {}", id);
        try {
            ZonaResponseDto zona = zonaMapper.toResponseDto(zonaService.buscarZonaPorId(id));
            log.info("Zona com ID {} encontrado com sucesso.", id);
            return ResponseEntity.ok(zona);
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao buscar zona com ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Operation(
            summary = "Buscar zonas por filtro",
            description = "Retorna uma lista de zonas que correspondem aos critérios de filtro fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de zonas filtrada retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ZonaResponseDto.class)))
            }
    )
    @GetMapping("/search")
    public ResponseEntity<List<ZonaResponseDto>> buscarZonasPorFiltro(ZonaFilter filter) {
        log.info("Buscando zonas com filtro: {}", filter);
        List<ZonaResponseDto> zonas = zonaService.buscarZonasPorFiltro(filter).stream()
                .map(zonaMapper::toResponseDto)
                .collect(Collectors.toList());
        log.info("Retornando {} zonas filtradas.", zonas.size());
        return ResponseEntity.ok(zonas);
    }

    @Operation(
            summary = "Criar nova zona",
            description = "Cria uma nova zona com os dados fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Zona criada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ZonaResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":400,\"error\":\"Requisição Inválida\",\"message\":\"Mensagem de validação ou erro de input.\",\"path\":\"/api/zonas\"}"))),
                    @ApiResponse(responseCode = "409", description = "Conflito de recurso (nome duplicado)",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":409,\"error\":\"Conflito de Dados\",\"message\":\"Zona com nome 'Nome Duplicado' já existe.\",\"path\":\"/api/zonas\"}")))
            }
    )
    @PostMapping
    public ResponseEntity<ZonaResponseDto> criarZona(@Valid @RequestBody ZonaRequestDto zonaRequestDto) {
        log.info("Recebida requisição para criar zona: {}", zonaRequestDto);
        try {
            ZonaResponseDto novaZona = zonaMapper.toResponseDto(zonaService.criarZona(zonaRequestDto));
            log.info("Zona criada com sucesso com ID: {}", novaZona.getIdZona());
            return ResponseEntity.status(HttpStatus.CREATED).body(novaZona);
        } catch (DuplicatedResourceException e) {
            log.error("Erro ao criar zona - Conflito: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao criar zona: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Atualizar zona existente",
            description = "Atualiza uma zona existente com base no ID e nos dados fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Zona atualizada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ZonaResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":400,\"error\":\"Requisição Inválida\",\"message\":\"Mensagem de validação ou erro de input.\",\"path\":\"/api/zonas/1\"}"))),
                    @ApiResponse(responseCode = "404", description = "Zona não encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Zona com ID 1 não encontrado(a).\",\"path\":\"/api/zonas/1\"}"))),
                    @ApiResponse(responseCode = "409", description = "Conflito de recurso (nome duplicado)",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":409,\"error\":\"Conflito de Dados\",\"message\":\"Zona com nome 'Nome Duplicado' já existe.\",\"path\":\"/api/zonas/1\"}")))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ZonaResponseDto> atualizarZona(@PathVariable Long id, @Valid @RequestBody ZonaRequestDto zonaRequestDto) {
        log.info("Recebida requisição para atualizar zona com ID {}: {}", id, zonaRequestDto);
        try {
            ZonaResponseDto zonaAtualizada = zonaMapper.toResponseDto(zonaService.atualizarZona(id, zonaRequestDto));
            log.info("Zona com ID {} atualizada com sucesso.", id);
            return ResponseEntity.ok(zonaAtualizada);
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao atualizar zona - Não encontrado: {}", e.getMessage());
            throw e;
        } catch (DuplicatedResourceException e) {
            log.error("Erro ao atualizar zona - Conflito: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao atualizar zona com ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Deletar zona",
            description = "Exclui uma zona com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Zona deletada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Zona não encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Zona com ID 1 não encontrado(a).\",\"path\":\"/api/zonas/1\"}")))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarZona(@PathVariable Long id) {
        log.info("Recebida requisição para deletar zona com ID: {}", id);
        try {
            zonaService.deletarZona(id);
            log.info("Zona com ID {} deletada com sucesso.", id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao deletar zona - Não encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao deletar zona com ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}