package app.vrabia.vrcommon.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.http.HttpMethod.*;

@Configuration
@Slf4j
public class SecurityBeans {

    @Bean
    public FilterChainExceptionHandlerFilter filterChainExceptionHandlerFilter(HandlerExceptionResolver handlerExceptionResolver) {
        return new FilterChainExceptionHandlerFilter(handlerExceptionResolver);
    }

    @Bean
    public WebMvcConfigurer corsMappingConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:12420", "http://localhost:4200")
                        .allowedMethods(GET.name(), POST.name(), PATCH.name(), PUT.name(), DELETE.name(), OPTIONS.name(), HEAD.name())
                        .allowCredentials(true)
                        .exposedHeaders("Set-Cookie", "token");
            }
        };
    }

}
