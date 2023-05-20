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
import subway.dto.StationCreateRequest;

@DisplayName("지하철역 관련 기능")
public class StationIntegrationTest extends IntegrationTest {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        StationCreateRequest stationCreateRequest = new StationCreateRequest("잠실역");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(stationCreateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }
}
