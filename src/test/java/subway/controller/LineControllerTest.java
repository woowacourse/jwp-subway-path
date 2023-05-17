package subway.controller;

import io.restassured.RestAssured;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql({"/dropTable.sql", "/schema.sql", "/dummyData.sql"})
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
            LineRequest request = new LineRequest("2호선", "초록색", 0);
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

    @Nested
    @DisplayName("새로운 노선을 등록하는 createLine 메서드 테스트")
    class CreateLineTest {

        @BeforeEach
        void setUp() {
            RestAssured.port = port;
        }

        @DisplayName("노선을 성공적으로 등록하면 상태코드 201 CREATED 를 반환하는지 확인한다.")
        @Test
        void successTest() {
            LineRequest request = new LineRequest("2호선", "초록색", 0);
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post("/lines")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
        }

        @DisplayName("중복된 노선 이름을 등록할 경우 상태코드 400 BadRequest 를 반환하는지 확인한다.")
        @Test
        void failTest1() {
            LineRequest request = new LineRequest("1호선", "초록색", 0);
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post("/lines")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @DisplayName("중복된 노선 색상을 등록할 경우 상태코드 400 BadRequest 를 반환하는지 확인한다.")
        @Test
        void failTest2() {
            LineRequest request = new LineRequest("2호선", "남색", 0);
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post("/lines")
                    .then().log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("새로운 노선을 수정하는 updateLine 메서드 테스트")
    class UpdateLineTest {

        @BeforeEach
        void setUp() {
            RestAssured.port = port;
            LineRequest request = new LineRequest("2호선", "초록색", 0);
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post("/lines")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
        }

        @DisplayName("노선을 성공적으로 수정하면 상태코드 204 NoContent 를 반환하는지 확인한다.")
        @Test
        void successTest() {
            LineRequest request = new LineRequest("2호선", "파랑색", 0);
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().put("/lines/2")
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        }

        @DisplayName("중복된 노선 이름으로 수정할 경우 상태코드 400 BadRequest 를 반환하는지 확인한다.")
        @Test
        void failTest1() {
            LineRequest request = new LineRequest("1호선", "초록색", 0);
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().put("/lines/2")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @DisplayName("중복된 노선 색상으로 수정할 경우 상태코드 400 BadRequest 를 반환하는지 확인한다.")
        @Test
        void failTest2() {
            LineRequest request = new LineRequest("2호선", "남색", 0);
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().put("/lines/2")
                    .then().log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("새로운 노선을 삭제하는 deleteLine 메서드 테스트")
    class DeleteLineTest {

        @BeforeEach
        void setUp() {
            RestAssured.port = port;
            LineRequest request = new LineRequest("2호선", "초록색", 0);
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post("/lines")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
        }

        @DisplayName("노선을 성공적으로 삭제하면 상태코드 204 NoContent 를 반환하는지 확인한다.")
        @Test
        void successTest() {
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().delete("/lines/2")
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        }

        @DisplayName("존재하지 않는 노선을 삭제할 경우 상태코드 400 BadRequest 를 반환하는지 확인한다.")
        @Test
        void failTest() {
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().delete("/lines/5")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }
}
