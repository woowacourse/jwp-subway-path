package subway.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class PathStep {

    public static ExtractableResponse<Response> 최단_경로_조회_요청(final String 시작역, final String 도착역) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("startStationName", 시작역)
                .param("endStationName", 도착역)
                .when().get("/path")
                .then().log().all()
                .extract();
    }

    public static void 최단_경로_조회_결과를_확인한다(
            final ExtractableResponse<Response> 요청결과,
            final int 거리,
            final int 요금,
            final String... 경로
    ) {
        assertAll(
                () -> assertThat(요청결과.body().jsonPath().getList("path")).containsExactly(경로),
                () -> assertThat(요청결과.body().jsonPath().getInt("distance")).isEqualTo(거리),
                () -> assertThat(요청결과.body().jsonPath().getInt("fare")).isEqualTo(요금)
        );
    }
}
