package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@DisplayName("지하철 경로 관련 기능 통합 테스트")
@Sql(scripts = {"classpath:data.sql", "classpath:data-sections.sql"})
public class RouteIntegrationTest extends IntegrationTest {

    @DisplayName("출발역, 도착역에 대한 최단 경로를 조회한다.")
    @Test
    void findShortestRoute() {
        // given, when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/routes?sourceStationId={sourceStationId}&targetStationId={targetStationId}",
                        Map.of("sourceStationId", 1L, "targetStationId", 9L))
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getList("stations.id"))
                .containsExactly(1, 2, 9);
        assertThat(response.body().jsonPath().getInt("totalDistance"))
                .isEqualTo(18);
        assertThat(response.body().jsonPath().getInt("totalFare"))
                .isEqualTo(1450);
    }

    @DisplayName("잘못된 요청 정보로 조회 시 404 상태코드와 적절한 에러 메시지를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"100:1:출발 역이 존재하지 않습니다.",
            "1:100:도착 역이 존재하지 않습니다.",
            "1:1:출발 역과 도착 역이 동일한 경로를 찾을 수 없습니다.",
            "100:100:출발 역이 존재하지 않습니다."}, delimiter = ':')
    void findShortestRouteBadRequest(final int sourceStationId,
                                     final int targetStationId,
                                     final String errorMessage) {
        // given, when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/routes?sourceStationId={sourceStationId}&targetStationId={targetStationId}",
                        Map.of("sourceStationId", sourceStationId, "targetStationId", targetStationId))
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().jsonPath().getString("message")).isEqualTo(errorMessage);
    }
}
