package br.com.fiap.mottu; // Este é o pacote correto

// import io.swagger.v3.oas.annotations.OpenAPIDefinition;
// import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate; // <-- Importar RestTemplate
import org.springframework.web.reactive.function.client.WebClient; // <-- Adicione esta importação AQUI!


@EnableJpaRepositories
@SpringBootApplication
@EnableCaching
// @OpenAPIDefinition(info = @Info(title="CHALLENGE-2025-FIAP-TEMMU-METAMIND SOLUTIONS-1.0", description = "CHALLENGE FIAP 2025", version = "v1"))
public class MottuApplication {

	public static void main(String[] args) {
		SpringApplication.run(MottuApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

	// Adicionado para suportar WebClient no EnderecoService
	@Bean // <-- Adicione este Bean AQUI!
	public WebClient.Builder webClientBuilder() {
		return WebClient.builder();
	}
}