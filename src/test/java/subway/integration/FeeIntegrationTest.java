package subway.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

class FeeIntegrationTest extends IntegrationTest {

    @DisplayName("최단 거리를 조회한다.")
    @Test
    void getShortest() {
        given().log().all()
                .when()
                .get("/fee?start=1&end=2")
                .then().log().all()
                .assertThat()
                .body("fee", is(1250))
                .body("stations", hasSize(2));
    }

    @DisplayName("경로가 없는 두 역의 최단 거리를 조회한다.")
    @Test
    void getShortestException() {
        given().log().all()
                .when()
                .get("/fee?start=2&end=5")
                .then().log().all()
                .statusCode(SC_BAD_REQUEST);
    }
}
