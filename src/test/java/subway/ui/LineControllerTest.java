package subway.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.application.request.CreateLineRequest;
import subway.config.ControllerTestConfig;

import static io.restassured.RestAssured.given;

class LineControllerTest extends ControllerTestConfig {

    @DisplayName("노선을 저장한다.")
    @Test
    void saveLine() {
        given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new CreateLineRequest("1 ", "파랑"))

        .when()
                .post("/v2/lines")

        .then()
                .statusCode(HttpStatus.CREATED.value());
    }
}
