package subway.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.ui.line.dto.LineCreateRequest;

public class LineCreateTest extends IntegrationTestSetUp {

    private final LineCreateRequest createRequest =
            new LineCreateRequest("3호선", "A", "B", 10);

    @BeforeEach
    public void init() {
        super.setUp();
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine_success() {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/lines/1");
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 노선을 생성하려 하면 BAD_REQUEST가 발생한다.")
    @Test
    void createLine_fail() {
        // given
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest)
                .when().post("/lines");

        // when, then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
