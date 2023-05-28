package subway.integration.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class LineStep {
    public static ExtractableResponse<Response> 노선_생성_요청(final LineRequest lineRequest) {
        return RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static LineRequest 노선_생성_요청을_생성한다(final String 노선_이름) {
        return new LineRequest(노선_이름);
    }

    public static ExtractableResponse<Response> 노선_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 특정_노선_조회_요청(final Long 노선_ID) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{노선_ID}", 노선_ID)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_삭제_요청(final Long 노선_ID, final Long 역_ID) {
        return RestAssured.given().log().all()
                .when().delete("/lines/" + 노선_ID + "/stations/" + 역_ID)
                .then().log().all()
                .extract();
    }

}
