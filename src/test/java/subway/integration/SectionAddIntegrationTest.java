package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.SectionSaveRequest;
import subway.dto.StationRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
public class SectionAddIntegrationTest extends IntegrationTest {

    @DisplayName("노선에 역을 최초로 추가한다.")
    @Test
    void addInitialSectionToLine_success() {
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

        // when
        long lineId = 1L;
        SectionSaveRequest request = new SectionSaveRequest(1L, 2L, 10);
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all().
                extract();

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("노선의 종점에 역을 추가한다.")
    @Test
    void addEndSectionToLine_success() {
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
        SectionSaveRequest upEndSaveRequest = new SectionSaveRequest(3L, 1L, 5);
        ExtractableResponse<Response> createResponse2 = RestAssured
                .given().log().all()
                .body(upEndSaveRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all().
                extract();

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("노선의 중간에 역을 추가한다.")
    @Test
    void addMiddleSectionToLine_success() {
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
        SectionSaveRequest middleSaveRequest = new SectionSaveRequest(1L, 3L, 2);
        ExtractableResponse<Response> createResponse2 = RestAssured
                .given().log().all()
                .body(middleSaveRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all().
                extract();

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
