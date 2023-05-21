package subway.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.LineRequest;
import subway.dto.LineStationsRequest;

import static io.restassured.RestAssured.given;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

@Transactional
@DisplayName("노선의 구간 관련")
public class LineStationIntegrationTest extends  IntegrationTest{

    @DisplayName("노선 역 등록")
    @Test
    void 노선_역_등록() {
        LineStationsRequest request = new LineStationsRequest(1L, 4L, 3);

        given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)

                .when()
                .post("/lines/1/stations")

                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("모든 노선의 역 조회")
    @Test
    void 모든_노선_역_조회() {
        given()
                .log().all()

        .when()
                .get("/lines/stations")

        .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("$", hasSize(2))
                .body("id", hasItems(1, 2))
                .body("name", hasItems("1", "2"))
                .body("color", hasItems("파랑", "초록"));
    }

    @DisplayName("노선 역 조회")
    @Test
    void 노선_역_조회() {
        given()
                .log().all()

        .when()
                .get("/lines/1/stations")

        .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", equalTo(1))
                .body("name", equalTo("1"))
                .body("color", equalTo("파랑"))
                .body("stationResponses", hasSize(2));
    }

    @DisplayName("노선 역 삭제")
    @Test
    void 노선_역_삭제() {
        given()
                .log().all()

        .when()
                .delete("/lines/1/stations/1")

        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
