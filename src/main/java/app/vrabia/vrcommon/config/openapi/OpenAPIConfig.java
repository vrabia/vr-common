package app.vrabia.vrcommon.config.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OpenAPIConfig {

    private final BuildProperties buildProperties;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Vrabia API")
                        .description("A collection of APIs for the interaction with Vrabia App")
                        .termsOfService("http://swagger.io/terms/")
                        .contact(new Contact().name("Lorena Arcalean").email("arcalean.lorena22@gmail.com"))
                        .version(buildProperties.getVersion())
                )
                .components(new Components()
                        .addSecuritySchemes("jwtAuthorization", new SecurityScheme()
                                .name("bearerAuth")
                                .description("Access Token")
                                .type(SecurityScheme.Type.HTTP)
                                .in(SecurityScheme.In.HEADER)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("jwtAuthorization"));
    }
}