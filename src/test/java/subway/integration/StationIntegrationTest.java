package subway.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.StationCreateRequest;

@DisplayName("지하철역 관련 기능")
public class StationIntegrationTest extends IntegrationTest {

    private StationCreateRequest stationCreateRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // 노선을 생성한다.
        LineRequest lineRequest = new LineRequest("2호선");
        RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        stationCreateRequest = new StationCreateRequest("잠실역", 1L);
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(stationCreateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header(HttpHeaders.LOCATION)).isEqualTo("/stations/1")
        );
    }
}
