package subway.integration.builder;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.application.response.QueryShortestRouteResponse;
import subway.application.response.RouteEdgeResponse;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.integration.support.RestAssuredFixture.get;

public class RouteAssured {

    private RouteAssured() {
    }

    public static QueryShortestRouteResponse 최종_경로_정보_응답(
            final String 출발역명,
            final String 도착역명,
            final List<String> 환승역_목록,
            final List<RouteEdgeResponse> 구간_목록,
            final Integer 총거리,
            final String 총금액
    ) {
        return new QueryShortestRouteResponse(출발역명, 도착역명, 환승역_목록, 구간_목록, 총거리, 총금액);
    }

    public static RouteEdgeResponse 중간_경로_응답(
            final String 출발역명,
            final String 도착역명,
            final String 노선명,
            final Integer 거리
    ) {
        return new RouteEdgeResponse(출발역명, 도착역명, 노선명, 거리);
    }

    public static RouteRequestBuilder 클라이언트_요청() {
        return new RouteRequestBuilder();
    }

    public static class RouteRequestBuilder {

        private ExtractableResponse<Response> response;

        public RouteRequestBuilder 최단경로와_총금액_조회한다(final Integer 승객_나이, final String 출발역명, final String 도착역명) {
            response = get("/routes", Map.of(
                    "passengerAge", 승객_나이,
                    "startStationName", 출발역명,
                    "endStationName", 도착역명
            ));
            return this;
        }

        public RouteResponseBuilder 서버_응답_검증() {
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

        public RouteResponseBuilder 최단_경로_조회_검증(final QueryShortestRouteResponse 최단_경로_응답) {
            final QueryShortestRouteResponse response = toBody(QueryShortestRouteResponse.class);

            assertAll(
                    () -> assertThat(response.getStartStation()).isEqualTo(최단_경로_응답.getStartStation()),
                    () -> assertThat(response.getEndStation()).isEqualTo(최단_경로_응답.getEndStation()),
                    () -> assertThat(response.getTotalDistance()).isEqualTo(최단_경로_응답.getTotalDistance()),
                    () -> assertThat(response.getTotalPrice()).isEqualTo(최단_경로_응답.getTotalPrice()),
                    () -> assertThat(response.getTransferStations()).isEqualTo(최단_경로_응답.getTransferStations()),
                    () -> assertThat(response.getSections())
                            .usingRecursiveFieldByFieldElementComparatorIgnoringFields()
                            .isEqualTo(최단_경로_응답.getSections())
            );

            return this;
        }
    }
}
