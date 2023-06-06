package subway.route.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.common.acceptance.CommonSteps.doPost;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import subway.route.dto.request.RouteFindRequest;
import subway.route.dto.response.RouteEdgeResponse;
import subway.route.dto.response.RouteFindResponse;

@SuppressWarnings("NonAsciiCharacters")
class RouteSteps {

    public static ExtractableResponse<Response> 역_거리_계산_요청(Long 시작역_ID, Long 도착역_ID) {
        return doPost("/routes", new RouteFindRequest(시작역_ID, 도착역_ID));
    }

    public static void 경로_거리_계산_결과_확인(ExtractableResponse<Response> 역_거리_계산_요청_결과, int 거리) {
        assertThat(역_거리_계산_요청_결과.as(RouteFindResponse.class).getDistance()).isEqualTo(거리);
    }

    public static void 경로_확인(ExtractableResponse<Response> 역_거리_계산_요청_결과, RouteEdgeResponse... 경로들) {
        assertThat(역_거리_계산_요청_결과.as(RouteFindResponse.class).getStations()).usingRecursiveComparison()
                .isEqualTo(Arrays.asList(경로들));
    }

    public static RouteEdgeResponse 경로_하나_만들기(Long 시작_역_id, Long 끝_역_id, Long 거리, Long 노선_id) {
        return new RouteEdgeResponse(시작_역_id, 끝_역_id, 거리, 노선_id);
    }

    public static void 경로_금액_계산_결과_확인(ExtractableResponse<Response> 역_거리_계산_요청_결과, int 금액) {
        assertThat(역_거리_계산_요청_결과.as(RouteFindResponse.class).getFare()).isEqualTo(금액);
    }
}
