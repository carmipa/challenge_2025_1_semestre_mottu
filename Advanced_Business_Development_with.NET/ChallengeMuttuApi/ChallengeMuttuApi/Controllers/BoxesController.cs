// Path: ChallengeMuttuApi/Controllers/BoxesController.cs
using ChallengeMuttuApi.Data;
using ChallengeMuttuApi.Model;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ChallengeMuttuApi.Controllers
{
    /// <summary>
    /// Controller responsável por gerenciar as operações CRUD para a entidade Box.
    /// </summary>
    [ApiController]
    [Route("api/[controller]")] // Define a rota base para a controller (ex: /api/boxes)
    [Produces("application/json")]
    public class BoxesController : ControllerBase
    {
        private readonly AppDbContext _context;

        /// <summary>
        /// Construtor da BoxesController.
        /// </summary>
        /// <param name="context">O contexto do banco de dados da aplicação.</param>
        public BoxesController(AppDbContext context)
        {
            _context = context;
        }

        // ---------------------------------------------------------------------
        // Rotas GET (Recuperação de Dados)
        // ---------------------------------------------------------------------

        /// <summary>
        /// Retorna uma lista de todos os boxes cadastrados.
        /// </summary>
        /// <response code="200">Retorna a lista de boxes.</response>
        /// <response code="204">Não há boxes cadastrados.</response>
        /// <response code="500">Ocorreu um erro interno no servidor.</response>
        [HttpGet]
        [ProducesResponseType(typeof(IEnumerable<Box>), 200)]
        [ProducesResponseType(204)]
        [ProducesResponseType(500)]
        public async Task<ActionResult<IEnumerable<Box>>> GetAllBoxes()
        {
            try
            {
                var boxes = await _context.Boxes.ToListAsync();
                if (!boxes.Any())
                {
                    return NoContent();
                }
                return Ok(boxes);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Erro ao buscar todos os boxes: {ex.Message}");
                return StatusCode(500, "Erro interno do servidor ao buscar boxes.");
            }
        }

        /// <summary>
        /// Retorna um box específico pelo seu ID.
        /// </summary>
        /// <param name="id">O ID do box a ser buscado.</param>
        /// <response code="200">Retorna o box encontrado.</response>
        /// <response code="404">Box não encontrado.</response>
        /// <response code="500">Ocorreu um erro interno no servidor.</response>
        [HttpGet("{id:int}")]
        [ProducesResponseType(typeof(Box), 200)]
        [ProducesResponseType(404)]
        [ProducesResponseType(500)]
        public async Task<ActionResult<Box>> GetBoxById(int id)
        {
            try
            {
                var box = await _context.Boxes.FindAsync(id);
                if (box == null)
                {
                    return NotFound("Box não encontrado.");
                }
                return Ok(box);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Erro ao buscar box por ID: {ex.Message}");
                return StatusCode(500, "Erro interno do servidor ao buscar box.");
            }
        }

        /// <summary>
        /// Pesquisa boxes por parte do nome.
        /// </summary>
        /// <param name="nome">A string a ser pesquisada no nome dos boxes.</param>
        /// <response code="200">Retorna a lista de boxes que correspondem à pesquisa.</response>
        /// <response code="204">Nenhum box encontrado com o nome especificado.</response>
        /// <response code="400">O parâmetro de nome para pesquisa é obrigatório.</response>
        /// <response code="500">Ocorreu um erro interno no servidor.</response>
        [HttpGet("search-by-name")]
        [ProducesResponseType(typeof(IEnumerable<Box>), 200)]
        [ProducesResponseType(204)]
        [ProducesResponseType(400)]
        [ProducesResponseType(500)]
        public async Task<ActionResult<IEnumerable<Box>>> SearchBoxesByName([FromQuery] string nome)
        {
            if (string.IsNullOrWhiteSpace(nome))
            {
                return BadRequest("O parâmetro 'nome' para pesquisa é obrigatório.");
            }

            try
            {
                var boxes = await _context.Boxes
                    .Where(b => b.Nome.ToLower().Contains(nome.ToLower()))
                    .ToListAsync();

                if (!boxes.Any())
                {
                    return NoContent();
                }
                return Ok(boxes);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Erro ao pesquisar boxes por nome: {ex.Message}");
                return StatusCode(500, "Erro interno do servidor ao pesquisar boxes por nome.");
            }
        }

        /// <summary>
        /// Retorna boxes com base em seu status.
        /// </summary>
        /// <param name="status">O status do box ('A' para Ativo, 'I' para Inativo, etc.).</param>
        /// <response code="200">Retorna a lista de boxes com o status especificado.</response>
        /// <response code="204">Nenhum box encontrado com o status especificado.</response>
        /// <response code="400">O status fornecido é inválido.</response>
        /// <response code="500">Ocorreu um erro interno no servidor.</response>
        [HttpGet("by-status/{status}")]
        [ProducesResponseType(typeof(IEnumerable<Box>), 200)]
        [ProducesResponseType(204)]
        [ProducesResponseType(400)]
        [ProducesResponseType(500)]
        public async Task<ActionResult<IEnumerable<Box>>> GetBoxesByStatus(string status)
        {
            if (string.IsNullOrWhiteSpace(status) || status.Length != 1)
            {
                return BadRequest("Status inválido. Deve conter apenas um caractere.");
            }

            try
            {
                var boxes = await _context.Boxes
                    .Where(b => b.Status.ToLower() == status.ToLower())
                    .ToListAsync();

                if (!boxes.Any())
                {
                    return NoContent();
                }
                return Ok(boxes);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Erro ao buscar boxes por status: {ex.Message}");
                return StatusCode(500, "Erro interno do servidor ao buscar boxes por status.");
            }
        }

        // ---------------------------------------------------------------------
        // Rotas POST (Criação de Dados)
        // ---------------------------------------------------------------------

        /// <summary>
        /// Cria um novo box.
        /// </summary>
        /// <param name="box">Os dados do box a serem criados.</param>
        /// <response code="201">Box criado com sucesso.</response>
        /// <response code="400">Dados do box inválidos.</response>
        /// <response code="500">Ocorreu um erro interno no servidor.</response>
        [HttpPost]
        [ProducesResponseType(typeof(Box), 201)]
        [ProducesResponseType(400)]
        [ProducesResponseType(500)]
        public async Task<ActionResult<Box>> CreateBox([FromBody] Box box)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            try
            {
                _context.Boxes.Add(box);
                await _context.SaveChangesAsync();

                return CreatedAtAction(nameof(GetBoxById), new { id = box.IdBox }, box);
            }
            catch (DbUpdateException ex)
            {
                Console.WriteLine($"Erro de banco de dados ao criar box: {ex.Message}");
                return StatusCode(500, "Erro ao persistir o box no banco de dados.");
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Erro inesperado ao criar box: {ex.Message}");
                return StatusCode(500, "Erro interno do servidor ao criar box.");
            }
        }

        // ---------------------------------------------------------------------
        // Rotas PUT (Atualização de Dados)
        // ---------------------------------------------------------------------

        /// <summary>
        /// Atualiza um box existente pelo ID.
        /// </summary>
        /// <param name="id">O ID do box a ser atualizado.</param>
        /// <param name="box">Os dados do box atualizados.</param>
        /// <response code="204">Box atualizado com sucesso.</response>
        /// <response code="400">ID na URL não corresponde ao ID do box no corpo da requisição, ou dados inválidos.</response>
        /// <response code="404">Box não encontrado.</response>
        /// <response code="500">Ocorreu um erro interno no servidor.</response>
        [HttpPut("{id:int}")]
        [ProducesResponseType(204)]
        [ProducesResponseType(400)]
        [ProducesResponseType(404)]
        [ProducesResponseType(500)]
        public async Task<ActionResult> UpdateBox(int id, [FromBody] Box box)
        {
            if (id != box.IdBox)
            {
                return BadRequest("O ID na URL não corresponde ao ID do box fornecido.");
            }

            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            try
            {
                var existingBox = await _context.Boxes.AsNoTracking().FirstOrDefaultAsync(b => b.IdBox == id);
                if (existingBox == null)
                {
                    return NotFound("Box não encontrado para atualização.");
                }

                _context.Entry(box).State = EntityState.Modified;
                await _context.SaveChangesAsync();

                return NoContent();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!await _context.Boxes.AnyAsync(e => e.IdBox == id))
                {
                    return NotFound("Box não encontrado para atualização (possivelmente foi excluído por outro processo).");
                }
                throw;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Erro inesperado ao atualizar box: {ex.Message}");
                return StatusCode(500, "Erro interno do servidor ao atualizar box.");
            }
        }

        // ---------------------------------------------------------------------
        // Rotas DELETE (Exclusão de Dados)
        // ---------------------------------------------------------------------

        /// <summary>
        /// Exclui um box pelo ID.
        /// </summary>
        /// <param name="id">O ID do box a ser excluído.</param>
        /// <response code="204">Box excluído com sucesso.</response>
        /// <response code="404">Box não encontrado.</response>
        /// <response code="500">Ocorreu um erro interno no servidor.</response>
        [HttpDelete("{id:int}")]
        [ProducesResponseType(204)]
        [ProducesResponseType(404)]
        [ProducesResponseType(500)]
        public async Task<ActionResult> DeleteBox(int id)
        {
            try
            {
                var box = await _context.Boxes.FindAsync(id);
                if (box == null)
                {
                    return NotFound("Box não encontrado para exclusão.");
                }

                _context.Boxes.Remove(box);
                await _context.SaveChangesAsync();

                return NoContent();
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Erro ao excluir box: {ex.Message}");
                return StatusCode(500, "Erro interno do servidor ao excluir box.");
            }
        }
    }
}