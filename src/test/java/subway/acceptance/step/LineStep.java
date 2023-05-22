package subway.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.dto.LineSaveRequest;

@SuppressWarnings("NonAsciiCharacters")
public class LineStep {

    public static ExtractableResponse<Response> 노선_생성_요청(final String 노선명, final String 노선색) {
        final LineSaveRequest request = new LineSaveRequest(노선명, 노선색);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }
}
