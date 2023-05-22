package subway.integration.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class PathSteps {

    public static ExtractableResponse<Response> 경로_조회_요청(final long from, final long to, final int age) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)

                .when()
                .get("/paths?from=" + from + "&to=" + to + "&age=" + age)

                .then()
                .extract();
    }
}
