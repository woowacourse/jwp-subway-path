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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/schema.sql")
@DisplayName("LineStationsController는 ")
class LineStationsIntegrationTest {

    @LocalServerPort
    int port;
    private long 잠실나루 = 1L;
    private long 잠실 = 2L;
    private long 강변 = 3L;
    private long 노량진 = 4L;
    private long 용산 = 5L;
    private long 서울역 = 6L;
    private long _1호선 = 1L;
    private long _2호선 = 2L;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("노선을 순서대로 조회할 수 있다.")
    @Test
    void readLineStations() {
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

        //then
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(String.format("/line-stations/%d", _2호선))
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("전체 노선을 순서대로 조회할 수 있다.")
    @Test
    void readAllLinesStations() {
        //given
        SectionRequest initRequest1 = new SectionRequest(_1호선, 서울역, 노량진, 10);
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest1)
                .when().post(String.format("/lines/%d/sections", _1호선))
                .then()
                .statusCode(HttpStatus.CREATED.value());

        SectionRequest initRequest2 = new SectionRequest(_1호선, 용산, 노량진, 7);
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest2)
                .when().post(String.format("/lines/%d/sections", _1호선))
                .then()
                .statusCode(HttpStatus.CREATED.value());

        SectionRequest initRequest3 = new SectionRequest(_2호선, 강변, 잠실, 10);
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest3)
                .when().post(String.format("/lines/%d/sections", _2호선))
                .then()
                .statusCode(HttpStatus.CREATED.value());

        SectionRequest initRequest4 = new SectionRequest(_2호선, 잠실나루, 잠실, 7);
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest4)
                .when().post(String.format("/lines/%d/sections", _2호선))
                .then()
                .statusCode(HttpStatus.CREATED.value());

        //then
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/line-stations")
                .then()
                .statusCode(HttpStatus.OK.value());
    }
}
