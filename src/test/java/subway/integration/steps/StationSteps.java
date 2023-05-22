package subway.integration.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.dto.request.StationRequest;

public class StationSteps {

    public static ExtractableResponse<Response> 전체_역_조회_요청() {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)

                .when()
                .get("/stations")

                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 단일_역_조회_요청(final long stationId) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)

                .when()
                .get("/stations/" + stationId)

                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 역_생성_요청(final StationRequest request) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)

                .when()
                .post("/stations")

                .then()
                .extract();
    }

    public static long 역_생성하고_아이디_반환(final StationRequest request) {
        final ExtractableResponse<Response> response = 역_생성_요청(request);
        return 응답에서_역_아이디_추출(response);
    }

    public static long 응답에서_역_아이디_추출(final ExtractableResponse<Response> 역_생성_응답) {
        final String location = 역_생성_응답.header("location");
        return Long.parseLong(location.split("/")[2]);
    }

    public static ExtractableResponse<Response> 역_수정_요청(final long stationId, final StationRequest request) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)

                .when()
                .put("/stations/" + stationId)

                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 역_삭제_요청(final long stationId) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)

                .when()
                .delete("/stations/" + stationId)

                .then()
                .extract();
    }

}
