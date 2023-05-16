package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.AddOneSectionRequest;
import subway.dto.AddTwoSectionRequest;
import subway.dto.LineRequest;
import subway.dto.StationRequest;

public class SectionIntegrationTest extends IntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    private void insertDummyData() throws JsonProcessingException {
        String 신림역 = objectMapper.writeValueAsString(new StationRequest("신림"));
        String 봉천역 = objectMapper.writeValueAsString(new StationRequest("봉천"));
        String _2호선 = objectMapper.writeValueAsString(new LineRequest("2호선", "초록색"));

        postStation(신림역);
        postStation(봉천역);
        postLine(_2호선);
    }

    private void postStation(final String body) {
        RestAssured.given()
                .body(body)
                .contentType(ContentType.JSON)
                .when()
                .post("/stations");
    }

    private void postLine(final String body) {
        RestAssured.given()
                .body(body)
                .contentType(ContentType.JSON)
                .when()
                .post("/lines");
    }

    @Test
    void 빈_노선에_2개의_역을_추가한다() throws JsonProcessingException {
        //given
        insertDummyData();

        AddOneSectionRequest addOneSectionRequest = new AddOneSectionRequest(1L, 2L, 10);
        String request = objectMapper.writeValueAsString(addOneSectionRequest);

        //when
        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/lines/{lineId}", 1L)
                .then()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/lines/" + 1L);
    }

    @Test
    void 비어있지_않은_노선에_역을_1개_추가한다() throws JsonProcessingException {
        //given
        insertDummyData();
        postStation(objectMapper.writeValueAsString(new StationRequest("낙성대")));

        AddOneSectionRequest addOneSectionRequest = new AddOneSectionRequest(1L, 2L, 10);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(addOneSectionRequest)
                .when()
                .post("/lines/1");

        AddTwoSectionRequest addTwoSectionRequest = new AddTwoSectionRequest(3L, 1L, 2L, 5, 5);

        //when
        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(addTwoSectionRequest)
                .when().post("/lines/{lineId}/stations", 1L)
                .then()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/lines/" + 1L);
    }

    @ParameterizedTest
    @CsvSource(value = {"1, 2", "10, 20", "11, 0"})
    void 새로운_역을_추가할_때_거리가_맞지않으면_BadRequest를_응답한다(int previousDistance, int nextDistance) throws JsonProcessingException {
        //given
        insertDummyData();
        postStation(objectMapper.writeValueAsString(new StationRequest("낙성대")));

        AddOneSectionRequest addOneSectionRequest = new AddOneSectionRequest(1L, 2L, 10);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(addOneSectionRequest)
                .when()
                .post("/lines/1");

        AddTwoSectionRequest addTwoSectionRequest = new AddTwoSectionRequest(3L, 1L, 2L, previousDistance, nextDistance);

        //when
        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(addTwoSectionRequest)
                .when().post("/lines/{lineId}/stations", 1L)
                .then()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
//
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
