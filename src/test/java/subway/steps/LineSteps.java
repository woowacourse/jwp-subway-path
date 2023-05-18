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

    public static ExtractableResponse<Response> 전체_노선_조회_요청() {
        final ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(APPLICATION_JSON_VALUE)

                .when()
                .get("/lines")

                .then()
                .log().all()
                .extract();
        return response;
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

    public static ExtractableResponse<Response> 노선에_최초의_역_2개_추가_요청(final LineResponse lineResponse, final InitialSectionCreateRequest initialSectionCreateRequest) {
        return RestAssured
                .given()
                .contentType(APPLICATION_JSON_VALUE)
                .body(initialSectionCreateRequest)

                .when()
                .post("/lines/" + lineResponse.getId() + "/station-init")

                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 노선에_최초의_역_2개_추가_요청(final Long lineId, final InitialSectionCreateRequest initialSectionCreateRequest) {
        return RestAssured
                .given()
                .contentType(APPLICATION_JSON_VALUE)
                .body(initialSectionCreateRequest)

                .when()
                .post("/lines/" + lineId + "/station-init")

                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 존재하는_노선에_역_1개_추가_요청(final long station1Id, final long station2Id, final Long lineId) {
        final SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(
                station1Id,
                station2Id,
                3);

        return RestAssured
                .given()
                .contentType(APPLICATION_JSON_VALUE)
                .body(sectionCreateRequest)

                .when()
                .post("/lines/" + lineId + "/stations")

                .then()
                .extract();
    }
}
