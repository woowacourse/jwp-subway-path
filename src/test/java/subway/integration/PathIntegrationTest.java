package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.request.PathRequest;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("최단 경로 조회 관련 기능")
class PathIntegrationTest extends IntegrationTest {

    @DisplayName("최단 경로 조회 시 최단 경로, 거리, 운임을 반환한다")
    @Test
    void findShortestPath() {
        // given
        PathRequest pathRequest = new PathRequest(7L, 3L);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pathRequest)
                .when().get("/path")
                .then().log().all()
                .extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.body()
                                         .jsonPath()
                                         .get("stations")
                                         .toString()).isEqualTo("[인천역, 방배역, 봉천역, 서울대입구역, 사당역, 낙성대역]"),
                () -> assertThat(response.body().jsonPath().get("distance").toString()).isEqualTo("30"),
                () -> assertThat(response.body().jsonPath().get("fare").toString()).isEqualTo("1650")

        );
    }

    @DisplayName("최단 경로 조회 시 잘못된 역을 요청한 경우 에외를 반환한다")
    @Test
    void findShortestPathException() {
        // given
        PathRequest pathRequest = new PathRequest(100L, 3L);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pathRequest)
                .when().get("/path")
                .then().log().all()
                .extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.jsonPath().get("messages").toString()).contains("해당 역을 찾을 수 없습니다.")
        );
    }
}
