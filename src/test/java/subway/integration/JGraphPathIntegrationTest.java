package subway.integration;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.SectionRequest;
import subway.dto.StationRequest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.fixture.IntegrationPostRequestFixture.*;
import static subway.fixture.StationFixture.*;

public class JGraphPathIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("경로와 거리, 요금이 응답으로 제대로 오는지 확인한다.")
    void findPathFareResponse() {
        // given
        addLineRequest(new LineRequest("1호선", "파랑"));
        addLineRequest(new LineRequest("2호선", "초록"));

        addStationRequest(new StationRequest(강남1.getName()));
        addStationRequest(new StationRequest(잠실2.getName()));
        addStationRequest(new StationRequest(선릉3.getName()));
        addStationRequest(new StationRequest(석촌4.getName()));

        addSectionRequest(new SectionRequest(2, 2, 1, "DOWN", 7));
        addSectionRequest(new SectionRequest(2, 3, 2, "DOWN", 10));
        addSectionRequest(new SectionRequest(1, 4, 1, "DOWN", 6));
        addSectionRequest(new SectionRequest(1, 3, 4, "DOWN", 9));

        // when
        Response response = given()
                .param("startStationId", 1)
                .param("endStationId", 3)
                .when()
                .get("/path")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        List<Station> shortestPath = response.jsonPath().getList("shortestPath", Station.class);
        int distance = response.jsonPath().getInt("distance");
        int fare = response.jsonPath().getInt("fare");

        // then
        assertThat(shortestPath.containsAll(List.of(강남1, 석촌4, 선릉3))).isTrue();
        assertThat(distance).isEqualTo(15);
        assertThat(fare).isEqualTo(1350);

    }

}
