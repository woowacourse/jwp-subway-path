package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.StationDeleteRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.integration.Utils.*;
import static subway.integration.Utils.addStation;

public class StationIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("")
    void createStationTest() {
        ExtractableResponse<Response> response = createStation("a역");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }


    @DisplayName("노선에 초기 지하철역을 추가한다.")
    @Test
    void addStationTest() {

        createLine("2호선");

        createStation("잠실역");
        createStation("잠실새내역");
        ExtractableResponse<Response> response = addStation(
                "2호선",
                "잠실역",
                "잠실새내역",
                5);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("기존에 존재하는 지하철을 추가하는 경우 CONFILCT를 응답한다.")
    void addDuplicateStation() {
        initLine("2호선",
                "잠실역",
                "잠실새내역",
                5);
        createStation("강변역");
        addStation("2호선",
                "잠실역",
                "강변역",
                4);

        ExtractableResponse<Response> response = addStation(
                "2호선",
                "강변역",
                "잠실새내역",
                5);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        initLine("2호선",
                "잠실역",
                "잠실새내역",
                5);
        createStation("강변역");
        addStation("2호선",
                "잠실역",
                "강변역",
                4);

        StationDeleteRequest stationDeleteRequest = new StationDeleteRequest(
                "2호선",
                "강변역");
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
        initLine("2호선",
                "잠실역",
                "잠실새내역",
                5);

        StationDeleteRequest stationDeleteRequest = new StationDeleteRequest(
                "2호선",
                "잠실역");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(stationDeleteRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations/station")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("존재하지 않는 지하철역을 삭제하면 NOT_FOUND를 응답한다.")
    @Test
    void deleteStationButNotFound() {
        initLine("2호선",
                "잠실역",
                "잠실새내역",
                5);

        StationDeleteRequest stationDeleteRequest = new StationDeleteRequest(
                "2호선",
                "없는역");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(stationDeleteRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations/station")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

}