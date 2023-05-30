package subway.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.StationRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@DisplayName("지하철역 관련 기능")
class StationIntegrationTest extends IntegrationTest {

    @DisplayName("역을 조회한다")
    @Test
    void 역을_조회한다() {
        given()
                .log().all()

        .when()
                .get("/stations/1")

        .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", equalTo(1))
                .body("name", equalTo("잠실새내"))
                .log().all();
    }

    @DisplayName("역을 저장한다.")
    @Test
    void 역을_저장한다() {
        StationRequest request = new StationRequest("테스트1");

        given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)

        .when()
                .post("/stations")

        .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("역을 수정한다.")
    @Test
    void 역을_수정한다() {
        StationRequest request = new StationRequest("테스트1");

        given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)

                .when()
                .put("/stations/1")

                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("역을 삭제한다.")
    @Test
    void 역을_삭제한다() {
        given()
                .log().all()

        .when()
                .delete("/stations/6")

        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
