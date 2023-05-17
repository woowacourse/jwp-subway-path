package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import subway.dto.StationCreateRequest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.CREATED;
import static subway.steps.StationSteps.역_생성_요청;

class StationIntegrationTest extends IntegrationTest {

    @Test
    void createStationTest() {
        final StationCreateRequest stationCreateRequest = new StationCreateRequest("사평역");

        final ExtractableResponse<Response> response = 역_생성_요청(stationCreateRequest);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank()
        );
    }
}
