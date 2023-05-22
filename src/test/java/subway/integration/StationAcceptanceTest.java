package subway.integration;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.fixture.StationFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static subway.fixture.StationFixture.수원;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    @Test
    void 지하철_역을_삭제한다() {
        final var response = 역을_제거한다(수원);
        역_제거에_성공한다(response);
    }

    @Test
    void 없는_역을_삭제한다() {
        역을_제거한다(수원);

        final var response = 역을_제거한다(수원);
        상태코드_404를_반환한다(response);
    }

    private ExtractableResponse<Response> 역을_제거한다(final StationFixture station) {
        final Long id = station.getId();
        return httpDeleteRequest(String.format("/stations/%s", id));
    }

    private void 역_제거에_성공한다(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
    }

    private void 상태코드_404를_반환한다(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }
}
