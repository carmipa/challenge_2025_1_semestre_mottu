// Path: ChallengeMuttuApi/Enums/EstadoCivil.cs

namespace ChallengeMuttuApi.Enums
{
    /// <summary>
    /// Define os estados civis possíveis para um cliente, mapeando os valores do banco de dados Oracle.
    /// </summary>
    public enum EstadoCivil
    {
        /// <summary>
        /// Representa o estado civil 'Solteiro'.
        /// </summary>
        Solteiro,
        /// <summary>
        /// Representa o estado civil 'Casado'.
        /// </summary>
        Casado,
        /// <summary>
        /// Representa o estado civil 'Divorciado'.
        /// </summary>
        Viuvo, // Representa o estado civil 'Viuvo'.
        /// <summary>
        /// Representa o estado civil 'Separado'.
        /// </summary>
        Separado,
        /// <summary>
        /// Representa o estado civil 'União Estável'.
        /// </summary>
        Uniao_Estavel // "Uni o Est vel" no DDL, adaptado para C#
    }
}