// br/com/fiap/mottu/service/ClienteService.java
package br.com.fiap.mottu.service;

import br.com.fiap.mottu.dto.cliente.ClienteRequestDto;
import br.com.fiap.mottu.dto.endereco.EnderecoRequestDto;
import br.com.fiap.mottu.dto.contato.ContatoRequestDto;
import br.com.fiap.mottu.filter.ClienteFilter;
import br.com.fiap.mottu.model.Cliente;
import br.com.fiap.mottu.model.Contato;
import br.com.fiap.mottu.model.Endereco;
import br.com.fiap.mottu.model.Veiculo;
import br.com.fiap.mottu.model.relacionamento.ClienteVeiculo;
import br.com.fiap.mottu.model.relacionamento.ClienteVeiculoId;
import br.com.fiap.mottu.repository.ClienteRepository;
import br.com.fiap.mottu.repository.relacionamento.ClienteVeiculoRepository;
import br.com.fiap.mottu.repository.ContatoRepository;
import br.com.fiap.mottu.repository.EnderecoRepository;
import br.com.fiap.mottu.repository.VeiculoRepository;
import br.com.fiap.mottu.specification.ClienteSpecification;
import br.com.fiap.mottu.exception.ResourceNotFoundException;
import br.com.fiap.mottu.exception.DuplicatedResourceException;
import br.com.fiap.mottu.exception.InvalidInputException;
import br.com.fiap.mottu.mapper.ClienteMapper;
import br.com.fiap.mottu.mapper.ContatoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final EnderecoRepository enderecoRepository;
    private final ContatoRepository contatoRepository;
    private final VeiculoRepository veiculoRepository;
    private final ClienteVeiculoRepository clienteVeiculoRepository;
    private final EnderecoService enderecoService;
    private final ClienteMapper clienteMapper;
    private final ContatoMapper contatoMapper;

    @Autowired
    public ClienteService(
            ClienteRepository clienteRepository,
            EnderecoRepository enderecoRepository,
            ContatoRepository contatoRepository,
            VeiculoRepository veiculoRepository,
            ClienteVeiculoRepository clienteVeiculoRepository,
            EnderecoService enderecoService,
            ClienteMapper clienteMapper,
            ContatoMapper contatoMapper
    ) {
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
        this.contatoRepository = contatoRepository;
        this.veiculoRepository = veiculoRepository;
        this.clienteVeiculoRepository = clienteVeiculoRepository;
        this.enderecoService = enderecoService;
        this.clienteMapper = clienteMapper;
        this.contatoMapper = contatoMapper;
    }

    public List<Cliente> listarTodosClientes() {
        return clienteRepository.findAll();
    }

    public Cliente buscarClientePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", id));
    }

    public List<Cliente> buscarClientesPorFiltro(ClienteFilter filter) {
        return clienteRepository.findAll(ClienteSpecification.withFilters(filter));
    }

    @Transactional
    public Mono<Cliente> criarCliente(ClienteRequestDto dto) {
        if (clienteRepository.findByCpf(dto.getCpf()).isPresent()) {
            return Mono.error(new DuplicatedResourceException("Cliente", "CPF", dto.getCpf()));
        }

        Mono<Endereco> enderecoMono = (dto.getEnderecoRequestDto().getIdEndereco() != null)
                ? Mono.justOrEmpty(enderecoRepository.findById(dto.getEnderecoRequestDto().getIdEndereco()))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Endereço", dto.getEnderecoRequestDto().getIdEndereco())))
                : enderecoService.criarEndereco(dto.getEnderecoRequestDto());

        Mono<Contato> contatoMono = (dto.getContatoRequestDto().getIdContato() != null)
                ? Mono.justOrEmpty(contatoRepository.findById(dto.getContatoRequestDto().getIdContato()))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Contato", dto.getContatoRequestDto().getIdContato())))
                : Mono.just(contatoRepository.save(contatoMapper.toEntity(dto.getContatoRequestDto())));

        return Mono.zip(enderecoMono, contatoMono)
                .flatMap(tuple -> {
                    Cliente cliente = clienteMapper.toEntity(dto);
                    cliente.setEndereco(tuple.getT1());
                    cliente.setContato(tuple.getT2());
                    return Mono.just(clienteRepository.save(cliente));
                })
                .onErrorResume(e -> {
                    if (e instanceof ResourceNotFoundException
                            || e instanceof DuplicatedResourceException
                            || e instanceof InvalidInputException) {
                        return Mono.error(e);
                    }
                    return Mono.error(new InvalidInputException("Erro inesperado ao criar cliente: " + e.getMessage()));
                });
    }

    @Transactional
    public Mono<Cliente> atualizarCliente(Long id, ClienteRequestDto dto) {
        return Mono.justOrEmpty(clienteRepository.findById(id))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cliente", id)))
                .flatMap(clienteExistente -> {
                    if (dto.getCpf() != null
                            && !dto.getCpf().equals(clienteExistente.getCpf())
                            && clienteRepository.findByCpf(dto.getCpf()).isPresent()) {
                        return Mono.error(new DuplicatedResourceException("Cliente", "CPF", dto.getCpf()));
                    }

                    clienteMapper.partialUpdate(dto, clienteExistente);

                    Mono<Endereco> enderecoMono;
                    if (dto.getEnderecoRequestDto() != null) {
                        if (dto.getEnderecoRequestDto().getIdEndereco() != null) {
                            enderecoMono = enderecoService.atualizarEndereco(dto.getEnderecoRequestDto().getIdEndereco(), dto.getEnderecoRequestDto());
                        } else {
                            enderecoMono = enderecoService.criarEndereco(dto.getEnderecoRequestDto());
                        }
                    } else {
                        enderecoMono = Mono.just(clienteExistente.getEndereco());
                    }

                    Mono<Contato> contatoMono;
                    if (dto.getContatoRequestDto() != null) {
                        if (dto.getContatoRequestDto().getIdContato() != null) {
                            contatoMono = Mono.justOrEmpty(contatoRepository.findById(dto.getContatoRequestDto().getIdContato()))
                                    .switchIfEmpty(Mono.error(new ResourceNotFoundException("Contato", dto.getContatoRequestDto().getIdContato())))
                                    .flatMap(contatoExistente -> {
                                        contatoMapper.partialUpdate(dto.getContatoRequestDto(), contatoExistente);
                                        return Mono.just(contatoRepository.save(contatoExistente));
                                    });
                        } else {
                            contatoMono = Mono.just(contatoRepository.save(contatoMapper.toEntity(dto.getContatoRequestDto())));
                        }
                    } else {
                        contatoMono = Mono.just(clienteExistente.getContato());
                    }

                    return Mono.zip(enderecoMono, contatoMono)
                            .flatMap(tuple -> {
                                clienteExistente.setEndereco(tuple.getT1());
                                clienteExistente.setContato(tuple.getT2());
                                return Mono.just(clienteRepository.save(clienteExistente));
                            });
                })
                .onErrorResume(e -> {
                    if (e instanceof ResourceNotFoundException
                            || e instanceof DuplicatedResourceException
                            || e instanceof InvalidInputException) {
                        return Mono.error(e);
                    }
                    return Mono.error(new InvalidInputException("Erro inesperado ao atualizar cliente: " + e.getMessage()));
                });
    }

    @Transactional
    public void deletarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente", id);
        }
        clienteRepository.deleteById(id);
    }

    @Transactional
    public ClienteVeiculo associarClienteVeiculo(Long clienteId, Long enderecoId, Long contatoId, Long veiculoId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", clienteId));
        Veiculo veiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo", veiculoId));

        ClienteVeiculoId id = new ClienteVeiculoId(clienteId, enderecoId, contatoId, veiculoId);
        if (clienteVeiculoRepository.existsById(id)) {
            throw new DuplicatedResourceException("Associação Cliente-Veículo", "IDs", id.toString());
        }
        ClienteVeiculo associacao = new ClienteVeiculo(cliente, veiculo);
        return clienteVeiculoRepository.save(associacao);
    }

    @Transactional
    public void desassociarClienteVeiculo(Long clienteId,
                                          Long enderecoId,
                                          Long contatoId,
                                          Long veiculoId) {
        // Cria o objeto de chave composta
        ClienteVeiculoId id = new ClienteVeiculoId(clienteId, enderecoId, contatoId, veiculoId);

        // Verifica existência usando a própria chave composta
        if (!clienteVeiculoRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Associação Cliente-Veículo",  // nome do recurso
                    "ids",                           // nome do campo
                    id.toString()                    // valor que faltou
            );
        }

        // Remove passando o próprio ID (e não uma String)
        clienteVeiculoRepository.deleteById(id);
    }


    public Set<Veiculo> getVeiculosByClienteId(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", clienteId));
        return cliente.getClienteVeiculos().stream()
                .map(ClienteVeiculo::getVeiculo)
                .collect(Collectors.toSet());
    }
}