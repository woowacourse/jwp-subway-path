package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.AddOneSectionRequest;
import subway.dto.AddTwoSectionRequest;
import subway.dto.LineRequest;
import subway.dto.StationRequest;
import java.util.Map;

public class SectionIntegrationTest extends IntegrationTest {

    private static final long 신림역ID = 1L;
    private static final long 봉천역ID = 2L;
    private static final long 서울대입구역ID = 3L;
    private static final long _2호선ID = 1L;

    private void insert_신림역_봉천역_2호선() {
        StationRequest 신림역 = new StationRequest("신림");
        StationRequest 봉천역 = new StationRequest("봉천");
        LineRequest _2호선 = new LineRequest("2호선", "초록색", 0);

        postStation(신림역);
        postStation(봉천역);
        postLine(_2호선);
    }

    private void postStation(final StationRequest body) {
        RestAssured.given()
                .body(body)
                .contentType(ContentType.JSON)
                .when()
                .post("/stations");
    }

    private void postLine(final LineRequest body) {
        RestAssured.given()
                .body(body)
                .contentType(ContentType.JSON)
                .when()
                .post("/lines");
    }

    private void insert_section_2호선_신림_봉천_거리10() {
        AddOneSectionRequest addOneSectionRequest = new AddOneSectionRequest(신림역ID, 봉천역ID, 10);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(addOneSectionRequest)
                .when()
                .post("/lines/" + _2호선ID);
    }

    private void insert_section_2호선_봉천역_서울대입구역_거리10() {
        AddOneSectionRequest addOneSectionRequest = new AddOneSectionRequest(봉천역ID, 서울대입구역ID, 10);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(addOneSectionRequest)
                .when()
                .post("/lines/" + _2호선ID);
    }

    @Test
    void 빈_노선에_2개의_역을_추가한다() {
        //given
        insert_신림역_봉천역_2호선();

        //when
        AddOneSectionRequest request = new AddOneSectionRequest(신림역ID, 봉천역ID, 10);
        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/lines/{lineId}", _2호선ID)
                .then()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/lines/" + _2호선ID);
    }

    @Test
    void 중간에_역을_1개_추가한다() {
        //given
        insert_신림역_봉천역_2호선();
        postStation(new StationRequest("낙성대"));
        insert_section_2호선_신림_봉천_거리10();

        //when
        AddTwoSectionRequest addTwoSectionRequest = new AddTwoSectionRequest(3L, 신림역ID, 봉천역ID, 5, 5);
        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(addTwoSectionRequest)
                .when().post("/lines/{lineId}/stations", _2호선ID)
                .then()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/lines/" + _2호선ID);
    }

    @ParameterizedTest
    @CsvSource(value = {"1, 2", "10, 20", "11, 0"})
    void 중간에_역을_추가할_때_거리가_맞지않으면_BAD_REQUEST를_응답한다(int previousDistance, int nextDistance) {
        //given
        insert_신림역_봉천역_2호선();
        postStation(new StationRequest("낙성대"));
        insert_section_2호선_신림_봉천_거리10();

        //when
        AddTwoSectionRequest addTwoSectionRequest = new AddTwoSectionRequest(3L, 신림역ID, 봉천역ID, previousDistance, nextDistance);
        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(addTwoSectionRequest)
                .when().post("/lines/{lineId}/stations", _2호선ID)
                .then()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 역을_하나_추가할_때_갈림길이_되면_BAD_REQUEST를_응답한다() {
        //given
        insert_신림역_봉천역_2호선();
        postStation(new StationRequest("낙성대"));
        insert_section_2호선_신림_봉천_거리10();

        //when
        AddOneSectionRequest request = new AddOneSectionRequest(신림역ID, 3L, 10);
        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines/{lineId}", _2호선ID)
                .then()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 맨_앞에_역을_1개_정상적으로_추가한다() {
        // given
        insert_신림역_봉천역_2호선();
        postStation(new StationRequest("낙성대"));
        insert_section_2호선_신림_봉천_거리10();

        // when
        AddOneSectionRequest request = new AddOneSectionRequest(3L, 신림역ID, 10);
        ExtractableResponse<Response> response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/lines/{lineId}", _2호선ID)
                .then().extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isEqualTo("/lines/" + _2호선ID)
        );
    }

    @Test
    void 맨_뒤에_역을_1개_정상적으로_추가한다() {
        // given
        insert_신림역_봉천역_2호선();
        postStation(new StationRequest("낙성대"));
        insert_section_2호선_신림_봉천_거리10();

        // when
        AddOneSectionRequest request = new AddOneSectionRequest(봉천역ID, 3L, 10);
        ExtractableResponse<Response> response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/lines/{lineId}", _2호선ID)
                .then().extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isEqualTo("/lines/" + _2호선ID)
        );
    }

    @Test
    void 노선에_역이_2개_존재할_때_하나의_역을_제거한다() {
        //given
        insert_신림역_봉천역_2호선();
        insert_section_2호선_신림_봉천_거리10();

        // when
        ExtractableResponse<Response> response = RestAssured
                .when().delete("/lines/{lineId}/stations/{stationId}", _2호선ID, 봉천역ID)
                .then().extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void 노선에_역이_3개_존재할_때_모든_역을_각각_제거할_수_있다(final long deleteStationId) {
        //given
        insert_신림역_봉천역_2호선();
        postStation(new StationRequest("서울대입구역"));
        insert_section_2호선_신림_봉천_거리10();
        insert_section_2호선_봉천역_서울대입구역_거리10();

        //when
        ExtractableResponse<Response> response = RestAssured
                .when()
                .delete("/lines/{lineId}/stations/{stationId}", _2호선ID, deleteStationId)
                .then()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 존재하지_않은_역을_제거하면_BAD_REQUEST를_응답한다() {
        //given
        insert_신림역_봉천역_2호선();
        insert_section_2호선_신림_봉천_거리10();

        // when
        ExtractableResponse<Response> response = RestAssured
                .when()
                .delete("/lines/{lineId}/stations/{stationId}", _2호선ID, 4L)
                .then().extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 존재하지_않는_호선으로_역_제거를_요청하면_BAD_REQUEST를_응답한다() {
        //given
        insert_신림역_봉천역_2호선();
        insert_section_2호선_신림_봉천_거리10();

        // when
        ExtractableResponse<Response> response = RestAssured
                .when()
                .delete("/lines/{lineId}/stations/{stationId}", 4L, 신림역ID)
                .then().extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 특정_호선을_조회한다() {
        // given
        insert_신림역_봉천역_2호선();
        insert_section_2호선_신림_봉천_거리10();

        // when
        ExtractableResponse<Response> response = RestAssured.when()
                .get("/lines/{lineId}", _2호선ID)
                .then()
                .contentType(ContentType.JSON)
                .extract();

        Map<Object, Object> responseBody = response.body().jsonPath().getMap("$");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(responseBody.get("line")).isNotNull(),
                () -> assertThat(responseBody.get("stations")).isNotNull()
        );
    }
}
