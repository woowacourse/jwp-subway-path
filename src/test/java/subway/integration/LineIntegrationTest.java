package subway.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.dto.LineRequest;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {

    @DisplayName("노선을 추가한다.")
    @Test
    void addLine() throws JsonProcessingException {
        final LineRequest lineRequest = new LineRequest("추가된 노선", "검정색");
        final String json = objectMapper.writeValueAsString(lineRequest);
        given().log().all()
                .contentType(JSON)
                .body(json)
                .when()
                .post("/lines")
                .then().log().all()
                .statusCode(SC_CREATED)
                .header("Location", notNullValue());
    }

    @DisplayName("기존에 있는 이름으로 노선을 추가한다.")
    @Test
    void addLineException() throws JsonProcessingException {
        final LineRequest lineRequest = new LineRequest("1호선", "아무색");
        final String json = objectMapper.writeValueAsString(lineRequest);

        given().log().all()
                .contentType(JSON)
                .body(json)
                .post("/lines")
                .then().log().all()
                .statusCode(SC_BAD_REQUEST);
    }

    @DisplayName("역이 있는 노선을 조회한다.")
    @Test
    void findSingleLine() {
        given().log().all()
                .when()
                .get("/lines/1")
                .then().log().all()
                .contentType(JSON)
                .statusCode(SC_OK)
                .assertThat()
                .body("name", is("1호선"))
                .body("stations", hasSize(2));
    }

    @DisplayName("역이 없는 노선을 조회한다.")
    @Test
    void findEmptyLine() {
        given().log().all()
                .when()
                .get("/lines/3")
                .then().log().all()
                .contentType(JSON)
                .statusCode(SC_OK)
                .assertThat()
                .body("stations", hasSize(0))
                .body("name", is("empty"));
    }

    @DisplayName("모든 역들을 조회한다.")
    @Test
    void findAllLines() {
        given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .contentType(JSON)
                .assertThat()
                .body("name", hasSize(3));
    }

    @DisplayName("노선을 삭제한다.")
    @Test
    void removeLine() {
        given().log().all()
                .when()
                .delete("/lines/1")
                .then().log().all()
                .statusCode(SC_NO_CONTENT);

        given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .contentType(JSON)
                .assertThat()
                .body("name", hasSize(2));
    }

    @DisplayName("없는 노선을 삭제하면 404를 반환한다.")
    @Test
    void removeLineNotFound() {
        given().log().all()
                .when()
                .delete("/lines/500")
                .then().log().all()
                .statusCode(SC_NOT_FOUND);
    }
}
