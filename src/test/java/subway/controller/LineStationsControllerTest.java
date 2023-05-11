package subway.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.InitSectionRequest;
import subway.dto.SectionRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/schema.sql")
@DisplayName("LineStationsController는 ")
class LineStationsControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("노선을 순서대로 조회할 수 있다.")
    @Test
    void readLineStations() {
        InitSectionRequest initRequest = new InitSectionRequest(2L, 3L, 2L, 10);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest)
                .when().post("/lines/2/init-sections")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        SectionRequest mainRequest = new SectionRequest(
                2L,
                1L,
                3L,
                2L,
                3,
                7);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mainRequest)
                .when().post("/lines/2/section")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/line-stations/2")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("전체 노선을 순서대로 조회할 수 있다.")
    @Test
    void readAllLinesStations() {

        //4L 노량진 하행
        //5L 용산
        //6L 서울역 상행
        InitSectionRequest initRequest1 = new InitSectionRequest(1L, 6L, 4L, 10);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest1)
                .when().post("/lines/1/init-sections")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        SectionRequest initRequest2 = new SectionRequest(
                1L,
                5L,
                6L,
                4L,
                3,
                7);

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest2)
                .when().post("/lines/1/section")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        InitSectionRequest initRequest3 = new InitSectionRequest(2L, 3L, 2L, 10);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest3)
                .when().post("/lines/2/init-sections")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        SectionRequest initRequest4 = new SectionRequest(
                2L,
                1L,
                3L,
                2L,
                3,
                7);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest4)
                .when().post("/lines/2/section")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/line-stations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
}
