package subway.integration;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class SubwayIntegrationTest extends IntegrationTest {

    @Test
    void 역을_제거하면_경로에서도_제거된다() {
        given().log().all()
                .when()
                .delete("/stations/1")
                .then().log().all()
                .statusCode(SC_NO_CONTENT);

        given().log().all()
                .when()
                .get("/lines/1")
                .then().log().all()
                .assertThat()
                .body("stations", empty());
    }

    @Test
    void 역을_제거하면_경로에서도_제거된다_2() {
        given().log().all()
                .when()
                .delete("/stations/1")
                .then().log().all()
                .statusCode(SC_NO_CONTENT);

        given().log().all()
                .when()
                .get("/lines/2")
                .then().log().all()
                .assertThat()
                .body("stations", hasSize(2))
                .body("stations.name", containsInAnyOrder("의왕", "선릉"));
    }

    @Test
    void 환승역을_삭제한다() {
        given().log().all()
                .when()
                .delete("/lines/1/stations/1")
                .then().log().all()
                .statusCode(SC_OK);

        given().log().all()
                .when()
                .get("/lines/2")
                .then().log().all()
                .assertThat()
                .body("stations[1].id", is(1))
                .body("stations[1].name", is("수원"));
    }
}
