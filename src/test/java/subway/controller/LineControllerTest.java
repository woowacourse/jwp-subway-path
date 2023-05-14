package subway.controller;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql({"/schema.sql", "/dummyData.sql"})
@DisplayName("LineController 테스트")
class LineControllerTest {

    @LocalServerPort
    int port;

    @Nested
    @DisplayName("모든 노선을 조회하는 findAllLines 메서드 테스트")
    class FindAllLinesTest {

        @BeforeEach
        void setUp() {
            RestAssured.port = port;
        }

        @DisplayName("모든 노선 목록을 성공적으로 조회하면 상태코드 200 OK를 반환하는지 확인한다.")
        @Test
        void successTest() {
            // given
            LineRequest request = new LineRequest("2호선", "초록색");
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post("/lines")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());

            // then
            RestAssured.given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/lines")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value());
        }
    }

    @Nested
    @DisplayName("id로 단일 노선을 조회하는 findLineById 메서드 테스트")
    class FindLineByIdTest {

        @BeforeEach
        void setUp() {
            RestAssured.port = port;
        }

        @DisplayName("특정 노선을 성공적으로 조회하면 상태코드 200 OK를 반환하는지 확인한다.")
        @Test
        void successTest() {
            RestAssured.given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/lines/1")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value());
        }

        @DisplayName("존재하지 않는 노선을 조회할 경우 상태코드 400 BadRequest 를 반환하는지 확인한다.")
        @Test
        void failTest() {
            RestAssured.given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/lines/5")
                    .then().log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

}
