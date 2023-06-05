package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import subway.domain.Station;
import subway.dto.ShortestPathRequest;
import subway.dto.ShortestPathResponse;

import java.util.List;

import static subway.integration.Utils.*;


public class PathIntegrationTest extends IntegrationTest {
    @Test
    @DisplayName("최단 경로를 구할 수 있다.")
    void findShortestPath() {
        initStations();
        initLine("2호선", "a역", "c역", 1);
        addStation("2호선", "c역", "b역", 2);
        addStation("2호선", "b역", "d역", 3);
        initLine("3호선", "c역", "e역", 1);
        addStation("3호선", "e역", "d역", 1);


        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(new ShortestPathRequest("c역", "d역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/paths/")
                .then().log().all().
                extract();


        ShortestPathResponse shortestPathResponse = response.as(ShortestPathResponse.class);


        Assertions.assertThat(shortestPathResponse.getDistance()).isEqualTo(2);
        Assertions.assertThat(shortestPathResponse.getFare()).isEqualTo(1250);
        Assertions.assertThat(shortestPathResponse.getPath()).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(
                        List.of(
                                new Station("c역"),
                                new Station("e역"),
                                new Station("d역")
                        ));
    }

    void initStations() {
        createStation("a역");
        createStation("b역");
        createStation("c역");
        createStation("d역");
        createStation("e역");
    }
}
