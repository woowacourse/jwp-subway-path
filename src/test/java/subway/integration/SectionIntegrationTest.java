package subway.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.SectionCreateRequest;

public class SectionIntegrationTest extends IntegrationTest{

    @DisplayName("지하철 구간을 생성한다.")
    @Test
    void createStation() {
        // given
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(1L, 2L, 10, 1L);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(sectionCreateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        // then
        assertAll(

                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header(HttpHeaders.LOCATION)).isEqualTo("/sections/1")
        );
    }
}
