package subway.shortestpathfinder.adapter.input.web.integrated;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.http.HttpStatus;
import subway.integrated.IntegrationTest;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

@SuppressWarnings("NonAsciiCharacters")
class GetShortestPathIntegratedTest extends IntegrationTest {
    @Test
    void 최단_경로를_조회한다() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        params.put("extraCharge", 3L);
        
        final String locationStartsWith = "/lines/";
        final ExtractableResponse<Response> response1 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith(locationStartsWith))
                .extract();
        
        final String lineId1 = response1.header("Location").substring(locationStartsWith.length());
        params.clear();
        params.put("firstStation", "잠실역");
        params.put("secondStation", "선릉역");
        params.put("distance", 32L);
        params.put("lineId", Long.parseLong(lineId1));
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations/init")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", is("/stations"));
        
        // 1호선 : 잠실역 (32) 선릉역
        
        params.clear();
        params.put("lineId", Long.parseLong(lineId1));
        params.put("baseStation", "잠실역");
        params.put("direction", "RIGHT");
        params.put("additionalStation", "청라역");
        params.put("distance", 30L);
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith("/stations"));
        
        // 1호선 : 잠실역 (30) 청라역 (2) 선릉역
        
        params.clear();
        params.put("name", "2호선");
        params.put("color", "초록");
        params.put("extraCharge", 3L);
        
        final ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith(locationStartsWith))
                .extract();
        
        final String lineId2 = response2.header("Location").substring(locationStartsWith.length());
        params.clear();
        params.put("firstStation", "김포공항역");
        params.put("secondStation", "청라역");
        params.put("distance", 5L);
        params.put("lineId", Long.parseLong(lineId2));
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations/init")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", is("/stations"));
        
        // 1호선 : 잠실역 (30) 청라역 (2) 선릉역
        // 2호선 : 김포공항역 (5) 청라역
        
        params.clear();
        params.put("lineId", Long.parseLong(lineId2));
        params.put("baseStation", "청라역");
        params.put("direction", "RIGHT");
        params.put("additionalStation", "계양역");
        params.put("distance", 29L);
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith("/stations"));
        
        // 1호선 : 잠실역 (30) 청라역 (2) 선릉역
        // 2호선 : 김포공항역 (5) 청라역 (29) 계양역
        
        // expect
        params.clear();
        params.put("startStationName", "잠실역");
        params.put("endStationName", "계양역");
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().get("/shortest-path")
                .then().log().all()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK.value())
                .body("shortestPath", contains("잠실역", "청라역", "계양역"))
                .body("shortestDistance", is(59))
                .body("fee", is(2250));
    }
    
    @Test
    void 최단_경로를_조회시_2개의_역이_모두_노선에_등록되어있지_않으면_예외_발생() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        params.put("extraCharge", 3L);
        
        final String locationStartsWith = "/lines/";
        final ExtractableResponse<Response> response1 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith(locationStartsWith))
                .extract();
        
        final String lineId1 = response1.header("Location").substring(locationStartsWith.length());
        params.clear();
        params.put("firstStation", "잠실역");
        params.put("secondStation", "선릉역");
        params.put("distance", 32L);
        params.put("lineId", Long.parseLong(lineId1));
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations/init")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", is("/stations"));
        
        // 1호선 : 잠실역 (32) 선릉역
        
        params.clear();
        params.put("lineId", Long.parseLong(lineId1));
        params.put("baseStation", "잠실역");
        params.put("direction", "RIGHT");
        params.put("additionalStation", "청라역");
        params.put("distance", 30L);
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith("/stations"));
        
        // 1호선 : 잠실역 (30) 청라역 (2) 선릉역
        
        params.clear();
        params.put("name", "2호선");
        params.put("color", "초록");
        params.put("extraCharge", 3L);
        
        final ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith(locationStartsWith))
                .extract();
        
        final String lineId2 = response2.header("Location").substring(locationStartsWith.length());
        params.clear();
        params.put("firstStation", "김포공항역");
        params.put("secondStation", "청라역");
        params.put("distance", 5L);
        params.put("lineId", Long.parseLong(lineId2));
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations/init")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", is("/stations"));
        
        // 1호선 : 잠실역 (30) 청라역 (2) 선릉역
        // 2호선 : 김포공항역 (5) 청라역
        
        params.clear();
        params.put("lineId", Long.parseLong(lineId2));
        params.put("baseStation", "청라역");
        params.put("direction", "RIGHT");
        params.put("additionalStation", "계양역");
        params.put("distance", 29L);
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", startsWith("/stations"));
        
        // 1호선 : 잠실역 (30) 청라역 (2) 선릉역
        // 2호선 : 김포공항역 (5) 청라역 (29) 계양역
        
        // expect
        params.clear();
        params.put("startStationName", "강남역");
        params.put("endStationName", "계양역");
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().get("/shortest-path")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
    
    @ParameterizedTest(name = "{displayName} : startStationName = {0}")
    @NullAndEmptySource
    void 최단_경로를_조회시_startStaionName이_null_또는_empty이면_예외_발생(final String startStationName) {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("startStationName", startStationName);
        params.put("endStationName", "계양역");
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().get("/shortest-path")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
    
    @ParameterizedTest(name = "{displayName} : endStationName = {0}")
    @NullAndEmptySource
    void 최단_경로를_조회시_endStaionName이_null_또는_empty이면_예외_발생(final String endStationName) {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("startStationName", "강남역");
        params.put("endStationName", endStationName);
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().get("/shortest-path")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
