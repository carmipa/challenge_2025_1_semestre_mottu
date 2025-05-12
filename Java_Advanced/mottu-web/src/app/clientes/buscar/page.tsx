// src/app/clientes/buscar/page.tsx
"use client";

import { useState, FormEvent } from 'react';
import Link from 'next/link';
import NavBar from '@/components/nav-bar';
import { MdSearch, MdErrorOutline, MdEdit, MdDelete, MdFilterList, MdInfo, MdCalendarToday } from 'react-icons/md';
import { User, Mail, MapPin, Edit3, Trash2, Hash, Search as SearchIcon, Filter, Briefcase, CheckCircle } from 'lucide-react';

// Interfaces dos DTOs e Filtro
import { ClienteResponseDto, ClienteFilter } from '@/types/cliente';
import { ClienteService } from '@/utils/api';

export default function BuscarClientesPage() {
    const [clientes, setClientes] = useState<ClienteResponseDto[]>([]);
    const [isLoading, setIsLoading] = useState(false); // Para controlar o estado de busca
    const [error, setError] = useState<string | null>(null);
    const [hasSearched, setHasSearched] = useState(false); // Para indicar se uma busca foi realizada

    // Estado para os filtros (corresponde a ClienteFilter.java)
    const [filter, setFilter] = useState<ClienteFilter>({
        nome: '',
        sobrenome: '',
        cpf: '',
        sexo: undefined,
        profissao: '',
        estadoCivil: undefined,
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

    // Handler para mudanças nos inputs de filtro
    const handleFilterChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setFilter((prevFilter) => ({
            ...prevFilter,
            [name]: value,
        }));
    };

    // Função para buscar os clientes da API com filtros
    const handleSearch = async (e: FormEvent) => {
        e.preventDefault();

        setIsLoading(true);
        setError(null);
        setClientes([]); // Limpa resultados anteriores
        setHasSearched(true); // Marca que uma busca foi realizada

        try {
            // Chama o serviço com os parâmetros de filtro
            const data = await ClienteService.getAll(filter);
            setClientes(data);
        } catch (err: any) {
            setError(err.response?.data?.message || err.message || 'Erro ao buscar clientes. Tente novamente.');
            console.error("Erro detalhado:", err);
        } finally {
            setIsLoading(false);
        }
    };

    const handleDelete = async (id: number, nomeCliente: string) => {
        if (window.confirm(`Tem certeza que deseja deletar o cliente "${nomeCliente}" (ID: ${id})?`)) {
            setIsLoading(true); // Mostra loading
            setError(null); // Limpa erro anterior
            try {
                await ClienteService.delete(id);
                // Se a busca foi realizada, remove o item da lista de resultados também
                setClientes(prev => prev.filter(c => c.idCliente !== id));
                alert(`Cliente "${nomeCliente}" (ID: ${id}) deletado com sucesso!`); // Feedback simples
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
            <NavBar active="clientes-buscar" />
            <main className="container mx-auto px-4 py-8 bg-[#012A46] min-h-screen text-white">
                <h1 className="flex items-center justify-center gap-2 text-2xl md:text-3xl font-bold mb-6 text-center">
                    <SearchIcon size={30} className="text-sky-400" /> Buscar Clientes
                </h1>

                {/* Formulário de Filtros Avançados */}
                <form onSubmit={handleSearch} className="mb-6 p-4 bg-slate-800 rounded-lg shadow-md grid grid-cols-1 md:grid-cols-3 gap-4 items-end">
                    <div>
                        <label htmlFor="nome" className="text-sm text-slate-300 block mb-1">Nome:</label>
                        <input type="text" id="nome" name="nome" value={filter.nome} onChange={handleFilterChange} className="p-2 h-10 rounded bg-slate-700 border border-slate-600 text-white" placeholder="Nome do cliente" />
                    </div>
                    <div>
                        <label htmlFor="sobrenome" className="text-sm text-slate-300 block mb-1">Sobrenome:</label>
                        <input type="text" id="sobrenome" name="sobrenome" value={filter.sobrenome} onChange={handleFilterChange} className="p-2 h-10 rounded bg-slate-700 border border-slate-600 text-white" placeholder="Sobrenome do cliente" />
                    </div>
                    <div>
                        <label htmlFor="cpf" className="text-sm text-slate-300 block mb-1">CPF:</label>
                        <input type="text" id="cpf" name="cpf" value={filter.cpf} onChange={handleFilterChange} maxLength={11} className="p-2 h-10 rounded bg-slate-700 border border-slate-600 text-white" placeholder="Apenas números" />
                    </div>
                    <div>
                        <label htmlFor="sexo" className="text-sm text-slate-300 block mb-1">Sexo:</label>
                        <select id="sexo" name="sexo" value={filter.sexo} onChange={handleFilterChange} className="p-2 h-10 rounded bg-slate-700 border border-slate-600 text-white min-w-[100px]">
                            <option value="">Todos</option>
                            <option value="M">Masculino</option>
                            <option value="H">Feminino</option>
                        </select>
                    </div>
                    <div>
                        <label htmlFor="profissao" className="text-sm text-slate-300 block mb-1">Profissão:</label>
                        <input type="text" id="profissao" name="profissao" value={filter.profissao} onChange={handleFilterChange} className="p-2 h-10 rounded bg-slate-700 border border-slate-600 text-white" placeholder="Profissão" />
                    </div>
                    <div>
                        <label htmlFor="estadoCivil" className="text-sm text-slate-300 block mb-1">Estado Civil:</label>
                        <select id="estadoCivil" name="estadoCivil" value={filter.estadoCivil} onChange={handleFilterChange} className="p-2 h-10 rounded bg-slate-700 border border-slate-600 text-white min-w-[150px]">
                            <option value="">Todos</option>
                            <option value="Solteiro">Solteiro</option>
                            <option value="Casado">Casado</option>
                            <option value="Divorciado">Divorciado</option>
                            <option value="Viúvo">Viúvo</option>
                            <option value="Separado">Separado</option>
                            <option value="União Estável">União Estável</option>
                        </select>
                    </div>
                    <div>
                        <label htmlFor="dataNascimentoInicio" className="text-sm text-slate-300 block mb-1">Nasc. (Início):</label>
                        <input type="date" id="dataNascimentoInicio" name="dataNascimentoInicio" value={filter.dataNascimentoInicio} onChange={handleFilterChange} className="p-2 h-10 rounded bg-slate-700 border border-slate-600 text-white date-input-fix" />
                    </div>
                    <div>
                        <label htmlFor="dataNascimentoFim" className="text-sm text-slate-300 block mb-1">Nasc. (Fim):</label>
                        <input type="date" id="dataNascimentoFim" name="dataNascimentoFim" value={filter.dataNascimentoFim} onChange={handleFilterChange} className="p-2 h-10 rounded bg-slate-700 border border-slate-600 text-white date-input-fix" />
                    </div>
                    <div>
                        <label htmlFor="enderecoCidade" className="text-sm text-slate-300 block mb-1">Cidade (End.):</label>
                        <input type="text" id="enderecoCidade" name="enderecoCidade" value={filter.enderecoCidade} onChange={handleFilterChange} className="p-2 h-10 rounded bg-slate-700 border border-slate-600 text-white" placeholder="Cidade do endereço" />
                    </div>
                    <div>
                        <label htmlFor="contatoEmail" className="text-sm text-slate-300 block mb-1">Email (Contato):</label>
                        <input type="email" id="contatoEmail" name="contatoEmail" value={filter.contatoEmail} onChange={handleFilterChange} className="p-2 h-10 rounded bg-slate-700 border border-slate-600 text-white" placeholder="Email do contato" />
                    </div>
                    <div>
                        <label htmlFor="veiculoPlaca" className="text-sm text-slate-300 block mb-1">Placa (Veículo):</label>
                        <input type="text" id="veiculoPlaca" name="veiculoPlaca" value={filter.veiculoPlaca} onChange={handleFilterChange} className="p-2 h-10 rounded bg-slate-700 border border-slate-600 text-white" placeholder="Placa do veículo associado" />
                    </div>
                    <div className="md:col-span-3 flex justify-center">
                        <button type="submit" className="p-2 h-10 bg-sky-600 hover:bg-sky-700 text-white font-semibold rounded-md flex items-center gap-2 px-4">
                            <SearchIcon size={20} /> Buscar Clientes
                        </button>
                    </div>
                </form>

                {/* Mensagens de Feedback */}
                {isLoading && <p className="text-center text-sky-300 py-10">Buscando clientes...</p>}
                {error && (
                    <div className="relative max-w-3xl mx-auto mb-6 text-red-400 bg-red-900/50 p-4 pr-10 rounded border border-red-500" role="alert">
                        <MdErrorOutline className="inline mr-2" />{error}
                        <button type="button" className="absolute top-0 bottom-0 right-0 px-4 py-3 text-red-400 hover:text-red-200" onClick={() => setError(null)} aria-label="Fechar"><span className="text-xl" aria-hidden="true">&times;</span></button>
                    </div>
                )}

                {/* Tabela de Resultados da Busca */}
                {!isLoading && hasSearched && clientes.length === 0 && !error && (
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
                                        <Link href={`/clientes/alterar/${cliente.idCliente}`}>
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