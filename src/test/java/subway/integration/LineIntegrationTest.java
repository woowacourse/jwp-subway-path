package subway.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.dto.LineRequest;

import static fixtures.IntegrationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

@DisplayName("지하철 노선 관련 기능")
class LineIntegrationTest extends IntegrationTest {

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
     * 7호선 : {비어있음}
     */

    @Test
    @DisplayName("노선을 저장한다.")
    void createLineTest() {
        // given
        LineRequest request = REQUEST_LINE4;

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/lines/" + LINE4_ID);
    }

    @Test
    @DisplayName("지정한 노선을 조회한다.")
    void getLineStationsTest() throws JsonProcessingException {
        // given
        Long lineId = LINE2_ID;

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .when()
                        .get("/lines/" + lineId)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()).isEqualTo(objectMapper.writeValueAsString(LINE2_노선도));
    }

    @Test
    @DisplayName("지하철 전체 노선을 조회한다.")
    void getAllLineStationsTest() throws JsonProcessingException {
        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .when()
                        .get("/lines/")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()).isEqualTo(objectMapper.writeValueAsString(ALL_LINE_노선도));
    }

    @Test
    @DisplayName("노선 정보를 수정한다.")
    void updateLineTest() {
        // given
        Long lineId = LINE2_ID;
        LineRequest request = REQUEST_NEW_LINE2;

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .put("/lines/" + lineId)
                .then().log().all()
                .extract();


        ExtractableResponse<Response> lineAfterUpdate =
                RestAssured.given().log().all()
                        .when()
                        .get("/lines/" + lineId)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(lineAfterUpdate.body().asString()).contains(LINE2_NEW_NAME);
    }

    @Test
    @DisplayName("노선을 삭제한다.")
    void deleteLineTest() {
        // given
        Long lineId = LINE2_ID;

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .delete("/lines/" + lineId)
                .then().log().all()
                .extract();

        ExtractableResponse<Response> linesAfterDelete =
                RestAssured.given().log().all()
                        .when()
                        .get("/lines/")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
        assertThat(linesAfterDelete.body().asString()).doesNotContain(LINE2_NAME);
    }
}