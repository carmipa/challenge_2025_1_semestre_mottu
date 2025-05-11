package br.com.fiap.mottu.service;

import br.com.fiap.mottu.dto.contato.ContatoRequestDto;
import br.com.fiap.mottu.filter.ContatoFilter;
import br.com.fiap.mottu.mapper.ContatoMapper;
import br.com.fiap.mottu.model.Contato;
import br.com.fiap.mottu.repository.ContatoRepository;
import br.com.fiap.mottu.exception.*;
import br.com.fiap.mottu.specification.ContatoSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ContatoService {

    private final ContatoRepository contatoRepository;
    private final ContatoMapper contatoMapper;

    @Autowired
    public ContatoService(ContatoRepository contatoRepository, ContatoMapper contatoMapper) {
        this.contatoRepository = contatoRepository;
        this.contatoMapper = contatoMapper;
    }

    public List<Contato> listarTodosContatos() {
        return contatoRepository.findAll();
    }

    public Contato buscarContatoPorId(Long id) {
        return contatoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato", id));
    }

    public List<Contato> buscarContatosPorFiltro(ContatoFilter filter) {
        return contatoRepository.findAll(ContatoSpecification.withFilters(filter));
    }

    @Transactional
    public Contato criarContato(ContatoRequestDto dto) {
        String email = dto.getEmail();
        if (contatoRepository.findByEmail(email).isPresent()) {
            throw new DuplicatedResourceException("Contato", "email", email);
        }
        return contatoRepository.save(contatoMapper.toEntity(dto));
    }

    @Transactional
    public Contato atualizarContato(Long id, ContatoRequestDto dto) {
        return contatoRepository.findById(id)
                .map(existente -> {
                    String novoEmail = dto.getEmail();
                    if (novoEmail != null
                            && !novoEmail.equals(existente.getEmail())
                            && contatoRepository.findByEmail(novoEmail).isPresent()) {
                        throw new DuplicatedResourceException("Contato", "email", novoEmail);
                    }
                    contatoMapper.partialUpdate(dto, existente);
                    return contatoRepository.save(existente);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Contato", id));
    }

    @Transactional
    public void deletarContato(Long id) {
        if (!contatoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contato", id);
        }
        contatoRepository.deleteById(id);
    }
}
