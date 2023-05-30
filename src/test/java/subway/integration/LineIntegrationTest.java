package subway.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.LineRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@DisplayName("노선 관련 기능")
class LineIntegrationTest extends IntegrationTest {

    @DisplayName("노선 조회")
    @Test
    void 노선_조회한다() {
        given()
                .log().all()
        .when()
                .get("/lines/1")

        .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", equalTo(1))
                .body("name", equalTo("2호선"))
                .body("color", equalTo("초록"));
    }

    @DisplayName("노선 추가")
    @Test
    void 노선_추가한다() {
        LineRequest request = new LineRequest("테스트명", "테스트색");

        given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)

        .when()
                .post("/lines")

        .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("노선 수정")
    @Test
    void 노선_수정한다() {
        LineRequest request = new LineRequest("테스트명", "테스트색");

        given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)

        .when()
                .put("/lines/1")

        .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("노선 삭제")
    @Test
    void 노선_삭제한다() {

        given()
                .log().all()

        .when()
                .delete("/lines/1")

        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
