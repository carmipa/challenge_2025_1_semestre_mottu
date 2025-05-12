// Path: ChallengeMuttuApi/Model/Cliente.cs - Este arquivo deve estar na pasta 'Model'
using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using ChallengeMuttuApi.Enums;
using System.Linq; // Necessário para .All(char.IsDigit)

namespace ChallengeMuttuApi.Model
{
    /// <summary>
    /// Representa a entidade Cliente no banco de dados, mapeando para a tabela "TB_CLIENTE".
    /// Contém informações pessoais e de contato de um cliente.
    /// </summary>
    [Table("TB_CLIENTE")]
    public class Cliente
    {
        /// <summary>
        /// Construtor padrão da classe Cliente.
        /// Inicializa propriedades de string não anuláveis para evitar warnings CS8618.
        /// </summary>
        public Cliente()
        {
            Nome = string.Empty;
            Sobrenome = string.Empty;
            Profissao = string.Empty;
            _sexo = string.Empty; // Inicializa o campo de apoio
            _cpf = string.Empty;   // Inicializa o campo de apoio
        }

        /// <summary>
        /// Obtém ou define o identificador único do Cliente.
        /// Mapeia para a coluna "ID_CLIENTE" (Chave Primária, gerada por identidade).
        /// </summary>
        [Key]
        [Column("ID_CLIENTE")]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int IdCliente { get; set; }

        /// <summary>
        /// Obtém ou define a data de cadastro do cliente.
        /// Mapeia para a coluna "DATA_CADASTRO" (DATE, Obrigatório, com valor padrão SYSDATE).
        /// </summary>
        [Column("DATA_CADASTRO")]
        [Required(ErrorMessage = "A data de cadastro é obrigatória.")]
        public DateTime DataCadastro { get; set; } = DateTime.Now; // Default SYSDATE no banco

        private string _sexo; // Campo de apoio para a propriedade Sexo

        /// <summary>
        /// Obtém ou define o sexo do cliente.
        /// Mapeia para a coluna "SEXO" (VARCHAR2(2 CHAR), Obrigatório).
        /// Possui validação para aceitar apenas 'M' (Mulher) ou 'H' (Homem).
        /// </summary>
        [Column("SEXO")]
        [Required(ErrorMessage = "O Sexo é obrigatório.")]
        [StringLength(2, ErrorMessage = "O Sexo deve ter no máximo 2 caracteres.")]
        public string Sexo
        {
            get => _sexo;
            set
            {
                if (value == "M" || value == "H") // 'M' para Masculino, 'H' para Feminino, ou similar. Verifique seu padrão real.
                    _sexo = value;
                else
                    throw new ArgumentException("Sexo inválido! Use 'M' para Masculino ou 'H' para Feminino.", nameof(Sexo));
            }
        }

        /// <summary>
        /// Obtém ou define o primeiro nome do cliente.
        /// Mapeia para a coluna "NOME" (VARCHAR2(100 BYTE), Obrigatório).
        /// </summary>
        [Column("NOME")]
        [Required(ErrorMessage = "O Nome é obrigatório.")]
        [StringLength(100, ErrorMessage = "O Nome deve ter no máximo 100 caracteres.")]
        public string Nome { get; set; }

        /// <summary>
        /// Obtém ou define o sobrenome do cliente.
        /// Mapeia para a coluna "SOBRENOME" (VARCHAR2(100 BYTE), Obrigatório).
        /// </summary>
        [Column("SOBRENOME")]
        [Required(ErrorMessage = "O Sobrenome é obrigatório.")]
        [StringLength(100, ErrorMessage = "O Sobrenome deve ter no máximo 100 caracteres.")]
        public string Sobrenome { get; set; }

        /// <summary>
        /// Obtém ou define a data de nascimento do cliente.
        /// Mapeia para a coluna "DATA_NASCIMENTO" (DATE, Obrigatório).
        /// Possui validação para datas a partir de '1900-01-01' via CHECK constraint no DB.
        /// </summary>
        [Column("DATA_NASCIMENTO")]
        [Required(ErrorMessage = "A Data de Nascimento é obrigatória.")]
        public DateTime DataNascimento { get; set; }

        private string _cpf; // Campo de apoio para a propriedade Cpf

        /// <summary>
        /// Obtém ou define o CPF (Cadastro de Pessoas Físicas) do cliente.
        /// Mapeia para a coluna "CPF" (VARCHAR2(11 CHAR), Obrigatório, Único).
        /// Possui validação para 11 dígitos numéricos.
        /// </summary>
        [Column("CPF")]
        [Required(ErrorMessage = "O CPF é obrigatório.")]
        [StringLength(11, MinimumLength = 11, ErrorMessage = "O CPF deve ter exatamente 11 dígitos.")]
        // O atributo [Index] foi removido daqui e será configurado via Fluent API no DbContext.
        public string Cpf
        {
            get => _cpf;
            set
            {
                if (!string.IsNullOrEmpty(value) && value.Length == 11 && value.All(char.IsDigit))
                    _cpf = value;
                else
                    throw new ArgumentException("CPF inválido! Deve conter 11 dígitos numéricos.", nameof(Cpf));
            }
        }

        /// <summary>
        /// Obtém ou define a profissão do cliente.
        /// Mapeia para a coluna "PROFISSAO" (VARCHAR2(50 BYTE), Obrigatório).
        /// </summary>
        [Column("PROFISSAO")]
        [Required(ErrorMessage = "A Profissão é obrigatória.")]
        [StringLength(50, ErrorMessage = "A Profissão deve ter no máximo 50 caracteres.")]
        public string Profissao { get; set; }

        private EstadoCivil _estadoCivil; // Campo de apoio para a propriedade EstadoCivil

        /// <summary>
        /// Obtém ou define o estado civil do cliente.
        /// Mapeia para a coluna "ESTADO_CIVIL" (VARCHAR2(50 BYTE), Obrigatório).
        /// Usa o enum <see cref="EstadoCivil"/> para validação e tipagem.
        /// </summary>
        [Column("ESTADO_CIVIL")]
        [Required(ErrorMessage = "O Estado Civil é obrigatório.")]
        [StringLength(50)] // Mapeamento para string no banco
        [EnumDataType(typeof(EstadoCivil), ErrorMessage = "Estado civil inválido!")]
        public EstadoCivil EstadoCivil
        {
            get => _estadoCivil;
            set
            {
                if (!Enum.IsDefined(typeof(EstadoCivil), value))
                    throw new ArgumentException("Estado civil inválido!", nameof(EstadoCivil));
                _estadoCivil = value;
            }
        }

        /// <summary>
        /// Obtém ou define o status do cliente.
        /// OBS: Esta propriedade não está presente no DDL da tabela TB_CLIENTE original.
        /// Assumimos que é um campo de status lógico (Ativo/Inativo), onde `true` é Ativo e `false` é Inativo.
        /// </summary>
        [Column("STATUS")]
        [Required(ErrorMessage = "O Status do cliente é obrigatório.")]
        public bool Status { get; set; }

        /// <summary>
        /// Obtém ou define o ID da chave estrangeira para a entidade Endereco.
        /// Mapeia para a coluna "TB_ENDERECO_ID_ENDERECO" (NUMBER, Obrigatório).
        /// </summary>
        [Column("TB_ENDERECO_ID_ENDERECO")]
        [Required(ErrorMessage = "O ID do Endereço é obrigatório.")]
        public int TbEnderecoIdEndereco { get; set; }

        /// <summary>
        /// Obtém ou define o ID da chave estrangeira para a entidade Contato.
        /// Mapeia para a coluna "TB_CONTATO_ID_CONTATO" (NUMBER, Obrigatório).
        /// </summary>
        [Column("TB_CONTATO_ID_CONTATO")]
        [Required(ErrorMessage = "O ID do Contato é obrigatório.")]
        public int TbContatoIdContato { get; set; }

        // Propriedades de Navegação

        /// <summary>
        /// Propriedade de navegação para a entidade Endereco associada.
        /// </summary>
        [ForeignKey("TbEnderecoIdEndereco")]
        public Endereco? Endereco { get; set; }

        /// <summary>
        /// Propriedade de navegação para a entidade Contato associada.
        /// </summary>
        [ForeignKey("TbContatoIdContato")]
        public Contato? Contato { get; set; }

        /// <summary>
        /// Coleção de entidades ClienteVeiculo, representando o relacionamento muitos-para-muitos com Veículos.
        /// </summary>
        public ICollection<ClienteVeiculo>? ClienteVeiculos { get; set; }

        /// <summary>
        /// Construtor parametrizado da classe Cliente.
        /// </summary>
        /// <param name="idCliente">O identificador único do cliente.</param>
        /// <param name="nome">O primeiro nome do cliente.</param>
        /// <param name="sobrenome">O sobrenome do cliente.</param>
        /// <param name="sexo">O sexo do cliente ('M' ou 'H').</param>
        /// <param name="cpf">O CPF do cliente (11 dígitos numéricos).</param>
        /// <param name="profissao">A profissão do cliente.</param>
        /// <param name="estadoCivil">O estado civil do cliente, usando o enum EstadoCivil.</param>
        /// <param name="dataNascimento">A data de nascimento do cliente.</param>
        /// <param name="status">O status do cliente (true para Ativo, false para Inativo).</param>
        public Cliente(int idCliente, string nome, string sobrenome, string sexo, string cpf,
                       string profissao, EstadoCivil estadoCivil, DateTime dataNascimento, bool status)
        {
            IdCliente = idCliente;
            Nome = nome;
            Sobrenome = sobrenome;
            Profissao = profissao;
            EstadoCivil = estadoCivil;
            DataNascimento = dataNascimento;
            Status = status;

            DataCadastro = DateTime.Now;

            Sexo = sexo;
            Cpf = cpf;
        }
    }
}