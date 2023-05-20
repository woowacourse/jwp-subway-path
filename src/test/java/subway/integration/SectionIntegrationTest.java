package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.fixture.SectionRequestFixture.SECTION_REQUEST_강남_잠실_5;
import static subway.fixture.SectionRequestFixture.SECTION_REQUEST_잠실_길동_10;
import static subway.fixture.SectionRequestFixture.SECTION_REQUEST_잠실_몽촌토성_5;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;

@DisplayName("지하철역 관련 기능")
public class SectionIntegrationTest extends IntegrationTest {

    private LineRequest lineRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();

        lineRequest = new LineRequest("신분당선", "bg-red-600");
    }

    @DisplayName("특정 노선에 지하철역을 추가한다.")
    @Test
    void add() {
        // given
        ExtractableResponse<Response> lineResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        Long lineId = Long.parseLong(lineResponse.header("Location").split("/")[2]);
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(SECTION_REQUEST_강남_잠실_5)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("lines/{lineId}/stations", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("특정 노선에 구간의 거리가 기존 구간의 거리보다 큰 구간을 추가한다.")
    @Test
    void add_fail_distance() {
        // given
        ExtractableResponse<Response> lineResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all().
                extract();

        Long lineId = Long.parseLong(lineResponse.header("Location").split("/")[2]);

        RestAssured.given().log().all()
                .body(SECTION_REQUEST_잠실_몽촌토성_5)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("lines/{lineId}/stations", lineId)
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(SECTION_REQUEST_잠실_길동_10)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("lines/{lineId}/stations", lineId)
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("특정 노선에 포함되는 지하철역 목록을 조회한다.")
    @Test
    void getStations() {
        // given
        ExtractableResponse<Response> lineResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all().
                extract();

        Long lineId = Long.parseLong(lineResponse.header("Location").split("/")[2]);

        ExtractableResponse<Response> createResponse1 = RestAssured
                .given().log().all()
                .body(SECTION_REQUEST_강남_잠실_5)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("lines/{lineId}/stations", lineId)
                .then().log().all()
                .extract();

        ExtractableResponse<Response> createResponse2 = RestAssured
                .given().log().all()
                .body(SECTION_REQUEST_잠실_몽촌토성_5)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("lines/{lineId}/stations", lineId)
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .get("lines/{lineId}/stations", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("해당 노선의 역과 구간을 제거한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> lineResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all().
                extract();

        Long lineId = Long.parseLong(lineResponse.header("Location").split("/")[2]);

        ExtractableResponse<Response> createResponse1 = RestAssured
                .given().log().all()
                .body(SECTION_REQUEST_강남_잠실_5)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("lines/{lineId}/stations", lineId)
                .then().log().all()
                .extract();

        ExtractableResponse<Response> createResponse2 = RestAssured
                .given().log().all()
                .body(SECTION_REQUEST_잠실_몽촌토성_5)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("lines/{lineId}/stations", lineId)
                .then().log().all()
                .extract();

        // when
        Long stationId = Long.parseLong(createResponse2.header("Location").split("/")[3]);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .delete("lines/{lineId}/stations/{stationId}", lineId, stationId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
