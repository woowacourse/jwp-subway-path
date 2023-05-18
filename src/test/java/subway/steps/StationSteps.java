package subway.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.dto.StationCreateRequest;
import subway.dto.StationResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class StationSteps {

    public static ExtractableResponse<Response> 역_생성_요청(final StationCreateRequest request) {
        final ExtractableResponse<Response> response = RestAssured
                .given()
                .log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)

                .when()
                .post("/stations")

                .then()
                .log().all()
                .extract();
        return response;
    }

    public static long 역_생성하고_아이디_반환(final StationCreateRequest request) {
        return 역_생성_요청(request).as(StationResponse.class).getId();
    }
}
