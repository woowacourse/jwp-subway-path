package subway.acceptance.station;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static subway.acceptance.common.JsonMapper.toJson;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import subway.line.presentation.request.StationCreateRequest;

@SuppressWarnings("NonAsciiCharacters")
public class StationSteps {

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
                .post("/stations")
                .then()
                .log().all()
                .extract();
    }

    public static void 역들을_생성한다(final String... names) {
        Arrays.stream(names)
                .forEach(StationSteps::역_생성_요청);
    }
}
