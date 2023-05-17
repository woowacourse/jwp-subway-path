package subway.line.adapter.input.web;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.integrated.IntegrationTest;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

@SuppressWarnings("NonAsciiCharacters")
class DeleteLineIntegratedTest extends IntegrationTest {
    @Test
    void lineId로_노선을_삭제한다() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        
        // expect
        final String locationStartsWith = "/lines/";
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith(locationStartsWith))
                .extract();
        
        final String lineId = response.header("Location").substring(locationStartsWith.length());
        
        // expect
        RestAssured.given().log().all()
                .when().delete("/lines/" + Long.parseLong(lineId))
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
    
    @Test
    void 존재하지_않는_노선을_삭제하려고_할_시_예외_발생() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        
        // expect
        final String locationStartsWith = "/lines/";
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith(locationStartsWith))
                .extract();
        
        final String lineId = response.header("Location").substring(locationStartsWith.length());
        
        // expect
        RestAssured.given().log().all()
                .when().delete("/lines/" + Long.parseLong(lineId) + 1L)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is("[ERROR] 존재하지 않는 노선을 지정하셨습니다."));
    }
    
    @Test
    void lineId가_null일_시_예외_발생() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        
        // expect
        final String locationStartsWith = "/lines/";
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith(locationStartsWith));
        
        // expect
        RestAssured.given().log().all()
                .when().delete("/lines/" + null)
                .then().log().all()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body("message", is("[ERROR] 서버가 응답할 수 없습니다."));
    }
}