package subway.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.request.PathRequest;
import subway.dto.response.PathResponse;
import subway.dto.response.StationResponse;

@Sql("/integration-data.sql")
class PathControllerTest extends IntegrationTest {

    @Test
    void 상행_출발역과_하행_도착역을_받아_경로와_총_거리_및_요금을_반환한다() {
        final PathRequest pathRequest = new PathRequest(1L, 3L);

        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pathRequest)
                .when().post("/path")
                .then().log().all().extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getObject(".", PathResponse.class))
                        .usingRecursiveComparison()
                        .isEqualTo(new PathResponse(
                                List.of(
                                        new StationResponse(1L, "종합운동장"),
                                        new StationResponse(2L, "잠실새내"),
                                        new StationResponse(3L, "잠실")
                                ), 1350, 10
                        ))
        );
    }

    @Test
    void 하행_출발역과_상행_도착역을_받아_경로와_총_거리_및_요금을_반환한다() {
        final PathRequest pathRequest = new PathRequest(3L, 1L);

        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pathRequest)
                .when().post("/path")
                .then().log().all().extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getObject(".", PathResponse.class))
                        .usingRecursiveComparison()
                        .isEqualTo(new PathResponse(
                                List.of(
                                        new StationResponse(3L, "잠실"),
                                        new StationResponse(2L, "잠실새내"),
                                        new StationResponse(1L, "종합운동장")
                                ), 1350, 10
                        ))
        );
    }

    @Test
    void 환승역을_포함한_경로와_총_거리_및_금액을_반환한다() {
        final PathRequest pathRequest = new PathRequest(4L, 3L);

        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pathRequest)
                .when().post("/path")
                .then().log().all().extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getObject(".", PathResponse.class))
                        .usingRecursiveComparison()
                        .isEqualTo(new PathResponse(
                                List.of(
                                        new StationResponse(4L, "강남"),
                                        new StationResponse(2L, "잠실새내"),
                                        new StationResponse(3L, "잠실")
                                ), 1350, 10
                        ))
        );
    }

    @Test
    void 전체_경로상_요청한_역이_없다면_400_Bad_Request_를_반환한다() {
        final PathRequest pathRequest = new PathRequest(3L, 7L);

        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pathRequest)
                .when().post("/path")
                .then().log().all().extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.jsonPath().getString("message")).isEqualTo("전체 경로상 없는 역이 있습니다.")
        );
    }

    @Test
    void 경로가_만들어질_수_없다면_400_Bad_Request_를_반환한다() {
        final PathRequest pathRequest = new PathRequest(3L, 5L);

        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pathRequest)
                .when().post("/path")
                .then().log().all().extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.jsonPath().getString("message")).isEqualTo("두 역은 이어져 있지 않습니다.")
        );
    }
}
