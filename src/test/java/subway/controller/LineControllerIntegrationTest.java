package subway.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.line.LineRequest;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = this.port;
    }

    @Test
    @DisplayName("노선을 생성한다.")
    void create_line_success() {
        // given
        LineRequest lineRequest = new LineRequest("2호선", "초록색");
        int lineId = 1;

        // when & then
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header("location", "/lines/" + lineId)
                .body("id", equalTo(lineId))
                .body("name", equalTo(lineRequest.getName()))
                .body("color", equalTo(lineRequest.getColor()));
    }


}
