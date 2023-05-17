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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.AddOneSectionRequest;
import subway.dto.AddTwoSectionRequest;
import subway.dto.LineRequest;
import subway.dto.StationRequest;

public class SectionIntegrationTest extends IntegrationTest {

    private static final long 신림역ID = 1L;
    private static final long 봉천역ID = 2L;
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

    //    @DisplayName("노선에서 역을 제거한다.")
//    @Test
//    void removeStation_success() {
//        //given
//        ExtractableResponse<Response> enrollResponse = enrollLine(lineRequest1);
//        long lineId = Long.parseLong(enrollResponse.header("Location").split("/")[2]);
//
//        long stationId = 1L;
//
//        RestAssured
//                .given().log().all()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body(ADD_TWO_STATION_REQUEST)
//                .when().post("/lines/{lineId}", lineId)
//                .then().log().all()
//                .extract();
//
//        //when
//        ExtractableResponse<Response> response = RestAssured
//                .when()
//                .delete("/lines/{lineId}/stations/{stationId}", lineId, stationId)
//                .then().log().all()
//                .extract();
//
//        //then
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
//    }
//
//    @DisplayName("노선에서 역을 제거한다.")
//    @Test
//    void removeStation_success_noStation() {
//        //given
//        ExtractableResponse<Response> enrollResponse = enrollLine(lineRequest1);
//        long lineId = Long.parseLong(enrollResponse.header("Location").split("/")[2]);
//
//        //when
//        ExtractableResponse<Response> response = RestAssured
//                .when()
//                .delete("/lines/{lineId}/stations/{stationId}", lineId, 1L)
//                .then().log().all()
//                .extract();
//
//        //then
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
//    }
//
//    private ExtractableResponse<Response> enrollLine(final LineRequest lineRequest) {
//        return RestAssured
//                .given().log().all()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body(lineRequest)
//                .when().post("/lines")
//                .then().log().all().
//                extract();
//    }
}
