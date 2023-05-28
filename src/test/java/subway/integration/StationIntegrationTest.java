package subway.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.integration.step.StationStep.역_생성_요청;
import static subway.integration.step.StationStep.역_조회_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("지하철역 관련 기능 테스트")
public class StationIntegrationTest extends IntegrationTest {
    @Test
    void 지하철_역을_생성한다() {
        // when
        ExtractableResponse<Response> response = 역_생성_요청("잠실역");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header(HttpHeaders.LOCATION)).isEqualTo("/stations/1")
        );
    }

    @Test
    void 지하철_역을_조회한다() {
        // given
        역_생성_요청("잠실역");

        // when
        ExtractableResponse<Response> response = 역_조회_요청(1L);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getLong("id")).isEqualTo(1L),
                () -> assertThat(response.jsonPath().getString("name")).isEqualTo("잠실역")
        );
    }
}
