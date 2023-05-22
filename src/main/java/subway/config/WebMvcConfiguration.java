package subway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import subway.presentation.LoggerInterceptor;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final LoggerInterceptor loggerInterceptor;

    public WebMvcConfiguration(LoggerInterceptor loggerInterceptor) {
        this.loggerInterceptor = loggerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggerInterceptor)
                .addPathPatterns("/**");
    }
}
