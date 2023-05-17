package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class RouteIntegrationTest extends IntegrationTest {

    @DisplayName("출발역, 도착역에 대한 최단 경로를 조회한다.")
    @Test
    void findShortestRoute() {
        // given, when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/routes?sourceStationId={sourceStationId}&targetStationId={targetStationId}",
                        Map.of("sourceStationId", 1L, "targetStationId", 8L))
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
