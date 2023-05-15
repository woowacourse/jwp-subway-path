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
            final String startStationName,
            final String endStationName
    ) {
        return given()
                .log().all()
                .param("startStationName", startStationName)
                .param("endStationName", endStationName)
                .when()
                .get("/path/shortest")
                .then()
                .log().all()
                .extract();
    }

    public static void 최단경로의_총_길이는(final ShortestRouteResponse response, final int distance) {
        assertThat(response.getTotalDistance()).isEqualTo(distance);
    }

    public static void 최단경로의_환승역은(final ShortestRouteResponse response, final String... transferStations) {
        assertThat(response.getTransferCount()).isEqualTo(transferStations.length);
        assertThat(response.getTransferStations())
                .containsExactly(transferStations);
    }

    public static void 최단경로의_각_구간은(final ShortestRouteResponse response, final String... sectionInfos) {
        assertThat(response.getSectionInfos())
                .extracting(it -> String.format("[%s: (%s) -> (%s), %dkm]",
                        it.getLine(), it.getFromStation(), it.getToStation(), it.getDistance()))
                .containsExactly(sectionInfos);
    }

    public static void 최단경로의_요금은(final ShortestRouteResponse response, final int fee) {
        assertThat(response.getTotalFee()).isEqualTo(fee);
    }

    public static void 경로가_없다(final ExceptionResponse response) {
        assertThat(response.getCode()).isEqualTo(String.valueOf(NO_PATH.errorCode()));
        assertThat(response.getMessage()).isEqualTo(NO_PATH.errorMessage());
    }
}
