package subway.acceptance.path;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static subway.path.exception.PathExceptionType.NO_PATH;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.common.exception.ExceptionResponse;
import subway.path.application.dto.ShortestRouteResponse;

@SuppressWarnings("NonAsciiCharacters")
public class PathSteps {

    public static ExtractableResponse<Response> 최단경로_조회_요청(
            final String 출발역,
            final String 도착역
    ) {
        return given()
                .log().all()
                .param("startStationName", 출발역)
                .param("endStationName", 도착역)
                .when()
                .get("/path/shortest")
                .then()
                .log().all()
                .extract();
    }

    public static void 최단경로의_총_길이는(final ShortestRouteResponse 최단경로_조회_응답, final int 거리) {
        assertThat(최단경로_조회_응답.getTotalDistance()).isEqualTo(거리);
    }

    public static void 최단경로의_환승역은(final ShortestRouteResponse 최단경로_조회_응답, final String... 환승역들) {
        assertThat(최단경로_조회_응답.getTransferCount()).isEqualTo(환승역들.length);
        assertThat(최단경로_조회_응답.getTransferStations())
                .containsExactly(환승역들);
    }

    public static void 최단경로의_각_구간은(final ShortestRouteResponse 최단경로_조회_응답, final String... 구간_정보들) {
        assertThat(최단경로_조회_응답.getSectionInfos())
                .extracting(it -> String.format("[%s: (%s) -> (%s), %dkm]",
                        it.getLine(), it.getFromStation(), it.getToStation(), it.getDistance()))
                .containsExactly(구간_정보들);
    }

    public static void 최단경로의_요금은(final ShortestRouteResponse 최단경로_조회_응답, final String... 가격_정보들) {
        assertThat(최단경로_조회_응답.getFeeInfos())
                .extracting(it -> it.getInfo() + ": " + it.getFee())
                .containsExactly(가격_정보들);
    }

    public static void 경로가_없다(final ExceptionResponse 최단경로_조회_응답) {
        assertThat(최단경로_조회_응답.getCode()).isEqualTo(String.valueOf(NO_PATH.errorCode()));
        assertThat(최단경로_조회_응답.getMessage()).isEqualTo(NO_PATH.errorMessage());
    }
}
