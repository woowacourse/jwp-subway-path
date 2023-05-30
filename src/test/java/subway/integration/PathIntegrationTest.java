package subway.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static fixtures.IntegrationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("경로 및 요금 조회 기능")
class PathIntegrationTest extends IntegrationTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        super.setUp();
        objectMapper = super.objectMapper;
    }

    /**
     * integration-test-data.sql 로 현재 등록되어 있는 지하철 정보
     * 2호선 : 선릉역 - 잠실역 - 건대역 (선릉~잠실 거리 : 5, 잠실~건대 거리 : 10
     * 8호선 : 잠실역 - 암사역 (거리: 8)
     */

    @Test
    @DisplayName("경로와 요금을 조회할 수 있다.")
    void findPathTest() throws JsonProcessingException {
        // given
        int age = 50;
        Long startStationId = STATION_LINE2_선릉역_ID;
        Long endStationId = STATION_LINE8_암사역_ID;

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .get("/path/" + startStationId + "/" + endStationId + "?age=" + age)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()).isEqualTo(objectMapper.writeValueAsString(PATH_선릉역_TO_암사역_FOR_ADULT));
    }

    @Test
    @DisplayName("경로와 청소년 요금을 조회할 수 있다.")
    void findPathTest_teenager() throws JsonProcessingException {
        // given
        int teenagerAge = 15;
        Long startStationId = STATION_LINE2_선릉역_ID;
        Long endStationId = STATION_LINE8_암사역_ID;

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .get("/path/" + startStationId + "/" + endStationId + "?age=" + teenagerAge)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()).isEqualTo(objectMapper.writeValueAsString(PATH_선릉역_TO_암사역_FOR_TEENAGER));
    }

    @Test
    @DisplayName("경로와 어린이 요금을 조회할 수 있다.")
    void findPathTest_child() throws JsonProcessingException {
        // given
        int childAge = 10;
        Long startStationId = STATION_LINE2_선릉역_ID;
        Long endStationId = STATION_LINE8_암사역_ID;

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .get("/path/" + startStationId + "/" + endStationId + "?age=" + childAge)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()).isEqualTo(objectMapper.writeValueAsString(PATH_선릉역_TO_암사역_FOR_CHILD));
    }

    @Test
    @DisplayName("경로와 우대 요금을 조회할 수 있다.")
    void findPathTest_preferential() throws JsonProcessingException {
        // given
        int preferentialAge = 80;
        Long startStationId = STATION_LINE2_선릉역_ID;
        Long endStationId = STATION_LINE8_암사역_ID;

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .get("/path/" + startStationId + "/" + endStationId + "?age=" + preferentialAge)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()).isEqualTo(objectMapper.writeValueAsString(PATH_선릉역_TO_암사역_FOR_PREFERENTIAL));
    }
}
