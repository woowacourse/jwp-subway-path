package subway.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import subway.dto.StationRequest;

import static fixtures.IntegrationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

@DisplayName("지하철역 관련 기능")
class StationIntegrationTest extends IntegrationTest {

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
    @DisplayName("노선에 역이 하나도 등록되지 않은 상황에서 최초 등록 시 두 역을 동시에 등록해야 한다.")
    void postStationsInNewLineTest_initial() throws JsonProcessingException {
        // given
        StationRequest request = REQUEST_온수역_TO_철산역;

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/stations")
                        .then().log().all()
                        .extract();


        ExtractableResponse<Response> lineAfterPost =
                RestAssured.given().log().all()
                        .when()
                        .get("/lines/" + LINE7_ID)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/lines/" + LINE7_ID);
        assertThat(lineAfterPost.asString()).isEqualTo(objectMapper.writeValueAsString(LINE7_노선도_AFTER_INITIAL_INSERT));
    }

    @Test
    @DisplayName("현재 상행역의 상행역을 추가한다.")
    void postStationInLineTest_insertUpEnd() throws JsonProcessingException {
        // given
        StationRequest request = REQUEST_대림역_TO_선릉역;

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/stations")
                        .then().log().all()
                        .extract();


        ExtractableResponse<Response> lineAfterPost =
                RestAssured.given().log().all()
                        .when()
                        .get("/lines/" + LINE2_ID)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/lines/" + LINE2_ID);
        assertThat(lineAfterPost.asString()).isEqualTo(objectMapper.writeValueAsString(LINE2_노선도_AFTER_INSERT_대림역));
    }

    @Test
    @DisplayName("현재 하행역의 하행역을 추가한다.")
    void postStationInLineTest_insertDownEnd() throws JsonProcessingException {
        // given
        StationRequest request = REQUEST_건대역_TO_성수역;

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/stations")
                        .then().log().all()
                        .extract();


        ExtractableResponse<Response> lineAfterPost =
                RestAssured.given().log().all()
                        .when()
                        .get("/lines/" + LINE2_ID)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/lines/" + LINE2_ID);
        assertThat(lineAfterPost.asString()).isEqualTo(objectMapper.writeValueAsString(LINE2_노선도_AFTER_INSERT_성수역));
    }

    @Test
    @DisplayName("역과 역 사이에 새로운 역을 등록한다.")
    void postStationInLineTest_insertMiddle() throws JsonProcessingException {
        // given
        StationRequest request = REQUEST_강변역_TO_건대역;

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/stations")
                        .then().log().all()
                        .extract();


        ExtractableResponse<Response> lineAfterPost =
                RestAssured.given().log().all()
                        .when()
                        .get("/lines/" + LINE2_ID)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/lines/" + LINE2_ID);
        assertThat(lineAfterPost.asString()).isEqualTo(objectMapper.writeValueAsString(LINE2_노선도_AFTER_INSERT_강변역));
    }

    @Test
    @DisplayName("역과 역 사이애 새로운 역을 등록할 때 각 역 사이의 거릭가 음수가 되면 예외가 발생한다.")
    void postStationInLineTest_insertMiddle_fail() {
        // given
        StationRequest request = REQUEST_잠실역_TO_성수역;

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/stations")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("역 사이 거리는 0km이상 100km 이하여야 합니다.");
    }

    @Test
    @DisplayName("노선에 역을 추가할 때 기준이 되는 역을 포함시키지 않으면 예외가 발생한다.")
    void postStationInLineTest_fail_cannotLink() {
        // given
        StationRequest request = REQUEST_대림역_TO_신림역;

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/stations")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("현재 등록된 역 중에 하나를 포함해야합니다.");
    }

    @Test
    @DisplayName("이미 존재하는 구간을 저장하려고 하면 예외가 발생한다.")
    void postStationInLineTest_fail_duplicateSection() {
        // given
        StationRequest request = REQUEST_잠실역_TO_강변역;

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/stations")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("이미 포함되어 있는 구간입니다.");
    }

    @Test
    @DisplayName("상행 종점역을 삭제한다.")
    void deleteStationInLineTest_deleteUpEnd() throws JsonProcessingException {
        // given
        Long stationIdToDelete = STATION_LINE2_선릉역_ID;

        // when
        ExtractableResponse<Response> response =
                RestAssured.when()
                        .delete("/stations/" + stationIdToDelete)
                        .then().log().all()
                        .extract();


        ExtractableResponse<Response> lineAfterDelete =
                RestAssured.given().log().all()
                        .when()
                        .get("/lines/" + LINE2_ID)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
        assertThat(response.header("Location")).isEqualTo("/lines/" + LINE2_ID);
        assertThat(lineAfterDelete.asString()).isEqualTo(objectMapper.writeValueAsString(LINE2_노선도_AFTER_DELETE_선릉역));
    }

    @Test
    @DisplayName("하행 종점역을 삭제한다.")
    void deleteStationInLineTest_deleteDownEnd() throws JsonProcessingException {
        // given
        Long stationIdToDelete = STATION_LINE2_건대역_ID;

        // when
        ExtractableResponse<Response> response =
                RestAssured.when()
                        .delete("/stations/" + stationIdToDelete)
                        .then().log().all()
                        .extract();


        ExtractableResponse<Response> lineAfterDelete =
                RestAssured.given().log().all()
                        .when()
                        .get("/lines/" + LINE2_ID)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
        assertThat(response.header("Location")).isEqualTo("/lines/" + LINE2_ID);
        assertThat(lineAfterDelete.asString()).isEqualTo(objectMapper.writeValueAsString(LINE2_노선도_AFTER_DELETE_건대역));
    }

    @Test
    @DisplayName("중간역을 삭제한다.")
    void deleteStationInLineTest_deleteMiddle() throws JsonProcessingException {
        // given
        Long stationIdToDelete = STATION_LINE2_잠실역_ID;

        // when
        ExtractableResponse<Response> response =
                RestAssured.when()
                        .delete("/stations/" + stationIdToDelete)
                        .then().log().all()
                        .extract();


        ExtractableResponse<Response> lineAfterDelete =
                RestAssured.given().log().all()
                        .when()
                        .get("/lines/" + LINE2_ID)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
        assertThat(response.header("Location")).isEqualTo("/lines/" + LINE2_ID);
        assertThat(lineAfterDelete.asString()).isEqualTo(objectMapper.writeValueAsString(LINE2_노선도_AFTER_DELETE_잠실역));
    }
}
