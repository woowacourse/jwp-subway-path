package subway.station.adapter.input.web.integrated;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import subway.integrated.IntegrationTest;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

@SuppressWarnings("NonAsciiCharacters")
class AddStationIntegratedTest extends IntegrationTest {
    @Test
    void 해당_노선의_중간에_역을_등록한다() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        params.put("extraCharge", 3L);
        
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
        
        // expect
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
                .header("Location", startsWith("/stations"));
    }
    
    @Test
    void 해당_노선의_가장_왼쪽에_역을_등록한다() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        params.put("extraCharge", 3L);
        
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
        
        // expect
        params.clear();
        params.put("lineId", Long.parseLong(lineId));
        params.put("baseStation", "잠실역");
        params.put("direction", "LEFT");
        params.put("additionalStation", "청라역");
        params.put("distance", 5L);
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith("/stations"));
    }
    
    @Test
    void 해당_노선의_가장_오른쪽에_역을_등록한다() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        params.put("extraCharge", 3L);
        
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
        
        // expect
        params.clear();
        params.put("lineId", Long.parseLong(lineId));
        params.put("baseStation", "선릉역");
        params.put("direction", "RIGHT");
        params.put("additionalStation", "청라역");
        params.put("distance", 5L);
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith("/stations"));
    }
    
    @Test
    void 역_등록시_lineId가_null이면_예외_발생() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        params.put("extraCharge", 3L);
        
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
        
        // expect
        params.clear();
        params.put("lineId", null);
        params.put("baseStation", "선릉역");
        params.put("direction", "RIGHT");
        params.put("additionalStation", "청라역");
        params.put("distance", 5L);
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is("[ERROR] lineId는 null일 수 없습니다."));
    }
    
    @ParameterizedTest(name = "{displayName} : baseStation = {0}")
    @NullAndEmptySource
    void 역_등록시_기준역이_null_또는_empty이면_예외_발생(final String baseStation) {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        params.put("extraCharge", 3L);
        
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
        
        // expect
        params.clear();
        params.put("lineId", Long.parseLong(lineId));
        params.put("baseStation", baseStation);
        params.put("direction", "RIGHT");
        params.put("additionalStation", "청라역");
        params.put("distance", 5L);
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is("[ERROR] 기준역은 null 또는 empty일 수 없습니다."));
    }
    
    @Test
    void 역_등록시_방향이_LEFT_또는_RIGHT가_아니면_예외_발생() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        params.put("extraCharge", 3L);
        
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
        
        // expect
        params.clear();
        params.put("lineId", Long.parseLong(lineId));
        params.put("baseStation", "선릉역");
        params.put("direction", "right");
        params.put("additionalStation", "청라역");
        params.put("distance", 5L);
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is("[ERROR] 해당 파라미터로 변환할 수 없습니다."));
    }
    
    @ParameterizedTest(name = "{displayName} : additionalStation = {0}")
    @NullAndEmptySource
    void 역_등록시_등록할_역이_null_또는_empty이면_예외_발생(final String additionalStation) {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        params.put("extraCharge", 3L);
        
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
        
        // expect
        params.clear();
        params.put("lineId", Long.parseLong(lineId));
        params.put("baseStation", "선릉역");
        params.put("direction", "RIGHT");
        params.put("additionalStation", additionalStation);
        params.put("distance", 5L);
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is("[ERROR] 등록할 역은 null 또는 empty일 수 없습니다."));
    }
    
    @Test
    void 역_등록시_거리가_null이면_예외_발생() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        params.put("extraCharge", 3L);
        
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
        
        // expect
        params.clear();
        params.put("lineId", Long.parseLong(lineId));
        params.put("baseStation", "선릉역");
        params.put("direction", "RIGHT");
        params.put("additionalStation", "청라역");
        params.put("distance", null);
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is("[ERROR] 거리는 null일 수 없습니다."));
    }
    
    @Test
    void 역_등록시_존재하지_않는_노선을_가리키면_예외_발생() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        params.put("extraCharge", 3L);
        
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
        
        // expect
        params.clear();
        params.put("lineId", Long.parseLong(lineId) + 1L);
        params.put("baseStation", "선릉역");
        params.put("direction", "RIGHT");
        params.put("additionalStation", "청라역");
        params.put("distance", 3L);
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is("[ERROR] 존재하지 않는 노선을 지정하셨습니다."));
    }
    
    @Test
    void 역_등록시_기준역이_노선_내에_존재하지_않으면_예외_발생() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        params.put("extraCharge", 3L);
        
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
        
        // expect
        params.clear();
        params.put("lineId", Long.parseLong(lineId));
        params.put("baseStation", "가정역");
        params.put("direction", "RIGHT");
        params.put("additionalStation", "청라역");
        params.put("distance", 3L);
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is("[ERROR] 기준역인 " + params.get("baseStation") + "이 해당 노선에 존재하지 않습니다."));
    }
    
    @Test
    void 역_등록시_등록할_역이_노선_내에_존재하면_예외_발생() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        params.put("extraCharge", 3L);
        
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
        
        // expect
        params.clear();
        params.put("lineId", Long.parseLong(lineId));
        params.put("baseStation", "선릉역");
        params.put("direction", "RIGHT");
        params.put("additionalStation", "잠실역");
        params.put("distance", 3L);
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is("[ERROR] " + params.get("additionalStation") + "은 이미 해당 노선에 존재하는 역입니다."));
    }
    
    @ParameterizedTest(name = "{displayName} : distance = {0}")
    @ValueSource(longs = {5L, 6L})
    void 중간역_등록시_추가할_구간의_거리가_기존_구간의_거리보다_길거나_같으면_예외_발생(final Long distance) {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        params.put("extraCharge", 3L);
        
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
        
        // expect
        params.clear();
        params.put("lineId", Long.parseLong(lineId));
        params.put("baseStation", "선릉역");
        params.put("direction", "LEFT");
        params.put("additionalStation", "청라역");
        params.put("distance", distance);
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is("[ERROR] 분리될 Section은 추가될 Section의 거리보다 길어야합니다."));
    }
}
