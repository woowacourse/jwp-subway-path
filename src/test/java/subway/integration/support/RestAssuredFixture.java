package subway.integration.support;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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

    public static ExtractableResponse<Response> get(final String url, final Map<String, Object> params) {
        return RestAssured
                .given()
                        .log().all()
                        .contentType(APPLICATION_JSON_VALUE)
                        .params(params)

                .when()
                       .get(url)

                .then()
                        .log().all()
                        .extract();
    }

    public static ExtractableResponse<Response> delete(final String url, final Object body) {
        return RestAssured
                .given()
                        .log().all()
                        .contentType(APPLICATION_JSON_VALUE)
                        .body(body)

                .when()
                        .delete(url)

                .then()
                        .log().all()
                        .extract();
    }
}
