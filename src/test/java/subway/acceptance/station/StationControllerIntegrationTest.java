package subway.acceptance.station;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.acceptance.common.LocationAsserter.location_헤더를_검증한다;
import static subway.acceptance.station.StationSteps.역_생성_요청;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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
        final ExtractableResponse<Response> response = 역_생성_요청(request);

        // then
        assertThat(response.statusCode()).isEqualTo(201);
        location_헤더를_검증한다(response);
    }
}
