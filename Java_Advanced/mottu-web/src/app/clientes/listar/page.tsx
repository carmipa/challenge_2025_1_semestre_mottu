// src/app/clientes/listar/page.tsx
"use client";

import { useState, useEffect, FormEvent } from 'react';
import Link from 'next/link';
import { ClienteService } from '@/utils/api'; // Importe o serviço de Cliente
import NavBar from '@/components/nav-bar';
import {
    MdList, MdAdd, MdSearch, MdErrorOutline, MdEdit, MdDelete,
    MdFilterList, MdCalendarToday, MdInfo, MdCheckCircle, MdPeopleAlt, MdPersonAdd
} from 'react-icons/md';
import { User, Mail, MapPin, Edit3, Trash2, Hash, Search as SearchIcon, Filter } from 'lucide-react';

// Interfaces dos DTOs e Filtro
import { ClienteResponseDto, ClienteFilter } from '@/types/cliente';

export default function ListarClientesPage() {
    const [clientes, setClientes] = useState<ClienteResponseDto[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [successMessage, setSuccessMessage] = useState<string | null>(null);

    // Estado para os filtros
    const [filter, setFilter] = useState<ClienteFilter>({
        nome: '',
        sobrenome: '',
        cpf: '',
        sexo: undefined, // 'M' | 'H'
        profissao: '',
        estadoCivil: undefined, // Enum
        dataCadastroInicio: '',
        dataCadastroFim: '',
        dataNascimentoInicio: '',
        dataNascimentoFim: '',
        enderecoCidade: '',
        enderecoEstado: '',
        contatoEmail: '',
        contatoCelular: '',
        veiculoPlaca: '',
        veiculoModelo: '',
    });

    // Funções de formatação
    const formatDate = (dateString: string | null | undefined): string => {
        if (!dateString) return '-';
        try {
            return new Date(dateString + 'T00:00:00Z').toLocaleDateString('pt-BR', { timeZone: 'UTC' });
        } catch (e) {
            console.error("Erro ao formatar data:", dateString, e);
            return 'Inválida';
        }
    };

    // Função para buscar os clientes da API com filtros
    const fetchClientes = async (e?: FormEvent) => {
        if (e) e.preventDefault();

        setIsLoading(true);
        setError(null);
        setSuccessMessage(null);

        try {
            const data = await ClienteService.getAll(filter);
            setClientes(data);
        } catch (err: any) {
            setError(err.response?.data?.message || err.message || 'Erro ao carregar clientes. Tente novamente.');
            console.error("Erro detalhado:", err);
            setClientes([]);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        fetchClientes();
    }, []);

    const handleFilterChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setFilter((prevFilter) => ({
            ...prevFilter,
            [name]: value,
        }));
    };

    const handleDelete = async (id: number, nomeCliente: string) => {
        if (window.confirm(`Tem certeza que deseja deletar o cliente "${nomeCliente}" (ID: ${id})?`)) {
            setIsLoading(true);
            setError(null);
            try {
                await ClienteService.delete(id);
                setSuccessMessage(`Cliente "${nomeCliente}" (ID: ${id}) deletado com sucesso!`);
                fetchClientes(); // Recarrega a lista
                setTimeout(() => setSuccessMessage(null), 5000);
            } catch (err: any) {
                setError(err.response?.data?.message || err.message || 'Erro ao deletar cliente.');
                console.error("Erro detalhado:", err);
            } finally {
                setIsLoading(false);
            }
        }
    };

    return (
        <>
            <NavBar active="clientes-listar" />
            <main className="container mx-auto px-4 py-8 bg-[#012A46] min-h-screen text-white">
                <div className="flex flex-col sm:flex-row justify-between items-center mb-6 gap-4">
                    <h1 className="flex items-center gap-2 text-2xl md:text-3xl font-bold text-center sm:text-left">
                        <MdPeopleAlt className="text-3xl text-sky-400" /> Lista de Clientes
                    </h1>
                    <Link href="/clientes/cadastrar">
                        <button className="flex items-center gap-2 px-4 py-2 bg-green-600 hover:bg-green-700 text-white font-semibold rounded-md shadow whitespace-nowrap">
                            <MdPersonAdd size={18} /> Novo Cliente
                        </button>
                    </Link>
                </div>

                {/* Formulário de Filtros */}
                <form onSubmit={fetchClientes} className="mb-6 p-4 bg-slate-800 rounded-lg shadow-md grid grid-cols-1 md:grid-cols-3 gap-4 items-end">
                    <div>
                        <label htmlFor="nome" className="text-sm text-slate-300 block mb-1">Nome:</label>
                        <input
                            type="text"
                            id="nome"
                            name="nome"
                            value={filter.nome}
                            onChange={handleFilterChange}
                            className="p-2 h-10 rounded bg-slate-700 border border-slate-600 text-white"
                            placeholder="Parte do nome"
                        />
                    </div>
                    <div>
                        <label htmlFor="cpf" className="text-sm text-slate-300 block mb-1">CPF:</label>
                        <input
                            type="text"
                            id="cpf"
                            name="cpf"
                            value={filter.cpf}
                            onChange={handleFilterChange}
                            maxLength={11}
                            className="p-2 h-10 rounded bg-slate-700 border border-slate-600 text-white"
                            placeholder="Apenas números"
                        />
                    </div>
                    <div>
                        <label htmlFor="sexo" className="text-sm text-slate-300 block mb-1">Sexo:</label>
                        <select
                            id="sexo"
                            name="sexo"
                            value={filter.sexo}
                            onChange={handleFilterChange}
                            className="p-2 h-10 rounded bg-slate-700 border border-slate-600 text-white min-w-[100px]"
                        >
                            <option value="">Todos</option>
                            <option value="M">Masculino</option>
                            <option value="H">Feminino</option>
                        </select>
                    </div>
                    <div>
                        <label htmlFor="enderecoCidade" className="text-sm text-slate-300 block mb-1">Cidade (End.):</label>
                        <input
                            type="text"
                            id="enderecoCidade"
                            name="enderecoCidade"
                            value={filter.enderecoCidade}
                            onChange={handleFilterChange}
                            className="p-2 h-10 rounded bg-slate-700 border border-slate-600 text-white"
                            placeholder="Cidade do endereço"
                        />
                    </div>
                    <div>
                        <label htmlFor="contatoEmail" className="text-sm text-slate-300 block mb-1">Email (Contato):</label>
                        <input
                            type="email"
                            id="contatoEmail"
                            name="contatoEmail"
                            value={filter.contatoEmail}
                            onChange={handleFilterChange}
                            className="p-2 h-10 rounded bg-slate-700 border border-slate-600 text-white"
                            placeholder="Email do contato"
                        />
                    </div>
                    <div>
                        <label htmlFor="veiculoPlaca" className="text-sm text-slate-300 block mb-1">Placa (Veículo):</label>
                        <input
                            type="text"
                            id="veiculoPlaca"
                            name="veiculoPlaca"
                            value={filter.veiculoPlaca}
                            onChange={handleFilterChange}
                            className="p-2 h-10 rounded bg-slate-700 border border-slate-600 text-white"
                            placeholder="Placa do veículo associado"
                        />
                    </div>
                    {/* Adicione mais campos de filtro aqui conforme o ClienteFilter.java */}
                    <div className="md:col-span-3 flex justify-center">
                        <button type="submit" className="p-2 h-10 bg-sky-600 hover:bg-sky-700 text-white font-semibold rounded-md flex items-center gap-2 px-4">
                            <SearchIcon size={20} /> Aplicar Filtros
                        </button>
                    </div>
                </form>

                {/* Mensagens de Feedback */}
                {isLoading && <p className="text-center text-sky-300 py-10">Carregando clientes...</p>}
                {error && (
                    <div className="relative max-w-3xl mx-auto mb-6 text-red-400 bg-red-900/50 p-4 pr-10 rounded border border-red-500" role="alert">
                        <MdErrorOutline className="inline mr-2" />{error}
                        <button type="button" className="absolute top-0 bottom-0 right-0 px-4 py-3 text-red-400 hover:text-red-200" onClick={() => setError(null)} aria-label="Fechar"><span className="text-xl" aria-hidden="true">&times;</span></button>
                    </div>
                )}
                {successMessage && (
                    <div className="relative max-w-3xl mx-auto mb-6 text-green-400 bg-green-900/50 p-4 pr-10 rounded border border-green-500" role="alert">
                        <MdCheckCircle className="inline mr-2" />{successMessage}
                        <button type="button" className="absolute top-0 bottom-0 right-0 px-4 py-3 text-green-400 hover:text-green-200" onClick={() => setSuccessMessage(null)} aria-label="Fechar"><span className="text-xl" aria-hidden="true">&times;</span></button>
                    </div>
                )}

                {/* Tabela de Clientes */}
                {!isLoading && !error && clientes.length === 0 && (
                    <p className="text-center text-slate-400 py-10 bg-slate-900 rounded-lg shadow-xl">Nenhum cliente encontrado para os critérios informados.</p>
                )}
                {!isLoading && clientes.length > 0 && (
                    <div className="overflow-x-auto bg-slate-900 rounded-lg shadow">
                        <table className="min-w-full table-auto">
                            <thead className="bg-slate-800 border-b border-slate-700">
                            <tr>
                                <th className="px-6 py-3 text-left text-xs font-medium text-slate-300 uppercase tracking-wider flex items-center gap-1"><Hash size={14}/> ID</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-slate-300 uppercase tracking-wider">Nome</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-slate-300 uppercase tracking-wider">CPF</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-slate-300 uppercase tracking-wider">Email</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-slate-300 uppercase tracking-wider">Cidade/UF</th>
                                <th className="px-6 py-3 text-center text-xs font-medium text-slate-300 uppercase tracking-wider">Ações</th>
                            </tr>
                            </thead>
                            <tbody className="divide-y divide-slate-700">
                            {clientes.map((cliente) => (
                                <tr key={cliente.idCliente} className="hover:bg-slate-800/50">
                                    <td className="px-6 py-4 whitespace-nowrap">{cliente.idCliente}</td>
                                    <td className="px-6 py-4 whitespace-nowrap">{`${cliente.nome} ${cliente.sobrenome}`}</td>
                                    <td className="px-6 py-4 whitespace-nowrap">{cliente.cpf}</td>
                                    <td className="px-6 py-4 whitespace-nowrap">{cliente.contatoResponseDto?.email || '-'}</td>
                                    <td className="px-6 py-4 whitespace-nowrap">{`${cliente.enderecoResponseDto?.cidade || '-'} / ${cliente.enderecoResponseDto?.estado || '-'}`}</td>
                                    <td className="px-6 py-4 whitespace-nowrap text-center space-x-2 flex items-center justify-center">
                                        <Link href={`/clientes/alterar/${cliente.idCliente}`}> {/* Rota de alteração ajustada */}
                                            <button className="px-3 py-1 text-sm bg-yellow-500 hover:bg-yellow-600 text-black rounded flex items-center gap-1">
                                                <MdEdit size={14} /> Editar
                                            </button>
                                        </Link>
                                        <button
                                            onClick={() => handleDelete(cliente.idCliente, `${cliente.nome} ${cliente.sobrenome}`)}
                                            className="px-3 py-1 text-sm bg-red-600 hover:bg-red-700 text-white rounded flex items-center gap-1"
                                        >
                                            <MdDelete size={14} /> Deletar
                                        </button>
                                        {/* Botão para associar veículos (exemplo, levaria a um modal ou página dedicada) */}
                                        <Link href={`/clientes/associar-veiculo/${cliente.idCliente}`}>
                                            <button className="px-3 py-1 text-sm bg-blue-500 hover:bg-blue-600 text-white rounded flex items-center gap-1">
                                                <MdInfo size={14} /> Ver Veículos
                                            </button>
                                        </Link>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </main>
            <style jsx global>{`
                .date-input-fix::-webkit-calendar-picker-indicator { filter: invert(0.8); cursor: pointer; }
                input[type="date"]:required:invalid::-webkit-datetime-edit { color: transparent; }
                input[type="date"]:focus::-webkit-datetime-edit { color: white !important; }
                input[type="date"]::-webkit-datetime-edit { color: white; }
            `}</style>
        </>
    );
}