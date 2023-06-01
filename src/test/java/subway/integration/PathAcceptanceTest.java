package subway.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.dto.request.AddPathRequest;
import subway.dto.response.LineWithStationResponse;
import subway.fixture.LineFixture;
import subway.fixture.StationFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.OK;
import static subway.fixture.LineFixture.이호선;
import static subway.fixture.LineFixture.일호선;
import static subway.fixture.StationFixture.선릉;
import static subway.fixture.StationFixture.수원;
import static subway.fixture.StationFixture.의왕;
import static subway.fixture.StationFixture.잠실나루;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class PathAcceptanceTest extends AcceptanceTest {

    @Test
    void 역이_두개만_있다면_동시에_삭제된다() {
        final var response = 노선의_역을_삭제한다(일호선, 수원);
        final var response2 = 노선을_조회한다(일호선);

        역_두개가_모두_삭제된다(response, response2);
    }

    @Test
    void 역이_세개있는_노선의_마지막_역을_삭제한다() {
        final var response = 노선의_역을_삭제한다(이호선, 선릉);
        final var response2 = 노선을_조회한다(이호선);

        마지막_노선_삭제에_성공한다(response, response2);
    }

    @Test
    void 노선_경로에_하행역을_추가한다() throws JsonProcessingException {
        final AddPathRequest request = new AddPathRequest(잠실나루.getId(), 선릉.getId(), 10, "down");
        final var response = 노선에_역을_추가한다(request, 일호선);
        final var response2 = 노선을_조회한다(일호선);

        하행역_추가에_성공한다(response, response2);
    }

    @Test
    void 노선_경로에_상행역을_추가한다() throws JsonProcessingException {
        final AddPathRequest request = new AddPathRequest(잠실나루.getId(), 선릉.getId(), 3, "up");
        final var response = 노선에_역을_추가한다(request, 일호선);
        final var response2 = 노선을_조회한다(일호선);

        상행역_추가에_성공한다(response, response2);
    }

    private ExtractableResponse<Response> 노선의_역을_삭제한다(final LineFixture line, final StationFixture station) {
        return httpDeleteRequest(String.format("/lines/%s/stations/%s", line.getId(), station.getId()));
    }

    private ExtractableResponse<Response> 노선을_조회한다(final LineFixture line) {
        return httpGetRequest(String.format("/lines/%s", line.getId()));
    }

    private ExtractableResponse<Response> 노선에_역을_추가한다(final AddPathRequest request, final LineFixture line) throws JsonProcessingException {
        return httpPostRequest(String.format("/lines/%s/stations/", line.getId()), request);
    }

    private void 역_두개가_모두_삭제된다(final ExtractableResponse<Response> response, final ExtractableResponse<Response> response2) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                () -> assertThat(response2.as(LineWithStationResponse.class).getStations())
                        .hasSize(일호선.getSize() - 2)
        );
    }

    private void 마지막_노선_삭제에_성공한다(final ExtractableResponse<Response> response, final ExtractableResponse<Response> response2) {
        final LineWithStationResponse actualResponse = response2.as(LineWithStationResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                () -> assertThat(actualResponse.getStations()).hasSize(이호선.getSize() - 1),
                () -> assertThat(actualResponse.getStations()).extracting("name")
                        .containsExactly(의왕.getName(), 수원.getName())
        );
    }

    private void 하행역_추가에_성공한다(final ExtractableResponse<Response> response, final ExtractableResponse<Response> response2) {
        final var actualResponse = response2.as(LineWithStationResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                () -> assertThat(actualResponse.getStations()).hasSize(일호선.getSize() + 1),
                () -> assertThat(actualResponse.getStations()).extracting("name")
                        .containsExactly(수원.getName(), 잠실나루.getName(), 선릉.getName())
        );
    }

    private void 상행역_추가에_성공한다(final ExtractableResponse<Response> response, final ExtractableResponse<Response> response2) {
        final var actualResponse = response2.as(LineWithStationResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                () -> assertThat(actualResponse.getStations()).hasSize(일호선.getSize() + 1),
                () -> assertThat(actualResponse.getStations()).extracting("name")
                        .containsExactly(수원.getName(), 선릉.getName(), 잠실나루.getName())
        );
    }
}
