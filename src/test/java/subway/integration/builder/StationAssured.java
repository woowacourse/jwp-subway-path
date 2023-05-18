package subway.integration.builder;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.application.request.CreateSectionRequest;
import subway.application.request.DeleteStationRequest;
import subway.application.response.StationResponse;
import subway.integration.support.RestAssuredFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.integration.support.RestAssuredFixture.post;

public class StationAssured {

    private StationAssured() {
    }

    public static CreateSectionRequest 구간_요청_데이터(
            final String 상행역명,
            final String 하행역명,
            final Long 노선_식별자값,
            final Integer 거리
    ) {
        return new CreateSectionRequest(상행역명, 하행역명, 노선_식별자값, 거리);
    }

    public static DeleteStationRequest 역_삭제_요청_데이터(
            final String 역명,
            final String 노선명
    ) {
        return new DeleteStationRequest(역명, 노선명);
    }

    public static StationRequestBuilder 클라이언트_요청() {
        return new StationRequestBuilder();
    }

    public static class StationRequestBuilder {

        private ExtractableResponse<Response> response;

        public StationRequestBuilder 역과_구간을_등록한다(final CreateSectionRequest 구간_요청_데이터) {
            response = post("/stations", 구간_요청_데이터);
            return this;
        }

        public StationRequestBuilder 역을_조회한다(final Long stationId) {
            response = RestAssuredFixture.get("/stations/" + stationId);
            return this;
        }

        public StationRequestBuilder 역과_구간을_삭제한다(final DeleteStationRequest request) {
            response = RestAssuredFixture.delete("/stations", request);
            return this;
        }

        public StationResponseBuilder 서버_응답_검증() {
            return new StationResponseBuilder(response);
        }
    }

    public static class StationResponseBuilder {
        private ExtractableResponse<Response> response;

        public StationResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public <T> T toBody(Class<T> 응답_추출_타입) {
            return response.as(응답_추출_타입);
        }

        public <T> List<T> toBodies(Class<T> cls) {
            return response.jsonPath().getList("", cls);
        }

        public StationResponseBuilder 등록된_역이_조회된다(final String 역명) {
            final StationResponse response = toBody(StationResponse.class);

            assertThat(response)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(new StationResponse(0L, 역명));

            return this;
        }
    }
}
