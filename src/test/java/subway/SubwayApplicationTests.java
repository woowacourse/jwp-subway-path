package subway;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestPropertySource(properties = "spring.config.location = classpath:application.yml")
class SubwayApplicationTests {

    @Test
    void 테스트_프로퍼티_설정(@Value("${spring.datasource.url}") String url) {
        Assertions.assertThat(url).isEqualTo("jdbc:h2:~/test;MODE=MySQL");
    }
}
