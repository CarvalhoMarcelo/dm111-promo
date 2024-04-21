package br.inatel.dm111promo.config;

import br.inatel.dm111promo.api.core.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class AppInterceptorRegistry implements WebMvcConfigurer {

    private static final List<String> ENDPOINTS_PATTERN = Arrays.asList(
            "/dm111/promo**",
            "/dm111/promo/**",
            "/dm111/promo/users**",
            "/dm111/promo/users/**"
    );

    private final AuthInterceptor authInterceptor;

    public AppInterceptorRegistry(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns(ENDPOINTS_PATTERN);
    }
}
