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
using ChallengeMuttuApi.Data; // Seu namespace para AppDbContext
using Microsoft.OpenApi.Models;
using Microsoft.Extensions.Logging;
using Microsoft.AspNetCore.HttpLogging; // Necessário para HttpLogging

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
builder.Logging.AddConsole(); // Loga no console
builder.Logging.AddDebug();
// builder.Logging.AddEventLog(); // Removido: Só há suporte para 'windows'
builder.Logging.AddEventSourceLogger();

// Adicionando HTTP Logging para detalhes da requisição e resposta
// Isso pode ser bastante verboso, então use com cautela em produção.
builder.Services.AddHttpLogging(logging =>
{
    logging.LoggingFields = HttpLoggingFields.All; // Loga tudo: Request, Response, Headers, Body etc.
    // Para produção, você pode querer ser mais seletivo:
    // logging.LoggingFields = HttpLoggingFields.RequestPropertiesAndHeaders |
    //                         HttpLoggingFields.ResponsePropertiesAndHeaders;
    logging.RequestBodyLogLimit = 4096; // Limite para o tamanho do corpo da requisição logado
    logging.ResponseBodyLogLimit = 4096; // Limite para o tamanho do corpo da resposta logado
});


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

builder.Services.AddCors(options =>
{
    options.AddDefaultPolicy(policy => // Você pode nomear esta política, ex: "AllowSpecificOrigins"
    {
        // Para desenvolvimento, seja mais específico ou use uma política nomeada mais aberta.
        // A URL que o Swagger UI está usando (http://localhost:5181) deve estar aqui.
        policy.WithOrigins("http://localhost:5181", "https://localhost:7183", "http://localhost:3000")
              .AllowAnyMethod()
              .AllowAnyHeader();
    });
});

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
    builder.Services.AddDbContext<AppDbContext>(options => // Certifique-se que AppDbContext é o nome correto
        options.UseOracle(configuration.GetConnectionString("OracleDb"))
               .LogTo(Console.WriteLine, LogLevel.Information) // Log de EF Core para o console
               .EnableSensitiveDataLogging() // Apenas para desenvolvimento, mostra valores de parâmetros
    );
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
            Email = "RM557881@fiap.com.br;RM557568@fiap.com.br;RM557808@fiap.com.br", // Separado por ;
            Url = new Uri("https://github.com/carmipa/challenge_2025_1_semestre_mottu")
        },
        License = new OpenApiLicense
        {
            Name = "Licença de Exemplo",
            Url = new Uri("https://github.com/carmipa/challenge_2025_1_semestre_mottu/tree/main/Advanced_Business_Development_with.NET")
        }
    });

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

// Middleware para logar informações de CORS
app.Use(async (context, next) =>
{
    var logger = context.RequestServices.GetRequiredService<ILogger<Program>>(); // Obter ILogger
    logger.LogInformation("===================================================");
    logger.LogInformation("CORS DEBUG: Entrando no middleware de log CORS.");
    logger.LogInformation("CORS DEBUG: Requisição: {Method} {Path}", context.Request.Method, context.Request.Path);
    logger.LogInformation("CORS DEBUG: Origem da Requisição: {Origin}", context.Request.Headers["Origin"]);

    // Para ver os cabeçalhos da resposta CORS, precisamos adicionar um hook após o CORS middleware ter rodado.
    context.Response.OnStarting(() => {
        logger.LogInformation("CORS DEBUG: Cabeçalhos da Resposta sendo enviados:");
        foreach (var header in context.Response.Headers)
        {
            // Log específico para cabeçalhos CORS
            if (header.Key.StartsWith("Access-Control-", StringComparison.OrdinalIgnoreCase) ||
                header.Key.Equals("Vary", StringComparison.OrdinalIgnoreCase))
            {
                logger.LogInformation("CORS DEBUG: Response Header: {Key}: {Value}", header.Key, header.Value);
            }
        }
        logger.LogInformation("===================================================");
        return Task.CompletedTask;
    });

    await next.Invoke();
});


// Habilitar HTTP Logging (deve vir cedo no pipeline)
app.UseHttpLogging();


/// <summary>
/// Configura o pipeline de requisições HTTP.
/// </summary>
if (app.Environment.IsDevelopment() || app.Environment.IsStaging() || app.Environment.IsProduction())
{
    app.UseSwagger();
    app.UseSwaggerUI(c =>
    {
        c.SwaggerEndpoint("/swagger/v1/swagger.json", "Challenge Muttu API v1");
        c.RoutePrefix = "swagger";
    });
}

// ATENÇÃO: A ORDEM DOS MIDDLEWARES IMPORTA!
// app.UseHttpsRedirection(); // Considere comentar para teste se o Swagger estiver em HTTP

app.UseStaticFiles();

app.UseRouting(); // UseRouting antes de UseCors e UseAuthorization

app.UseCors(); // Aplicar a política CORS

app.UseAuthorization();

app.MapControllers();

/// <summary>
/// Middleware global para tratamento de exceções (deve ser um dos últimos).
/// </summary>
app.Use(async (context, next) =>
{
    try
    {
        await next.Invoke();
    }
    catch (Exception ex)
    {
        var logger = context.RequestServices.GetRequiredService<ILogger<Program>>();
        logger.LogError(ex, "❌ Erro não tratado durante a requisição: {ErrorMessage}", ex.Message);

        // Evita escrever na resposta se ela já foi iniciada (ex: por um controller)
        if (!context.Response.HasStarted)
        {
            context.Response.StatusCode = 500;
            await context.Response.WriteAsync("Ocorreu um erro interno no servidor. Verifique os logs para mais detalhes.");
        }
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
    // Usar o logger do builder se o app.Logger não estiver disponível
    var logger = app.Services.GetService<ILogger<Program>>() ?? builder.Logging.Services.BuildServiceProvider().GetService<ILogger<Program>>();
    logger?.LogError(ex, "❌ Erro crítico durante a inicialização da aplicação: {ErrorMessage}", ex.Message);
    throw;
}