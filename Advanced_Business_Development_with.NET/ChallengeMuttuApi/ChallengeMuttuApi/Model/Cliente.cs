using System.ComponentModel.DataAnnotations;
using ChallengeMuttuApi.Enums;

namespace ChallengeMuttuApi.Model
{
    public class Cliente
    {
        public Cliente() { } // 🔹 Equivalente ao @NoArgsConstructor

        public int ClienteId { get; set; }
        private DateTime DataCadastro { get; set; } = DateTime.Now;

        private string sexo;
        public string Sexo
        {
            get => sexo;
            set
            {
                if (value == "M" || value == "H")
                    sexo = value;
                else
                    throw new ArgumentException("Sexo inválido! Use 'M' para Mulher ou 'H' para Homem.");
            }
        }

        [MaxLength(50)]
        [Required]
        public string Nome { get; set; }

        [MaxLength(50)]
        [Required]
        public string Sobrenome { get; set; }

        private string cpf;

        public string GetCPF()
        {
            return cpf;
        }

        public void SetCPF(string value)
        {
            if (!string.IsNullOrEmpty(value) && value.Length == 11 && value.All(char.IsDigit))
                cpf = value;
            else
                throw new ArgumentException("CPF inválido! Deve conter 11 dígitos numéricos.");
        }

        [MaxLength(50)]
        [Required]
        public string Profissao { get; set; }

        private EstadoCivil estadoCivil;
        public EstadoCivil EstadoCivil
        {
            get => estadoCivil;
            set
            {
                if (!Enum.IsDefined(typeof(EstadoCivil), value))
                    throw new ArgumentException("Estado civil inválido!");
                estadoCivil = value;
            }
        }

        private string status;
        public bool Status { get; set; } // 🔹 Agora trabalha com `true` (Ativo) ou `false` (Inativo)

        // 🔥 Construtor Parametrizado - Agora está dentro da classe corretamente!
        public Cliente(int clienteId, string nome, string sobrenome, string sexo, string cpf, string profissao, EstadoCivil estadoCivil, bool status)
        {
            ClienteId = clienteId;
            Nome = nome;
            Sobrenome = sobrenome;
            Profissao = profissao;
            EstadoCivil = estadoCivil;
            Status = status;

            // 🔹 Definir `DataCadastro` no momento da criação
            DataCadastro = DateTime.Now;

            // 🔹 Validar `Sexo`
            if (sexo == "M" || sexo == "H")
                this.sexo = sexo;
            else
                throw new ArgumentException("Sexo inválido! Use 'M' para Mulher ou 'H' para Homem.");

            // 🔹 Validar CPF
            if (!string.IsNullOrEmpty(cpf) && cpf.Length == 11 && cpf.All(char.IsDigit))
                this.cpf = cpf;
            else
                throw new ArgumentException("CPF inválido! Deve conter 11 dígitos numéricos.");
        }
    }
}