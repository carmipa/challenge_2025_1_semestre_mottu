// br/com/fiap/mottu/service/EnderecoService.java
package br.com.fiap.mottu.service;

import br.com.fiap.mottu.dto.endereco.EnderecoRequestDto;
import br.com.fiap.mottu.external.viacep.ViaCepService;
import br.com.fiap.mottu.model.Endereco;
import br.com.fiap.mottu.mapper.EnderecoMapper;
import br.com.fiap.mottu.repository.EnderecoRepository;
import br.com.fiap.mottu.exception.ResourceNotFoundException;
import br.com.fiap.mottu.exception.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import java.util.List;

@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final EnderecoMapper enderecoMapper;
    private final ViaCepService viaCepService;

    @Autowired
    public EnderecoService(EnderecoRepository enderecoRepository,
                           EnderecoMapper enderecoMapper,
                           ViaCepService viaCepService) {
        this.enderecoRepository = enderecoRepository;
        this.enderecoMapper = enderecoMapper;
        this.viaCepService = viaCepService;
    }

    public List<Endereco> listarTodosEnderecos() {
        return enderecoRepository.findAll();
    }

    public Endereco buscarEnderecoPorId(Long id) {
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço", id));
    }

    @Transactional
    public Mono<Endereco> criarEndereco(EnderecoRequestDto dto) {
        // Validação básica
        if (dto.getCep() == null || dto.getCep().isBlank()) {
            return Mono.error(new InvalidInputException("CEP não pode ser nulo ou vazio."));
        }

        return viaCepService.buscarEnderecoPorCep(dto.getCep())
                .flatMap(resp -> {
                    Endereco endereco = enderecoMapper.toEntity(dto);
                    endereco.setLogradouro(resp.getLogradouro());
                    endereco.setBairro(resp.getBairro());
                    endereco.setCidade(resp.getLocalidade());
                    endereco.setEstado(resp.getUf());
                    endereco.setPais("Brasil");
                    return Mono.just(enderecoRepository.save(endereco));
                })
                // Aqui usamos o construtor de três argumentos:
                // (nome do recurso, nome do campo, valor do campo)
                .switchIfEmpty(Mono.error(
                        new ResourceNotFoundException("CEP", "cep", dto.getCep())
                ))
                .onErrorResume(e -> (e instanceof InvalidInputException)
                        ? Mono.error(e)
                        : Mono.error(new InvalidInputException("Erro ao criar endereço: " + e.getMessage())));
    }


    @Transactional
    public Mono<Endereco> atualizarEndereco(Long id, EnderecoRequestDto dto) {
        return Mono.justOrEmpty(enderecoRepository.findById(id))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Endereço", id)))
                .flatMap(existing -> {
                    if (dto.getCep() != null && !dto.getCep().isBlank()) {
                        return viaCepService.buscarEnderecoPorCep(dto.getCep())
                                .flatMap(resp -> {
                                    enderecoMapper.partialUpdate(dto, existing);
                                    existing.setLogradouro(resp.getLogradouro());
                                    existing.setBairro(resp.getBairro());
                                    existing.setCidade(resp.getLocalidade());
                                    existing.setEstado(resp.getUf());
                                    existing.setPais("Brasil");
                                    return Mono.just(enderecoRepository.save(existing));
                                })
                                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Dados de CEP para atualização", "CEP", dto.getCep())));
                    } else {
                        enderecoMapper.partialUpdate(dto, existing);
                        return Mono.just(enderecoRepository.save(existing));
                    }
                })
                .onErrorResume(e -> (e instanceof InvalidInputException || e instanceof ResourceNotFoundException)
                        ? Mono.error(e)
                        : Mono.error(new InvalidInputException("Erro inesperado ao atualizar endereço: " + e.getMessage())));
    }

    @Transactional
    public void deletarEndereco(Long id) {
        if (!enderecoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Endereço", id);
        }
        enderecoRepository.deleteById(id);
    }
}