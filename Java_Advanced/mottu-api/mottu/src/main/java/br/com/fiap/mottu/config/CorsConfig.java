package br.com.fiap.mottu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica a todas as rotas da API
                .allowedOrigins(
                        "http://localhost:3000", // Exemplo: Frontend rodando localmente (React, Vue, Angular)
                        "https://seu-dominio-de-producao.com" // Exemplo: Seu domínio de frontend em produção
                        // Adicione outras origens permitidas aqui
                )
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS", "HEAD") // Métodos HTTP permitidos
                .allowedHeaders("*") // Quaisquer cabeçalhos são permitidos na requisição
                .allowCredentials(true); // Permite credenciais (cookies, authorization headers)
    }
}