// Caminho original no seu .txt: Model\Cliente.cs
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
            // ClienteVeiculos poderia ser inicializado aqui se não for anulável,
            // mas como é ICollection?, está ok não inicializar ou pode-se optar por new List<ClienteVeiculo>();
            // ClienteVeiculos = new List<ClienteVeiculo>();
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
        /// Possui validação para aceitar apenas 'M' (Masculino) ou 'F' (Feminino) - ajuste conforme sua regra de negócio.
        /// </summary>
        [Column("SEXO")]
        [Required(ErrorMessage = "O Sexo é obrigatório.")]
        [StringLength(2, ErrorMessage = "O Sexo deve ter no máximo 2 caracteres.")]
        public required string Sexo // 'required' garante que será fornecido ou inicializado.
        {
            get => _sexo;
            set
            {
                // Ajuste a validação conforme sua regra. O DDL original tinha CHECK (SEXO IN ('M', 'H'))
                if (value?.ToUpper() == "M" || value?.ToUpper() == "H" || value?.ToUpper() == "F")
                    _sexo = value.ToUpper();
                else
                    throw new ArgumentException("Sexo inválido! Use 'M', 'H' ou 'F' (conforme sua regra).", nameof(Sexo));
            }
        }

        /// <summary>
        /// Obtém ou define o primeiro nome do cliente.
        /// Mapeia para a coluna "NOME" (VARCHAR2(100 BYTE), Obrigatório).
        /// </summary>
        [Column("NOME")]
        [Required(ErrorMessage = "O Nome é obrigatório.")]
        [StringLength(100, ErrorMessage = "O Nome deve ter no máximo 100 caracteres.")]
        public required string Nome { get; set; } // 'required' garante que será fornecido ou inicializado.

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
        /// </summary>
        [Column("DATA_NASCIMENTO")]
        [Required(ErrorMessage = "A Data de Nascimento é obrigatória.")]
        public DateTime DataNascimento { get; set; }

        private string _cpf; // Campo de apoio para a propriedade Cpf

        /// <summary>
        /// Obtém ou define o CPF (Cadastro de Pessoas Físicas) do cliente.
        /// Mapeia para a coluna "CPF" (VARCHAR2(11 CHAR), Obrigatório, Único).
        /// </summary>
        [Column("CPF")]
        [Required(ErrorMessage = "O CPF é obrigatório.")]
        [StringLength(11, MinimumLength = 11, ErrorMessage = "O CPF deve ter exatamente 11 dígitos.")]
        public required string Cpf // 'required' garante que será fornecido ou inicializado.
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
        /// </summary>
        [Column("ESTADO_CIVIL")]
        [Required(ErrorMessage = "O Estado Civil é obrigatório.")]
        [StringLength(50)]
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
        public Cliente(int idCliente, string nome, string sobrenome, string sexo, string cpf,
                                string profissao, EstadoCivil estadoCivil, DateTime dataNascimento,
                                int tbEnderecoIdEndereco, int tbContatoIdContato) // Adicionado IDs de FK
        {
            IdCliente = idCliente;
            Nome = nome;
            Sobrenome = sobrenome;
            Profissao = profissao;
            EstadoCivil = estadoCivil;
            DataNascimento = dataNascimento;
            DataCadastro = DateTime.Now;
            Sexo = sexo;
            Cpf = cpf;
            TbEnderecoIdEndereco = tbEnderecoIdEndereco; // Atribuir IDs de FK
            TbContatoIdContato = tbContatoIdContato;     // Atribuir IDs de FK
        }
    }
}