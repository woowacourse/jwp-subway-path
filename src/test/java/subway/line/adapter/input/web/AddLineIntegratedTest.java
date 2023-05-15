package subway.line.adapter.input.web;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.http.HttpStatus;
import subway.integrated.IntegrationTest;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

@SuppressWarnings("NonAsciiCharacters")
class AddLineIntegratedTest extends IntegrationTest {
    @Test
    void 노선_추가하기() {
        // given
        final Map<String, String> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        
        // expect
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith("/lines/"));
    }
    
    @Test
    void 이미_존재하는_노선_이름으로_추가할_시_예외_발생() {
        // given
        final Map<String, String> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith("/lines/"));
        
        // expect
        params.clear();
        params.put("name", "1호선");
        params.put("color", "초록");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(ContentType.JSON)
                .body("message", containsString("1호선"))
                .body("message", containsString("초록"));
    }
    
    @Test
    void 이미_존재하는_노선_색상으로_추가할_시_예외_발생() {
        // given
        final Map<String, String> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith("/lines/"));
        
        // expect
        params.clear();
        params.put("name", "2호선");
        params.put("color", "파랑");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(ContentType.JSON)
                .body("message", containsString("2호선"))
                .body("message", containsString("파랑"));
    }
    
    @ParameterizedTest(name = "{displayName} : name = {0}")
    @NullAndEmptySource
    void 이름이_null_또는_empty일_수_없다(final String name) {
        // given
        final Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", "파랑");
        
        // expect
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(ContentType.JSON)
                .body("message", is("[ERROR] 노선의 이름은 null 또는 빈값일 수 없습니다."));
    }
    
    @ParameterizedTest(name = "{displayName} : color = {0}")
    @NullAndEmptySource
    void 색상이_null_또는_empty일_수_없다(final String color) {
        // given
        final Map<String, String> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", color);
        
        // expect
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(ContentType.JSON)
                .body("message", is("[ERROR] 노선의 색상은 null 또는 빈값일 수 없습니다."));
    }
}
