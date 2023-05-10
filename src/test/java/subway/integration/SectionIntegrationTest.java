package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.SectionRequest;
import subway.dto.SectionStations;

public class SectionIntegrationTest extends IntegrationTest {

    @DisplayName("지하철역을 노선에 등록한다.")
    @Test
    void addStations() {
        // given
        SectionRequest sectionRequest = new SectionRequest(1L, new SectionStations(1L, 2L, 10));

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("지하철역을 노선에서 삭제한다.")
    @Test
    void deleteStation() {
        // given, when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .delete("/sections/1/1")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
