package br.com.fiap.mottu.service;

import br.com.fiap.mottu.dto.patio.PatioRequestDto;
import br.com.fiap.mottu.filter.PatioFilter;
import br.com.fiap.mottu.mapper.PatioMapper;
import br.com.fiap.mottu.model.Patio;
import br.com.fiap.mottu.model.Veiculo;
import br.com.fiap.mottu.model.Zona;
import br.com.fiap.mottu.model.Box; // NOVO: Importar Box
import br.com.fiap.mottu.model.Contato;
import br.com.fiap.mottu.model.Endereco;

// Importar as classes de relacionamento e seus IDs
import br.com.fiap.mottu.model.relacionamento.VeiculoPatio;
import br.com.fiap.mottu.model.relacionamento.VeiculoPatioId;
import br.com.fiap.mottu.model.relacionamento.ZonaPatio;
import br.com.fiap.mottu.model.relacionamento.ZonaPatioId;
import br.com.fiap.mottu.model.relacionamento.ContatoPatio;
import br.com.fiap.mottu.model.relacionamento.ContatoPatioId;
import br.com.fiap.mottu.model.relacionamento.EnderecoPatio;
import br.com.fiap.mottu.model.relacionamento.EnderecoPatioId;
import br.com.fiap.mottu.model.relacionamento.PatioBox;    // NOVO
import br.com.fiap.mottu.model.relacionamento.PatioBoxId;  // NOVO


import br.com.fiap.mottu.repository.PatioRepository;
import br.com.fiap.mottu.repository.VeiculoRepository;
import br.com.fiap.mottu.repository.ZonaRepository;
import br.com.fiap.mottu.repository.BoxRepository;          // NOVO: Injetar BoxRepository
import br.com.fiap.mottu.repository.ContatoRepository;
import br.com.fiap.mottu.repository.EnderecoRepository;

// Importar os repositórios das tabelas de junção
import br.com.fiap.mottu.repository.relacionamento.VeiculoPatioRepository;
import br.com.fiap.mottu.repository.relacionamento.ZonaPatioRepository;
import br.com.fiap.mottu.repository.relacionamento.ContatoPatioRepository;
import br.com.fiap.mottu.repository.relacionamento.EnderecoPatioRepository;
import br.com.fiap.mottu.repository.relacionamento.PatioBoxRepository; // NOVO: Injetar


import br.com.fiap.mottu.exception.DuplicatedResourceException;
import br.com.fiap.mottu.exception.ResourceNotFoundException;
import br.com.fiap.mottu.specification.PatioSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set; // Para coleções
import java.util.stream.Collectors; // Para operações com streams

@Service
public class PatioService {

    private final PatioRepository patioRepository;
    private final PatioMapper patioMapper;
    private final VeiculoRepository veiculoRepository;
    private final ZonaRepository zonaRepository;
    private final BoxRepository boxRepository; // NOVO
    private final ContatoRepository contatoRepository;
    private final EnderecoRepository enderecoRepository;

    // Repositórios das tabelas de junção
    private final VeiculoPatioRepository veiculoPatioRepository;
    private final ZonaPatioRepository zonaPatioRepository;
    private final ContatoPatioRepository contatoPatioRepository;
    private final EnderecoPatioRepository enderecoPatioRepository;
    private final PatioBoxRepository patioBoxRepository; // NOVO


    @Autowired
    public PatioService(PatioRepository patioRepository, PatioMapper patioMapper,
                        VeiculoRepository veiculoRepository, ZonaRepository zonaRepository,
                        ContatoRepository contatoRepository, EnderecoRepository enderecoRepository,
                        VeiculoPatioRepository veiculoPatioRepository, ZonaPatioRepository zonaPatioRepository,
                        ContatoPatioRepository contatoPatioRepository, EnderecoPatioRepository enderecoPatioRepository,
                        BoxRepository boxRepository, PatioBoxRepository patioBoxRepository) { // NOVO: Adicione BoxRepository e PatioBoxRepository aqui
        this.patioRepository = patioRepository;
        this.patioMapper = patioMapper;
        this.veiculoRepository = veiculoRepository;
        this.zonaRepository = zonaRepository;
        this.contatoRepository = contatoRepository;
        this.enderecoRepository = enderecoRepository;
        this.veiculoPatioRepository = veiculoPatioRepository;
        this.zonaPatioRepository = zonaPatioRepository;
        this.contatoPatioRepository = contatoPatioRepository;
        this.enderecoPatioRepository = enderecoPatioRepository;
        this.boxRepository = boxRepository;
        this.patioBoxRepository = patioBoxRepository;
    }

    public List<Patio> listarTodosPatios() {
        return patioRepository.findAll();
    }

    public Patio buscarPatioPorId(Long id) {
        return patioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pátio", id));
    }

    public List<Patio> buscarPatiosPorFiltro(PatioFilter filter) {
        return patioRepository.findAll(PatioSpecification.withFilters(filter));
    }

    @Transactional
    public Patio criarPatio(PatioRequestDto dto) {
        String nome = dto.getNomePatio();
        if (patioRepository.findByNomePatioContainingIgnoreCase(nome)
                .stream().anyMatch(p -> p.getNomePatio().equalsIgnoreCase(nome))) {
            throw new DuplicatedResourceException("Pátio", "nome", nome);
        }
        Patio patio = patioMapper.toEntity(dto);
        return patioRepository.save(patio);
    }

    @Transactional
    public Patio atualizarPatio(Long id, PatioRequestDto dto) {
        return patioRepository.findById(id)
                .map(existente -> {
                    String novoNome = dto.getNomePatio();
                    if (novoNome != null
                            && !novoNome.equalsIgnoreCase(existente.getNomePatio())
                            && patioRepository.findByNomePatioContainingIgnoreCase(novoNome)
                            .stream().anyMatch(p -> p.getNomePatio().equalsIgnoreCase(novoNome))) {
                        throw new DuplicatedResourceException("Pátio", "nome", novoNome);
                    }
                    patioMapper.partialUpdate(dto, existente);
                    return patioRepository.save(existente);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Pátio", id));
    }

    @Transactional
    public void deletarPatio(Long id) {
        if (!patioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pátio", id);
        }
        patioRepository.deleteById(id);
    }

    // Métodos para gerenciar associações de Patio

    // --- VeiculoPatio ---
    @Transactional
    public VeiculoPatio associarPatioVeiculo(Long patioId, Long veiculoId) {
        Patio patio = patioRepository.findById(patioId)
                .orElseThrow(() -> new ResourceNotFoundException("Pátio", patioId));
        Veiculo veiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo", veiculoId));

        VeiculoPatioId id = new VeiculoPatioId(veiculoId, patioId); // Cria o ID composto
        if (veiculoPatioRepository.existsById(id)) {
            throw new DuplicatedResourceException("Associação Pátio-Veículo", "IDs", id.toString());
        }

        VeiculoPatio associacao = new VeiculoPatio(veiculo, patio); // Cria a entidade de relacionamento
        return veiculoPatioRepository.save(associacao);
    }

    @Transactional
    public void desassociarPatioVeiculo(Long patioId, Long veiculoId) {
        VeiculoPatioId id = new VeiculoPatioId(veiculoId, patioId); // Cria o ID composto
        if (!veiculoPatioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Associação Pátio-Veículo", "IDs", id.toString());
        }
        veiculoPatioRepository.deleteById(id);
    }

    public Set<Veiculo> getVeiculosByPatioId(Long patioId) {
        Patio patio = patioRepository.findById(patioId)
                .orElseThrow(() -> new ResourceNotFoundException("Pátio", patioId));
        return patio.getVeiculoPatios().stream() // Assume que Patio tem um Set<VeiculoPatio>
                .map(VeiculoPatio::getVeiculo) // Mapeia para a entidade Veiculo
                .collect(Collectors.toSet());
    }

    // --- ZonaPatio ---
    @Transactional
    public ZonaPatio associarPatioZona(Long patioId, Long zonaId) {
        Patio patio = patioRepository.findById(patioId)
                .orElseThrow(() -> new ResourceNotFoundException("Pátio", patioId));
        Zona zona = zonaRepository.findById(zonaId)
                .orElseThrow(() -> new ResourceNotFoundException("Zona", zonaId));

        ZonaPatioId id = new ZonaPatioId(patioId, zonaId); // Cria o ID composto
        if (zonaPatioRepository.existsById(id)) {
            throw new DuplicatedResourceException("Associação Pátio-Zona", "IDs", id.toString());
        }

        ZonaPatio associacao = new ZonaPatio(patio, zona); // Cria a entidade de relacionamento
        return zonaPatioRepository.save(associacao);
    }

    @Transactional
    public void desassociarPatioZona(Long patioId, Long zonaId) {
        ZonaPatioId id = new ZonaPatioId(patioId, zonaId); // Cria o ID composto
        if (!zonaPatioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Associação Pátio-Zona", "IDs", id.toString());
        }
        zonaPatioRepository.deleteById(id);
    }

    public Set<Zona> getZonasByPatioId(Long patioId) {
        Patio patio = patioRepository.findById(patioId)
                .orElseThrow(() -> new ResourceNotFoundException("Pátio", patioId));
        return patio.getZonaPatios().stream() // Assume que Patio tem um Set<ZonaPatio>
                .map(ZonaPatio::getZona) // Mapeia para a entidade Zona
                .collect(Collectors.toSet());
    }

    // --- ContatoPatio ---
    @Transactional
    public ContatoPatio associarPatioContato(Long patioId, Long contatoId) {
        Patio patio = patioRepository.findById(patioId)
                .orElseThrow(() -> new ResourceNotFoundException("Pátio", patioId));
        Contato contato = contatoRepository.findById(contatoId)
                .orElseThrow(() -> new ResourceNotFoundException("Contato", contatoId));

        ContatoPatioId id = new ContatoPatioId(patioId, contatoId); // Cria o ID composto
        if (contatoPatioRepository.existsById(id)) {
            throw new DuplicatedResourceException("Associação Pátio-Contato", "IDs", id.toString());
        }

        ContatoPatio associacao = new ContatoPatio(patio, contato); // Cria a entidade de relacionamento
        return contatoPatioRepository.save(associacao);
    }

    @Transactional
    public void desassociarPatioContato(Long patioId, Long contatoId) {
        ContatoPatioId id = new ContatoPatioId(patioId, contatoId); // Cria o ID composto
        if (!contatoPatioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Associação Pátio-Contato", "IDs", id.toString());
        }
        contatoPatioRepository.deleteById(id);
    }

    public Set<Contato> getContatosByPatioId(Long patioId) {
        Patio patio = patioRepository.findById(patioId)
                .orElseThrow(() -> new ResourceNotFoundException("Pátio", patioId));
        return patio.getContatoPatios().stream() // Assume que Patio tem um Set<ContatoPatio>
                .map(ContatoPatio::getContato) // Mapeia para a entidade Contato
                .collect(Collectors.toSet());
    }

    // --- EnderecoPatio ---
    @Transactional
    public EnderecoPatio associarPatioEndereco(Long patioId, Long enderecoId) {
        Patio patio = patioRepository.findById(patioId)
                .orElseThrow(() -> new ResourceNotFoundException("Pátio", patioId));
        Endereco endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço", enderecoId));

        // O EnderecoPatioId usa enderecoId primeiro, conforme seu DDL
        EnderecoPatioId id = new EnderecoPatioId(enderecoId, patioId); // Cria o ID composto
        if (enderecoPatioRepository.existsById(id)) {
            throw new DuplicatedResourceException("Associação Pátio-Endereço", "IDs", id.toString());
        }

        EnderecoPatio associacao = new EnderecoPatio(endereco, patio); // Cria a entidade de relacionamento
        return enderecoPatioRepository.save(associacao);
    }

    @Transactional
    public void desassociarPatioEndereco(Long patioId, Long enderecoId) {
        // O EnderecoPatioId usa enderecoId primeiro, conforme seu DDL
        EnderecoPatioId id = new EnderecoPatioId(enderecoId, patioId); // Cria o ID composto
        if (!enderecoPatioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Associação Pátio-Endereço", "IDs", id.toString());
        }
        enderecoPatioRepository.deleteById(id);
    }

    public Set<Endereco> getEnderecosByPatioId(Long patioId) {
        Patio patio = patioRepository.findById(patioId)
                .orElseThrow(() -> new ResourceNotFoundException("Pátio", patioId));
        return patio.getEnderecoPatios().stream()
                .map(EnderecoPatio::getEndereco)
                .collect(Collectors.toSet());
    }

    // --- PatioBox (NOVO) ---
    @Transactional
    public PatioBox associarPatioBox(Long patioId, Long boxId) {
        Patio patio = patioRepository.findById(patioId)
                .orElseThrow(() -> new ResourceNotFoundException("Pátio", patioId));
        Box box = boxRepository.findById(boxId)
                .orElseThrow(() -> new ResourceNotFoundException("Box", boxId));

        PatioBoxId id = new PatioBoxId(patioId, boxId);
        if (patioBoxRepository.existsById(id)) {
            throw new DuplicatedResourceException("Associação Pátio-Box", "IDs", id.toString());
        }

        PatioBox associacao = new PatioBox(patio, box);
        return patioBoxRepository.save(associacao);
    }

    @Transactional
    public void desassociarPatioBox(Long patioId, Long boxId) {
        PatioBoxId id = new PatioBoxId(patioId, boxId);
        if (!patioBoxRepository.existsById(id)) {
            throw new ResourceNotFoundException("Associação Pátio-Box", "IDs", id.toString());
        }
        patioBoxRepository.deleteById(id);
    }

    public Set<Box> getBoxesByPatioId(Long patioId) {
        Patio patio = patioRepository.findById(patioId)
                .orElseThrow(() -> new ResourceNotFoundException("Pátio", patioId));
        return patio.getPatioBoxes().stream()
                .map(PatioBox::getBox)
                .collect(Collectors.toSet());
    }
}