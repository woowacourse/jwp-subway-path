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
import subway.dto.SectionCreateRequest;
import subway.dto.StationDeleteRequest;

@DisplayName("지하철 구간 관련 기능")
public class SectionIntegrationTest extends IntegrationTest {
    private SectionCreateRequest sectionCreateRequest;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        // 노선을 생성한다.
        LineRequest lineRequest = new LineRequest("2호선");
        RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        sectionCreateRequest = new SectionCreateRequest("잠실역", "잠실새내역", 10, 1);
    }

    @DisplayName("지하철 구간을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(sectionCreateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("역이 두 개 존재하는 노선의 지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        StationDeleteRequest stationDeletedRequest = new StationDeleteRequest("잠실역", 1L);

        RestAssured.given().log().all()
                .body(sectionCreateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(stationDeletedRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/stations")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
