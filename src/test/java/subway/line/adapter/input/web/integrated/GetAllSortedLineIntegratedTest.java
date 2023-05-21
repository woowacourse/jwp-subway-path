package subway.line.adapter.input.web.integrated;

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
class GetAllSortedLineIntegratedTest extends IntegrationTest {
    @Test
    void 모든_노선의_역을_정렬하기() {
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
        
        // expect
        RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK.value())
                .body("allSortedLines.size()", is(1))
                .body("allSortedLines[0].lineName", is("1호선"))
                .body("allSortedLines[0].lineColor", is("파랑"))
                .body("allSortedLines[0].sortedStations", contains("잠실역", "청라역", "선릉역"));
    }
}
