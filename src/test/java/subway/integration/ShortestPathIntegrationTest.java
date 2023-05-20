package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.SectionCreateRequest;
import subway.dto.ShortestPathResponse;

public class ShortestPathIntegrationTest extends IntegrationTest {

    @DisplayName("지하철 노선에서 시작과 도착역의 최단 경로, 최단 거리, 요금을 조회한다.")
    @Test
    void findShortestPath_firstCase() {
        //given
        final Long 첫번째노선_Id = createLine("첫번째노선", "빨강", 200);
        final Long 두번째노선_Id = createLine("두번째노선", "파랑", 300);
        addSection(첫번째노선_Id, "첫번째역", "두번째역", "하행", 4);
        addSection(첫번째노선_Id, "두번째역", "세번째역", "하행", 6);
        addSection(두번째노선_Id, "첫번째역", "세번째역", "하행", 9);

        //when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when()
            .get("/shortest-path?start=첫번째역&end=세번째역&age=18")
            .then()
            .extract();

        //then
        final ShortestPathResponse shortestPathResponse = response.body().as(ShortestPathResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(shortestPathResponse.getTotalDistance()).isEqualTo(9);
        assertThat(shortestPathResponse.getTotalFare()).isEqualTo(1310);
    }

    @DisplayName("지하철 노선에서 시작과 도착역의 최단 경로, 최단 거리, 요금을 조회한다.")
    @Test
    void findShortestPath_secondCase() {
        //given
        final Long 첫번째노선_Id = createLine("첫번째노선", "빨강", 200);
        final Long 두번째노선_Id = createLine("두번째노선", "파랑", 300);
        addSection(첫번째노선_Id, "첫번째역", "두번째역", "하행", 4);
        addSection(첫번째노선_Id, "두번째역", "세번째역", "하행", 6);
        addSection(두번째노선_Id, "첫번째역", "세번째역", "하행", 9);

        //when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when()
            .get("/shortest-path?start=첫번째역&end=첫번째역&age=6")
            .then()
            .extract();

        //then
        final ShortestPathResponse shortestPathResponse = response.body().as(ShortestPathResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(shortestPathResponse.getTotalDistance()).isEqualTo(0);
        assertThat(shortestPathResponse.getTotalFare()).isEqualTo(0);
    }

    private Long createLine(final String lineName, final String color, final int additionalFare) {
        final LineRequest lineRequest = new LineRequest(lineName, color, additionalFare);

        final ExtractableResponse<Response> extract = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(lineRequest)
            .when().post("/lines")
            .then().log().all()
            .extract();

        final String location = extract.header("Location");
        return Long.parseLong(location.split("/", -1)[2]);
    }

    private void addSection(final Long lineId, final String baseStation, final String newStation,
        final String direction, final int distance) {
        final SectionCreateRequest firstRequest = new SectionCreateRequest(baseStation, newStation, direction,
            distance);

        RestAssured.given().log().all()
            .body(firstRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/" + lineId + "/sections")
            .then().log().all()
            .extract();
    }
}
