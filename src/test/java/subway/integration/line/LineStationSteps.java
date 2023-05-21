package subway.integration.line;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static subway.integration.common.JsonMapper.toJson;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.dto.request.StationAddToLineRequest;
import subway.dto.request.StationDeleteFromLineRequest;

@SuppressWarnings("NonAsciiCharacters")
public class LineStationSteps {

    private static final String API_URL = "/lines/stations";

    public static ExtractableResponse<Response> 노선에_역_추가_요청(
            final String lineName,
            final String upStationName,
            final String downStationName,
            final Integer distance
    ) {
        final StationAddToLineRequest request =
                new StationAddToLineRequest(lineName, upStationName, downStationName, distance);
        return 노선에_역_추가_요청(request);
    }

    public static ExtractableResponse<Response> 노선에_역_추가_요청(final StationAddToLineRequest request) {
        final String body = toJson(request);
        return given().log().all()
                .contentType(JSON)
                .body(body)
                .when()
                .post(API_URL)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선에_역_제거_요청(
            final String lineName,
            final String deleteStationName
    ) {
        final StationDeleteFromLineRequest request = new StationDeleteFromLineRequest(lineName, deleteStationName);
        return 노선에_역_제거_요청(request);
    }

    public static ExtractableResponse<Response> 노선에_역_제거_요청(final StationDeleteFromLineRequest request) {
        final String body = toJson(request);
        return given().log().all()
                .contentType(JSON)
                .body(body)
                .when()
                .delete(API_URL)
                .then()
                .log().all()
                .extract();
    }
}
