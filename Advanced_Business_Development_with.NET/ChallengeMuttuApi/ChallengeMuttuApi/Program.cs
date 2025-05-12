// Path: ChallengeMuttuApi/Program.cs

using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.EntityFrameworkCore;
using System;
using System.IO;
using System.Reflection;
using ChallengeMuttuApi.Data;
using Microsoft.OpenApi.Models;
using Microsoft.Extensions.Logging;

/// <summary>
/// Inicializa a aplicação Web e configura os serviços.
/// </summary>
var builder = WebApplication.CreateBuilder(args);

/// <summary>
/// Carrega a configuração do appsettings.json.
/// </summary>
var configuration = builder.Configuration;

/// <summary>
/// Define as URLs de execução da aplicação.
/// </summary>
builder.WebHost.UseUrls("http://localhost:5181", "https://localhost:7183");

/// <summary>
/// Configura o sistema de logging da aplicação.
/// </summary>
builder.Logging.ClearProviders();
builder.Logging.AddConsole();
builder.Logging.AddDebug();
builder.Logging.AddEventLog();
builder.Logging.AddEventSourceLogger();

/// <summary>
/// Verifica e cria a pasta 'wwwroot' se não existir.
/// </summary>
string wwwrootPath = Path.Combine(AppContext.BaseDirectory, "wwwroot");
if (!Directory.Exists(wwwrootPath))
{
    Directory.CreateDirectory(wwwrootPath);
    Console.WriteLine($"📂 Pasta 'wwwroot' criada em: {wwwrootPath}");
}
else
{
    Console.WriteLine($"📂 Pasta 'wwwroot' já existe em: {wwwrootPath}");
}

/// <summary>
/// Adiciona os serviços necessários ao container de injeção de dependência.
/// </summary>
builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();

/// <summary>
/// Configura o contexto do banco de dados Oracle.
/// </summary>
try
{
    builder.Services.AddDbContext<AppDbContext>(options =>
        options.UseOracle(configuration.GetConnectionString("OracleDb")));
}
catch (Exception ex)
{
    Console.WriteLine($"❌ Erro ao conectar ao banco de dados Oracle: {ex.Message}");
}

/// <summary>
/// Configura o Swagger para documentação da API.
/// </summary>
builder.Services.AddSwaggerGen(c =>
{
    /// <summary>
    /// Define informações básicas sobre a API.
    /// </summary>
    c.SwaggerDoc("v1", new OpenApiInfo
    {
        Title = "Challenge Muttu API",
        Version = "v1",
        Description = @"
            API RESTful para o Challenge Muttu - Gestão de Clientes, Veículos, Endereços, Contatos e mais.

            **Endereço do Projeto GitHub:** [https://github.com/carmipa/challenge_2025_1_semestre_mottu](https://github.com/carmipa/challenge_2025_1_semestre_mottu)
            
            **Turma:** 2TDSPV / 2TDSPZ
            
            **Contatos da Equipe:**
            - Arthur Bispo de Lima RM557568: [https://github.com/ArthurBispo00](https://github.com/ArthurBispo00)
            - João Paulo Moreira RM557808: [https://github.com/joao1015](https://github.com/joao1015)
            - Paulo André Carminati RM557881: [https://github.com/carmipa](https://github.com/carmipa)
        ",
        Contact = new OpenApiContact
        {
            Name = "Equipe Metamind Solution",
            Email = "RM557881@fiap;RM557568@fiap.com.br;RM557808@fiap.com.br",
            Url = new Uri("https://github.com/carmipa/challenge_2025_1_semestre_mottu")
        },

        License = new OpenApiLicense
        {
            Name = "Licença de Exemplo",
            Url = new Uri("https://github.com/carmipa/challenge_2025_1_semestre_mottu/tree/main/Advanced_Business_Development_with.NET")
        }
    });

    /// <summary>
    /// Inclui o arquivo XML de documentação, se existente.
    /// </summary>
    var xmlFile = $"{Assembly.GetExecutingAssembly().GetName().Name}.xml";
    var xmlPath = Path.Combine(AppContext.BaseDirectory, xmlFile);

    if (File.Exists(xmlPath))
    {
        c.IncludeXmlComments(xmlPath);
    }
    else
    {
        Console.WriteLine($"⚠️ Arquivo de documentação XML não encontrado: {xmlPath}");
    }
});

/// <summary>
/// Cria a aplicação Web.
/// </summary>
var app = builder.Build();

/// <summary>
/// Configura o pipeline de requisições HTTP.
/// </summary>
if (app.Environment.IsDevelopment() || app.Environment.IsStaging() || app.Environment.IsProduction())
{
    /// <summary>
    /// Habilita o Swagger e a interface Swagger UI.
    /// </summary>
    app.UseSwagger();
    app.UseSwaggerUI(c =>
    {
        c.SwaggerEndpoint("/swagger/v1/swagger.json", "Challenge Muttu API v1");
        c.RoutePrefix = "swagger";  // Define o caminho Swagger em /swagger
    });
}

/// <summary>
/// Redireciona requisições HTTP para HTTPS.
/// </summary>
app.UseHttpsRedirection();

/// <summary>
/// Serve arquivos estáticos a partir da pasta 'wwwroot'.
/// </summary>
app.UseStaticFiles();

/// <summary>
/// Habilita o middleware de autorização.
/// </summary>
app.UseAuthorization();

/// <summary>
/// Mapeia os controllers para as rotas configuradas.
/// </summary>
app.MapControllers();

/// <summary>
/// Middleware global para tratamento de exceções.
/// </summary>
app.Use(async (context, next) =>
{
    try
    {
        await next.Invoke();
    }
    catch (Exception ex)
    {
        Console.WriteLine($"❌ Erro durante a requisição: {ex.Message}");
        context.Response.StatusCode = 500;
        await context.Response.WriteAsync("Ocorreu um erro interno no servidor.");
    }
});

/// <summary>
/// Inicia a aplicação Web.
/// </summary>
try
{
    app.Run();
}
catch (Exception ex)
{
    Console.WriteLine($"❌ Erro crítico durante a inicialização da aplicação: {ex.Message}");
    throw;
}
