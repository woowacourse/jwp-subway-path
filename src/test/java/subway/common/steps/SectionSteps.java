package subway.common.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.domain.core.Section;
import subway.dto.StationDeleteRequest;
import subway.dto.StationInitialSaveRequest;
import subway.dto.StationSaveRequest;

@SuppressWarnings("NonAsciiCharacters")
public class SectionSteps {

    public static final String 왼쪽 = "LEFT";
    public static final String 오른쪽 = "RIGHT";

    public static ExtractableResponse<Response> 노선에_구간이_존재하지_않을_때_초기_구간_생성_요청(
            final String 노선명,
            final String 상행종점역,
            final String 하행종점역,
            final int 거리
    ) {
        final StationInitialSaveRequest request = new StationInitialSaveRequest(노선명, 상행종점역, 하행종점역, 거리);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/stations/init")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 구간_생성_요청(
            final String 노선명,
            final String 기준역,
            final String 추가역,
            final String 방향,
            final int 거리
    ) {
        final StationSaveRequest request = new StationSaveRequest(노선명, 기준역, 추가역, 방향, 거리);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 구간_삭제_요청(final String 노선명, final String 삭제역) {
        final StationDeleteRequest request = new StationDeleteRequest(노선명, 삭제역);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().delete("/stations")
                .then().log().all()
                .extract();
    }

    public static Section 구간(final String 상행역, final String 하행역, final int 거리) {
        return new Section(상행역, 하행역, 거리);
    }
}
