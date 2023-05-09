package subway.integration;


import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static subway.integration.common.JsonMapper.toJson;
import static subway.integration.common.LocationAsserter.location_헤더를_검증한다;

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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import subway.application.StationService;
import subway.presentation.request.StationCreateRequest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("StationController 통합테스트")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StationControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @MockBean
    private StationService stationService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 역을_생성한다() {
        // given
        final StationCreateRequest request = new StationCreateRequest("오리역");
        final String body = toJson(request);
        given(stationService.create(any())).willReturn(2L);

        // when
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(JSON)
                .body(body)
                .when()
                .post("/stations")
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(201);
        location_헤더를_검증한다(response, 2L);
    }
}
