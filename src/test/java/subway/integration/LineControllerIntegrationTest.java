package subway.integration;


import static groovy.json.JsonOutput.toJson;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static subway.integration.common.LocationAsserter.location_헤더를_검증한다;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import subway.application.LineService;
import subway.exception.NotFoundStationException;
import subway.presentation.request.LineCreateRequest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("LineController 통합테스트")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LineControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @MockBean
    private LineService lineService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Nested
    class 노선을_생성할_떄 {

        @Test
        void 존재하지_않은_역으로_생성하면_예외() {
            // given
            final LineCreateRequest request = new LineCreateRequest("1호선", "잠실역", "사당역", 10);
            final String body = toJson(request);
            given(lineService.create(any()))
                    .willThrow(new NotFoundStationException());

            // when
            final ExtractableResponse<Response> response = given().log().all()
                    .contentType(JSON)
                    .body(body)
                    .when()
                    .post("/lines")
                    .then()
                    .log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
        }

        @Test
        void 두_종착역이_존재하는_경우_생성된다() {
            // given
            final LineCreateRequest request = new LineCreateRequest("1호선", "잠실역", "사당역", 10);
            final String body = toJson(request);
            given(lineService.create(any())).willReturn(1L);

            // when
            final ExtractableResponse<Response> response = given().log().all()
                    .contentType(JSON)
                    .body(body)
                    .when()
                    .post("/lines")
                    .then()
                    .log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(201);
            location_헤더를_검증한다(response, 1L);
        }

        @Test
        void 역_사이의_거리가_양수가_아니면_오류이다() {
            // given
            final LineCreateRequest request = new LineCreateRequest("1호선", "잠실역", "사당역", 0);
            final String body = toJson(request);
            given(lineService.create(any()))
                    .willThrow(new IllegalArgumentException("역사이의 거리는 양수여야 합니다."));

            // when
            final ExtractableResponse<Response> response = given().log().all()
                    .contentType(JSON)
                    .body(body)
                    .when()
                    .post("/lines")
                    .then()
                    .log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
        }
    }
}
