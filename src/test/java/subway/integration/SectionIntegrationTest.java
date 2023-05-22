package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
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
import subway.dto.SectionResponse;
import subway.dto.StationToLineRequest;

@DisplayName("노선에 역 관리 기능 통합 테스트")
public class SectionIntegrationTest extends IntegrationTest {

    private Long lineId;
    private Long stationId1;
    private Long stationId2;

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
        stationId1 = Long.parseLong(createStationResponse1.header("Location").split("/")[2]);

        params.put("name", "삼성역");

        ExtractableResponse<Response> createStationResponse2 = RestAssured.given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().extract();
        stationId2 = Long.parseLong(createStationResponse2.header("Location").split("/")[2]);
    }

    @AfterEach
    void cleanup() {
        RestAssured
                .given()
                .when().delete("lines/{id}/stations", lineId);
    }

    @DisplayName("새로운 지하철 노선에 2개의 역을 등록한다.")
    @Test
    void registerStation_success_init() {
        //given
        int distance = 11;
        StationToLineRequest stationToLineRequest = new StationToLineRequest(stationId1, stationId2, distance);

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationToLineRequest)
                .when().post("lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        SectionResponse section = response.body().as(SectionResponse.class);
        //then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(section.getUpStationId()).isEqualTo(stationId1),
                () -> assertThat(section.getDownStationId()).isEqualTo(stationId2),
                () -> assertThat(section.getDistance()).isEqualTo(distance)
        );
    }

    @DisplayName("기존 지하철 노선 사이에 하나의 역을 등록한다.")
    @Test
    void registerStation_success_between() {
        //given
        StationToLineRequest initRequest = new StationToLineRequest(stationId1, stationId2, 11);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest)
                .when().post("lines/{lineId}", lineId);

        Map<String, String> params = new HashMap<>();
        params.put("name", "선릉역");

        ExtractableResponse<Response> createStationResponse1 = RestAssured.given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .extract();
        Long newStationId = Long.parseLong(createStationResponse1.header("Location").split("/")[2]);

        StationToLineRequest request = new StationToLineRequest(stationId1, newStationId, 4);
        //when
        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("lines/{lineId}", lineId)
                .then()
                .extract();

        //then
        SectionResponse section = response.body().as(SectionResponse.class);
        //then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(section.getUpStationId()).isEqualTo(stationId1),
                () -> assertThat(section.getDownStationId()).isEqualTo(newStationId),
                () -> assertThat(section.getDistance()).isEqualTo(4)
        );
    }

    @DisplayName("기존 지하철 노선 종점에 하나의 역을 등록한다.")
    @Test
    void registerStation_success_end() {
        //given
        StationToLineRequest initRequest = new StationToLineRequest(stationId1, stationId2, 11);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest)
                .when().post("lines/{lineId}", lineId);

        Map<String, String> params = new HashMap<>();
        params.put("name", "선릉역");

        ExtractableResponse<Response> createStationResponse1 = RestAssured.given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
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
        SectionResponse section = response.body().as(SectionResponse.class);
        //then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(section.getUpStationId()).isEqualTo(stationId2),
                () -> assertThat(section.getDownStationId()).isEqualTo(newStationId),
                () -> assertThat(section.getDistance()).isEqualTo(4)
        );
    }

    @DisplayName("노선에서 역 하나를 제거한다.")
    @Test
    void deleteSection_success() {
        //given
        StationToLineRequest initRequest = new StationToLineRequest(stationId1, stationId2, 11);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest)
                .when().post("lines/{lineId}", lineId);

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("lines/{lineId}/stations/{stationId}", lineId, stationId1)
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
