package subway.integration.support;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static org.springframework.http.MediaType.*;

public class RestAssuredFixture {

    public static ExtractableResponse<Response> post(final String url, final Object body) {
        return RestAssured
                .given()
                        .log().all()
                        .contentType(APPLICATION_JSON_VALUE)
                        .body(body)

                .when()
                        .post(url)

                .then()
                        .log().all()
                        .extract();
    }

    public static ExtractableResponse<Response> get(final String url) {
        return RestAssured
                .given()
                        .log().all()
                        .contentType(APPLICATION_JSON_VALUE)

                .when()
                        .get(url)

                .then()
                        .log().all()
                        .extract();
    }
}
