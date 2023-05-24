package subway.integration;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("경로 조회 API를 테스트한다.")
public class PathIntegrationTest extends SubwayFixture {

    /**
     * when 석촌역에서 잠실역으로 조회를 요청하면
     * then 최단 경로, 거리, 요금을 반환한다.
     */
    @Test
    void 최단경로를_조회한다() {
        // when
        final ExtractableResponse<Response> response = given()
                .queryParams(Map.of("start", 석촌역, "end", 잠실새내역))
                .when()
                .get("/path")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        final JsonPath result = response.jsonPath();
        assertAll(
                () -> assertThat(result.getList("routes")).hasSize(3),
                () -> assertThat(result.getInt("distance")).isEqualTo(25),
                () -> assertThat(result.getInt("fare")).isEqualTo(1550)
        );
    }
}
