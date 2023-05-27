package subway.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.request.PathRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/schema.sql")
@DisplayName("지하철 경로 관련 기능 테스트")
public class SubwayPathIntegrationTest {

    @LocalServerPort
    private int port;
    private long 강변 = 3L;
    private long 강동구청 = 7L;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("지하철 역 간 최단거리를 조회할 수 있다.")
    void getShortestPathTest() {
        PathRequest request = new PathRequest(강변, 강동구청, 10);

        //then
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().get("/paths")
                .then()
                .statusCode(HttpStatus.OK.value());
    }
}
