package br.com.fiap.mottu.controller;

import br.com.fiap.mottu.dto.patio.PatioRequestDto;
import br.com.fiap.mottu.dto.patio.PatioResponseDto;
import br.com.fiap.mottu.dto.veiculo.VeiculoResponseDto; // Para listar veículos associados
import br.com.fiap.mottu.dto.zona.ZonaResponseDto;       // Para listar zonas associadas
import br.com.fiap.mottu.dto.box.BoxResponseDto;    // NOVO: Para listar boxes associados
import br.com.fiap.mottu.dto.contato.ContatoResponseDto; // Para listar contatos associados
import br.com.fiap.mottu.dto.endereco.EnderecoResponseDto; // Para listar endereços associados

import br.com.fiap.mottu.filter.PatioFilter;
import br.com.fiap.mottu.service.PatioService;
import br.com.fiap.mottu.mapper.PatioMapper;
import br.com.fiap.mottu.mapper.VeiculoMapper; // Injetar para mapear Veiculo
import br.com.fiap.mottu.mapper.ZonaMapper;    // Injetar para mapear Zona
import br.com.fiap.mottu.mapper.BoxMapper;           // NOVO: Injetar para mapear Box
import br.com.fiap.mottu.mapper.ContatoMapper;  // Injetar para mapear Contato
import br.com.fiap.mottu.mapper.EnderecoMapper; // Injetar para mapear Endereco

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
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patios")
@Tag(name = "Patios", description = "Gerenciamento de Pátios e Suas Associações")
public class PatioController {

    private static final Logger log = LoggerFactory.getLogger(PatioController.class);
    private final PatioService patioService;
    private final PatioMapper patioMapper;
    private final VeiculoMapper veiculoMapper;
    private final ZonaMapper zonaMapper;
    private final BoxMapper boxMapper;           // NOVO
    private final ContatoMapper contatoMapper;
    private final EnderecoMapper enderecoMapper;

    @Autowired
    public PatioController(PatioService patioService, PatioMapper patioMapper,
                           VeiculoMapper veiculoMapper, ZonaMapper zonaMapper,
                           ContatoMapper contatoMapper, EnderecoMapper enderecoMapper,
                           BoxMapper boxMapper) { // NOVO: Adicione BoxMapper aqui
        this.patioService = patioService;
        this.patioMapper = patioMapper;
        this.veiculoMapper = veiculoMapper;
        this.zonaMapper = zonaMapper;
        this.contatoMapper = contatoMapper;
        this.enderecoMapper = enderecoMapper;
        this.boxMapper = boxMapper;
    }

    @Operation(
            summary = "Listar todos os pátios",
            description = "Retorna uma lista de todos os pátios cadastrados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de pátios retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PatioResponseDto.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<PatioResponseDto>> listarTodosPatios() {
        log.info("Buscando todos os pátios.");
        List<PatioResponseDto> patios = patioService.listarTodosPatios().stream()
                .map(patioMapper::toResponseDto)
                .collect(Collectors.toList());
        log.info("Retornando {} pátios.", patios.size());
        return ResponseEntity.ok(patios);
    }

    @Operation(
            summary = "Buscar pátio por ID",
            description = "Retorna um pátio específico com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pátio encontrado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PatioResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Pátio não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Pátio com ID 1 não encontrado(a).\",\"path\":\"/api/patios/1\"}")))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PatioResponseDto> buscarPatioPorId(@PathVariable Long id) {
        log.info("Buscando pátio com ID: {}", id);
        try {
            PatioResponseDto patio = patioMapper.toResponseDto(patioService.buscarPatioPorId(id));
            log.info("Pátio com ID {} encontrado com sucesso.", id);
            return ResponseEntity.ok(patio);
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao buscar pátio com ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Operation(
            summary = "Buscar pátios por filtro",
            description = "Retorna uma lista de pátios que correspondem aos critérios de filtro fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de pátios filtrada retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PatioResponseDto.class)))
            }
    )
    @GetMapping("/search")
    public ResponseEntity<List<PatioResponseDto>> buscarPatiosPorFiltro(PatioFilter filter) {
        log.info("Buscando pátios com filtro: {}", filter);
        List<PatioResponseDto> patios = patioService.buscarPatiosPorFiltro(filter).stream()
                .map(patioMapper::toResponseDto)
                .collect(Collectors.toList());
        log.info("Retornando {} pátios filtrados.", patios.size());
        return ResponseEntity.ok(patios);
    }

    @Operation(
            summary = "Criar novo pátio",
            description = "Cria um novo pátio com os dados fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Pátio criado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PatioResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":400,\"error\":\"Requisição Inválida\",\"message\":\"Mensagem de validação ou erro de input.\",\"path\":\"/api/patios\"}"))),
                    @ApiResponse(responseCode = "409", description = "Conflito de recurso (nome duplicado)",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":409,\"error\":\"Conflito de Dados\",\"message\":\"Pátio com nome 'Nome Duplicado' já existe.\",\"path\":\"/api/patios\"}")))
            }
    )
    @PostMapping
    public ResponseEntity<PatioResponseDto> criarPatio(@Valid @RequestBody PatioRequestDto patioRequestDto) {
        log.info("Recebida requisição para criar pátio: {}", patioRequestDto);
        try {
            PatioResponseDto novoPatio = patioMapper.toResponseDto(patioService.criarPatio(patioRequestDto));
            log.info("Pátio criado com sucesso com ID: {}", novoPatio.getIdPatio());
            return ResponseEntity.status(HttpStatus.CREATED).body(novoPatio);
        } catch (DuplicatedResourceException e) {
            log.error("Erro ao criar pátio - Conflito: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao criar pátio: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Atualizar pátio existente",
            description = "Atualiza um pátio existente com base no ID e nos dados fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pátio atualizado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PatioResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":400,\"error\":\"Requisição Inválida\",\"message\":\"Mensagem de validação ou erro de input.\",\"path\":\"/api/patios/1\"}"))),
                    @ApiResponse(responseCode = "404", description = "Pátio não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Pátio com ID 1 não encontrado(a).\",\"path\":\"/api/patios/1\"}"))),
                    @ApiResponse(responseCode = "409", description = "Conflito de recurso (nome duplicado)",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":409,\"error\":\"Conflito de Dados\",\"message\":\"Pátio com nome 'Nome Duplicado' já existe.\",\"path\":\"/api/patios/1\"}")))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<PatioResponseDto> atualizarPatio(@PathVariable Long id, @Valid @RequestBody PatioRequestDto patioRequestDto) {
        log.info("Recebida requisição para atualizar pátio com ID {}: {}", id, patioRequestDto);
        try {
            PatioResponseDto patioAtualizado = patioMapper.toResponseDto(patioService.atualizarPatio(id, patioRequestDto));
            log.info("Pátio com ID {} atualizado com sucesso.", id);
            return ResponseEntity.ok(patioAtualizado);
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao atualizar pátio - Não encontrado: {}", e.getMessage());
            throw e;
        } catch (DuplicatedResourceException e) {
            log.error("Erro ao atualizar pátio - Conflito: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao atualizar pátio com ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Deletar pátio",
            description = "Exclui um pátio com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Pátio deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Pátio não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timestamp\":\"2025-01-01T12:00:00\",\"status\":404,\"error\":\"Não Encontrado\",\"message\":\"Pátio com ID 1 não encontrado(a).\",\"path\":\"/api/patios/1\"}")))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPatio(@PathVariable Long id) {
        log.info("Recebida requisição para deletar pátio com ID: {}", id);
        try {
            patioService.deletarPatio(id);
            log.info("Pátio com ID {} deletado com sucesso.", id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao deletar pátio - Não encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao deletar pátio com ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    // --- Endpoints de Associação de Veículos com Pátio ---
    @Operation(
            summary = "Associar veículo a um pátio",
            description = "Associa um veículo existente a um pátio existente.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Associação criada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Pátio ou Veículo não encontrado"),
                    @ApiResponse(responseCode = "409", description = "Associação já existe")
            }
    )
    @PostMapping("/{patioId}/veiculos/{veiculoId}/associar")
    public ResponseEntity<String> associarPatioVeiculo(
            @PathVariable Long patioId,
            @PathVariable Long veiculoId) {
        log.info("Associando veículo ID {} ao pátio ID {}.", veiculoId, patioId);
        try {
            patioService.associarPatioVeiculo(patioId, veiculoId);
            log.info("Associação entre Pátio {} e Veículo {} criada com sucesso.", patioId, veiculoId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Associação criada com sucesso.");
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao associar - Recurso não encontrado: {}", e.getMessage());
            throw e;
        } catch (DuplicatedResourceException e) {
            log.error("Erro ao associar - Conflito: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao associar Pátio {} e Veículo {}: {}", patioId, veiculoId, e.getMessage(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Desassociar veículo de um pátio",
            description = "Remove a associação entre um veículo e um pátio.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Associação removida com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Associação não encontrada")
            }
    )
    @DeleteMapping("/{patioId}/veiculos/{veiculoId}/desassociar")
    public ResponseEntity<Void> desassociarPatioVeiculo(
            @PathVariable Long patioId,
            @PathVariable Long veiculoId) {
        log.info("Desassociando veículo ID {} do pátio ID {}.", veiculoId, patioId);
        try {
            patioService.desassociarPatioVeiculo(patioId, veiculoId);
            log.info("Associação entre Pátio {} e Veículo {} removida com sucesso.", patioId, veiculoId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao desassociar - Associação não encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao desassociar Pátio {} e Veículo {}: {}", patioId, veiculoId, e.getMessage(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Listar veículos de um pátio",
            description = "Retorna todos os veículos associados a um pátio específico.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Veículos do pátio retornados com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = VeiculoResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Pátio não encontrado")
            }
    )
    @GetMapping("/{patioId}/veiculos")
    public ResponseEntity<Set<VeiculoResponseDto>> getVeiculosByPatioId(@PathVariable Long patioId) {
        log.info("Buscando veículos associados ao pátio com ID: {}", patioId);
        try {
            Set<VeiculoResponseDto> veiculos = patioService.getVeiculosByPatioId(patioId).stream()
                    .map(veiculoMapper::toResponseDto)
                    .collect(Collectors.toSet());
            log.info("Retornando {} veículos para o pátio com ID {}.", veiculos.size(), patioId);
            return ResponseEntity.ok(veiculos);
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao buscar veículos do pátio com ID {}: {}", patioId, e.getMessage());
            throw e;
        }
    }


    // --- Endpoints de Associação de Zonas com Pátio ---
    @Operation(
            summary = "Associar zona a um pátio",
            description = "Associa uma zona existente a um pátio existente.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Associação criada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Pátio ou Zona não encontrada"),
                    @ApiResponse(responseCode = "409", description = "Associação já existe")
            }
    )
    @PostMapping("/{patioId}/zonas/{zonaId}/associar")
    public ResponseEntity<String> associarPatioZona(
            @PathVariable Long patioId,
            @PathVariable Long zonaId) {
        log.info("Associando zona ID {} ao pátio ID {}.", zonaId, patioId);
        try {
            patioService.associarPatioZona(patioId, zonaId);
            log.info("Associação entre Pátio {} e Zona {} criada com sucesso.", patioId, zonaId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Associação criada com sucesso.");
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao associar - Recurso não encontrado: {}", e.getMessage());
            throw e;
        } catch (DuplicatedResourceException e) {
            log.error("Erro ao associar - Conflito: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao associar Pátio {} e Zona {}: {}", patioId, zonaId, e.getMessage(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Desassociar zona de um pátio",
            description = "Remove a associação entre uma zona e um pátio.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Associação removida com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Associação não encontrada")
            }
    )
    @DeleteMapping("/{patioId}/zonas/{zonaId}/desassociar")
    public ResponseEntity<Void> desassociarPatioZona(
            @PathVariable Long patioId,
            @PathVariable Long zonaId) {
        log.info("Desassociando zona ID {} do pátio ID {}.", zonaId, patioId);
        try {
            patioService.desassociarPatioZona(patioId, zonaId);
            log.info("Associação entre Pátio {} e Zona {} removida com sucesso.", patioId, zonaId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao desassociar - Associação não encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao desassociar Pátio {} e Zona {}: {}", patioId, zonaId, e.getMessage(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Listar zonas de um pátio",
            description = "Retorna todas as zonas associadas a um pátio específico.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Zonas do pátio retornadas com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ZonaResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Pátio não encontrado")
            }
    )
    @GetMapping("/{patioId}/zonas")
    public ResponseEntity<Set<ZonaResponseDto>> getZonasByPatioId(@PathVariable Long patioId) {
        log.info("Buscando zonas associadas ao pátio com ID: {}", patioId);
        try {
            Set<ZonaResponseDto> zonas = patioService.getZonasByPatioId(patioId).stream()
                    .map(zonaMapper::toResponseDto)
                    .collect(Collectors.toSet());
            log.info("Retornando {} zonas para o pátio com ID {}.", zonas.size(), patioId);
            return ResponseEntity.ok(zonas);
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao buscar zonas do pátio com ID {}: {}", patioId, e.getMessage());
            throw e;
        }
    }

    // --- Endpoints de Associação de Contatos com Pátio ---
    @Operation(
            summary = "Associar contato a um pátio",
            description = "Associa um contato existente a um pátio existente.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Associação criada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Pátio ou Contato não encontrado"),
                    @ApiResponse(responseCode = "409", description = "Associação já existe")
            }
    )
    @PostMapping("/{patioId}/contatos/{contatoId}/associar")
    public ResponseEntity<String> associarPatioContato(
            @PathVariable Long patioId,
            @PathVariable Long contatoId) {
        log.info("Associando contato ID {} ao pátio ID {}.", contatoId, patioId);
        try {
            patioService.associarPatioContato(patioId, contatoId);
            log.info("Associação entre Pátio {} e Contato {} criada com sucesso.", patioId, contatoId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Associação criada com sucesso.");
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao associar - Recurso não encontrado: {}", e.getMessage());
            throw e;
        } catch (DuplicatedResourceException e) {
            log.error("Erro ao associar - Conflito: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao associar Pátio {} e Contato {}: {}", patioId, contatoId, e.getMessage(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Desassociar contato de um pátio",
            description = "Remove a associação entre um contato e um pátio.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Associação removida com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Associação não encontrada")
            }
    )
    @DeleteMapping("/{patioId}/contatos/{contatoId}/desassociar")
    public ResponseEntity<Void> desassociarPatioContato(
            @PathVariable Long patioId,
            @PathVariable Long contatoId) {
        log.info("Desassociando contato ID {} do pátio ID {}.", contatoId, patioId);
        try {
            patioService.desassociarPatioContato(patioId, contatoId);
            log.info("Associação entre Pátio {} e Contato {} removida com sucesso.", patioId, contatoId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao desassociar - Associação não encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao desassociar Pátio {} e Contato {}: {}", patioId, contatoId, e.getMessage(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Listar contatos de um pátio",
            description = "Retorna todos os contatos associados a um pátio específico.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contatos do pátio retornados com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ContatoResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Pátio não encontrado")
            }
    )
    @GetMapping("/{patioId}/contatos")
    public ResponseEntity<Set<ContatoResponseDto>> getContatosByPatioId(@PathVariable Long patioId) {
        log.info("Buscando contatos associados ao pátio com ID: {}", patioId);
        try {
            Set<ContatoResponseDto> contatos = patioService.getContatosByPatioId(patioId).stream()
                    .map(contatoMapper::toResponseDto)
                    .collect(Collectors.toSet());
            log.info("Retornando {} contatos para o pátio com ID {}.", contatos.size(), patioId);
            return ResponseEntity.ok(contatos);
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao buscar contatos do pátio com ID {}: {}", patioId, e.getMessage());
            throw e;
        }
    }

    // --- Endpoints de Associação de Endereços com Pátio ---
    @Operation(
            summary = "Associar endereço a um pátio",
            description = "Associa um endereço existente a um pátio existente.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Associação criada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Pátio ou Endereço não encontrado"),
                    @ApiResponse(responseCode = "409", description = "Associação já existe")
            }
    )
    @PostMapping("/{patioId}/enderecos/{enderecoId}/associar")
    public ResponseEntity<String> associarPatioEndereco(
            @PathVariable Long patioId,
            @PathVariable Long enderecoId) {
        log.info("Associando endereço ID {} ao pátio ID {}.", enderecoId, patioId);
        try {
            patioService.associarPatioEndereco(patioId, enderecoId);
            log.info("Associação entre Pátio {} e Endereço {} criada com sucesso.", patioId, enderecoId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Associação criada com sucesso.");
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao associar - Recurso não encontrado: {}", e.getMessage());
            throw e;
        } catch (DuplicatedResourceException e) {
            log.error("Erro ao associar - Conflito: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao associar Pátio {} e Endereço {}: {}", patioId, enderecoId, e.getMessage(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Desassociar endereço de um pátio",
            description = "Remove a associação entre um endereço e um pátio.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Associação removida com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Associação não encontrada")
            }
    )
    @DeleteMapping("/{patioId}/enderecos/{enderecoId}/desassociar")
    public ResponseEntity<Void> desassociarPatioEndereco(
            @PathVariable Long patioId,
            @PathVariable Long enderecoId) {
        log.info("Desassociando endereço ID {} do pátio ID {}.", enderecoId, patioId);
        try {
            patioService.desassociarPatioEndereco(patioId, enderecoId);
            log.info("Associação entre Pátio {} e Endereço {} removida com sucesso.", patioId, enderecoId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao desassociar - Associação não encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao desassociar Pátio {} e Endereço {}: {}", patioId, enderecoId, e.getMessage(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Listar endereços de um pátio",
            description = "Retorna todos os endereços associados a um pátio específico.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Endereços do pátio retornados com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EnderecoResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Pátio não encontrado")
            }
    )
    @GetMapping("/{patioId}/enderecos")
    public ResponseEntity<Set<EnderecoResponseDto>> getEnderecosByPatioId(@PathVariable Long patioId) {
        log.info("Buscando endereços associados ao pátio com ID: {}", patioId);
        try {
            Set<EnderecoResponseDto> enderecos = patioService.getEnderecosByPatioId(patioId).stream()
                    .map(enderecoMapper::toResponseDto)
                    .collect(Collectors.toSet());
            log.info("Retornando {} endereços para o pátio com ID {}.", enderecos.size(), patioId);
            return ResponseEntity.ok(enderecos);
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao buscar endereços do pátio com ID {}: {}", patioId, e.getMessage());
            throw e;
        }
    }

    // --- Endpoints de Associação de Boxes com Pátio (NOVO) ---
    @Operation(
            summary = "Associar box a um pátio",
            description = "Associa um box existente a um pátio existente.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Associação criada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Pátio ou Box não encontrado"),
                    @ApiResponse(responseCode = "409", description = "Associação já existe")
            }
    )
    @PostMapping("/{patioId}/boxes/{boxId}/associar")
    public ResponseEntity<String> associarPatioBox(
            @PathVariable Long patioId,
            @PathVariable Long boxId) {
        log.info("Associando box ID {} ao pátio ID {}.", boxId, patioId);
        try {
            patioService.associarPatioBox(patioId, boxId);
            log.info("Associação entre Pátio {} e Box {} criada com sucesso.", patioId, boxId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Associação criada com sucesso.");
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao associar - Recurso não encontrado: {}", e.getMessage());
            throw e;
        } catch (DuplicatedResourceException e) {
            log.error("Erro ao associar - Conflito: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao associar Pátio {} e Box {}: {}", patioId, boxId, e.getMessage(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Desassociar box de um pátio",
            description = "Remove a associação entre um box e um pátio.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Associação removida com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Associação não encontrada")
            }
    )
    @DeleteMapping("/{patioId}/boxes/{boxId}/desassociar")
    public ResponseEntity<Void> desassociarPatioBox(
            @PathVariable Long patioId,
            @PathVariable Long boxId) {
        log.info("Desassociando box ID {} do pátio ID {}.", boxId, patioId);
        try {
            patioService.desassociarPatioBox(patioId, boxId);
            log.info("Associação entre Pátio {} e Box {} removida com sucesso.", patioId, boxId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao desassociar - Associação não encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao desassociar Pátio {} e Box {}: {}", patioId, boxId, e.getMessage(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Listar boxes de um pátio",
            description = "Retorna todos os boxes associados a um pátio específico.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Boxes do pátio retornados com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BoxResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Pátio não encontrado")
            }
    )
    @GetMapping("/{patioId}/boxes")
    public ResponseEntity<Set<BoxResponseDto>> getBoxesByPatioId(@PathVariable Long patioId) {
        log.info("Buscando boxes associados ao pátio com ID: {}", patioId);
        try {
            Set<BoxResponseDto> boxes = patioService.getBoxesByPatioId(patioId).stream()
                    .map(boxMapper::toResponseDto)
                    .collect(Collectors.toSet());
            log.info("Retornando {} boxes para o pátio com ID {}.", boxes.size(), patioId);
            return ResponseEntity.ok(boxes);
        } catch (ResourceNotFoundException e) {
            log.error("Erro ao buscar boxes do pátio com ID {}: {}", patioId, e.getMessage());
            throw e;
        }
    }
}