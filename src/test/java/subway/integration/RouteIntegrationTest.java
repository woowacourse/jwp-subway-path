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
import org.springframework.test.context.jdbc.Sql;

// TODO 여러 통합 테스트에서 일관성 있게 더미 데이터를 다루도록 하기
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
}
