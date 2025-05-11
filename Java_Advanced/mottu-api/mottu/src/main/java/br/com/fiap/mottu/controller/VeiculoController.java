package br.com.fiap.mottu.controller;

import br.com.fiap.mottu.dto.veiculo.VeiculoRequestDto;
import br.com.fiap.mottu.dto.veiculo.VeiculoResponseDto;
import br.com.fiap.mottu.dto.veiculo.VeiculoLocalizacaoResponseDto; // NOVO: Importar

import br.com.fiap.mottu.filter.VeiculoFilter;
import br.com.fiap.mottu.service.VeiculoService;
import br.com.fiap.mottu.mapper.VeiculoMapper;
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
@RequestMapping("/api/veiculos")
@Tag(name = "Veiculos", description = "Gerenciamento de Veículos e Rastreamento") // Altere o Tag para refletir o rastreamento
public class VeiculoController {

    private static final Logger log = LoggerFactory.getLogger(VeiculoController.class);
    private final VeiculoService veiculoService;
    private final VeiculoMapper veiculoMapper;

    @Autowired
    public VeiculoController(VeiculoService veiculoService,
                             VeiculoMapper veiculoMapper) {
        this.veiculoService = veiculoService;
        this.veiculoMapper = veiculoMapper;
    }

    @Operation(
            summary = "Listar todos os veículos",
            description = "Retorna uma lista de todos os veículos cadastrados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de veículos retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = VeiculoResponseDto.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<VeiculoResponseDto>> listarTodosVeiculos() {
        log.info("Buscando todos os veículos.");
        List<VeiculoResponseDto> veiculos = veiculoService.listarTodosVeiculos().stream()
                .map(veiculoMapper::toResponseDto)
                .collect(Collectors.toList());
        log.info("Retornando {} veículos.", veiculos.size());
        return ResponseEntity.ok(veiculos);
    }

    @Operation(
            summary = "Buscar veículo por ID",
            description = "Retorna um veículo específico com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Veículo encontrado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = VeiculoResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Veículo não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Veículo com ID 1 não encontrado(a).\",\"path\":\"/api/veiculos/1\"}")))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<VeiculoResponseDto> buscarVeiculoPorId(@PathVariable Long id) {
        log.info("Buscando veículo com ID: {}", id);
        try {
            VeiculoResponseDto veiculo = veiculoMapper.toResponseDto(veiculoService.buscarVeiculoPorId(id));
            log.info("Veículo com ID {} encontrado com sucesso.", id);
            return ResponseEntity.ok(veiculo);
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao buscar veículo com ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Operation(
            summary = "Buscar veículos por filtro",
            description = "Retorna uma lista de veículos que correspondem aos critérios de filtro fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de veículos filtrada retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = VeiculoResponseDto.class)))
            }
    )
    @GetMapping("/search")
    public ResponseEntity<List<VeiculoResponseDto>> buscarVeiculosPorFiltro(VeiculoFilter filter) {
        log.info("Buscando veículos com filtro: {}", filter);
        List<VeiculoResponseDto> veiculos = veiculoService.buscarVeiculosPorFiltro(filter).stream()
                .map(veiculoMapper::toResponseDto)
                .collect(Collectors.toList());
        log.info("Retornando {} veículos filtrados.", veiculos.size());
        return ResponseEntity.ok(veiculos);
    }

    @Operation(
            summary = "Criar novo veículo",
            description = "Cria um novo veículo com os dados fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Veículo criado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = VeiculoResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":400,\"error\":\"Requisição Inválida\",\"message\":\"Mensagem de validação ou erro de input.\",\"path\":\"/api/veiculos\"}"))),
                    @ApiResponse(responseCode = "409", description = "Conflito de recurso (placa, RENAVAM ou chassi duplicado)",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":409,\"error\":\"Conflito de Dados\",\"message\":\"Veículo com placa 'ABC1234' já existe.\",\"path\":\"/api/veiculos\"}")))
            }
    )
    @PostMapping
    public ResponseEntity<VeiculoResponseDto> criarVeiculo(@Valid @RequestBody VeiculoRequestDto veiculoRequestDto) {
        log.info("Recebida requisição para criar veículo: {}", veiculoRequestDto);
        try {
            VeiculoResponseDto novoVeiculo = veiculoMapper.toResponseDto(veiculoService.criarVeiculo(veiculoRequestDto));
            log.info("Veículo criado com sucesso com ID: {}", novoVeiculo.getIdVeiculo());
            return ResponseEntity.status(HttpStatus.CREATED).body(novoVeiculo);
        } catch (DuplicatedResourceException e) {
            log.error("Erro ao criar veículo - Conflito: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao criar veículo: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Atualizar veículo existente",
            description = "Atualiza um veículo existente com base no ID e nos dados fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Veículo atualizado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = VeiculoResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":400,\"error\":\"Requisição Inválida\",\"message\":\"Mensagem de validação ou erro de input.\",\"path\":\"/api/veiculos/1\"}"))),
                    @ApiResponse(responseCode = "404", description = "Veículo não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Veículo com ID 1 não encontrado(a).\",\"path\":\"/api/veiculos/1\"}"))),
                    @ApiResponse(responseCode = "409", description = "Conflito de recurso (placa, RENAVAM ou chassi duplicado)",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":409,\"error\":\"Conflito de Dados\",\"message\":\"Veículo com placa 'ABC1234' já existe.\",\"path\":\"/api/veiculos/1\"}")))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<VeiculoResponseDto> atualizarVeiculo(@PathVariable Long id, @Valid @RequestBody VeiculoRequestDto veiculoRequestDto) {
        log.info("Recebida requisição para atualizar veículo com ID {}: {}", id, veiculoRequestDto);
        try {
            VeiculoResponseDto veiculoAtualizado = veiculoMapper.toResponseDto(veiculoService.atualizarVeiculo(id, veiculoRequestDto));
            log.info("Veículo com ID {} atualizado com sucesso.", id);
            return ResponseEntity.ok(veiculoAtualizado);
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao atualizar veículo - Não encontrado: {}", e.getMessage());
            throw e;
        } catch (DuplicatedResourceException e) {
            log.error("Erro ao atualizar veículo - Conflito: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao atualizar veículo com ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Deletar veículo",
            description = "Exclui um veículo com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Veículo deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Veículo não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Veículo com ID 1 não encontrado(a).\",\"path\":\"/api/veiculos/1\"}")))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVeiculo(@PathVariable Long id) {
        log.info("Recebida requisição para deletar veículo com ID: {}", id);
        try {
            veiculoService.deletarVeiculo(id);
            log.info("Veículo com ID {} deletado com sucesso.", id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao deletar veículo - Não encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao deletar veículo com ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Endpoint para obter a localização de um veículo.
     * Retorna o último ponto de rastreamento do veículo e suas associações atuais com Pátio, Zona e Box.
     *
     * @param id O ID do veículo a ser localizado.
     * @return ResponseEntity contendo o DTO de localização do veículo ou um erro.
     */
    @Operation(
            summary = "Obter localização de um veículo",
            description = "Retorna o último ponto de rastreamento de um veículo e suas associações atuais com Pátio, Zona e Box.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Localização do veículo retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = VeiculoLocalizacaoResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Veículo não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Veículo com ID 1 não encontrado(a).\",\"path\":\"/api/veiculos/1/localizacao\"}")))
            }
    )
    @GetMapping("/{id}/localizacao")
    public ResponseEntity<VeiculoLocalizacaoResponseDto> getLocalizacaoVeiculo(@PathVariable Long id) {
        log.info("Buscando localização para o veículo com ID: {}", id);
        try {
            VeiculoLocalizacaoResponseDto localizacao = veiculoService.getLocalizacaoVeiculo(id);
            log.info("Localização do veículo com ID {} encontrada com sucesso.", id);
            return ResponseEntity.ok(localizacao);
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao buscar localização para o veículo com ID {}: {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao buscar localização para o veículo com ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}