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

@DisplayName("지하철 경로 기능")
class PathControllerTest extends IntegrationTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    @DisplayName("지하철 경로를 찾는다.")
    void findPath() {
        LineRequest lineRequest = new LineRequest("신분당선", "강남역", "논현역", 5);
        ExtractableResponse<Response> lineResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/subway/lines")
                .then().log().all().extract();

        Long lineId = Long.parseLong(lineResponse.header("Location").split("/")[3]);
        SectionRequest sectionRequest = new SectionRequest("논현역", "잠실역", 5);
        RestAssured.given().log().uri()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/subway/lines/{lineId}/sections", lineId)
                .then().log().all();

        ExtractableResponse<Response> pathResponse = RestAssured.given().log().uri()
                .when().get("/subway/path?startStationId=1&endStationId=2")
                .then()
                .log().all().extract();

        assertThat(pathResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
