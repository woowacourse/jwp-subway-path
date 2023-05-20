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
@DisplayName("노선을 구성하는 구간들에 대한 기능")
class SectionIntegrationTest {

    @LocalServerPort
    private int port;
    private long 잠실나루 = 1L;
    private long 잠실 = 2L;
    private long 강변 = 3L;
    private long _2호선 = 2L;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("처음 노선에 구간을 등록할 수 있다.")
    @Test
    void createInitSections() {
        SectionRequest request = new SectionRequest(_2호선, 잠실나루, 잠실, 7);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(String.format("/lines/%d/sections", _2호선))
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("노선을 구성하는 역 사이에 역을 추가할 수 있다.")
    @Test
    void createSection() {
        //given
        SectionRequest initRequest = new SectionRequest(_2호선, 강변, 잠실, 10);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest)
                .when().post(String.format("/lines/%d/sections", _2호선))
                .then()
                .statusCode(HttpStatus.CREATED.value());

        // then
        SectionRequest mainRequest = new SectionRequest(_2호선, 잠실나루, 잠실, 7);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mainRequest)
                .when().post(String.format("/lines/%d/sections", _2호선))
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("노선에 종착역을 추가할 수 있다.")
    @Test
    void createEndSection() {
        // given
        SectionRequest initRequest = new SectionRequest(_2호선, 잠실나루, 잠실, 7);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest)
                .when().post(String.format("/lines/%d/sections", _2호선))
                .then()
                .statusCode(HttpStatus.CREATED.value());

        // then
        SectionRequest mainRequest = new SectionRequest(_2호선, 강변, 잠실나루, 3);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mainRequest)
                .when().post(String.format("/lines/%d/sections", _2호선))
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("노선에서 중간에 위치하는 하나의 역을 제거할 수 있다.")
    @Test
    void deleteSection() {
        // given
        SectionRequest initRequest1 = new SectionRequest(_2호선, 강변, 잠실, 10);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest1)
                .when().post(String.format("/lines/%d/sections", _2호선))
                .then()
                .statusCode(HttpStatus.CREATED.value());

        SectionRequest mainRequest = new SectionRequest(_2호선, 잠실나루, 잠실, 7);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mainRequest)
                .when().post(String.format("/lines/%d/sections", _2호선))
                .then()
                .statusCode(HttpStatus.CREATED.value());

        // then
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(String.format("/lines/%d/sections?station-id=%d", _2호선, 잠실나루))
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }


    @DisplayName("노선에서 하나의 종착역을 제거할 수 있다.")
    @Test
    void deleteEndSection() {
        // given
        SectionRequest initRequest1 = new SectionRequest(_2호선, 강변, 잠실, 10);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest1)
                .when().post(String.format("/lines/%d/sections", _2호선))
                .then()
                .statusCode(HttpStatus.CREATED.value());

        SectionRequest mainRequest = new SectionRequest(_2호선, 잠실나루, 잠실, 7);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mainRequest)
                .when().post(String.format("/lines/%d/sections", _2호선))
                .then()
                .statusCode(HttpStatus.CREATED.value());

        // then
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(String.format("/lines/%d/sections?station-id=%d", _2호선, 강변))
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("노선에서 마지막 남은 두개의 역을 제거할 수 있다.")
    @Test
    void deleteLastSection() {
        // given
        SectionRequest initRequest1 = new SectionRequest(_2호선, 강변, 잠실, 10);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest1)
                .when().post(String.format("/lines/%d/sections", _2호선))
                .then()
                .statusCode(HttpStatus.CREATED.value());

        // then
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(String.format("/lines/%d/sections?station-id=%d", _2호선, 강변))
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
