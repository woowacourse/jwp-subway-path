package subway.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.domain.Direction;
import subway.dto.StationInitialSaveRequest;
import subway.dto.StationSaveRequest;

@SuppressWarnings("NonAsciiCharacters")
public class SectionStep {

    public static ExtractableResponse<Response> 초기_구간_생성_요청(final String 노선명, final String 상행종점역, final String 하행종점역, final int 거리) {
        final StationInitialSaveRequest request = new StationInitialSaveRequest(노선명, 상행종점역, 하행종점역, 거리);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/stations/init")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 구간_생성_요청(final String 노선명, final String 기준역, final String 추가할역, final Direction 방향, final int 거리) {
        final StationSaveRequest request = new StationSaveRequest(노선명, 기준역, 추가할역, 방향, 거리);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }
}
