// src/utils/api.ts
import axios from 'axios';

// ===============================================================
// IMPORTAÇÕES DE INTERFACES DTOs E FILTROS
// ===============================================================
// Certifique-se de que estes caminhos e nomes de arquivos correspondem à sua estrutura.
// Se você definiu EnderecoRequestDto/ResponseDto ou ContatoRequestDto/ResponseDto
// em arquivos separados (ex: types/endereco.d.ts, types/contato.d.ts),
// ajuste os imports de '@/types/cliente' para o caminho correto.

import { BoxResponseDto, BoxFilter, BoxRequestDto } from '@/types/box';
import { ContatoResponseDto, ContatoFilter, ContatoRequestDto, EnderecoResponseDto, EnderecoFilter, EnderecoRequestDto, ClienteResponseDto, ClienteFilter, ClienteRequestDto } from '@/types/cliente'; // Endereco e Contato foram mantidos aqui por clareza
import { PatioResponseDto, PatioFilter, PatioRequestDto } from '@/types/patio';
import { RastreamentoResponseDto, RastreamentoFilter, RastreamentoRequestDto, VeiculoResponseDto, VeiculoFilter, VeiculoRequestDto, VeiculoLocalizacaoResponseDto } from '@/types/veiculo'; // Rastreamento foi mantido aqui por clareza
import { ZonaResponseDto, ZonaFilter, ZonaRequestDto } from '@/types/zona';

// ===============================================================
// CONFIGURAÇÃO DA INSTÂNCIA AXIOS
// ===============================================================
// Lê a URL base da API de uma variável de ambiente.
// Isso é essencial para que você possa facilmente mudar o ambiente (desenvolvimento, produção).
const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080/api';

// Cria uma instância do Axios com a URL base configurada e headers padrão.
const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// ===============================================================
// DEFINIÇÃO DOS SERVIÇOS DA API PARA CADA ENTIDADE
// ===============================================================

/**
 * Serviço para interagir com os endpoints da API de Boxes.
 * Inclui operações CRUD e busca filtrada.
 */
export const BoxService = {
    /**
     * Busca todos os boxes, com opção de filtragem.
     * @param filterParams Objeto com os parâmetros de filtro (opcional).
     * @returns Uma Promise que resolve com uma lista de BoxResponseDto.
     * @throws Erro se a requisição falhar.
     */
    getAll: async (filterParams: BoxFilter = {}): Promise<BoxResponseDto[]> => {
        try {
            const response = await api.get<BoxResponseDto[]>('/boxes', { params: filterParams });
            return response.data;
        } catch (error: any) {
            console.error('Erro ao buscar boxes:', error.response?.data || error.message);
            throw error; // Re-lança o erro para ser tratado no componente/página.
        }
    },

    /**
     * Busca um box específico por seu ID.
     * @param id O ID do box a ser buscado.
     * @returns Uma Promise que resolve com um BoxResponseDto.
     * @throws Erro se o box não for encontrado ou a requisição falhar.
     */
    getById: async (id: number): Promise<BoxResponseDto> => {
        try {
            const response = await api.get<BoxResponseDto>(`/boxes/${id}`);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao buscar box com ID ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },

    /**
     * Cria um novo box.
     * @param boxData Os dados do box a serem criados.
     * @returns Uma Promise que resolve com o BoxResponseDto do box criado.
     * @throws Erro se a criação falhar (ex: dados inválidos, nome duplicado).
     */
    create: async (boxData: BoxRequestDto): Promise<BoxResponseDto> => {
        try {
            const response = await api.post<BoxResponseDto>('/boxes', boxData);
            return response.data;
        } catch (error: any) {
            console.error('Erro ao criar box:', error.response?.data || error.message);
            throw error;
        }
    },

    /**
     * Atualiza um box existente.
     * @param id O ID do box a ser atualizado.
     * @param boxData Os dados do box para atualização.
     * @returns Uma Promise que resolve com o BoxResponseDto do box atualizado.
     * @throws Erro se o box não for encontrado, dados inválidos ou conflito.
     */
    update: async (id: number, boxData: BoxRequestDto): Promise<BoxResponseDto> => {
        try {
            const response = await api.put<BoxResponseDto>(`/boxes/${id}`, boxData);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao atualizar box com ID ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },

    /**
     * Deleta um box específico por seu ID.
     * @param id O ID do box a ser deletado.
     * @returns Uma Promise vazia em caso de sucesso.
     * @throws Erro se o box não for encontrado ou a deleção falhar.
     */
    delete: async (id: number): Promise<void> => {
        try {
            await api.delete(`/boxes/${id}`);
        } catch (error: any) {
            console.error(`Erro ao deletar box com ID ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },
};

/**
 * Serviço para interagir com os endpoints da API de Endereços.
 * Utilizado principalmente por Cliente e Patio para gerenciar seus endereços.
 */
export const EnderecoService = {
    getAll: async (filterParams: EnderecoFilter = {}): Promise<EnderecoResponseDto[]> => {
        try {
            const response = await api.get<EnderecoResponseDto[]>('/enderecos', { params: filterParams });
            return response.data;
        } catch (error: any) {
            console.error('Erro ao buscar endereços:', error.response?.data || error.message);
            throw error;
        }
    },
    getById: async (id: number): Promise<EnderecoResponseDto> => {
        try {
            const response = await api.get<EnderecoResponseDto>(`/enderecos/${id}`);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao buscar endereço com ID ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },
    create: async (enderecoData: EnderecoRequestDto): Promise<EnderecoResponseDto> => {
        try {
            const response = await api.post<EnderecoResponseDto>('/enderecos', enderecoData);
            return response.data;
        } catch (error: any) {
            console.error('Erro ao criar endereço:', error.response?.data || error.message);
            throw error;
        }
    },
    update: async (id: number, enderecoData: EnderecoRequestDto): Promise<EnderecoResponseDto> => {
        try {
            const response = await api.put<EnderecoResponseDto>(`/enderecos/${id}`, enderecoData);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao atualizar endereço com ID ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },
    delete: async (id: number): Promise<void> => {
        try {
            await api.delete(`/enderecos/${id}`);
        } catch (error: any) {
            console.error(`Erro ao deletar endereço com ID ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },
};

/**
 * Serviço para interagir com os endpoints da API de Contatos.
 * Utilizado principalmente por Cliente e Patio para gerenciar seus contatos.
 */
export const ContatoService = {
    getAll: async (filterParams: ContatoFilter = {}): Promise<ContatoResponseDto[]> => {
        try {
            const response = await api.get<ContatoResponseDto[]>('/contatos', { params: filterParams });
            return response.data;
        } catch (error: any) {
            console.error('Erro ao buscar contatos:', error.response?.data || error.message);
            throw error;
        }
    },
    getById: async (id: number): Promise<ContatoResponseDto> => {
        try {
            const response = await api.get<ContatoResponseDto>(`/contatos/${id}`);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao buscar contato com ID ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },
    create: async (contatoData: ContatoRequestDto): Promise<ContatoResponseDto> => {
        try {
            const response = await api.post<ContatoResponseDto>('/contatos', contatoData);
            return response.data;
        } catch (error: any) {
            console.error('Erro ao criar contato:', error.response?.data || error.message);
            throw error;
        }
    },
    update: async (id: number, contatoData: ContatoRequestDto): Promise<ContatoResponseDto> => {
        try {
            const response = await api.put<ContatoResponseDto>(`/contatos/${id}`, contatoData);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao atualizar contato com ID ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },
    delete: async (id: number): Promise<void> => {
        try {
            await api.delete(`/contatos/${id}`);
        } catch (error: any) {
            console.error(`Erro ao deletar contato com ID ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },
};

/**
 * Serviço para interagir com os endpoints da API de Clientes.
 * Inclui operações CRUD e busca filtrada, além de métodos para associações de veículos.
 */
export const ClienteService = {
    getAll: async (filterParams: ClienteFilter = {}): Promise<ClienteResponseDto[]> => {
        try {
            const response = await api.get<ClienteResponseDto[]>('/clientes', { params: filterParams });
            return response.data;
        } catch (error: any) {
            console.error('Erro ao buscar clientes:', error.response?.data || error.message);
            throw error;
        }
    },
    getById: async (id: number): Promise<ClienteResponseDto> => {
        try {
            const response = await api.get<ClienteResponseDto>(`/clientes/${id}`);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao buscar cliente com ID ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },
    create: async (clienteData: ClienteRequestDto): Promise<ClienteResponseDto> => {
        try {
            const response = await api.post<ClienteResponseDto>('/clientes', clienteData);
            return response.data;
        } catch (error: any) {
            console.error('Erro ao criar cliente:', error.response?.data || error.message);
            throw error;
        }
    },
    update: async (id: number, clienteData: ClienteRequestDto): Promise<ClienteResponseDto> => {
        try {
            const response = await api.put<ClienteResponseDto>(`/clientes/${id}`, clienteData);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao atualizar cliente com ID ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },
    delete: async (id: number): Promise<void> => {
        try {
            await api.delete(`/clientes/${id}`);
        } catch (error: any) {
            console.error(`Erro ao deletar cliente com ID ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },
    /**
     * Lista todos os veículos associados a um cliente.
     * @param clienteId O ID do cliente.
     * @returns Uma Promise que resolve com uma lista de VeiculoResponseDto.
     */
    getVeiculosByClienteId: async (clienteId: number): Promise<VeiculoResponseDto[]> => {
        try {
            const response = await api.get<VeiculoResponseDto[]>(`/clientes/${clienteId}/veiculos`);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao buscar veículos do cliente ${clienteId}:`, error.response?.data || error.message);
            throw error;
        }
    },
    /**
     * Associa um veículo a um cliente. Nota: Este endpoint no backend espera `enderecoId` e `contatoId` na URL.
     * Isso pode ser um ponto de atenção se a intenção era apenas associar Cliente e Veículo diretamente.
     * @param clienteId O ID do cliente.
     * @param enderecoId O ID do endereço do cliente (da chave composta ClienteVeiculoId no backend).
     * @param contatoId O ID do contato do cliente (da chave composta ClienteVeiculoId no backend).
     * @param veiculoId O ID do veículo a ser associado.
     * @returns Uma Promise que resolve com os dados da associação (pode ser uma string de sucesso no backend).
     */
    associarVeiculo: async (clienteId: number, enderecoId: number, contatoId: number, veiculoId: number): Promise<any> => {
        try {
            const response = await api.post(`/clientes/${clienteId}/enderecos/${enderecoId}/contatos/${contatoId}/veiculos/${veiculoId}/associar`);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao associar veículo ${veiculoId} ao cliente ${clienteId}:`, error.response?.data || error.message);
            throw error;
        }
    },
    /**
     * Desassocia um veículo de um cliente. Nota: Este endpoint no backend espera `enderecoId` e `contatoId` na URL.
     * @param clienteId O ID do cliente.
     * @param enderecoId O ID do endereço do cliente (da chave composta ClienteVeiculoId no backend).
     * @param contatoId O ID do contato do cliente (da chave composta ClienteVeiculoId no backend).
     * @param veiculoId O ID do veículo a ser desassociado.
     * @returns Uma Promise vazia em caso de sucesso.
     */
    desassociarVeiculo: async (clienteId: number, enderecoId: number, contatoId: number, veiculoId: number): Promise<void> => {
        try {
            const response = await api.delete(`/clientes/${clienteId}/enderecos/${enderecoId}/contatos/${contatoId}/veiculos/${veiculoId}/desassociar`);
            return response.data; // Pode retornar dados, mas tipado como void se o backend retornar 204
        } catch (error: any) {
            console.error(`Erro ao desassociar veículo ${veiculoId} do cliente ${clienteId}:`, error.response?.data || error.message);
            throw error;
        }
    },
};

/**
 * Serviço para interagir com os endpoints da API de Pátios.
 * Inclui operações CRUD e busca filtrada, além de métodos para gerenciar associações.
 */
export const PatioService = {
    getAll: async (filterParams: PatioFilter = {}): Promise<PatioResponseDto[]> => {
        try {
            const response = await api.get<PatioResponseDto[]>('/patios', { params: filterParams });
            return response.data;
        } catch (error: any) {
            console.error('Erro ao buscar pátios:', error.response?.data || error.message);
            throw error;
        }
    },
    getById: async (id: number): Promise<PatioResponseDto> => {
        try {
            const response = await api.get<PatioResponseDto>(`/patios/${id}`);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao buscar pátio com ID ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },
    create: async (patioData: PatioRequestDto): Promise<PatioResponseDto> => {
        try {
            const response = await api.post<PatioResponseDto>('/patios', patioData);
            return response.data;
        } catch (error: any) {
            console.error('Erro ao criar pátio:', error.response?.data || error.message);
            throw error;
        }
    },
    update: async (id: number, patioData: PatioRequestDto): Promise<PatioResponseDto> => {
        try {
            const response = await api.put<PatioResponseDto>(`/patios/${id}`, patioData);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao atualizar pátio com ID ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },
    delete: async (id: number): Promise<void> => {
        try {
            await api.delete(`/patios/${id}`);
        } catch (error: any) {
            console.error(`Erro ao deletar pátio com ID ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },

    // -----------------------------------------------------
    // Métodos de Associação de Pátio (Endpoints Muitos-para-Muitos)
    // -----------------------------------------------------

    // Veículo
    associarVeiculo: async (patioId: number, veiculoId: number): Promise<void> => {
        try {
            await api.post(`/patios/${patioId}/veiculos/${veiculoId}/associar`);
        } catch (error: any) {
            console.error(`Erro ao associar veículo ${veiculoId} ao pátio ${patioId}:`, error.response?.data || error.message);
            throw error;
        }
    },
    desassociarVeiculo: async (patioId: number, veiculoId: number): Promise<void> => {
        try {
            await api.delete(`/patios/${patioId}/veiculos/${veiculoId}/desassociar`);
        } catch (error: any) {
            console.error(`Erro ao desassociar veículo ${veiculoId} do pátio ${patioId}:`, error.response?.data || error.message);
            throw error;
        }
    },
    getVeiculosByPatioId: async (patioId: number): Promise<VeiculoResponseDto[]> => {
        try {
            const response = await api.get<VeiculoResponseDto[]>(`/patios/${patioId}/veiculos`);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao buscar veículos do pátio ${patioId}:`, error.response?.data || error.message);
            throw error;
        }
    },

    // Zona
    associarZona: async (patioId: number, zonaId: number): Promise<void> => {
        try {
            await api.post(`/patios/${patioId}/zonas/${zonaId}/associar`);
        } catch (error: any) {
            console.error(`Erro ao associar zona ${zonaId} ao pátio ${patioId}:`, error.response?.data || error.message);
            throw error;
        }
    },
    desassociarZona: async (patioId: number, zonaId: number): Promise<void> => {
        try {
            await api.delete(`/patios/${patioId}/zonas/${zonaId}/desassociar`);
        } catch (error: any) {
            console.error(`Erro ao desassociar zona ${zonaId} do pátio ${patioId}:`, error.response?.data || error.message);
            throw error;
        }
    },
    getZonasByPatioId: async (patioId: number): Promise<ZonaResponseDto[]> => {
        try {
            const response = await api.get<ZonaResponseDto[]>(`/patios/${patioId}/zonas`);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao buscar zonas do pátio ${patioId}:`, error.response?.data || error.message);
            throw error;
        }
    },

    // Contato
    associarContato: async (patioId: number, contatoId: number): Promise<void> => {
        try {
            await api.post(`/patios/${patioId}/contatos/${contatoId}/associar`);
        } catch (error: any) {
            console.error(`Erro ao associar contato ${contatoId} ao pátio ${patioId}:`, error.response?.data || error.message);
            throw error;
        }
    },
    desassociarContato: async (patioId: number, contatoId: number): Promise<void> => {
        try {
            await api.delete(`/patios/${patioId}/contatos/${contatoId}/desassociar`);
        } catch (error: any) {
            console.error(`Erro ao desassociar contato ${contatoId} do pátio ${patioId}:`, error.response?.data || error.message);
            throw error;
        }
    },
    getContatosByPatioId: async (patioId: number): Promise<ContatoResponseDto[]> => {
        try {
            const response = await api.get<ContatoResponseDto[]>(`/patios/${patioId}/contatos`);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao buscar contatos do pátio ${patioId}:`, error.response?.data || error.message);
            throw error;
        }
    },

    // Endereco
    associarEndereco: async (patioId: number, enderecoId: number): Promise<void> => {
        try {
            await api.post(`/patios/${patioId}/enderecos/${enderecoId}/associar`);
        } catch (error: any) {
            console.error(`Erro ao associar endereço ${enderecoId} ao pátio ${patioId}:`, error.response?.data || error.message);
            throw error;
        }
    },
    desassociarEndereco: async (patioId: number, enderecoId: number): Promise<void> => {
        try {
            await api.delete(`/patios/${patioId}/enderecos/${enderecoId}/desassociar`);
        } catch (error: any) {
            console.error(`Erro ao desassociar endereço ${enderecoId} do pátio ${patioId}:`, error.response?.data || error.message);
            throw error;
        }
    },
    getEnderecosByPatioId: async (patioId: number): Promise<EnderecoResponseDto[]> => {
        try {
            const response = await api.get<EnderecoResponseDto[]>(`/patios/${patioId}/enderecos`);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao buscar endereços do pátio ${patioId}:`, error.response?.data || error.message);
            throw error;
        }
    },

    // Box
    associarBox: async (patioId: number, boxId: number): Promise<void> => {
        try {
            await api.post(`/patios/${patioId}/boxes/${boxId}/associar`);
        } catch (error: any) {
            console.error(`Erro ao associar box ${boxId} ao pátio ${patioId}:`, error.response?.data || error.message);
            throw error;
        }
    },
    desassociarBox: async (patioId: number, boxId: number): Promise<void> => {
        try {
            await api.delete(`/patios/${patioId}/boxes/${boxId}/desassociar`);
        } catch (error: any) {
            console.error(`Erro ao desassociar box ${boxId} do pátio ${patioId}:`, error.response?.data || error.message);
            throw error;
        }
    },
    getBoxesByPatioId: async (patioId: number): Promise<BoxResponseDto[]> => {
        try {
            const response = await api.get<BoxResponseDto[]>(`/patios/${patioId}/boxes`);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao buscar boxes do pátio ${patioId}:`, error.response?.data || error.message);
            throw error;
        }
    },
};

/**
 * Serviço para interagir com os endpoints da API de Rastreamentos.
 */
export const RastreamentoService = {
    getAll: async (filterParams: RastreamentoFilter = {}): Promise<RastreamentoResponseDto[]> => {
        try {
            const response = await api.get<RastreamentoResponseDto[]>('/rastreamentos', { params: filterParams });
            return response.data;
        } catch (error: any) {
            console.error('Erro ao buscar rastreamentos:', error.response?.data || error.message);
            throw error;
        }
    },
    getById: async (id: number): Promise<RastreamentoResponseDto> => {
        try {
            const response = await api.get<RastreamentoResponseDto>(`/rastreamentos/${id}`);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao buscar rastreamento com ID ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },
    create: async (rastreamentoData: RastreamentoRequestDto): Promise<RastreamentoResponseDto> => {
        try {
            const response = await api.post<RastreamentoResponseDto>('/rastreamentos', rastreamentoData);
            return response.data;
        } catch (error: any) {
            console.error('Erro ao criar rastreamento:', error.response?.data || error.message);
            throw error;
        }
    },
    update: async (id: number, rastreamentoData: RastreamentoRequestDto): Promise<RastreamentoResponseDto> => {
        try {
            const response = await api.put<RastreamentoResponseDto>(`/rastreamentos/${id}`, rastreamentoData);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao atualizar rastreamento com ID ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },
    delete: async (id: number): Promise<void> => {
        try {
            await api.delete(`/rastreamentos/${id}`);
        } catch (error: any) {
            console.error(`Erro ao deletar rastreamento com ID ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },
};

/**
 * Serviço para interagir com os endpoints da API de Veículos.
 * Inclui operações CRUD, busca filtrada e métodos para localização e associações de rastreamento.
 */
export const VeiculoService = {
    getAll: async (filterParams: VeiculoFilter = {}): Promise<VeiculoResponseDto[]> => {
        try {
            const response = await api.get<VeiculoResponseDto[]>('/veiculos', { params: filterParams });
            return response.data;
        } catch (error: any) {
            console.error('Erro ao buscar veículos:', error.response?.data || error.message);
            throw error;
        }
    },
    getById: async (id: number): Promise<VeiculoResponseDto> => {
        try {
            const response = await api.get<VeiculoResponseDto>(`/veiculos/${id}`);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao buscar veículo com ID ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },
    create: async (veiculoData: VeiculoRequestDto): Promise<VeiculoResponseDto> => {
        try {
            const response = await api.post<VeiculoResponseDto>('/veiculos', veiculoData);
            return response.data;
        } catch (error: any) {
            console.error('Erro ao criar veículo:', error.response?.data || error.message);
            throw error;
        }
    },
    update: async (id: number, veiculoData: VeiculoRequestDto): Promise<VeiculoResponseDto> => {
        try {
            const response = await api.put<VeiculoResponseDto>(`/veiculos/${id}`, veiculoData);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao atualizar veículo com ID ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },
    delete: async (id: number): Promise<void> => {
        try {
            await api.delete(`/veiculos/${id}`);
        } catch (error: any) {
            console.error(`Erro ao deletar veículo com ID ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },
    /**
     * Obtém a localização atual e associações de um veículo.
     * @param id O ID do veículo.
     * @returns Uma Promise que resolve com VeiculoLocalizacaoResponseDto.
     */
    getLocalizacao: async (id: number): Promise<VeiculoLocalizacaoResponseDto> => {
        try {
            const response = await api.get<VeiculoLocalizacaoResponseDto>(`/veiculos/${id}/localizacao`);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao buscar localização do veículo ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },
    /**
     * Associa um rastreamento a um veículo.
     * @param veiculoId O ID do veículo.
     * @param rastreamentoId O ID do rastreamento a ser associado.
     * @returns Uma Promise vazia em caso de sucesso.
     */
    associarRastreamento: async (veiculoId: number, rastreamentoId: number): Promise<void> => {
        try {
            await api.post(`/veiculos/${veiculoId}/rastreamentos/${rastreamentoId}/associar`);
        } catch (error: any) {
            console.error(`Erro ao associar rastreamento ${rastreamentoId} ao veículo ${veiculoId}:`, error.response?.data || error.message);
            throw error;
        }
    },
    /**
     * Desassocia um rastreamento de um veículo.
     * @param veiculoId O ID do veículo.
     * @param rastreamentoId O ID do rastreamento a ser desassociado.
     * @returns Uma Promise vazia em caso de sucesso.
     */
    desassociarRastreamento: async (veiculoId: number, rastreamentoId: number): Promise<void> => {
        try {
            await api.delete(`/veiculos/${veiculoId}/rastreamentos/${rastreamentoId}/desassociar`);
        } catch (error: any) {
            console.error(`Erro ao desassociar rastreamento ${rastreamentoId} do veículo ${veiculoId}:`, error.response?.data || error.message);
            throw error;
        }
    },
    /**
     * Lista todos os rastreamentos associados a um veículo.
     * @param veiculoId O ID do veículo.
     * @returns Uma Promise que resolve com uma lista de RastreamentoResponseDto.
     */
    getRastreamentosByVeiculoId: async (veiculoId: number): Promise<RastreamentoResponseDto[]> => {
        try {
            const response = await api.get<RastreamentoResponseDto[]>(`/veiculos/${veiculoId}/rastreamentos`);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao buscar rastreamentos do veículo ${veiculoId}:`, error.response?.data || error.message);
            throw error;
        }
    },
};

/**
 * Serviço para interagir com os endpoints da API de Zonas.
 * Inclui operações CRUD e busca filtrada, além de métodos para gerenciar associações.
 */
export const ZonaService = {
    getAll: async (filterParams: ZonaFilter = {}): Promise<ZonaResponseDto[]> => {
        try {
            const response = await api.get<ZonaResponseDto[]>('/zonas', { params: filterParams });
            return response.data;
        } catch (error: any) {
            console.error('Erro ao buscar zonas:', error.response?.data || error.message);
            throw error;
        }
    },
    getById: async (id: number): Promise<ZonaResponseDto> => {
        try {
            const response = await api.get<ZonaResponseDto>(`/zonas/${id}`);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao buscar zona com ID ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },
    create: async (zonaData: ZonaRequestDto): Promise<ZonaResponseDto> => {
        try {
            const response = await api.post<ZonaResponseDto>('/zonas', zonaData);
            return response.data;
        } catch (error: any) {
            console.error('Erro ao criar zona:', error.response?.data || error.message);
            throw error;
        }
    },
    update: async (id: number, zonaData: ZonaRequestDto): Promise<ZonaResponseDto> => {
        try {
            const response = await api.put<ZonaResponseDto>(`/zonas/${id}`, zonaData);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao atualizar zona com ID ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },
    delete: async (id: number): Promise<void> => {
        try {
            await api.delete(`/zonas/${id}`);
        } catch (error: any) {
            console.error(`Erro ao deletar zona com ID ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },

    // -----------------------------------------------------
    // Métodos de Associação de Zona (Endpoints Muitos-para-Muitos)
    // -----------------------------------------------------

    // Box
    associarBox: async (zonaId: number, boxId: number): Promise<void> => {
        try {
            await api.post(`/zonas/${zonaId}/boxes/${boxId}/associar`);
        } catch (error: any) {
            console.error(`Erro ao associar box ${boxId} à zona ${zonaId}:`, error.response?.data || error.message);
            throw error;
        }
    },
    desassociarBox: async (zonaId: number, boxId: number): Promise<void> => {
        try {
            await api.delete(`/zonas/${zonaId}/boxes/${boxId}/desassociar`);
        } catch (error: any) {
            console.error(`Erro ao desassociar box ${boxId} da zona ${zonaId}:`, error.response?.data || error.message);
            throw error;
        }
    },
    getBoxesByZonaId: async (zonaId: number): Promise<BoxResponseDto[]> => {
        try {
            const response = await api.get<BoxResponseDto[]>(`/zonas/${zonaId}/boxes`);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao buscar boxes da zona ${zonaId}:`, error.response?.data || error.message);
            throw error;
        }
    },

    // Veículo
    associarVeiculo: async (zonaId: number, veiculoId: number): Promise<void> => {
        try {
            await api.post(`/zonas/${zonaId}/veiculos/${veiculoId}/associar`);
        } catch (error: any) {
            console.error(`Erro ao associar veículo ${veiculoId} à zona ${zonaId}:`, error.response?.data || error.message);
            throw error;
        }
    },
    desassociarVeiculo: async (zonaId: number, veiculoId: number): Promise<void> => {
        try {
            await api.delete(`/zonas/${zonaId}/veiculos/${veiculoId}/desassociar`);
        } catch (error: any) {
            console.error(`Erro ao desassociar veículo ${veiculoId} da zona ${zonaId}:`, error.response?.data || error.message);
            throw error;
        }
    },
    getVeiculosByZonaId: async (zonaId: number): Promise<VeiculoResponseDto[]> => {
        try {
            const response = await api.get<VeiculoResponseDto[]>(`/zonas/${zonaId}/veiculos`);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao buscar veículos da zona ${zonaId}:`, error.response?.data || error.message);
            throw error;
        }
    },

    // Patio
    associarPatio: async (zonaId: number, patioId: number): Promise<void> => {
        try {
            await api.post(`/zonas/${zonaId}/patios/${patioId}/associar`);
        } catch (error: any) {
            console.error(`Erro ao associar pátio ${patioId} à zona ${zonaId}:`, error.response?.data || error.message);
            throw error;
        }
    },
    desassociarPatio: async (zonaId: number, patioId: number): Promise<void> => {
        try {
            await api.delete(`/zonas/${zonaId}/patios/${patioId}/desassociar`);
        } catch (error: any) {
            console.error(`Erro ao desassociar pátio ${patioId} da zona ${zonaId}:`, error.response?.data || error.message);
            throw error;
        }
    },
    getPatiosByZonaId: async (zonaId: number): Promise<PatioResponseDto[]> => {
        try {
            const response = await api.get<PatioResponseDto[]>(`/zonas/${zonaId}/patios`);
            return response.data;
        } catch (error: any) {
            console.error(`Erro ao buscar pátios da zona ${zonaId}:`, error.response?.data || error.message);
            throw error;
        }
    },
};

// ===============================================================
// EXPORT DEFAULT DA INSTÂNCIA AXIOS (OPCIONAL, MAS ÚTIL)
// ===============================================================
export default api; // Exporta a instância do axios configurada.