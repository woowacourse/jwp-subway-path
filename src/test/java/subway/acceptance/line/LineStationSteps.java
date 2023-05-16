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
            final String lineName,
            final String upStationName,
            final String downStationName,
            final Integer distance
    ) {
        final AddStationToLineRequest request =
                new AddStationToLineRequest(lineName, upStationName, downStationName, distance);
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

    public static ExtractableResponse<Response> 노선에_역_제거_요청(
            final String lineName,
            final String deleteStationName
    ) {
        final DeleteStationFromLineRequest request = new DeleteStationFromLineRequest(lineName, deleteStationName);
        return 노선에_역_제거_요청(request);
    }

    public static ExtractableResponse<Response> 노선에_역_제거_요청(final DeleteStationFromLineRequest request) {
        final String body = toJson(request);
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
