package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.request.SectionSaveRequest;
import subway.dto.request.StationRequest;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionRemoveIntegrationTest extends IntegrationTest {
    @DisplayName("노선의 상행 종점을 제거한다.")
    @Test
    void removeUpEndSection_success() {
        //given
        ExtractableResponse<Response> response1 = RestAssured.given().log().all()
                .body(new StationRequest("강남역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .body(new StationRequest("잠실역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        ExtractableResponse<Response> response3 = RestAssured.given().log().all()
                .body(new StationRequest("잠실나루역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        //노선에 첫 구간 등록
        long lineId = 1L;
        SectionSaveRequest initSaveRequest = new SectionSaveRequest(1L, 2L, 10);
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .body(initSaveRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all().
                extract();

        SectionSaveRequest saveRequest2 = new SectionSaveRequest(2L, 3L, 10);
        ExtractableResponse<Response> createResponse2 = RestAssured
                .given().log().all()
                .body(saveRequest2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all().
                extract();

        //when
        //현재상태: 1-2-3
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/" + lineId + "/sections/" + 1)
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("노선의 하행종점을 제거한다.")
    @Test
    void removeDownEndSection_success() {
        //given
        ExtractableResponse<Response> response1 = RestAssured.given().log().all()
                .body(new StationRequest("강남역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .body(new StationRequest("잠실역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        ExtractableResponse<Response> response3 = RestAssured.given().log().all()
                .body(new StationRequest("잠실나루역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        //노선에 첫 구간 등록
        long lineId = 1L;
        SectionSaveRequest initSaveRequest = new SectionSaveRequest(1L, 2L, 10);
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .body(initSaveRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all().
                extract();

        SectionSaveRequest saveRequest2 = new SectionSaveRequest(2L, 3L, 10);
        ExtractableResponse<Response> createResponse2 = RestAssured
                .given().log().all()
                .body(saveRequest2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all().
                extract();

        //when
        //현재상태: 1-2-3
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/" + lineId + "/sections/" + 3)
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("노선의 중간역을 제거한다.")
    @Test
    void removeMiddleSection_success() {
        //given
        ExtractableResponse<Response> response1 = RestAssured.given().log().all()
                .body(new StationRequest("강남역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .body(new StationRequest("잠실역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        ExtractableResponse<Response> response3 = RestAssured.given().log().all()
                .body(new StationRequest("잠실나루역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        //노선에 첫 구간 등록
        long lineId = 1L;
        SectionSaveRequest initSaveRequest = new SectionSaveRequest(1L, 2L, 10);
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .body(initSaveRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all().
                extract();

        SectionSaveRequest saveRequest2 = new SectionSaveRequest(2L, 3L, 10);
        ExtractableResponse<Response> createResponse2 = RestAssured
                .given().log().all()
                .body(saveRequest2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all().
                extract();

        //when
        //현재상태: 1-2-3
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/" + lineId + "/sections/" + 2)
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("노선에 존재하지 않는 역을 제거하면 400에러를 발생시킨다.")
    @Test
    void removeSection_not_exist_fail() {
        //given
        ExtractableResponse<Response> response1 = RestAssured.given().log().all()
                .body(new StationRequest("강남역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .body(new StationRequest("잠실역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        //노선에 첫 구간 등록
        long lineId = 1L;
        SectionSaveRequest initSaveRequest = new SectionSaveRequest(1L, 2L, 10);
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .body(initSaveRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all().
                extract();

        //when
        //현재상태: 1-2
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/" + lineId + "/sections/" + 5)
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
