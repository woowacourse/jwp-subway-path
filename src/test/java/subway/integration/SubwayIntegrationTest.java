package subway.integration;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.dto.response.LineWithStationResponse;
import subway.fixture.LineFixture;
import subway.fixture.StationFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.fixture.LineFixture.이호선;
import static subway.fixture.LineFixture.일호선;
import static subway.fixture.StationFixture.선릉;
import static subway.fixture.StationFixture.수원;
import static subway.fixture.StationFixture.의왕;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class SubwayIntegrationTest extends IntegrationTest {

    @Test
    void 역을_제거하면_경로에서도_제거된다() {
        역을_제거한다(수원);
        final var response = 노선을_조회한다(이호선);
        노선에서_제거된_것을_확인한다(response);
    }

    @Test
    void 환승역을_삭제한다() {
        노선의_역을_삭제한다(일호선, 수원);
        final var response = 노선을_조회한다(이호선);
        환승역을_삭제해도_다른_노선에_영향이_없다(response);
    }

    private ExtractableResponse<Response> 역을_제거한다(final StationFixture station) {
        final Long id = station.getId();
        return httpDeleteRequest(String.format("/stations/%s", id));
    }

    private ExtractableResponse<Response> 노선을_조회한다(final LineFixture line) {
        final Long id = line.getId();
        return httpGetRequest(String.format("/lines/%s", id));
    }

    private ExtractableResponse<Response> 노선의_역을_삭제한다(final LineFixture line, final StationFixture station) {
        return httpDeleteRequest(String.format("/lines/%s/stations/%s", line.getId(), station.getId()));
    }

    private void 노선에서_제거된_것을_확인한다(final ExtractableResponse<Response> response) {
        final var actualResponse = response.as(LineWithStationResponse.class);
        assertAll(
                () -> assertThat(actualResponse.getStations()).hasSize(이호선.getSize() - 1),
                () -> assertThat(actualResponse.getStations()).extracting("name")
                        .doesNotContain(수원.getName())
        );
    }

    private void 환승역을_삭제해도_다른_노선에_영향이_없다(final ExtractableResponse<Response> response) {
        final var actualResponse = response.as(LineWithStationResponse.class);
        assertThat(actualResponse.getStations()).extracting("name")
                .containsExactly(의왕.getName(), 수원.getName(), 선릉.getName());
    }
}
