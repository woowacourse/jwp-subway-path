package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.TestFixture.STATION_A;
import static subway.TestFixture.STATION_C;
import static subway.TestFixture.STATION_E;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.domain.Station;
import subway.dto.PathResponse;

public class SubwayIntegrationTest extends IntegrationTest {

    public static final List<Station> SHORTEST_PATH_STATIONS_IN_LINE_A_AND_B_STATION_A_TO_E = List.of(
            STATION_A, STATION_C, STATION_E
    );

    @DisplayName("최단거리를 조회한다")
    @Test
    void getLine() {
        var expectedIds = SHORTEST_PATH_STATIONS_IN_LINE_A_AND_B_STATION_A_TO_E.stream()
                .map(Station::getId)
                .collect(Collectors.toList());

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/subway/shortest-path?from={from}&to={to}", STATION_A.getId(), STATION_E.getId())
                .then().log().all()
                .extract();

        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(pathResponse.getStations()).extracting("id")
                .containsExactlyElementsOf(expectedIds);
        assertThat(pathResponse.getFare()).isEqualTo(1250);
        assertThat(pathResponse.getDistance()).isEqualTo(7);
    }
}
