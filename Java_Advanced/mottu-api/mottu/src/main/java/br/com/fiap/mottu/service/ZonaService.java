package br.com.fiap.mottu.service;

import br.com.fiap.mottu.dto.zona.ZonaRequestDto;
import br.com.fiap.mottu.filter.ZonaFilter;
import br.com.fiap.mottu.mapper.ZonaMapper;
import br.com.fiap.mottu.model.Zona;
import br.com.fiap.mottu.repository.ZonaRepository;
import br.com.fiap.mottu.exception.*;
import br.com.fiap.mottu.specification.ZonaSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ZonaService {

    private final ZonaRepository zonaRepository;
    private final ZonaMapper zonaMapper;

    @Autowired
    public ZonaService(ZonaRepository zonaRepository, ZonaMapper zonaMapper) {
        this.zonaRepository = zonaRepository;
        this.zonaMapper = zonaMapper;
    }

    public List<Zona> listarTodasZonas() {
        return zonaRepository.findAll();
    }

    public Zona buscarZonaPorId(Long id) {
        return zonaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zona", id));
    }

    public List<Zona> buscarZonasPorFiltro(ZonaFilter filter) {
        return zonaRepository.findAll(ZonaSpecification.withFilters(filter));
    }

    @Transactional
    public Zona criarZona(ZonaRequestDto dto) {
        String nome = dto.getNome();
        if (zonaRepository.findByNomeContainingIgnoreCase(nome)
                .stream().anyMatch(z -> z.getNome().equalsIgnoreCase(nome))) {
            throw new DuplicatedResourceException("Zona", "nome", nome);
        }
        Zona z = zonaMapper.toEntity(dto);
        return zonaRepository.save(z);
    }

    @Transactional
    public Zona atualizarZona(Long id, ZonaRequestDto dto) {
        return zonaRepository.findById(id)
                .map(existente -> {
                    String novoNome = dto.getNome();
                    if (novoNome != null
                            && !novoNome.equalsIgnoreCase(existente.getNome())
                            && zonaRepository.findByNomeContainingIgnoreCase(novoNome)
                            .stream().anyMatch(z -> z.getNome().equalsIgnoreCase(novoNome))) {
                        throw new DuplicatedResourceException("Zona", "nome", novoNome);
                    }
                    zonaMapper.partialUpdate(dto, existente);
                    return zonaRepository.save(existente);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Zona", id));
    }

    @Transactional
    public void deletarZona(Long id) {
        if (!zonaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Zona", id);
        }
        zonaRepository.deleteById(id);
    }
}
