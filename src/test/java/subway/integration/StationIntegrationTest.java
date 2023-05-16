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

    @DisplayName("두개의 역이 남은 노선에서 지하철역을 삭제하면 노선이 삭제된다.")
    @Test
    void deleteStation2() {
        createLine();

        StationDeleteRequest stationDeleteRequest = new StationDeleteRequest(
                "2호선", "잠실역");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(stationDeleteRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations/station")
                .then().log().all()
                .extract();


        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("존재하지않는 지하철역을 삭제하면 NOT_FOUND를 응답한다.")
    @Test
    void deleteStationButNotFound() {
        createLine();
        addStation();

        StationDeleteRequest stationDeleteRequest = new StationDeleteRequest(
                "2호선", "없는역");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(stationDeleteRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations/station")
                .then().log().all()
                .extract();


        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }


    @Test
    @DisplayName("기존에 존재하는 지하철을 추가하는 경우 CONFILCT를 응답한다.")
    void addDuplicateStation() {
        createLine();
        addStation();
        StationAddRequest stationAddRequest = new StationAddRequest(
                "2호선",
                "강변역",
                "잠실나루",
                5);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(stationAddRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

}