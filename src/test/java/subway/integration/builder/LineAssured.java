package subway.integration.builder;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.application.request.CreateLineRequest;
import subway.application.response.LineResponse;
import subway.application.response.StationResponse;
import subway.integration.support.RestAssuredFixture;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.integration.support.RestAssuredFixture.post;

public class LineAssured {

    private LineAssured() {
    }

    public static CreateLineRequest 노선_요청_데이터(final String 노선명, final String 노선_색상) {
        return new CreateLineRequest(노선명, 노선_색상);
    }

    public static LineRequestBuilder 클라이언트_요청() {
        return new LineRequestBuilder();
    }

    public static class LineRequestBuilder {

        private ExtractableResponse<Response> response;

        public LineRequestBuilder 노선을_등록한다(final CreateLineRequest 노선_요청_데이터) {
            response = post("/lines", 노선_요청_데이터);
            return this;
        }

        public LineRequestBuilder 노선을_조회한다(final Long 노선_식별자값) {
            response = RestAssuredFixture.get("/lines/" + 노선_식별자값);
            return this;
        }

        public LineResponseBuilder 서버_응답_검증() {
            return new LineResponseBuilder(response);
        }
    }

    public static class LineResponseBuilder {
        private ExtractableResponse<Response> response;

        public LineResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public <T> T 서버_응답_추출(Class<T> cls) {
            return response.as(cls);
        }

        public void 노선이_조회된다(final Long 노선_식별자값, final String 노선명, final String 노선_색상, final List<String> 노선_역_목록) {
            final LineResponse response = 서버_응답_추출(LineResponse.class);

            final List<String> responseStationNames = response.getStations()
                    .stream()
                    .map(StationResponse::getName)
                    .collect(Collectors.toList());

            assertAll(
                    () -> assertThat(response.getId()).isEqualTo(노선_식별자값),
                    () -> assertThat(response.getName()).isEqualTo(노선명),
                    () -> assertThat(response.getColor()).isEqualTo(노선_색상),
                    () -> assertThat(responseStationNames).containsExactlyElementsOf(노선_역_목록)
            );
        }
    }
}
