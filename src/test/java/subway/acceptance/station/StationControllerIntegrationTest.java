package subway.acceptance.station;

import static subway.acceptance.common.CommonSteps.요청_결과의_상태를_검증한다;
import static subway.acceptance.common.CommonSteps.정상_생성;
import static subway.acceptance.common.LocationAsserter.location_헤더를_검증한다;
import static subway.acceptance.station.StationSteps.역_생성_요청;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import subway.line.presentation.request.StationCreateRequest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("StationController 통합테스트")
@Sql("/truncate.sql")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StationControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 역을_생성한다() {
        // given
        final StationCreateRequest request = new StationCreateRequest("오리역");

        // when
        final var 역_생성_응답 = 역_생성_요청(request);

        // then
        요청_결과의_상태를_검증한다(역_생성_응답, 정상_생성);
        location_헤더를_검증한다(역_생성_응답);
    }
}
