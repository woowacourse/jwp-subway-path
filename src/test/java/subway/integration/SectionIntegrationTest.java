package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.AddOneSectionRequest;
import subway.dto.AddTwoSectionRequest;
import subway.dto.LineRequest;
import subway.dto.StationRequest;

public class SectionIntegrationTest extends IntegrationTest {

    private static final long 신림역ID = 1L;
    private static final long 봉천역ID = 1L;
    private static final long _2호선ID = 1L;

    private void insert_신림역_봉천역_2호선() {
        StationRequest 신림역 = new StationRequest("신림");
        StationRequest 봉천역 = new StationRequest("봉천");
        LineRequest _2호선 = new LineRequest("2호선", "초록색");

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
                .post("/lines/1");
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
    void 비어있지_않은_노선에_역을_1개_추가한다() {
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
    void 새로운_역을_추가할_때_거리가_맞지않으면_BAD_REQUEST를_응답한다(int previousDistance, int nextDistance) {
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
    void 역을_중간에_추가할_때_갈림길이_되면_BAD_REQUEST를_응답한다() {
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
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
