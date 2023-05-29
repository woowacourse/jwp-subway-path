package subway.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.domain.Direction;
import subway.dto.StationDeleteRequest;
import subway.dto.StationInitialSaveRequest;
import subway.dto.StationSaveRequest;

@SuppressWarnings("NonAsciiCharacters")
public class StationStep {

    public static ExtractableResponse<Response> 역_등록_요청(
            final String 노선명,
            final String 기준역명,
            final String 추가할_역명,
            final Direction 추가할_방향,
            final int 거리
    ) {
        final StationSaveRequest request = new StationSaveRequest(노선명, 기준역명, 추가할_역명, 추가할_방향, 거리);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선에_역이_없는_경우_역_등록_요청(
            final String 노선명,
            final String 상행종점,
            final String 하행종점,
            final int 거리
    ) {
        final StationInitialSaveRequest request = new StationInitialSaveRequest(노선명, 상행종점, 하행종점, 거리);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/stations/init")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 역_삭제_요청(final String 노선명, final String 역명) {
        final StationDeleteRequest request = new StationDeleteRequest(노선명, 역명);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().delete("/stations")
                .then().log().all()
                .extract();
    }
}
