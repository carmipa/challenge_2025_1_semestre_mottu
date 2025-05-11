package br.com.fiap.mottu.controller;

import br.com.fiap.mottu.dto.box.BoxRequestDto;
import br.com.fiap.mottu.dto.box.BoxResponseDto;
import br.com.fiap.mottu.filter.BoxFilter;
import br.com.fiap.mottu.service.BoxService;
import br.com.fiap.mottu.mapper.BoxMapper;
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
@RequestMapping("/api/boxes")
@Tag(name = "Boxes", description = "Gerenciamento de Boxes")
public class BoxController {

    private static final Logger log = LoggerFactory.getLogger(BoxController.class);
    private final BoxService boxService;
    private final BoxMapper boxMapper;

    @Autowired
    public BoxController(BoxService boxService, BoxMapper boxMapper) {
        this.boxService = boxService;
        this.boxMapper = boxMapper;
    }

    @Operation(
            summary = "Listar todos os boxes",
            description = "Retorna uma lista de todos os boxes cadastrados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de boxes retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BoxResponseDto.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<BoxResponseDto>> listarTodosBoxes() {
        log.info("Buscando todos os boxes.");
        List<BoxResponseDto> boxes = boxService.listarTodosBoxes().stream()
                .map(boxMapper::toResponseDto)
                .collect(Collectors.toList());
        log.info("Retornando {} boxes.", boxes.size());
        return ResponseEntity.ok(boxes);
    }

    @Operation(
            summary = "Buscar box por ID",
            description = "Retorna um box específico com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Box encontrado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BoxResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Box não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Box com ID 1 não encontrado(a).\",\"path\":\"/api/boxes/1\"}")))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<BoxResponseDto> buscarBoxPorId(@PathVariable Long id) {
        log.info("Buscando box com ID: {}", id);
        try {
            BoxResponseDto box = boxMapper.toResponseDto(boxService.buscarBoxPorId(id));
            log.info("Box com ID {} encontrado com sucesso.", id);
            return ResponseEntity.ok(box);
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao buscar box com ID {}: {}", id, e.getMessage());
            throw e; // O GlobalExceptionHandler cuidará da resposta HTTP
        }
    }

    @Operation(
            summary = "Buscar boxes por filtro",
            description = "Retorna uma lista de boxes que correspondem aos critérios de filtro fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de boxes filtrada retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BoxResponseDto.class)))
            }
    )
    @GetMapping("/search")
    public ResponseEntity<List<BoxResponseDto>> buscarBoxesPorFiltro(BoxFilter filter) {
        log.info("Buscando boxes com filtro: {}", filter);
        List<BoxResponseDto> boxes = boxService.buscarBoxesPorFiltro(filter).stream()
                .map(boxMapper::toResponseDto)
                .collect(Collectors.toList());
        log.info("Retornando {} boxes filtrados.", boxes.size());
        return ResponseEntity.ok(boxes);
    }

    @Operation(
            summary = "Criar novo box",
            description = "Cria um novo box com os dados fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Box criado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BoxResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":400,\"error\":\"Requisição Inválida\",\"message\":\"Mensagem de validação ou erro de input.\",\"path\":\"/api/boxes\"}"))),
                    @ApiResponse(responseCode = "409", description = "Conflito de recurso (nome duplicado)",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":409,\"error\":\"Conflito de Dados\",\"message\":\"Box com nome 'Nome Duplicado' já existe.\",\"path\":\"/api/boxes\"}")))
            }
    )
    @PostMapping
    public ResponseEntity<BoxResponseDto> criarBox(@Valid @RequestBody BoxRequestDto boxRequestDto) {
        log.info("Recebida requisição para criar box: {}", boxRequestDto);
        try {
            BoxResponseDto novoBox = boxMapper.toResponseDto(boxService.criarBox(boxRequestDto));
            log.info("Box criado com sucesso com ID: {}", novoBox.getIdBox());
            return ResponseEntity.status(HttpStatus.CREATED).body(novoBox);
        } catch (DuplicatedResourceException e) {
            log.error("Erro ao criar box - Conflito: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao criar box: {}", e.getMessage(), e);
            throw e; // O GlobalExceptionHandler pode pegar outras exceções inesperadas
        }
    }

    @Operation(
            summary = "Atualizar box existente",
            description = "Atualiza um box existente com base no ID e nos dados fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Box atualizado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BoxResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":400,\"error\":\"Requisição Inválida\",\"message\":\"Mensagem de validação ou erro de input.\",\"path\":\"/api/boxes/1\"}"))),
                    @ApiResponse(responseCode = "404", description = "Box não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Box com ID 1 não encontrado(a).\",\"path\":\"/api/boxes/1\"}"))),
                    @ApiResponse(responseCode = "409", description = "Conflito de recurso (nome duplicado)",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":409,\"error\":\"Conflito de Dados\",\"message\":\"Box com nome 'Nome Duplicado' já existe.\",\"path\":\"/api/boxes/1\"}")))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<BoxResponseDto> atualizarBox(@PathVariable Long id, @Valid @RequestBody BoxRequestDto boxRequestDto) {
        log.info("Recebida requisição para atualizar box com ID {}: {}", id, boxRequestDto);
        try {
            BoxResponseDto boxAtualizado = boxMapper.toResponseDto(boxService.atualizarBox(id, boxRequestDto));
            log.info("Box com ID {} atualizado com sucesso.", id);
            return ResponseEntity.ok(boxAtualizado);
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao atualizar box - Não encontrado: {}", e.getMessage());
            throw e;
        } catch (DuplicatedResourceException e) {
            log.error("Erro ao atualizar box - Conflito: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao atualizar box com ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Deletar box",
            description = "Exclui um box com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Box deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Box não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Box com ID 1 não encontrado(a).\",\"path\":\"/api/boxes/1\"}")))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarBox(@PathVariable Long id) {
        log.info("Recebida requisição para deletar box com ID: {}", id);
        try {
            boxService.deletarBox(id);
            log.info("Box com ID {} deletado com sucesso.", id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao deletar box - Não encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao deletar box com ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}