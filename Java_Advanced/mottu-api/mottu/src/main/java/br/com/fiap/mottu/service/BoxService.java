package br.com.fiap.mottu.service;

import br.com.fiap.mottu.dto.box.BoxRequestDto;
import br.com.fiap.mottu.filter.BoxFilter;
import br.com.fiap.mottu.mapper.BoxMapper;
import br.com.fiap.mottu.model.Box;
import br.com.fiap.mottu.repository.BoxRepository;
import br.com.fiap.mottu.exception.DuplicatedResourceException;
import br.com.fiap.mottu.exception.ResourceNotFoundException;
import br.com.fiap.mottu.specification.BoxSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BoxService {

    private final BoxRepository boxRepository;
    private final BoxMapper boxMapper;

    @Autowired
    public BoxService(BoxRepository boxRepository, BoxMapper boxMapper) {
        this.boxRepository = boxRepository;
        this.boxMapper = boxMapper;
    }

    public List<Box> listarTodosBoxes() {
        return boxRepository.findAll();
    }

    public Box buscarBoxPorId(Long id) {
        return boxRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Box", id));
    }

    public List<Box> buscarBoxesPorFiltro(BoxFilter filter) {
        return boxRepository.findAll(BoxSpecification.withFilters(filter));
    }

    @Transactional
    public Box criarBox(BoxRequestDto boxRequestDto) {
        // Evita duplicação de nome
        String nome = boxRequestDto.getNome();
        if (boxRepository.findByNomeContainingIgnoreCase(nome)
                .stream().anyMatch(b -> b.getNome().equalsIgnoreCase(nome))) {
            throw new DuplicatedResourceException("Box", "nome", nome);
        }
        Box box = boxMapper.toEntity(boxRequestDto);
        return boxRepository.save(box);
    }

    @Transactional
    public Box atualizarBox(Long id, BoxRequestDto boxRequestDto) {
        return boxRepository.findById(id)
                .map(boxExistente -> {
                    String novoNome = boxRequestDto.getNome();
                    if (novoNome != null
                            && !novoNome.isBlank()
                            && !novoNome.equalsIgnoreCase(boxExistente.getNome())
                            && boxRepository.findByNomeContainingIgnoreCase(novoNome)
                            .stream().anyMatch(b -> b.getNome().equalsIgnoreCase(novoNome))) {
                        throw new DuplicatedResourceException("Box", "nome", novoNome);
                    }
                    boxMapper.partialUpdate(boxRequestDto, boxExistente);
                    return boxRepository.save(boxExistente);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Box", id));
    }

    @Transactional
    public void deletarBox(Long id) {
        if (!boxRepository.existsById(id)) {
            throw new ResourceNotFoundException("Box", id);
        }
        boxRepository.deleteById(id);
    }
}
