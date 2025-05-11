package br.com.fiap.mottu.service;

import br.com.fiap.mottu.dto.rastreamento.RastreamentoRequestDto;
import br.com.fiap.mottu.filter.RastreamentoFilter;
import br.com.fiap.mottu.mapper.RastreamentoMapper;
import br.com.fiap.mottu.model.Rastreamento;
import br.com.fiap.mottu.repository.RastreamentoRepository;
import br.com.fiap.mottu.exception.*;
import br.com.fiap.mottu.specification.RastreamentoSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.math.BigDecimal; // Importe BigDecimal!

@Service
public class RastreamentoService {

    private final RastreamentoRepository rastreamentoRepository;
    private final RastreamentoMapper rastreamentoMapper;

    @Autowired
    public RastreamentoService(RastreamentoRepository rastreamentoRepository,
                               RastreamentoMapper rastreamentoMapper) {
        this.rastreamentoRepository = rastreamentoRepository;
        this.rastreamentoMapper = rastreamentoMapper;
    }

    public List<Rastreamento> listarTodosRastreamentos() {
        return rastreamentoRepository.findAll();
    }

    public Rastreamento buscarRastreamentoPorId(Long id) {
        return rastreamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rastreamento", id));
    }

    public List<Rastreamento> buscarRastreamentosPorFiltro(RastreamentoFilter filter) {
        return rastreamentoRepository.findAll(RastreamentoSpecification.withFilters(filter));
    }

    @Transactional
    public Rastreamento criarRastreamento(RastreamentoRequestDto dto) {
        if (dto.getIpsX() == null || dto.getIpsY() == null || dto.getIpsZ() == null ||
                dto.getGprsLatitude() == null || dto.getGprsLongitude() == null || dto.getGprsAltitude() == null) {
            throw new InvalidInputException("Todas as coordenadas (IPS_X, IPS_Y, IPS_Z, GPRS_LATITUDE, GPRS_LONGITUDE, GPRS_ALTITUDE) são obrigatórias.");
        }
        Rastreamento r = rastreamentoMapper.toEntity(dto);
        return rastreamentoRepository.save(r);
    }

    @Transactional
    public Rastreamento atualizarRastreamento(Long id, RastreamentoRequestDto dto) {
        return rastreamentoRepository.findById(id)
                .map(existente -> {
                    rastreamentoMapper.partialUpdate(dto, existente);
                    return rastreamentoRepository.save(existente);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Rastreamento", id));
    }

    @Transactional
    public void deletarRastreamento(Long id) {
        if (!rastreamentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rastreamento", id);
        }
        rastreamentoRepository.deleteById(id);
    }
}