using System.ComponentModel.DataAnnotations;

namespace ChallengeMuttuApi.Model
{
    public class Filial
    {
        public int FilialId { get; set; }

        [MaxLength(50)]
        [Required]
        public string Nome { get; set; }

        private string status;
        public bool Status { get; set; } // 🔹 Agora trabalha com `true` (Ativo) ou `false` (Inativo)

    }
}
