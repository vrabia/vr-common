package app.vrabia.vrcommon.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class SecurityBeans {

    @Bean
    public FilterChainExceptionHandlerFilter filterChainExceptionHandlerFilter(HandlerExceptionResolver handlerExceptionResolver) {
        return new FilterChainExceptionHandlerFilter(handlerExceptionResolver);
    }
}
