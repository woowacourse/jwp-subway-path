package subway.acceptance.route;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.acceptance.CommonSteps.doPost;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import subway.ui.route.find.dto.RouteEdgeResponse;
import subway.ui.route.find.dto.RouteFindRequest;
import subway.ui.route.find.dto.RouteFindResponse;

@SuppressWarnings("NonAsciiCharacters")
class RouteSteps {

    public static ExtractableResponse<Response> 역_거리_계산_요청(final Long 시작역_ID, final Long 도착역_ID) {
        return doPost("/routes", new RouteFindRequest(시작역_ID, 도착역_ID));
    }

    public static void 경로_거리_계산_결과_확인(final ExtractableResponse<Response> 역_거리_계산_요청_결과, final int 거리) {
        assertThat(역_거리_계산_요청_결과.as(RouteFindResponse.class).getDistance()).isEqualTo(거리);
    }

    public static void 경로_확인(final ExtractableResponse<Response> 역_거리_계산_요청_결과, final RouteEdgeResponse... 경로들) {
        assertThat(역_거리_계산_요청_결과.as(RouteFindResponse.class).getStations()).usingRecursiveComparison()
                .isEqualTo(Arrays.asList(경로들));
    }

    public static RouteEdgeResponse 경로_하나_만들기(final Long 시작_역_id, final Long 끝_역_id, final Long 거리, final Long 노선_id) {
        return new RouteEdgeResponse(시작_역_id, 끝_역_id, 거리, 노선_id);
    }

    public static void 경로_금액_계산_결과_확인(final ExtractableResponse<Response> 역_거리_계산_요청_결과, final int 금액) {
        assertThat(역_거리_계산_요청_결과.as(RouteFindResponse.class).getFare()).isEqualTo(금액);
    }
}
