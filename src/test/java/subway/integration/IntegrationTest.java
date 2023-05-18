package subway.integration;

import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@Sql({"classpath:schema.sql", "classpath:data.sql"})
@TestPropertySource(properties = "spring.config.location = classpath:application.yml")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 테스트_프로퍼티_설정(@Value("${spring.datasource.url}") String url) {
        Assertions.assertThat(url).isEqualTo("jdbc:h2:~/test;MODE=MySQL");
    }
}
