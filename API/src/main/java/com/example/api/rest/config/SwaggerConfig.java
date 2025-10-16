package com.example.api.rest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

@Bean
public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                    .title("API Gateway - Donaciones ONG")
                    .description("Documentación de los endpoints REST que se comunican con los microservicios RPC de donaciones y usuarios.")
                    .version("1.0.0")
                    .contact(new Contact()
                            .name("Equipo de Desarrollo Grupo B")
                            .email("soporte@ong.org")
                            .url("https://ong.org")));
}

}
