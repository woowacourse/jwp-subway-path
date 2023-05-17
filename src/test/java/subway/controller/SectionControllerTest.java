package subway.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.request.LineRequest;
import subway.dto.request.SectionCreationRequest;

import static subway.TestFixtures.강변;
import static subway.TestFixtures.이_호선;
import static subway.TestFixtures.잠실;
import static subway.TestFixtures.잠실나루;
import static subway.TestFixtures.잠실새내;
import static subway.TestFixtures.종합운동장;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql({"/dropTable.sql", "/schema.sql", "/dummyData.sql"})
@DisplayName("SectionController 테스트")
class SectionControllerTest {

    @LocalServerPort
    int port;

    @Nested
    @DisplayName("노선에 구간을 추가하는 createSection 메서드 테스트")
    class CreateSectionTest {

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

        @DisplayName("노선에 두 역을 최초로 추가할 때, 정상 추가가 되었다면 상태코드 201 Created 를 반환하는지 확인한다")
        @Test
        void successTest1() {
            SectionCreationRequest request = new SectionCreationRequest(강변, 잠실, 10);
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post("/lines/" + 이_호선 + "/stations")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
        }

        @DisplayName("하행 종점에 역을 추가할 때, 정상 추가가 되었다면 상태코드 201 Created 를 반환하는지 확인한다")
        @Test
        void successTest2() {
            //given
            SectionCreationRequest initRequest = new SectionCreationRequest(강변, 잠실, 10);
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(initRequest)
                    .when().post("/lines/" + 이_호선 + "/stations")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
            //then
            SectionCreationRequest mainRequest = new SectionCreationRequest(잠실, 잠실새내, 10);
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(mainRequest)
                    .when().post("/lines/" + 이_호선 + "/stations")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
        }

        @DisplayName("상행 종점에 역을 추가할 때, 정상 추가가 되었다면 상태코드 201 Created 를 반환하는지 확인한다")
        @Test
        void successTest3() {
            //given
            SectionCreationRequest initRequest = new SectionCreationRequest(잠실나루, 잠실, 10);
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(initRequest)
                    .when().post("/lines/" + 이_호선 + "/stations")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
            //then
            SectionCreationRequest mainRequest = new SectionCreationRequest(강변, 잠실나루, 10);
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(mainRequest)
                    .when().post("/lines/" + 이_호선 + "/stations")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
        }

        @DisplayName("존재하는 역들의 사이에 역을 추가할 때, 정상 추가가 되었다면 상태코드 201 Created 를 반환하는지 확인한다 case1")
        @Test
        void successTest4() {
            //given
            SectionCreationRequest initRequest = new SectionCreationRequest(강변, 잠실, 10);
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(initRequest)
                    .when().post("/lines/" + 이_호선 + "/stations")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
            //then
            SectionCreationRequest mainRequest = new SectionCreationRequest(잠실나루, 잠실, 6);
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(mainRequest)
                    .when().post("/lines/" + 이_호선 + "/stations")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
        }

        @DisplayName("존재하는 역들의 사이에 역을 추가할 때, 정상 추가가 되었다면 상태코드 201 Created 를 반환하는지 확인한다 case2")
        @Test
        void successTest5() {
            //given
            SectionCreationRequest initRequest = new SectionCreationRequest(강변, 잠실, 10);
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(initRequest)
                    .when().post("/lines/" + 이_호선 + "/stations")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
            //then
            SectionCreationRequest mainRequest = new SectionCreationRequest(강변, 잠실나루, 6);
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(mainRequest)
                    .when().post("/lines/" + 이_호선 + "/stations")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
        }

        @DisplayName("노선에 이미 등록된 역이 있을 때, 새로운 두개의 역을 추가하면 400 BadRequest 를 반환하는지 확인한다")
        @Test
        void failTest1() {
            //given
            SectionCreationRequest initRequest = new SectionCreationRequest(강변, 잠실, 10);
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(initRequest)
                    .when().post("/lines/" + 이_호선 + "/stations")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
            //then
            SectionCreationRequest mainRequest = new SectionCreationRequest(잠실새내, 종합운동장, 10);
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(mainRequest)
                    .when().post("/lines/" + 이_호선 + "/stations")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @DisplayName("기존 구간 사이에 역을 추가할 경우, 쪼갠 결과로 나온 두개의 거리 중 양수가 아닌 수가 있다면 400 BadRequest 를 반환하는지 확인한다")
        @ParameterizedTest
        @ValueSource(ints = {10, 11, 0, -1})
        void failTest2(int input) {
            //given
            SectionCreationRequest initRequest = new SectionCreationRequest(강변, 잠실, 10);
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(initRequest)
                    .when().post("/lines/" + 이_호선 + "/stations")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
            //then
            SectionCreationRequest mainRequest = new SectionCreationRequest(잠실나루, 잠실, input);
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(mainRequest)
                    .when().post("/lines/" + 이_호선 + "/stations")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("노선에서 역을 삭제하는 deleteSection 메서드 테스트")
    class DeleteSectionTest {

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

        @DisplayName("노선에서 역을 삭제할 때, 정상 삭제가 되었다면 상태코드 204 NoContent 를 반환하는지 확인한다")
        @Test
        void successTest1() {
            //given
            SectionCreationRequest initRequest = new SectionCreationRequest(강변, 잠실, 10);
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(initRequest)
                    .when().post("/lines/" + 이_호선 + "/stations")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
            //then
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().delete("/lines/" + 이_호선 + "/stations/" + 잠실)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        }

        @DisplayName("노선에서 역을 삭제할 때, 없는 역을 삭제하려 하면 상태코드 400 BadRequest 를 반환하는지 확인한다")
        @Test
        void failTest1() {
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().delete("/lines/" + 이_호선 + "/stations/" + 잠실)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }
}
