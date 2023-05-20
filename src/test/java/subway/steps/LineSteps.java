package subway.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.dto.InitialSectionCreateRequest;
import subway.dto.LineCreateRequest;
import subway.dto.LineResponse;
import subway.dto.SectionCreateRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class LineSteps {

    public static final LineCreateRequest 노선_9호선 = new LineCreateRequest("9호선", "BROWN");
    public static final LineCreateRequest 노선_3호선 = new LineCreateRequest("3호선", "ORANGE");

    public static ExtractableResponse<Response> 전체_노선_조회_요청() {
        return RestAssured
                .given()
                .contentType(APPLICATION_JSON_VALUE)

                .when()
                .get("/lines")

                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_생성_요청(final LineCreateRequest request) {
        return RestAssured
                .given()
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)

                .when()
                .post("/lines")

                .then()
                .extract();
    }

    public static long 노선_생성하고_아이디_반환(final LineCreateRequest request) {
        return 노선_생성_요청(request).as(LineResponse.class).getId();
    }

    public static ExtractableResponse<Response> 노선에_최초의_역_2개_추가_요청(final InitialSectionCreateRequest request) {
        return RestAssured
                .given()
                .log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)

                .when()
                .post("/lines/" + request.getLineId() + "/station-init")

                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 존재하는_노선에_역_1개_추가_요청(final Long lineId, final SectionCreateRequest request) {
        return RestAssured
                .given()
                .log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)

                .when()
                .post("/lines/" + lineId + "/stations")

                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 두_역_사이의_최단_경로_조회_요청(final long sourceId, final long targetId) {
        return RestAssured
                .given()
                .log().all()

                .when()
                .get("/paths?source=" + sourceId + "&target=" + targetId)

                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 나이를_포함해_두_역_사이의_최단_경로_조회_요청(final long sourceId, final long targetId, final int age) {
        return RestAssured
                .given()
                .log().all()

                .when()
                .get("/paths?source=" + sourceId + "&target=" + targetId + "&age=" + age)

                .then()
                .extract();
    }
}
