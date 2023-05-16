package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.SectionRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 Section, 역 관련 기능")
public class SectionIntegrationTest extends IntegrationTest {

    private LineRequest lineRequest1;
    private LineRequest lineRequest2;
    private SectionRequest sectionRequest1;
    private SectionRequest sectionRequest2;
    private SectionRequest sectionRequest3;
    private SectionRequest sectionRequest4;


    @BeforeEach
    public void setUp() {
        super.setUp();
        lineRequest1 = new LineRequest("1호선", "선릉", "잠실새내", 5);
        lineRequest2 = new LineRequest("2호선", "선릉", "논현역", 5);
        sectionRequest1 = new SectionRequest("잠실새내", "잠실", 5);
        sectionRequest2 = new SectionRequest("선릉", "인천역", 20);
        sectionRequest3 = new SectionRequest("선릉", "강변", 3);
        sectionRequest4 = new SectionRequest("논현역", "강릉역", 300);
    }

    @Test
    @DisplayName("지하철 역을 추가한다.")
    void createSectionStation() {
        ExtractableResponse<Response> lineResponse = RestAssured.given().log().uri()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/subway/lines")
                .then().log().all()
                .extract();

        Long lineId = Long.parseLong(lineResponse.header("Location").split("/")[3]);
        ExtractableResponse<Response> response = RestAssured.given().log().uri()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest1)
                .when().post("/subway/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("역 사이에 지하철 역을 추가시 사이 거리보다 크면 예외가 발생한다.")
    void createSectionStationBetweenStationException() {
        ExtractableResponse<Response> lineResponse = RestAssured.given().log().uri()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest2)
                .when().post("/subway/lines")
                .then().log().all()
                .extract();

        Long lineId = Long.parseLong(lineResponse.header("Location").split("/")[3]);
        ExtractableResponse<Response> createdBetweenResponse = RestAssured.given().log().uri()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest4)
                .when().post("/subway/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();

        assertThat(createdBetweenResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("역 사이에 지하철 역을 추가한다.")
    void createSectionStationBetweenStation() {
        ExtractableResponse<Response> lineResponse = RestAssured.given().log().uri()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/subway/lines")
                .then().log().all()
                .extract();

        Long lineId = Long.parseLong(lineResponse.header("Location").split("/")[3]);
        ExtractableResponse<Response> createdResponse = RestAssured.given().log().uri()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest1)
                .when().post("/subway/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();

        ExtractableResponse<Response> createdBetweenResponse = RestAssured.given().log().uri()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest3)
                .when().post("/subway/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();

        assertThat(createdBetweenResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("지하철 역을 삭제한다.")
    void deleteSectionStation() {
        ExtractableResponse<Response> lineResponse = RestAssured.given().log().uri()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/subway/lines")
                .then().log().all()
                .extract();

        Long lineId = Long.parseLong(lineResponse.header("Location").split("/")[3]);
        ExtractableResponse<Response> createdResponse = RestAssured.given().log().uri()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest1)
                .when().post("/subway/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();

        ExtractableResponse<Response> response = RestAssured.given().log().uri()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest1)
                .when().delete("/subway/lines/{lineId}/stations/{stationId}", lineId, 3)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
