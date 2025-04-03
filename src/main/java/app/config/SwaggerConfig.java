package app.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Notification Microservice API")
                        .version("1.0.0")
                        .description("API documentation for Notification Microservice")
                        .contact(new Contact()
                                .name("Your Name")
                                .email("your.email@example.com")
                                .url("https://localhost:8083"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org"))
                );
    }

    @Bean
    public GroupedOpenApi notificationApi() {
        return GroupedOpenApi.builder()
                .group("notification-svc")
                .pathsToMatch("/api/**")
                .build();
    }
}
