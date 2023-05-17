package subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

public class CommonSteps {

    public static HttpStatus 정상_요청 = HttpStatus.OK;
    public static HttpStatus 정상_생성 = HttpStatus.CREATED;
    public static HttpStatus 정상_요청이지만_반환값_없음 = HttpStatus.NO_CONTENT;
    public static HttpStatus 비정상_요청 = HttpStatus.BAD_REQUEST;
    public static HttpStatus 비정상_요청_요청한_리소스_찾을_수_없음 = HttpStatus.NOT_FOUND;

    public static void 요청_결과의_상태를_검증한다(final ExtractableResponse<Response> 요청_결과, final HttpStatus 상태) {
        assertThat(요청_결과.statusCode()).isEqualTo(상태.value());
    }

    public static void 요청_결과가_반환하는_리소스의_위차가_존재하는지_확인한다(final ExtractableResponse<Response> 요청_결과) {
        assertThat(요청_결과.header("Location")).isNotBlank();
    }

    public static ExtractableResponse<Response> doPost(final String path, final Object body) {
        return RestAssured
            .given().log().all()
            .body(body)
            .contentType(ContentType.JSON)
            .when().post(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> doGet(final String path) {
        return RestAssured
            .given().log().all()
            .when().get(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> doDelete(final String path) {
        return RestAssured
            .given().log().all()
            .when().delete(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> doPut(final String path, final Object body) {
        return RestAssured
            .given().log().all()
            .body(body)
            .contentType(ContentType.JSON)
            .when().put(path)
            .then().log().all()
            .extract();
    }
}
