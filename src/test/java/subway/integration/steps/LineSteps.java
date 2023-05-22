package subway.integration.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.dto.request.LineRequest;

@SuppressWarnings("NonAsciiCharacters")
public class LineSteps {

    public static ExtractableResponse<Response> 전체_노선_조회_요청() {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)

                .when()
                .get("/lines")

                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 단일_노선_조회_요청(final long lineId) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)

                .when()
                .get("/lines/" + lineId)

                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_생성_요청(final LineRequest request) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)

                .when()
                .post("/lines")

                .then()
                .extract();
    }

    public static long 노선_생성하고_아이디_반환(final LineRequest request) {
        final ExtractableResponse<Response> response = 노선_생성_요청(request);
        return 응답에서_노선_아이디_추출(response);
    }

    public static long 응답에서_노선_아이디_추출(final ExtractableResponse<Response> 노선_생성_응답) {
        final String location = 노선_생성_응답.header("location");
        return Long.parseLong(location.split("/")[2]);
    }

    public static ExtractableResponse<Response> 노선_수정_요청(final long lineId, final LineRequest request) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)

                .when()
                .put("/lines/" + lineId)

                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_삭제_요청(final long lineId) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)

                .when()
                .delete("/lines/" + lineId)

                .then()
                .extract();
    }

}
