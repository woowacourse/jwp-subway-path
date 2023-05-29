package subway.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

// 설정이 같으면 (컨텍스트가 오염되지 않으면) 캐싱된 컨텍스트를 그대로 사용하고
// 설정이 바뀐 경우 - webEnvironment 변경, MockBean 변경, Bean 변경 등 - 컨텍스트가 오염되었다 판단하고
// 어플리케이션 컨텍스트를 새로 만든다.
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }
}
