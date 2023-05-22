package subway.integration;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.dto.response.ShortestWayResponse;
import subway.fixture.StationFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static subway.fixture.StationFixture.수원;
import static subway.fixture.StationFixture.여긴못감;
import static subway.fixture.StationFixture.잠실나루;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FeeAcceptanceTest extends AcceptanceTest {

    @Test
    void 최단_거리를_조회한다() {
        final var response = 요금을_조회한다(수원, 잠실나루);
        요금이_일치한다(response, 1250);
    }

    @Test
    void 경로가_없는_두_역의_최단_거리를_조회한다() {
        final var response = 요금을_조회한다(잠실나루, 여긴못감);
        요금_조회에_실패한다(response);

    }

    private ExtractableResponse<Response> 요금을_조회한다(final StationFixture start, final StationFixture end) {
        return httpGetRequest(String.format("/fee?start=%s&end=%s", start.getId(), end.getId()));
    }

    private void 요금이_일치한다(final ExtractableResponse<Response> response, final int fee) {
        assertThat(response.as(ShortestWayResponse.class).getFee()).isEqualTo(fee);
    }

    private void 요금_조회에_실패한다(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
    }
}
