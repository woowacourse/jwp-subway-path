package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineCreateRequest;
import subway.dto.StationAddRequest;
import subway.dto.StationResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationIntegrationTest extends IntegrationTest {

    private LineCreateRequest lineCreateRequest;

    private void createLine() {
        lineCreateRequest = new LineCreateRequest(
                "2호선", "잠실역", "잠실나루", 5);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineCreateRequest)
                .when().post("/lines")
                .then().log().all();
    }

    @DisplayName("지하철역을 추가한다.")
    @Test
    void addStation() {
        createLine();

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

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }
}