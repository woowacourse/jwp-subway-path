package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class SectionIntegrationTest extends IntegrationTest {

    @DisplayName("지하철역을 노선에 등록한다.")
    @Test
    void addStations() {
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("lineId", 1L);
        params.put("stations", List.of(
                Map.of("id", 1L),
                Map.of("id", 2L)
        ));

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
