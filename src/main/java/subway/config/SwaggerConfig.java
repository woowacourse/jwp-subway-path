package subway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi stationApi() {
        return GroupedOpenApi.builder()
                .group("STATION")
                .pathsToMatch("/stations/**")
                .build();
    }

    @Bean
    public GroupedOpenApi linePropertyApi() {
        return GroupedOpenApi.builder()
                .group("LINE PROPERTY")
                .pathsToMatch("/lines/**")
                .build();
    }

    @Bean
    public GroupedOpenApi lineApi() {
        return GroupedOpenApi.builder()
                .group("LINE")
                .pathsToMatch("/subway/**")
                .build();
    }

    @Bean
    public GroupedOpenApi journeyApi() {
        return GroupedOpenApi.builder()
                .group("JOURNEY")
                .pathsToMatch("/journey/**")
                .build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("subway-path API")
                        .description("지하철 노선도 미션 API입니다."));
    }
}
