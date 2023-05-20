package subway.acceptance.line;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static subway.acceptance.common.JsonMapper.toJson;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.line.presentation.request.AddStationToLineRequest;
import subway.line.presentation.request.DeleteStationFromLineRequest;

@SuppressWarnings("NonAsciiCharacters")
public class LineStationSteps {

    public static ExtractableResponse<Response> 노선에_역_추가_요청(
            final String 노선,
            final String 상행역,
            final String 하행역,
            final Integer 거리
    ) {
        final AddStationToLineRequest request =
                new AddStationToLineRequest(노선, 상행역, 하행역, 거리);
        return 노선에_역_추가_요청(request);
    }

    public static ExtractableResponse<Response> 노선에_역_추가_요청(final AddStationToLineRequest request) {
        final String body = toJson(request);
        return given().log().all()
                .contentType(JSON)
                .body(body)
                .when()
                .post("/lines/stations")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선에서_역_제거_요청(
            final String 노선,
            final String 제거할_역
    ) {
        final DeleteStationFromLineRequest request = new DeleteStationFromLineRequest(노선, 제거할_역);
        return 노선에서_역_제거_요청(request);
    }

    public static ExtractableResponse<Response> 노선에서_역_제거_요청(final DeleteStationFromLineRequest 노선에서_역_제거_요청) {
        final String body = toJson(노선에서_역_제거_요청);
        return given().log().all()
                .contentType(JSON)
                .body(body)
                .when()
                .delete("/lines/stations")
                .then()
                .log().all()
                .extract();
    }
}
