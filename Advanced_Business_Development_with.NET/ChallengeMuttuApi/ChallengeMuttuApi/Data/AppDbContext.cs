using ChallengeMuttuApi.Model;
using Microsoft.EntityFrameworkCore;

namespace ChallengeMuttuApi.Data
{
    public class AppDbContext : DbContext
    {
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

        // 🔹 Adicione aqui as tabelas do banco como DbSet<>
        public DbSet<Cliente> Clientes { get; set; }
    }
}