package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.StationToLineRequest;

@DisplayName("노선에 역 관리 기능")
public class SectionIntegrationTest extends IntegrationTest{

    private Long lineId;
    private Long stationId1;
    private Long stationId2;

    @BeforeEach
    public void setUp() {
        super.setUp();
        LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600");

        ExtractableResponse<Response> createLineResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        lineId = Long.parseLong(createLineResponse.header("Location").split("/")[2]);

        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        ExtractableResponse<Response> createStationResponse1 = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
        stationId1 = Long.parseLong(createStationResponse1.header("Location").split("/")[2]);

        params.put("name", "삼성역");

        ExtractableResponse<Response> createStationResponse2 = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
        stationId2 = Long.parseLong(createStationResponse2.header("Location").split("/")[2]);
    }

    @DisplayName("새로운 지하철 노선에 2개의 역을 등록한다.")
    @Test
    void registerStation_success_init() {
        //given
        StationToLineRequest stationToLineRequest = new StationToLineRequest(stationId1, stationId2, 11);

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationToLineRequest)
                .when().post("lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("기존 지하철 노선 사이에 하나의 역을 등록한다.")
    @Test
    void registerStation_success_between() {
        //given
        StationToLineRequest initRequest = new StationToLineRequest(stationId1, stationId2, 11);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest)
                .when().post("lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        Map<String, String> params = new HashMap<>();
        params.put("name", "선릉역");

        ExtractableResponse<Response> createStationResponse1 = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
        Long newStationId = Long.parseLong(createStationResponse1.header("Location").split("/")[2]);

        StationToLineRequest request = new StationToLineRequest(stationId1, newStationId, 4);
        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("기존 지하철 노선 종점에 하나의 역을 등록한다.")
    @Test
    void registerStation_success_end() {
        //given
        StationToLineRequest initRequest = new StationToLineRequest(stationId1, stationId2, 11);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest)
                .when().post("lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        Map<String, String> params = new HashMap<>();
        params.put("name", "선릉역");

        ExtractableResponse<Response> createStationResponse1 = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
        Long newStationId = Long.parseLong(createStationResponse1.header("Location").split("/")[2]);

        StationToLineRequest request = new StationToLineRequest(stationId2, newStationId, 4);

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
