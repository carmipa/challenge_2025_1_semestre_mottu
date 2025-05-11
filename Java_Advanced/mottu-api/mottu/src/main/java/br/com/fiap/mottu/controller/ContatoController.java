package br.com.fiap.mottu.controller;

import br.com.fiap.mottu.dto.contato.ContatoRequestDto;
import br.com.fiap.mottu.dto.contato.ContatoResponseDto;
import br.com.fiap.mottu.filter.ContatoFilter;
import br.com.fiap.mottu.service.ContatoService;
import br.com.fiap.mottu.mapper.ContatoMapper;
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
@RequestMapping("/api/contatos")
@Tag(name = "Contatos", description = "Gerenciamento de Contatos")
public class ContatoController {

    private static final Logger log = LoggerFactory.getLogger(ContatoController.class);
    private final ContatoService contatoService;
    private final ContatoMapper contatoMapper;

    @Autowired
    public ContatoController(ContatoService contatoService, ContatoMapper contatoMapper) {
        this.contatoService = contatoService;
        this.contatoMapper = contatoMapper;
    }

    @Operation(
            summary = "Listar todos os contatos",
            description = "Retorna uma lista de todos os contatos cadastrados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de contatos retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ContatoResponseDto.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<ContatoResponseDto>> listarTodosContatos() {
        log.info("Buscando todos os contatos.");
        List<ContatoResponseDto> contatos = contatoService.listarTodosContatos().stream()
                .map(contatoMapper::toResponseDto)
                .collect(Collectors.toList());
        log.info("Retornando {} contatos.", contatos.size());
        return ResponseEntity.ok(contatos);
    }

    @Operation(
            summary = "Buscar contato por ID",
            description = "Retorna um contato específico com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contato encontrado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ContatoResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Contato não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Contato com ID 1 não encontrado(a).\",\"path\":\"/api/contatos/1\"}")))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ContatoResponseDto> buscarContatoPorId(@PathVariable Long id) {
        log.info("Buscando contato com ID: {}", id);
        try {
            ContatoResponseDto contato = contatoMapper.toResponseDto(contatoService.buscarContatoPorId(id));
            log.info("Contato com ID {} encontrado com sucesso.", id);
            return ResponseEntity.ok(contato);
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao buscar contato com ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Operation(
            summary = "Buscar contatos por filtro",
            description = "Retorna uma lista de contatos que correspondem aos critérios de filtro fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de contatos filtrada retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ContatoResponseDto.class)))
            }
    )
    @GetMapping("/search")
    public ResponseEntity<List<ContatoResponseDto>> buscarContatosPorFiltro(ContatoFilter filter) {
        log.info("Buscando contatos com filtro: {}", filter);
        List<ContatoResponseDto> contatos = contatoService.buscarContatosPorFiltro(filter).stream()
                .map(contatoMapper::toResponseDto)
                .collect(Collectors.toList());
        log.info("Retornando {} contatos filtrados.", contatos.size());
        return ResponseEntity.ok(contatos);
    }

    @Operation(
            summary = "Criar novo contato",
            description = "Cria um novo contato com os dados fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Contato criado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ContatoResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":400,\"error\":\"Requisição Inválida\",\"message\":\"Mensagem de validação ou erro de input.\",\"path\":\"/api/contatos\"}"))),
                    @ApiResponse(responseCode = "409", description = "Conflito de recurso (email duplicado)",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":409,\"error\":\"Conflito de Dados\",\"message\":\"Contato com email 'email@duplicado.com' já existe.\",\"path\":\"/api/contatos\"}")))
            }
    )
    @PostMapping
    public ResponseEntity<ContatoResponseDto> criarContato(@Valid @RequestBody ContatoRequestDto contatoRequestDto) {
        log.info("Recebida requisição para criar contato: {}", contatoRequestDto);
        try {
            ContatoResponseDto novoContato = contatoMapper.toResponseDto(contatoService.criarContato(contatoRequestDto));
            log.info("Contato criado com sucesso com ID: {}", novoContato.getIdContato());
            return ResponseEntity.status(HttpStatus.CREATED).body(novoContato);
        } catch (DuplicatedResourceException e) {
            log.error("Erro ao criar contato - Conflito: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao criar contato: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Atualizar contato existente",
            description = "Atualiza um contato existente com base no ID e nos dados fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contato atualizado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ContatoResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":400,\"error\":\"Requisição Inválida\",\"message\":\"Mensagem de validação ou erro de input.\",\"path\":\"/api/contatos/1\"}"))),
                    @ApiResponse(responseCode = "404", description = "Contato não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Contato com ID 1 não encontrado(a).\",\"path\":\"/api/contatos/1\"}"))),
                    @ApiResponse(responseCode = "409", description = "Conflito de recurso (email duplicado)",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":409,\"error\":\"Conflito de Dados\",\"message\":\"Contato com email 'email@duplicado.com' já existe.\",\"path\":\"/api/contatos/1\"}")))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ContatoResponseDto> atualizarContato(@PathVariable Long id, @Valid @RequestBody ContatoRequestDto contatoRequestDto) {
        log.info("Recebida requisição para atualizar contato com ID {}: {}", id, contatoRequestDto);
        try {
            ContatoResponseDto contatoAtualizado = contatoMapper.toResponseDto(contatoService.atualizarContato(id, contatoRequestDto));
            log.info("Contato com ID {} atualizado com sucesso.", id);
            return ResponseEntity.ok(contatoAtualizado);
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao atualizar contato - Não encontrado: {}", e.getMessage());
            throw e;
        } catch (DuplicatedResourceException e) {
            log.error("Erro ao atualizar contato - Conflito: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao atualizar contato com ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Deletar contato",
            description = "Exclui um contato com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Contato deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Contato não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Contato com ID 1 não encontrado(a).\",\"path\":\"/api/contatos/1\"}")))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarContato(@PathVariable Long id) {
        log.info("Recebida requisição para deletar contato com ID: {}", id);
        try {
            contatoService.deletarContato(id);
            log.info("Contato com ID {} deletado com sucesso.", id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao deletar contato - Não encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao deletar contato com ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}