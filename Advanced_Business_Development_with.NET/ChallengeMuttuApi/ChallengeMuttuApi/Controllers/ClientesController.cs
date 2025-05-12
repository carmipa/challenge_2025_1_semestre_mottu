// Path: ChallengeMuttuApi/Controllers/ClientesController.cs
using ChallengeMuttuApi.Data;
using ChallengeMuttuApi.Model;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ChallengeMuttuApi.Controllers
{
    /// <summary>
    /// Controller responsável por gerenciar as operações CRUD para a entidade Cliente.
    /// </summary>
    [ApiController] // Indica que a classe é uma controller de API
    [Route("api/[controller]")] // Define a rota base para a controller (ex: /api/clientes)
    [Produces("application/json")] // Garante que a API sempre retorne JSON
    public class ClientesController : ControllerBase
    {
        private readonly AppDbContext _context;

        /// <summary>
        /// Construtor da ClientesController.
        /// Injeta a instância do AppDbContext para acesso ao banco de dados.
        /// </summary>
        /// <param name="context">O contexto do banco de dados da aplicação.</param>
        public ClientesController(AppDbContext context)
        {
            _context = context;
        }

        // ---------------------------------------------------------------------
        // Rotas GET (Recuperação de Dados)
        // ---------------------------------------------------------------------

        /// <summary>
        /// Retorna uma lista de todos os clientes cadastrados.
        /// </summary>
        /// <remarks>
        /// Este endpoint retorna todos os clientes disponíveis no banco de dados.
        /// </remarks>
        /// <response code="200">Retorna a lista de clientes.</response>
        /// <response code="204">Não há clientes cadastrados.</response>
        /// <response code="500">Ocorreu um erro interno no servidor.</response>
        [HttpGet]
        [ProducesResponseType(typeof(IEnumerable<Cliente>), 200)]
        [ProducesResponseType(204)]
        [ProducesResponseType(500)]
        public async Task<ActionResult<IEnumerable<Cliente>>> GetAllClientes()
        {
            try
            {
                var clientes = await _context.Clientes.ToListAsync();
                if (!clientes.Any())
                {
                    return NoContent(); // Retorna 204 se não houver clientes
                }
                return Ok(clientes); // Retorna 200 com a lista de clientes
            }
            catch (Exception ex)
            {
                // Log the exception (e.g., using Serilog, NLog, or ILogger)
                Console.WriteLine($"Erro ao buscar todos os clientes: {ex.Message}");
                return StatusCode(500, "Erro interno do servidor ao buscar clientes.");
            }
        }

        /// <summary>
        /// Retorna um cliente específico pelo seu ID.
        /// </summary>
        /// <remarks>
        /// Este endpoint permite buscar um cliente utilizando o seu identificador único.
        /// Exemplo: GET /api/clientes/1
        /// </remarks>
        /// <param name="id">O ID do cliente a ser buscado.</param>
        /// <response code="200">Retorna o cliente encontrado.</response>
        /// <response code="404">Cliente não encontrado.</response>
        /// <response code="500">Ocorreu um erro interno no servidor.</response>
        [HttpGet("{id:int}")] // Rota parametrizada com PathParam (id inteiro)
        [ProducesResponseType(typeof(Cliente), 200)]
        [ProducesResponseType(404)]
        [ProducesResponseType(500)]
        public async Task<ActionResult<Cliente>> GetClienteById(int id)
        {
            try
            {
                var cliente = await _context.Clientes.FindAsync(id);
                if (cliente == null)
                {
                    return NotFound("Cliente não encontrado."); // Retorna 404
                }
                return Ok(cliente); // Retorna 200 com o cliente
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Erro ao buscar cliente por ID: {ex.Message}");
                return StatusCode(500, "Erro interno do servidor ao buscar cliente.");
            }
        }

        /// <summary>
        /// Retorna um cliente específico pelo seu CPF.
        /// </summary>
        /// <remarks>
        /// Este endpoint permite buscar um cliente utilizando o seu número de CPF.
        /// Exemplo: GET /api/clientes/by-cpf/12345678901
        /// </remarks>
        /// <param name="cpf">O CPF do cliente a ser buscado.</param>
        /// <response code="200">Retorna o cliente encontrado.</response>
        /// <response code="400">O CPF fornecido é inválido.</response>
        /// <response code="404">Cliente com o CPF especificado não encontrado.</response>
        /// <response code="500">Ocorreu um erro interno no servidor.</response>
        [HttpGet("by-cpf/{cpf}")] // Rota parametrizada com PathParam (cpf string)
        [ProducesResponseType(typeof(Cliente), 200)]
        [ProducesResponseType(400)]
        [ProducesResponseType(404)]
        [ProducesResponseType(500)]
        public async Task<ActionResult<Cliente>> GetClienteByCpf(string cpf)
        {
            // Adiciona validação básica de CPF para BadRequest
            if (string.IsNullOrWhiteSpace(cpf) || cpf.Length != 11 || !cpf.All(char.IsDigit))
            {
                return BadRequest("CPF inválido. Deve conter 11 dígitos numéricos.");
            }

            try
            {
                var cliente = await _context.Clientes.FirstOrDefaultAsync(c => c.Cpf == cpf);
                if (cliente == null)
                {
                    return NotFound($"Cliente com CPF '{cpf}' não encontrado."); // Retorna 404
                }
                return Ok(cliente); // Retorna 200 com o cliente
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Erro ao buscar cliente por CPF: {ex.Message}");
                return StatusCode(500, "Erro interno do servidor ao buscar cliente por CPF.");
            }
        }

        /// <summary>
        /// Pesquisa clientes por parte do nome.
        /// </summary>
        /// <remarks>
        /// Este endpoint permite buscar clientes cujos nomes contenham a string fornecida.
        /// A pesquisa não diferencia maiúsculas de minúsculas.
        /// Exemplo: GET /api/clientes/search-by-name?nome=maria
        /// </remarks>
        /// <param name="nome">A string a ser pesquisada no nome dos clientes.</param>
        /// <response code="200">Retorna a lista de clientes que correspondem à pesquisa.</response>
        /// <response code="204">Nenhum cliente encontrado com o nome especificado.</response>
        /// <response code="400">O parâmetro de nome para pesquisa é obrigatório.</response>
        /// <response code="500">Ocorreu um erro interno no servidor.</response>
        [HttpGet("search-by-name")] // Rota parametrizada com QueryParam
        [ProducesResponseType(typeof(IEnumerable<Cliente>), 200)]
        [ProducesResponseType(204)]
        [ProducesResponseType(400)]
        [ProducesResponseType(500)]
        public async Task<ActionResult<IEnumerable<Cliente>>> SearchClientesByName([FromQuery] string nome)
        {
            if (string.IsNullOrWhiteSpace(nome))
            {
                return BadRequest("O parâmetro 'nome' para pesquisa é obrigatório.");
            }

            try
            {
                var clientes = await _context.Clientes
                    .Where(c => c.Nome.ToLower().Contains(nome.ToLower()))
                    .ToListAsync();

                if (!clientes.Any())
                {
                    return NoContent(); // Retorna 204 se não houver resultados
                }
                return Ok(clientes); // Retorna 200 com a lista de clientes
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Erro ao pesquisar clientes por nome: {ex.Message}");
                return StatusCode(500, "Erro interno do servidor ao pesquisar clientes por nome.");
            }
        }

        // ---------------------------------------------------------------------
        // Rotas POST (Criação de Dados)
        // ---------------------------------------------------------------------

        /// <summary>
        /// Cria um novo cliente.
        /// </summary>
        /// <remarks>
        /// Este endpoint permite criar um novo registro de cliente no banco de dados.
        /// Um ID de cliente não deve ser fornecido no corpo da requisição, pois é gerado automaticamente.
        /// </remarks>
        /// <param name="cliente">Os dados do cliente a serem criados.</param>
        /// <response code="201">Cliente criado com sucesso.</response>
        /// <response code="400">Dados do cliente inválidos ou já existe um cliente com o mesmo CPF.</response>
        /// <response code="500">Ocorreu um erro interno no servidor.</response>
        [HttpPost]
        [ProducesResponseType(typeof(Cliente), 201)]
        [ProducesResponseType(400)]
        [ProducesResponseType(500)]
        public async Task<ActionResult<Cliente>> CreateCliente([FromBody] Cliente cliente)
        {
            // Validação de modelo automática via [ApiController] e DataAnnotations
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState); // Retorna 400 com detalhes da validação
            }

            try
            {
                // A validação de CPF no setter da model já deve tratar isso,
                // mas é bom ter uma verificação de duplicidade no DB aqui.
                var existingCliente = await _context.Clientes.FirstOrDefaultAsync(c => c.Cpf == cliente.Cpf);
                if (existingCliente != null)
                {
                    return BadRequest("Já existe um cliente cadastrado com este CPF.");
                }

                // A DataCadastro é definida no construtor padrão do model, mas se for recebida via POST,
                // garantimos que é definida no servidor ou usada do banco.
                if (cliente.DataCadastro == DateTime.MinValue) // Se não foi definida no cliente recebido
                {
                    cliente.DataCadastro = DateTime.Now;
                }

                _context.Clientes.Add(cliente);
                await _context.SaveChangesAsync();

                // Retorna 201 Created, incluindo o novo recurso e sua URI de acesso
                return CreatedAtAction(nameof(GetClienteById), new { id = cliente.IdCliente }, cliente);
            }
            catch (ArgumentException ex) // Captura exceções da validação customizada no model
            {
                return BadRequest(ex.Message);
            }
            catch (DbUpdateException ex) // Captura erros de banco de dados (ex: violação de constraint única)
            {
                // Pode inspecionar ex.InnerException para detalhes específicos do DB
                Console.WriteLine($"Erro de banco de dados ao criar cliente: {ex.Message}");
                return StatusCode(500, "Erro ao persistir o cliente no banco de dados.");
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Erro inesperado ao criar cliente: {ex.Message}");
                return StatusCode(500, "Erro interno do servidor ao criar cliente.");
            }
        }

        // ---------------------------------------------------------------------
        // Rotas PUT (Atualização de Dados)
        // ---------------------------------------------------------------------

        /// <summary>
        /// Atualiza um cliente existente pelo ID.
        /// </summary>
        /// <remarks>
        /// Este endpoint permite a atualização completa de um cliente existente.
        /// O ID na URL deve corresponder ao ID do cliente no corpo da requisição.
        /// </remarks>
        /// <param name="id">O ID do cliente a ser atualizado.</param>
        /// <param name="cliente">Os dados do cliente atualizados.</param>
        /// <response code="204">Cliente atualizado com sucesso.</response>
        /// <response code="400">ID na URL não corresponde ao ID do cliente no corpo da requisição, ou dados inválidos.</response>
        /// <response code="404">Cliente não encontrado.</response>
        /// <response code="500">Ocorreu um erro interno no servidor.</response>
        [HttpPut("{id:int}")]
        [ProducesResponseType(204)]
        [ProducesResponseType(400)]
        [ProducesResponseType(404)]
        [ProducesResponseType(500)]
        public async Task<ActionResult> UpdateCliente(int id, [FromBody] Cliente cliente)
        {
            if (id != cliente.IdCliente)
            {
                return BadRequest("O ID na URL não corresponde ao ID do cliente fornecido.");
            }

            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            try
            {
                // Verifica se o cliente existe antes de tentar atualizar
                var existingCliente = await _context.Clientes.AsNoTracking().FirstOrDefaultAsync(c => c.IdCliente == id);
                if (existingCliente == null)
                {
                    return NotFound("Cliente não encontrado para atualização.");
                }

                // Verifica duplicidade de CPF se ele foi alterado
                if (existingCliente.Cpf != cliente.Cpf)
                {
                    var clienteWithSameCpf = await _context.Clientes.AsNoTracking().FirstOrDefaultAsync(c => c.Cpf == cliente.Cpf);
                    if (clienteWithSameCpf != null && clienteWithSameCpf.IdCliente != id)
                    {
                        return BadRequest("Já existe outro cliente cadastrado com este CPF.");
                    }
                }

                // Anexa a entidade ao contexto no estado Modified
                _context.Entry(cliente).State = EntityState.Modified;
                await _context.SaveChangesAsync();

                return NoContent(); // Retorna 204
            }
            catch (ArgumentException ex) // Captura exceções da validação customizada no model
            {
                return BadRequest(ex.Message);
            }
            catch (DbUpdateConcurrencyException) // Erro de concorrência (ex: cliente excluído por outra transação)
            {
                if (!await _context.Clientes.AnyAsync(e => e.IdCliente == id))
                {
                    return NotFound("Cliente não encontrado para atualização (possivelmente foi excluído por outro processo).");
                }
                throw; // Lança o erro de concorrência para ser tratado em um nível superior
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Erro inesperado ao atualizar cliente: {ex.Message}");
                return StatusCode(500, "Erro interno do servidor ao atualizar cliente.");
            }
        }

        // ---------------------------------------------------------------------
        // Rotas DELETE (Exclusão de Dados)
        // ---------------------------------------------------------------------

        /// <summary>
        /// Exclui um cliente pelo ID.
        /// </summary>
        /// <remarks>
        /// Este endpoint remove permanentemente um registro de cliente do banco de dados.
        /// </remarks>
        /// <param name="id">O ID do cliente a ser excluído.</param>
        /// <response code="204">Cliente excluído com sucesso.</response>
        /// <response code="404">Cliente não encontrado.</response>
        /// <response code="500">Ocorreu um erro interno no servidor.</response>
        [HttpDelete("{id:int}")]
        [ProducesResponseType(204)]
        [ProducesResponseType(404)]
        [ProducesResponseType(500)]
        public async Task<ActionResult> DeleteCliente(int id)
        {
            try
            {
                var cliente = await _context.Clientes.FindAsync(id);
                if (cliente == null)
                {
                    return NotFound("Cliente não encontrado para exclusão."); // Retorna 404
                }

                _context.Clientes.Remove(cliente);
                await _context.SaveChangesAsync();

                return NoContent(); // Retorna 204
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Erro ao excluir cliente: {ex.Message}");
                return StatusCode(500, "Erro interno do servidor ao excluir cliente.");
            }
        }
    }
}