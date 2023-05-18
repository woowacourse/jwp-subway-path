package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = {"/schema.sql", "/data.sql"})
public class RouteIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("최단 거리 경로를 요청하면 구간 정보와 총 거리, 요금을 계산할 수 있다.")
    void routeTest() {
        final ExtractableResponse<Response> response = 경로를_요청한다(1, 3);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("역이 존재하지 않으면 NOT_FOUND를 반환한다")
    void routeTestFail1() {
        final ExtractableResponse<Response> response = 경로를_요청한다(1, 0);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("역이 존재하지 않으면 NOT_FOUND를 반환한다")
    void routeTestFail2() {
        final ExtractableResponse<Response> response = 경로를_요청한다(0, 3);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("역 id가 long이 아니면 BAD_REQUEST를 반환한다")
    void routeTestFail3() {
        final ExtractableResponse<Response> response = 경로를_요청한다("a", "b");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("출발역과 도착역이 연결되어 있지 않으면 NOT_FOUND를 반환한다")
    void routeTestFail4() {
        final ExtractableResponse<Response> response = 경로를_요청한다(1, 5);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("출발역과 도착역이 같으면 BAD_REQUEST를 반환한다")
    void routeTestFail5() {
        final ExtractableResponse<Response> response = 경로를_요청한다(1, 1);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 경로를_요청한다(Object sourceId, Object destinationId) {
        return given().log()
                      .all()
                      .when()
                      .get("/routes?source=" + sourceId + "&destination=" + destinationId)
                      .then()
                      .log()
                      .all()
                      .extract();
    }
}
