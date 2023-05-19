package subway.integration;

import static org.assertj.core.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

@Sql("/initial-path.sql")
public class PathIntegrationTest extends IntegrationTest {
    @Test
    @DisplayName("두 역 사이 최단 경로를 조회한다.")
    void testGetShortestPath() {
        //given
        Long departureStationId = 1L;
        Long arrivalStationId = 2L;

        //when
        ExtractableResponse<Response> response = RestAssured
                .given()
                .queryParam("departureStationId", departureStationId)
                .queryParam("arrivalStationId", arrivalStationId)
                .get("/path")
                .then()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getLong("departureStation.id")).isEqualTo(departureStationId);
        assertThat(response.jsonPath().getLong("arrivalStation.id")).isEqualTo(arrivalStationId);
        assertThat(response.jsonPath().getBoolean("doesPathExists")).isTrue();
        assertThat(response.jsonPath().getList("path")).isNotEmpty();
        assertThat(response.jsonPath().getInt("totalDistance")).isNotZero();
        assertThat(response.jsonPath().getInt("fare")).isNotNull();
    }

    @Test
    @DisplayName("같은 역의 최단 경로를 조회한다.")
    void testGetShortestPathWithSameStationIds() {
        //given
        Long departureStationId = 1L;

        //when
        ExtractableResponse<Response> response = RestAssured
                .given()
                .queryParam("departureStationId", departureStationId)
                .queryParam("arrivalStationId", departureStationId)
                .get("/path")
                .then()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(400);
    }

    @Test
    @DisplayName("경로가 존재하지 않는 역들의 경로를 조회한다.")
    void testGetShortestPathWithNoPathToArrivalStation() {
        //given
        Long departureStationId = 1L;
        Long arrivalStationId = 10L;

        //when
        ExtractableResponse<Response> response = RestAssured
                .given()
                .queryParam("departureStationId", departureStationId)
                .queryParam("arrivalStationId", arrivalStationId)
                .get("/path")
                .then().extract();

        //then
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getBoolean("doesPathExists")).isFalse();
    }
}
