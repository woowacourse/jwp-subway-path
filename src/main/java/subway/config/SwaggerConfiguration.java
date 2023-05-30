package subway.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        final Info info = new Info()
                .title("지하철 API 문서")
                .version("v0.0.1")
                .description("지하철 API 명세서입니다.");
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
