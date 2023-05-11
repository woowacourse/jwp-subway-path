package subway.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.InitSectionRequest;
import subway.dto.LineRequest;
import subway.dto.SectionAtLastRequest;
import subway.dto.SectionRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SectionControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("처음 노선에 구간을 등록할 수 있다.")
    @Test
    void createInitSections() {
        InitSectionRequest request = new InitSectionRequest(1L, 1L, 2L, 10);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines/1/init-sections")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("노선에 역을 추가할 수 있다.")
    @Test
    void createSection() {
        // 3L 강변
        // 2L 잠실
        // 1L 잠실나루

        InitSectionRequest initRequest = new InitSectionRequest(1L, 3L, 2L, 10);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest)
                .when().post("/lines/1/init-sections")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        SectionRequest mainRequest = new SectionRequest(
                1L,
                1L,
                3L,
                2L,
                3,
                7);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mainRequest)
                .when().post("/lines/1/section")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("노선에 종착역을 추가할 수 있다.")
    @Test
    void createSectionAtLast() {
        // 3L 강변
        // 2L 잠실
        // 1L 잠실나루

        InitSectionRequest initRequest = new InitSectionRequest(1L, 1L, 2L, 10);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest)
                .when().post("/lines/1/init-sections")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        SectionAtLastRequest mainRequest = new SectionAtLastRequest(
                1L,
                3L,
                1L,
                5
        );

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mainRequest)
                .when().post("/lines/1/last-section")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

}
