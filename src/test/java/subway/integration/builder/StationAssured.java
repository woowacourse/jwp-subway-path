package subway.integration.builder;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.dto.StationResponse;
import subway.integration.support.RestAssuredFixture;
import subway.application.request.CreateStationRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static subway.integration.support.RestAssuredFixture.*;

public class StationAssured {

    private StationAssured() {
    }

    public static CreateStationRequest 역_요청(final String stationName) {
        return new CreateStationRequest(stationName);
    }

    public static StationRequestBuilder request() {
        return new StationRequestBuilder();
    }

    public static class StationRequestBuilder {

        private ExtractableResponse<Response> response;

        public StationRequestBuilder 역을_등록한다(final CreateStationRequest request) {
            response = post("/v2/stations", request);
            return this;
        }

        public StationRequestBuilder 역을_조회한다(final Long stationId) {
            response = RestAssuredFixture.get("/v2/stations/" + stationId);
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

        public void 등록된_역이_조회된다(final Long stationId, final String stationName) {
            final StationResponse response = toBody(StationResponse.class);

            assertThat(response)
                    .usingRecursiveComparison()
                    .isEqualTo(new StationResponse(stationId, stationName));
        }
    }
}
