package subway.integration.builder;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.application.response.QueryShortestRouteResponse;
import subway.application.response.RouteEdgeResponse;
import subway.integration.support.RestAssuredFixture;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class RouteAssured {

    private RouteAssured() {
    }

    public static QueryShortestRouteResponse 최종_경로_정보_응답(
            final String startStationName,
            final String endStationName,
            final List<String> transferStations,
            final List<RouteEdgeResponse> routeEdges,
            final Integer totalDistance,
            final Integer totalPrice
    ) {
        return new QueryShortestRouteResponse(startStationName, endStationName, transferStations, routeEdges, totalDistance, totalPrice);
    }

    public static RouteEdgeResponse 중간_경로_응답(
            final String startStationName,
            final String endStationName,
            final String lineName,
            final Integer distance
    ) {
        return new RouteEdgeResponse(startStationName, endStationName, lineName, distance);
    }

    public static RouteRequestBuilder request() {
        return new RouteRequestBuilder();
    }

    public static class RouteRequestBuilder {

        private ExtractableResponse<Response> response;

        public RouteRequestBuilder 출발역과_도착역의_최단경로를_조회한다(final String startStationName, final String endStationName) {
            response = RestAssuredFixture.get("/route", Map.of(
                    "startStationName", startStationName,
                    "endStationName", endStationName
            ));
            return this;
        }

        public RouteResponseBuilder response() {
            return new RouteResponseBuilder(response);
        }
    }

    public static class RouteResponseBuilder {
        private ExtractableResponse<Response> response;

        public RouteResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public <T> T toBody(Class<T> cls) {
            return response.as(cls);
        }

        public RouteResponseBuilder 최단_경로가_조회된다(final QueryShortestRouteResponse expect) {
            final QueryShortestRouteResponse response = toBody(QueryShortestRouteResponse.class);

            assertAll(
                    () -> assertThat(response.getStartStation()).isEqualTo(expect.getStartStation()),
                    () -> assertThat(response.getEndStation()).isEqualTo(expect.getEndStation()),
                    () -> assertThat(response.getTotalDistance()).isEqualTo(expect.getTotalDistance()),
                    () -> assertThat(response.getTotalPrice()).isEqualTo(expect.getTotalPrice()),
                    () -> assertThat(response.getTransferStations()).isEqualTo(expect.getTransferStations()),
                    () -> assertThat(response.getSections())
                            .usingRecursiveFieldByFieldElementComparatorIgnoringFields()
                            .isEqualTo(expect.getSections())
            );

            return this;
        }
    }
}
