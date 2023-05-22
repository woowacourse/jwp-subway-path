package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.LineStationAddRequest;
import subway.dto.LineStationInitRequest;
import subway.dto.StationRequest;

@DisplayName("지하철역 관련 기능")
public class LineStationIntegrationTest extends IntegrationTest {

    private LineRequest lineRequest1;
    private LineStationInitRequest initialStationsRequest;
    private LineStationAddRequest additionalStationRequest;
    private StationRequest stationRequest1;
    private StationRequest stationRequest2;
    private StationRequest stationRequest3;


    @BeforeEach
    public void setUp() {
        super.setUp();

        lineRequest1 = new LineRequest("2호선", "bg-red-600");
        stationRequest1 = new StationRequest("강남역");
        stationRequest2 = new StationRequest("선릉역");
        stationRequest3 = new StationRequest("역삼역");

        initialStationsRequest = new LineStationInitRequest("강남역", "선릉역", 10);
        additionalStationRequest = new LineStationAddRequest("강남역", "역삼역", "UP", 5);
    }

    @DisplayName("초기 지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines");

        역_생성(stationRequest1);
        역_생성(stationRequest2);
        역_생성(stationRequest3);
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(initialStationsRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("lines/1/stations/init")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철역을 추가 생성한다.")
    @Test
    void createAdditionalStation() {
        // given
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines");

        역_생성(stationRequest1);
        역_생성(stationRequest2);
        역_생성(stationRequest3);

        RestAssured.given().log().all()
                .body(initialStationsRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/1/stations/init")
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(additionalStationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/1/stations")
                .then().log().all()
                .extract();

        // then
         assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines");

        역_생성(stationRequest1);
        역_생성(stationRequest2);
        역_생성(stationRequest3);

        RestAssured.given().log().all()
                .body(initialStationsRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("lines/1/stations/init");

        RestAssured.given().log().all()
                .body(additionalStationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/1/stations")
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .delete("/lines/1/stations/1")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 역_생성(StationRequest request) {
        RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all();

    }
}
