package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.application.dto.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AcceptanceTest extends IntegrationTest {

    @DisplayName("전체 기능 인수 테스트 - 환승")
    @Test
    void acceptanceTest_oneWay() {
        // station 저장
        Map<String, String> stationParams1 = new HashMap<>();
        stationParams1.put("name", "강남역");

        ExtractableResponse<Response> response1 = RestAssured.given().log().all()
                .body(stationParams1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        StationResponse stationResponse1 = response1.as(StationResponse.class);

        // station 저장
        Map<String, String> stationParams2 = new HashMap<>();
        stationParams2.put("name", "양재역");

        ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .body(stationParams2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        StationResponse stationResponse2 = response2.as(StationResponse.class);


        // station 저장
        Map<String, String> stationParams3 = new HashMap<>();
        stationParams3.put("name", "신림역");

        ExtractableResponse<Response> response3 = RestAssured.given().log().all()
                .body(stationParams3)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        StationResponse stationResponse3 = response3.as(StationResponse.class);


        // line 저장
        LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600");

        ExtractableResponse<Response> response4 = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all()
                .extract();

        LineResponse lineResponse = response4.as(LineResponse.class);

        // Section 저장 (강남 - 양재, 신분당선, 7km)
        SectionRequest sectionRequest1 = new SectionRequest(lineResponse.getId(), new SectionStations(stationResponse1.getId(), stationResponse2.getId(), 7));

        RestAssured.given().log().all()
                .body(sectionRequest1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        // Section 저장 (신림 - 강남, 2호선, 15km)
        SectionRequest sectionRequest2 = new SectionRequest(2L, new SectionStations(stationResponse3.getId(), stationResponse1.getId(), 15));

        RestAssured.given().log().all()
                .body(sectionRequest2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/paths/" + stationResponse3.getId() + "/" + stationResponse2.getId())
                .then().log().all()
                .extract();

        PathsResponse expected = PathsResponse.of(List.of(stationResponse3, stationResponse1, stationResponse2), 22, 1550);

        PathsResponse pathsResponse = response.as(PathsResponse.class);
        System.out.println(pathsResponse);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(pathsResponse).isEqualTo(expected)
        );
    }

    @DisplayName("전체 기능 인수 테스트 - 가는 경로 2개 이상")
    @Test
    void acceptanceTest_multiWay() {
        // station 저장
        Map<String, String> stationParams1 = new HashMap<>();
        stationParams1.put("name", "강남역");

        ExtractableResponse<Response> response1 = RestAssured.given().log().all()
                .body(stationParams1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        StationResponse stationResponse1 = response1.as(StationResponse.class);

        // station 저장
        Map<String, String> stationParams2 = new HashMap<>();
        stationParams2.put("name", "양재역");

        ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .body(stationParams2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        StationResponse stationResponse2 = response2.as(StationResponse.class);


        // station 저장
        Map<String, String> stationParams3 = new HashMap<>();
        stationParams3.put("name", "신림역");

        ExtractableResponse<Response> response3 = RestAssured.given().log().all()
                .body(stationParams3)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        StationResponse stationResponse3 = response3.as(StationResponse.class);


        // line 저장
        LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600");

        ExtractableResponse<Response> response4 = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all()
                .extract();

        LineResponse lineResponse = response4.as(LineResponse.class);

        // Section 저장 (강남 - 양재, 신분당선, 7km)
        SectionRequest sectionRequest1 = new SectionRequest(lineResponse.getId(), new SectionStations(stationResponse1.getId(), stationResponse2.getId(), 7));

        RestAssured.given().log().all()
                .body(sectionRequest1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        // Section 저장 (신림 - 강남, 2호선, 15km)
        SectionRequest sectionRequest2 = new SectionRequest(2L, new SectionStations(stationResponse3.getId(), stationResponse1.getId(), 15));

        RestAssured.given().log().all()
                .body(sectionRequest2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        // Section 저장 (신림 - 양재, 1호선, 18km)
        SectionRequest sectionRequest3 = new SectionRequest(1L, new SectionStations(stationResponse3.getId(), stationResponse2.getId(), 18));

        RestAssured.given().log().all()
                .body(sectionRequest3)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/paths/" + stationResponse3.getId() + "/" + stationResponse2.getId())
                .then().log().all()
                .extract();

        PathsResponse expected = PathsResponse.of(List.of(stationResponse3, stationResponse2), 18, 1450);

        PathsResponse pathsResponse = response.as(PathsResponse.class);
        System.out.println(pathsResponse);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(pathsResponse).isEqualTo(expected)
        );
    }
}
