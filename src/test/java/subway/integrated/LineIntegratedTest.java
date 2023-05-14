package subway.integrated;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

@SuppressWarnings("NonAsciiCharacters")
class LineIntegratedTest extends IntegrationTest {
    @Test
    void 노선_추가하기() {
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
    }
}
