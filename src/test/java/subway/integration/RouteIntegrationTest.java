package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
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
    void routeTest() {
        final ExtractableResponse<Response> response = 최단_거리_경로_요청(1, 3);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void routeTestFail1() {
        final ExtractableResponse<Response> response = 최단_거리_경로_요청(1, 0);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void routeTestFail2() {
        final ExtractableResponse<Response> response = 최단_거리_경로_요청(0, 3);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void routeTestFail3() {
        final ExtractableResponse<Response> response = 최단_거리_경로_요청("a", "b");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private ExtractableResponse<Response> 최단_거리_경로_요청(long sourceId, long destinationId) {
        return given().log().all()
                      .when().get("/routes?source=" + sourceId + "&destination=" + destinationId)
                      .then().log().all()
                      .extract();
    }

    private ExtractableResponse<Response> 최단_거리_경로_요청(String sourceId, String destinationId) {
        return given().log().all()
                      .when().get("/routes?source=" + sourceId + "&destination=" + destinationId)
                      .then().log().all()
                      .extract();
    }
}
