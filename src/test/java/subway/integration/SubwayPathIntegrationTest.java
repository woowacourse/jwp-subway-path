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
import subway.dto.request.SectionRequest;
import subway.dto.request.PathRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/schema.sql")
@DisplayName("지하철 경로 관련 기능 테스트")
public class SubwayPathIntegrationTest {

    @LocalServerPort
    private int port;
    private long 잠실나루 = 1L;
    private long 잠실 = 2L;
    private long 강변 = 3L;
    private long 강동구청 = 7L;
    private long 몽촌토성 = 8L;
    private long 석촌 = 9L;
    private long _2호선 = 2L;
    private long _8호선 = 3L;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("지하철 역 간 최단거리를 조회할 수 있다.")
    void getShortestPathTest() {
        //given
        SectionRequest initRequest1 = new SectionRequest(_2호선, 강변, 잠실, 10);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest1)
                .when().post(String.format("/lines/%d/sections", _2호선))
                .then()
                .statusCode(HttpStatus.CREATED.value());

        SectionRequest initRequest2 = new SectionRequest(_2호선, 잠실나루, 잠실, 7);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest2)
                .when().post(String.format("/lines/%d/sections", _2호선))
                .then()
                .statusCode(HttpStatus.CREATED.value());

        SectionRequest initRequest3 = new SectionRequest(_8호선, 강동구청, 몽촌토성, 15);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest3)
                .when().post(String.format("/lines/%d/sections", _8호선))
                .then()
                .statusCode(HttpStatus.CREATED.value());

        SectionRequest initRequest4 = new SectionRequest(_8호선, 몽촌토성, 잠실, 4);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest4)
                .when().post(String.format("/lines/%d/sections", _8호선))
                .then()
                .statusCode(HttpStatus.CREATED.value());

        SectionRequest initRequest5 = new SectionRequest(_8호선, 잠실, 석촌, 9);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest5)
                .when().post(String.format("/lines/%d/sections", _8호선))
                .then()
                .statusCode(HttpStatus.CREATED.value());

        PathRequest request = new PathRequest(3L, 7L, 10);

        //then
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().get("/path")
                .then()
                .statusCode(HttpStatus.OK.value());
    }
}
