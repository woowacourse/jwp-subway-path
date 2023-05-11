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

public class SectionFindIntegrationTest extends IntegrationTest {
    @DisplayName("지하철 노선의 연결된 순서대로 정렬된 역들을 조회한다.")
    @Test
    void find_sections_Line() {
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
        SectionSaveRequest initSaveRequest = new SectionSaveRequest(3L, 1L, 10);
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .body(initSaveRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all().
                extract();


        SectionSaveRequest saveRequest2 = new SectionSaveRequest(1L, 2L, 10);
        RestAssured
                .given().log().all()
                .body(saveRequest2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all().
                extract();

        //when
        //현재상태 3-1-2
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/" + lineId + "/sections")
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
