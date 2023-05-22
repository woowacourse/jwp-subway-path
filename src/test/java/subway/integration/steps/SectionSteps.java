package subway.integration.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.dto.request.SectionCreationRequest;

public class SectionSteps {

    public static ExtractableResponse<Response> 노선에_구간_생성_요청(final long lineId, final SectionCreationRequest request) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)

                .when()
                .post("/lines/" + lineId + "/stations")

                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 노선에서_역_삭제_요청(final long lineId, final long stationId) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)

                .when()
                .delete("/lines/" + lineId + "/stations/" + stationId)

                .then()
                .extract();
    }
}
