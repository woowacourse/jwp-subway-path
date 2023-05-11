package subway.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.StationRequest;

@DisplayName("지하철역 관련 기능")
public class StationIntegrationTest extends IntegrationTest {

    private StationRequest stationRequest1;

    @BeforeEach
    public void setUp() {
        super.setUp();

        stationRequest1 = new StationRequest("잠실역", "잠실새내역", 10, 1);
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        LineRequest lineRequest = new LineRequest("2호선");
        RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(stationRequest1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


}
