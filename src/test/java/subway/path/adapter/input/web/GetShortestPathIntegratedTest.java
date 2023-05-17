package subway.path.adapter.input.web;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
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
                .when().get("/path")
                .then().log().all()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK.value())
                .body("shortestPath", contains("잠실역", "청라역", "계양역"))
                .body("shortestDistance", is(59))
                .body("fee", is(2250));
    }
}
