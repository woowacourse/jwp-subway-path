package subway.integration.station;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static subway.integration.common.JsonMapper.toJson;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.dto.request.StationCreateRequest;

@SuppressWarnings("NonAsciiCharacters")
public class StationSteps {

    private static final String API_URL = "/stations";

    public static ExtractableResponse<Response> 역_생성_요청(final String stationName) {
        final StationCreateRequest request = new StationCreateRequest(stationName);
        return 역_생성_요청(request);
    }

    public static ExtractableResponse<Response> 역_생성_요청(final StationCreateRequest request) {
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
}
