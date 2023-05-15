package subway.station.adapter.input.web;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.integrated.IntegrationTest;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

@SuppressWarnings("NonAsciiCharacters")
class InitAddStationIntegratedTest extends IntegrationTest {
    @Test
    void 지정한_노선에_최초로_역을_추가한다() {
        // given
        final Map<String, Object> params = new HashMap<>();
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
        
        params.clear();
        params.put("firstStation", "잠실역");
        params.put("secondStation", "선릉역");
        params.put("distance", 3L);
        params.put("lineId", 1L);

        // expect
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations/init")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith("/stations"));
    }
}
