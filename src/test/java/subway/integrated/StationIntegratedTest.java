package subway.integrated;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;

@SuppressWarnings("NonAsciiCharacters")
class StationIntegratedTest extends IntegrationTest {
    // TODO : 노선에_최초_역_추가하기 테스트 주석 처리중
//    @Test
//    void 노선에_최초_역_추가하기() {
//        // given
//        final Map<String, Object> params = new HashMap<>();
//        params.put("lineName", "1호선");
//        params.put("leftStation", "잠실역");
//        params.put("secondStation", "선릉역");
//        params.put("distance", 5L);
//
//        // expect
//        RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(params)
//                .when().post("/stations")
//                .then().log().all()
//                .statusCode(HttpStatus.CREATED.value())
//                .header("Location", notNullValue());
//    }
}
