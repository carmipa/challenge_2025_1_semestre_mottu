package br.com.fiap.mottu.service;

import br.com.fiap.mottu.dto.veiculo.VeiculoRequestDto;
import br.com.fiap.mottu.dto.veiculo.VeiculoResponseDto;
import br.com.fiap.mottu.dto.veiculo.VeiculoLocalizacaoResponseDto; // NOVO: Importar o DTO de localização

import br.com.fiap.mottu.filter.VeiculoFilter;
import br.com.fiap.mottu.mapper.VeiculoMapper;
import br.com.fiap.mottu.mapper.RastreamentoMapper; // NOVO: Injetar para mapear Rastreamento
import br.com.fiap.mottu.mapper.PatioMapper;       // NOVO: Injetar para mapear Patio
import br.com.fiap.mottu.mapper.ZonaMapper;         // NOVO: Injetar para mapear Zona
import br.com.fiap.mottu.mapper.BoxMapper;           // NOVO: Injetar para mapear Box

import br.com.fiap.mottu.model.Veiculo;
import br.com.fiap.mottu.model.Rastreamento; // NOVO: Importar Rastreamento
import br.com.fiap.mottu.model.Patio;       // NOVO: Importar Patio
import br.com.fiap.mottu.model.Zona;         // NOVO: Importar Zona
import br.com.fiap.mottu.model.Box;          // NOVO: Importar Box

// Importar as classes de relacionamento
import br.com.fiap.mottu.model.relacionamento.VeiculoRastreamento;
import br.com.fiap.mottu.model.relacionamento.VeiculoPatio;
import br.com.fiap.mottu.model.relacionamento.VeiculoZona;
import br.com.fiap.mottu.model.relacionamento.VeiculoBox;

import br.com.fiap.mottu.repository.VeiculoRepository;
import br.com.fiap.mottu.repository.relacionamento.VeiculoRastreamentoRepository; // NOVO: Injetar

import br.com.fiap.mottu.exception.DuplicatedResourceException;
import br.com.fiap.mottu.exception.ResourceNotFoundException;
import br.com.fiap.mottu.specification.VeiculoSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime; // Para o timestamp da consulta
import java.util.List;
import java.util.Comparator; // Para encontrar o último rastreamento
import java.util.Optional; // Para lidar com Optional
import java.util.Set; // Para coleções
import java.util.stream.Collectors; // Para streams

@Service
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final VeiculoMapper veiculoMapper;
    private final VeiculoRastreamentoRepository veiculoRastreamentoRepository;
    private final RastreamentoMapper rastreamentoMapper;
    private final PatioMapper patioMapper;
    private final ZonaMapper zonaMapper;
    private final BoxMapper boxMapper;

    @Autowired
    public VeiculoService(VeiculoRepository veiculoRepository,
                          VeiculoMapper veiculoMapper,
                          VeiculoRastreamentoRepository veiculoRastreamentoRepository,
                          RastreamentoMapper rastreamentoMapper,
                          PatioMapper patioMapper,
                          ZonaMapper zonaMapper,
                          BoxMapper boxMapper) {
        this.veiculoRepository = veiculoRepository;
        this.veiculoMapper = veiculoMapper;
        this.veiculoRastreamentoRepository = veiculoRastreamentoRepository;
        this.rastreamentoMapper = rastreamentoMapper;
        this.patioMapper = patioMapper;
        this.zonaMapper = zonaMapper;
        this.boxMapper = boxMapper;
    }

    @Transactional(readOnly = true)
    public List<Veiculo> listarTodosVeiculos() {
        return veiculoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Veiculo buscarVeiculoPorId(Long id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo", id));
    }

    @Transactional(readOnly = true)
    public List<Veiculo> buscarVeiculosPorFiltro(VeiculoFilter filter) {
        return veiculoRepository.findAll(VeiculoSpecification.withFilters(filter));
    }

    @Transactional
    public Veiculo criarVeiculo(VeiculoRequestDto dto) {
        // Validações de duplicidade: placa, RENAVAM e chassi
        if (veiculoRepository.findByPlaca(dto.getPlaca()).isPresent()) {
            throw new DuplicatedResourceException("Veículo", "placa", dto.getPlaca());
        }
        if (veiculoRepository.findByRenavam(dto.getRenavam()).isPresent()) {
            throw new DuplicatedResourceException("Veículo", "RENAVAM", dto.getRenavam());
        }
        if (veiculoRepository.findByChassi(dto.getChassi()).isPresent()) {
            throw new DuplicatedResourceException("Veículo", "chassi", dto.getChassi());
        }
        Veiculo veiculo = veiculoMapper.toEntity(dto);
        return veiculoRepository.save(veiculo);
    }

    @Transactional
    public Veiculo atualizarVeiculo(Long id, VeiculoRequestDto dto) {
        Veiculo existente = veiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo", id));

        // Validação de placa duplicada (se alterada e diferente da placa de outro veículo)
        if (dto.getPlaca() != null && !dto.getPlaca().isBlank() && !dto.getPlaca().equals(existente.getPlaca())) {
            if (veiculoRepository.findByPlaca(dto.getPlaca())
                    .filter(v -> !v.getIdVeiculo().equals(id)) // Exclui o próprio veículo da checagem
                    .isPresent()) {
                throw new DuplicatedResourceException("Veículo", "placa", dto.getPlaca());
            }
        }

        // Validação de RENAVAM duplicado (se alterado e diferente do RENAVAM de outro veículo)
        if (dto.getRenavam() != null && !dto.getRenavam().isBlank() && !dto.getRenavam().equals(existente.getRenavam())) {
            if (veiculoRepository.findByRenavam(dto.getRenavam())
                    .filter(v -> !v.getIdVeiculo().equals(id)) // Exclui o próprio veículo
                    .isPresent()) {
                throw new DuplicatedResourceException("Veículo", "RENAVAM", dto.getRenavam());
            }
        }

        // Validação de chassi duplicado (se alterado e diferente do chassi de outro veículo)
        if (dto.getChassi() != null && !dto.getChassi().isBlank() && !dto.getChassi().equals(existente.getChassi())) {
            if (veiculoRepository.findByChassi(dto.getChassi())
                    .filter(v -> !v.getIdVeiculo().equals(id)) // Exclui o próprio veículo
                    .isPresent()) {
                throw new DuplicatedResourceException("Veículo", "chassi", dto.getChassi());
            }
        }

        veiculoMapper.partialUpdate(dto, existente);
        return veiculoRepository.save(existente);
    }

    @Transactional
    public void deletarVeiculo(Long id) {
        if (!veiculoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Veículo", id);
        }
        veiculoRepository.deleteById(id);
    }

    /**
     * Retorna o último ponto de rastreamento de um veículo e suas associações atuais
     * com Pátio, Zona e Box.
     * @param veiculoId ID do veículo.
     * @return DTO com informações de localização do veículo.
     * @throws ResourceNotFoundException se o veículo não for encontrado.
     */
    @Transactional(readOnly = true)
    public VeiculoLocalizacaoResponseDto getLocalizacaoVeiculo(Long veiculoId) {
        Veiculo veiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo", veiculoId));

        Rastreamento ultimoRastreamento = null;
        // Encontrar o último rastreamento associado ao veículo.
        // Assumimos que a tabela VeiculoRastreamento não tem um campo de timestamp próprio para a associação,
        // então usamos o ID do Rastreamento como proxy para o mais recente.
        // Se sua entidade Rastreamento tiver um campo de data/hora (ex: `dataHoraRegistro`),
        // é MELHOR usar: `Comparator.comparing(va -> va.getRastreamento().getDataHoraRegistro())`
        Optional<VeiculoRastreamento> ultimaAssociacaoRastreamento = veiculo.getVeiculoRastreamentos().stream()
                .max(Comparator.comparing(va -> va.getRastreamento().getIdRastreamento())); // Usando ID como proxy para o mais recente

        if (ultimaAssociacaoRastreamento.isPresent()) {
            ultimoRastreamento = ultimaAssociacaoRastreamento.get().getRastreamento();
        }

        // Encontrar Patio associado (um veículo pode estar em um Patio via VeiculoPatio)
        // Assumimos que um veículo está em no máximo um pátio por vez ou pegamos o primeiro.
        Patio patioAssociado = veiculo.getVeiculoPatios().stream()
                .map(VeiculoPatio::getPatio)
                .findFirst()
                .orElse(null);

        // Encontrar Zona associada (um veículo pode estar em uma Zona via VeiculoZona)
        // Assumimos que um veículo está em no máximo uma zona por vez ou pegamos a primeira.
        Zona zonaAssociada = veiculo.getVeiculoZonas().stream()
                .map(VeiculoZona::getZona)
                .findFirst()
                .orElse(null);

        // Encontrar Box associado (um veículo pode estar em um Box via VeiculoBox)
        // Assumimos que um veículo está em no máximo um box por vez ou pegamos o primeiro.
        Box boxAssociado = veiculo.getVeiculoBoxes().stream()
                .map(VeiculoBox::getBox)
                .findFirst()
                .orElse(null);

        return new VeiculoLocalizacaoResponseDto(
                veiculo.getIdVeiculo(),
                veiculo.getPlaca(),
                veiculo.getModelo(),
                veiculo.getFabricante(),
                (ultimoRastreamento != null) ? rastreamentoMapper.toResponseDto(ultimoRastreamento) : null,
                (patioAssociado != null) ? patioMapper.toResponseDto(patioAssociado) : null,
                (zonaAssociada != null) ? zonaMapper.toResponseDto(zonaAssociada) : null,
                (boxAssociado != null) ? boxMapper.toResponseDto(boxAssociado) : null,
                LocalDateTime.now() // Timestamp da consulta
        );
    }
}