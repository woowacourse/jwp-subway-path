package subway.integration;


import static groovy.json.JsonOutput.toJson;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

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
import subway.domain.Direction;
import subway.exception.NotFoundStationException;
import subway.presentation.request.AddStationToLineRequest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("LineStationController 통합테스트")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LineStationControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @MockBean
    private LineService lineService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Nested
    class 노선에_역을_추가할_떄 {

        private final AddStationToLineRequest request = new AddStationToLineRequest(
                "2호선",
                "말랑역",
                Direction.UP,
                "오리역",
                5);

        @Test
        void 기준역에_대해_요청한_방향으로_추가된다() {
            // given
            final String body = toJson(request);

            // when
            final ExtractableResponse<Response> response = given().log().all()
                    .contentType(JSON)
                    .body(body)
                    .when()
                    .post("/lines/stations")
                    .then()
                    .log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(OK.value());
        }

        @Test
        void 중간_삽입시_기존_역간_거리가_재조정_된다() {
            // given

            // when
            //???? 조회기능 만들어서 위 테스트랑 합쳐도 될듯

            // then
        }

        @Test
        void 중간_삽입시_추가할려는_역간_거리가_기존_역간_길이와_같거나_크다면_예외() {
            // given
            willThrow(new IllegalArgumentException("추가하려는 역간 거리가 기존 역간 길이보다 같거나 큽니다."))
                    .given(lineService)
                    .addStation(any());
            final String body = toJson(request);

            // when
            final ExtractableResponse<Response> response = given().log().all()
                    .contentType(JSON)
                    .body(body)
                    .when()
                    .post("/lines/stations")
                    .then()
                    .log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
        }

        @Test
        void 역이_존재하지_않으면_예외() {
            // given
            willThrow(new NotFoundStationException())
                    .given(lineService)
                    .addStation(any());
            final String body = toJson(request);

            // when
            final ExtractableResponse<Response> response = given().log().all()
                    .contentType(JSON)
                    .body(body)
                    .when()
                    .post("/lines/stations")
                    .then()
                    .log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
        }

        @Test
        void 역간의_거리가_0이하이면_예외() {
            // given
            final AddStationToLineRequest request = new AddStationToLineRequest(
                    "2호선",
                    "말랑역",
                    Direction.UP,
                    "오리역",
                    0);
            willThrow(new IllegalArgumentException("역간 거리는 0보다 커야 합니다."))
                    .given(lineService)
                    .addStation(any());
            final String body = toJson(request);

            // when
            final ExtractableResponse<Response> response = given().log().all()
                    .contentType(JSON)
                    .body(body)
                    .when()
                    .post("/lines/stations")
                    .then()
                    .log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
        }
    }
}
