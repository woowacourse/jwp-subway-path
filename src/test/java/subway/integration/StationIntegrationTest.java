package subway.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.integration.step.LineStep.노선_생성_요청;
import static subway.integration.step.StationStep.역_생성_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import subway.dto.LineRequest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("지하철역 관련 기능")
public class StationIntegrationTest extends IntegrationTest {
    @Test
    void 지하철_역을_생성한다() {
        // given
        노선_생성_요청(new LineRequest("2호선"));

        // when
        ExtractableResponse<Response> response = 역_생성_요청("잠실역", 1L);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header(HttpHeaders.LOCATION)).isEqualTo("/stations/1")
        );
    }
}
