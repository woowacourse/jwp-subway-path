package subway.station.adapter.input.web.integrated;

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
class DeleteStationOnAllLinesIntegratedTest extends IntegrationTest {
    @Test
    void 해당_노선의_가장_왼쪽_역을_삭제한다() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        
        final String locationStartsWith = "/lines/";
        final ExtractableResponse<Response> response1 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith(locationStartsWith))
                .extract();
        
        final String lineId = response1.header("Location").substring(locationStartsWith.length());
        params.clear();
        params.put("firstStation", "잠실역");
        params.put("secondStation", "선릉역");
        params.put("distance", 5L);
        params.put("lineId", Long.parseLong(lineId));
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations/init")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", is("/stations"));
        
        params.clear();
        params.put("lineId", Long.parseLong(lineId));
        params.put("baseStation", "잠실역");
        params.put("direction", "LEFT");
        params.put("additionalStation", "청라역");
        params.put("distance", 5L);
        
        final ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith("/stations/"))
                .extract();
        
        final String stationId = response2.header("Location").substring("/stations/".length());
        
        // expect
        // 청라 - 잠실 - 선릉
        RestAssured.given().log().all()
                .when().delete("/stations/" + Long.parseLong(stationId))
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
    
    @Test
    void 해당_노선의_가장_오른쪽_역을_삭제한다() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        
        final String locationStartsWith = "/lines/";
        final ExtractableResponse<Response> response1 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith(locationStartsWith))
                .extract();
        
        final String lineId = response1.header("Location").substring(locationStartsWith.length());
        params.clear();
        params.put("firstStation", "잠실역");
        params.put("secondStation", "선릉역");
        params.put("distance", 5L);
        params.put("lineId", Long.parseLong(lineId));
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations/init")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", is("/stations"));
        
        params.clear();
        params.put("lineId", Long.parseLong(lineId));
        params.put("baseStation", "선릉역");
        params.put("direction", "RIGHT");
        params.put("additionalStation", "청라역");
        params.put("distance", 5L);
        
        final ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith("/stations/"))
                .extract();
        
        final String stationId = response2.header("Location").substring("/stations/".length());
        
        // expect
        // 잠실 - 선릉 - 청라
        RestAssured.given().log().all()
                .when().delete("/stations/" + Long.parseLong(stationId))
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
    
    @Test
    void 해당_노선의_중간_역을_삭제한다() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        
        final String locationStartsWith = "/lines/";
        final ExtractableResponse<Response> response1 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith(locationStartsWith))
                .extract();
        
        final String lineId = response1.header("Location").substring(locationStartsWith.length());
        params.clear();
        params.put("firstStation", "잠실역");
        params.put("secondStation", "선릉역");
        params.put("distance", 5L);
        params.put("lineId", Long.parseLong(lineId));
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations/init")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", is("/stations"));
        
        params.clear();
        params.put("lineId", Long.parseLong(lineId));
        params.put("baseStation", "잠실역");
        params.put("direction", "RIGHT");
        params.put("additionalStation", "청라역");
        params.put("distance", 3L);
        
        final ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith("/stations/"))
                .extract();
        
        final String stationId = response2.header("Location").substring("/stations/".length());
        
        // expect
        // 잠실 - 청라 - 선릉
        RestAssured.given().log().all()
                .when().delete("/stations/" + Long.parseLong(stationId))
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
    
    @Test
    void 두개의_역만_존재할_시_나머지_구간을_지운다() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        
        final String locationStartsWith = "/lines/";
        final ExtractableResponse<Response> response1 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith(locationStartsWith))
                .extract();
        
        final String lineId = response1.header("Location").substring(locationStartsWith.length());
        params.clear();
        params.put("firstStation", "잠실역");
        params.put("secondStation", "선릉역");
        params.put("distance", 5L);
        params.put("lineId", Long.parseLong(lineId));
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations/init")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", is("/stations"));
        
        params.clear();
        params.put("lineId", Long.parseLong(lineId));
        params.put("baseStation", "잠실역");
        params.put("direction", "LEFT");
        params.put("additionalStation", "청라역");
        params.put("distance", 5L);
        
        final ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith("/stations/"))
                .extract();
        
        final String stationId = response2.header("Location").substring("/stations/".length());
        
        params.clear();
        params.put("lineId", Long.parseLong(lineId));
        params.put("stationId", Long.parseLong(stationId));
        
        // 청라 - 잠실 - 선릉
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().delete("/stations/one")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
        
        // expect
        // 청라 - 선릉
        RestAssured.given().log().all()
                .when().delete("/stations/" + (Long.parseLong(stationId) - 1L))
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
    
    @Test
    void 모든_노선에_존재하지_않는_역이면_예외_발생() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        
        final String locationStartsWith = "/lines/";
        final ExtractableResponse<Response> response1 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith(locationStartsWith))
                .extract();
        
        final String lineId = response1.header("Location").substring(locationStartsWith.length());
        params.clear();
        params.put("firstStation", "잠실역");
        params.put("secondStation", "선릉역");
        params.put("distance", 5L);
        params.put("lineId", Long.parseLong(lineId));
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations/init")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", is("/stations"));
        
        params.clear();
        params.put("lineId", Long.parseLong(lineId));
        params.put("baseStation", "잠실역");
        params.put("direction", "RIGHT");
        params.put("additionalStation", "청라역");
        params.put("distance", 3L);
        
        final ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith("/stations/"))
                .extract();
        
        final String stationId = response2.header("Location").substring("/stations/".length());
        
        params.clear();
        params.put("lineId", Long.parseLong(lineId));
        params.put("stationId", Long.parseLong(stationId));
        
        // 잠실 - 청라 - 선릉
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().delete("/stations/one")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
        
        // expect
        // 잠실 - 선릉
        RestAssured.given().log().all()
                .when().delete("/stations/" + (Long.parseLong(stationId)))
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is("[ERROR] stationId에 해당하는 역이 모든 노선에 존재하지 않습니다."));
    }
    
    @Test
    void stationId가_null이면_예외_발생() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        
        final String locationStartsWith = "/lines/";
        final ExtractableResponse<Response> response1 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith(locationStartsWith))
                .extract();
        
        final String lineId = response1.header("Location").substring(locationStartsWith.length());
        params.clear();
        params.put("firstStation", "잠실역");
        params.put("secondStation", "선릉역");
        params.put("distance", 5L);
        params.put("lineId", Long.parseLong(lineId));
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations/init")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", is("/stations"));
        
        params.clear();
        params.put("lineId", Long.parseLong(lineId));
        params.put("baseStation", "잠실역");
        params.put("direction", "RIGHT");
        params.put("additionalStation", "청라역");
        params.put("distance", 3L);
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith("/stations/"));
        
        // expect
        // 잠실 - 청라 - 선릉
        RestAssured.given().log().all()
                .when().delete("/stations/" + null)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
