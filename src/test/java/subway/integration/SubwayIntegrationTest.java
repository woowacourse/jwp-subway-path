package subway.integration;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.hamcrest.Matchers.empty;

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
}
