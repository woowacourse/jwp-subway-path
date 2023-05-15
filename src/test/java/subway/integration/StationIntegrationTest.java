package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineCreateRequest;
import subway.dto.StationAddRequest;
import subway.dto.StationDeleteRequest;

import static org.assertj.core.api.Assertions.assertThat;

public class StationIntegrationTest extends IntegrationTest {

    private LineCreateRequest lineCreateRequest;

    private ExtractableResponse<Response> createLine() {
        lineCreateRequest = new LineCreateRequest(
                "2호선", "잠실역", "잠실나루", 5);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineCreateRequest)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    @DisplayName("지하철역을 추가한다.")
    @Test
    void addStationTest() {
        createLine();
        ExtractableResponse<Response> response = addStation();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private ExtractableResponse<Response> addStation() {
        StationAddRequest stationAddRequest = new StationAddRequest(
                "2호선",
                "잠실나루",
                "강변역",
                5);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(stationAddRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
        return response;
    }

    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        createLine();
        addStation();

        StationDeleteRequest stationDeleteRequest = new StationDeleteRequest(
                "2호선", "강변역");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(stationDeleteRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations/station")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

}