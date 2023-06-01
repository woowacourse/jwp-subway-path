package subway.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.dto.request.LineRequest;
import subway.dto.response.LineWithStationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static subway.fixture.LineFixture.빈호선;
import static subway.fixture.LineFixture.이호선;
import static subway.fixture.LineFixture.일호선;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @Test
    void 노선을_추가한다() throws JsonProcessingException {
        final LineRequest lineRequest = new LineRequest("추가된 노선", "검정색");
        final var response = 노선을_추가한다(lineRequest);
        노선_추가에_성공한다(response);
    }

    @Test
    void 기존에_있는_이름으로_노선을_추가한다() throws JsonProcessingException {
        final LineRequest lineRequest = new LineRequest(일호선.getName(), "아무색");
        final var response = 노선을_추가한다(lineRequest);
        노선_추가에_실패한다(response);
    }

    @Test
    void 역이_있는_노선을_조회한다() {
        final var response = 노선을_조회한다(일호선.getId());
        역이_있는_노선_조회에_성공한다(response);
    }

    @Test
    void 역이_없는_노선을_조회한다() {
        final var response = 노선을_조회한다(빈호선.getId());
        역이_없는_노선_조회에_성공한다(response);
    }

    @Test
    void 모든_역들을_조회한다() {
        final var response = 모든_노선을_조회한다();
        세개의_노선이_있다(response);
    }

    @Test
    void 노선을_삭제한다() {
        final var response = 노선을_삭제한다(일호선.getId());
        final var response2 = 모든_노선을_조회한다();
        노선_삭제에_성공한다(response, response2);
    }

    @Test
    void 없는_노선_삭제를_시도한다() {
        final var response = 노선을_삭제한다(Long.MAX_VALUE);
        노선_삭제에_실패한다(response);
    }

    private ExtractableResponse<Response> 노선을_조회한다(final Long id) {
        return httpGetRequest("/lines/" + id);
    }

    private ExtractableResponse<Response> 노선을_추가한다(final LineRequest request) throws JsonProcessingException {
        return httpPostRequest("/lines", request);
    }

    private List<LineWithStationResponse> 모든_노선을_조회한다() {
        return httpGetRequest("/lines").jsonPath().getList(".", LineWithStationResponse.class);
    }

    private ExtractableResponse<Response> 노선을_삭제한다(final Long id) {
        return httpDeleteRequest(String.format("/lines/%s", id));
    }

    private void 노선_추가에_성공한다(final ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(CREATED.value()),
                () -> assertThat(response.header("Location")).isNotNull()
        );
    }

    private void 노선_추가에_실패한다(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
    }

    private void 역이_있는_노선_조회에_성공한다(final ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.as(LineWithStationResponse.class).getName()).isEqualTo(일호선.getName()),
                () -> assertThat(response.as(LineWithStationResponse.class).getStations()).hasSize(일호선.getSize()),
                () -> assertThat(response.statusCode()).isEqualTo(OK.value())
        );
    }

    private void 역이_없는_노선_조회에_성공한다(final ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.as(LineWithStationResponse.class).getStations()).hasSize(빈호선.getSize()),
                () -> assertThat(response.as(LineWithStationResponse.class).getName()).isEqualTo(빈호선.getName())
        );
    }

    private void 세개의_노선이_있다(final List<LineWithStationResponse> response) {
        assertAll(
                () -> assertThat(response).hasSize(3),
                () -> assertThat(response).extracting("name")
                        .containsExactly(일호선.getName(), 이호선.getName(), 빈호선.getName())
        );
    }

    private void 노선_삭제에_성공한다(final ExtractableResponse<Response> response, final List<LineWithStationResponse> response2) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value()),
                () -> assertThat(response2).extracting("name").doesNotContain(일호선.getName())
        );
    }

    private void 노선_삭제에_실패한다(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }
}
