package subway.integration.builder;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.application.request.CreateSectionRequest;
import subway.application.response.StationResponse;
import subway.integration.support.RestAssuredFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.integration.support.RestAssuredFixture.post;

public class StationAssured {

    private StationAssured() {
    }

    public static CreateSectionRequest 상행역_하행역_노선_거리_요청(
            final String upStationName,
            final String downStationName,
            final Long lineId,
            final Integer distance
    ) {
        return new CreateSectionRequest(upStationName, downStationName, lineId, distance);
    }

    public static StationRequestBuilder request() {
        return new StationRequestBuilder();
    }

    public static class StationRequestBuilder {

        private ExtractableResponse<Response> response;

        public StationRequestBuilder 역과_구간을_등록한다(final CreateSectionRequest request) {
            response = post("/stations", request);
            return this;
        }

        public StationRequestBuilder 역을_조회한다(final Long stationId) {
            response = RestAssuredFixture.get("/stations/" + stationId);
            return this;
        }

        public StationResponseBuilder response() {
            return new StationResponseBuilder(response);
        }
    }

    public static class StationResponseBuilder {
        private ExtractableResponse<Response> response;

        public StationResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public <T> T toBody(Class<T> cls) {
            return response.as(cls);
        }

        public <T> List<T> toBodies(Class<T> cls) {
            return response.jsonPath().getList("", cls);
        }

        public void 등록된_역이_조회된다(final String stationName) {
            final StationResponse response = toBody(StationResponse.class);

            assertThat(response)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(new StationResponse(0L, stationName));
        }
    }
}
