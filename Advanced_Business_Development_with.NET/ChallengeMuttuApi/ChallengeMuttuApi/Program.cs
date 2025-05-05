using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.EntityFrameworkCore;
using System;
using ChallengeMuttuApi.Data;

var builder = WebApplication.CreateBuilder(args);

// 🔹 Carregar configuração do appsettings.json
var configuration = builder.Configuration;

// 🔹 Adicionar serviços ao container
builder.Services.AddControllers();

// 🔹 Configurar banco de dados Oracle
builder.Services.AddDbContext<AppDbContext>(options =>
    options.UseOracle(configuration.GetConnectionString("OracleDb")));

// 🔹 Configuração do Swagger
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

// 🔹 Configurar pipeline de requisição
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();
app.UseAuthorization();
app.MapControllers();

try
{
    // 🔥 Executando aplicação
    app.Run();
}
catch (Exception ex)
{
    Console.WriteLine($"❌ Erro crítico na inicialização: {ex.Message}");
}