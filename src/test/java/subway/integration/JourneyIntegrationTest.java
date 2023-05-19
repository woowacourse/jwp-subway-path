package subway.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import subway.presentation.dto.JourneyRequest;
import subway.presentation.dto.JourneyResponse;
import subway.presentation.dto.LineRequest;
import subway.presentation.dto.StationEnrollRequest;
import subway.presentation.dto.StationRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("최단 경로 관련 기능")
public class JourneyIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("최단 경로를 찾을 수 있다")
    void test_shortestPath() {
        // given
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationRequest("잠실역"))

                .when().post("/stations");

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationRequest("방배역"))

                .when().post("/stations");

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationRequest("서초역"))

                .when().post("/stations");

        long blueLineId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LineRequest("1호선", "파랑"))

                .when().post("/lines")

                .then()
                .extract()
                .jsonPath()
                .getLong("id");

        long greenLineId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LineRequest("2호선", "초록"))

                .when().post("/lines")

                .then()
                .extract()
                .jsonPath()
                .getLong("id");

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationEnrollRequest(1L, 2L, 3))

                .when().post("/subway/{lineId}", blueLineId);

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationEnrollRequest(2L, 3L, 4))

                .when().post("/subway/{lineId}", greenLineId);

        // when
        JourneyResponse journeyResponse = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new JourneyRequest(1L, 3L))

                .when().post("/journey")

                .then()
                .extract()
                .body()
                .jsonPath()
                .getObject(".", JourneyResponse.class);

        // then
        assertThat(journeyResponse.getDistance()).isEqualTo(7);
        assertThat(journeyResponse.getPath()).hasSize(3);
        assertThat(journeyResponse.getFare()).isEqualTo(1_250);
    }
}
