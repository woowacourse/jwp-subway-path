package subway.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.StationToLineRequest;


@DisplayName("역 경로 관련 기능")
public class RoutingIntegrationTest extends IntegrationTest {

    private Long lineId;

    @BeforeEach
    public void setUp() {
        super.setUp();
        LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600");

        ExtractableResponse<Response> createLineResponse = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().extract();

        lineId = Long.parseLong(createLineResponse.header("Location").split("/")[2]);

        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        ExtractableResponse<Response> createStationResponse1 = RestAssured.given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().extract();
        Long stationId1 = Long.parseLong(createStationResponse1.header("Location").split("/")[2]);

        params.put("name", "삼성역");

        ExtractableResponse<Response> createStationResponse2 = RestAssured.given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().extract();
        Long stationId2 = Long.parseLong(createStationResponse2.header("Location").split("/")[2]);

        params.put("name", "선릉역");

        ExtractableResponse<Response> createStationResponse3 = RestAssured.given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .extract();
        Long stationId3 = Long.parseLong(createStationResponse3.header("Location").split("/")[2]);

        params.put("name", "잠실역");

        ExtractableResponse<Response> createStationResponse4 = RestAssured.given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .extract();
        Long stationId4 = Long.parseLong(createStationResponse4.header("Location").split("/")[2]);

        StationToLineRequest sectionReq1 = new StationToLineRequest(stationId1, stationId2, 4);
        StationToLineRequest sectionReq2 = new StationToLineRequest(stationId2, stationId3, 4);
        StationToLineRequest sectionReq3 = new StationToLineRequest(stationId3, stationId4, 4);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionReq1)
                .when().post("lines/{lineId}", lineId);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionReq2)
                .when().post("lines/{lineId}", lineId);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionReq3)
                .when().post("lines/{lineId}", lineId);
    }

    @AfterEach
    void cleanup() {
        RestAssured
                .given()
                .when().delete("lines/{id}/stations", lineId);
    }

    @DisplayName("호선에 해당하는 역들을 순서대로 반환한다.")
    @Test
    void RoutingIntegrationTest() {
        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("lines/{id}/stations", lineId)
                .then().log().all()
                .extract();
        //then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        );

    }
}
