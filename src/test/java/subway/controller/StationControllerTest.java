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
import subway.dto.request.StationRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql({"/dropTable.sql", "/schema.sql", "/dummyData.sql"})
@DisplayName("StationController 테스트")
public class StationControllerTest {

    @LocalServerPort
    int port;

    @Nested
    @DisplayName("역을 생성하는 createStation 메서드 테스트")
    class CreatStationTest {

        @BeforeEach
        void setUp() {
            RestAssured.port = port;
        }

        @DisplayName("역을 성공적으로 생성하면 상태코드 201 CREATED 를 반환하는지 확인한다.")
        @Test
        void successTest() {
            StationRequest request = new StationRequest("강남역");
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post("/stations")
                    .then().log().all()
                    .statusCode(HttpStatus.CREATED.value());
        }

        @DisplayName("이미 있는 역을 생성하려 할 경우 상태코드 400 BadRequest 를 반환하는지 확인한다.")
        @Test
        void failTest() {
            //given
            StationRequest request = new StationRequest("강남역");
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post("/stations")
                    .then().log().all()
                    .statusCode(HttpStatus.CREATED.value());

            //then
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post("/stations")
                    .then().log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("모든 역들을 조회하는 showStations 메서드 테스트")
    class ShowStationsTest {

        @BeforeEach
        void setUp() {
            RestAssured.port = port;
        }

        @DisplayName("역을 성공적으로 조회하면 상태코드 200 OK 를 반환하는지 확인한다.")
        @Test
        void successTest() {
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/stations")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value());
        }
    }

    @Nested
    @DisplayName("id로 단일 역을 조회하는 showStation 메서드 테스트")
    class ShowStationTest {

        @BeforeEach
        void setUp() {
            RestAssured.port = port;
        }

        @DisplayName("역을 성공적으로 조회하면 상태코드 200 OK 를 반환하는지 확인한다.")
        @Test
        void successTest() {
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/stations/1")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value());
        }

        @DisplayName("역 조회에 실패하면 상태코드 400 BadRequest 를 반환하는지 확인한다.")
        @Test
        void failTest() {
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/stations/999")
                    .then().log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("단일 역을 수정하는 updateStation 메서드 테스트")
    class UpdateStationTest {

        @BeforeEach
        void setUp() {
            RestAssured.port = port;
        }

        @DisplayName("역을 성공적으로 수정하면 상태코드 204 NoContent 를 반환하는지 확인한다.")
        @Test
        void successTest() {
            StationRequest request = new StationRequest("수정할역");
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().put("/stations/1")
                    .then().log().all()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        }

        @DisplayName("중복된 역 이름으로 수정할 경우 상태코드 400 BadRequest 를 반환하는지 확인한다.")
        @Test
        void failTest() {
            StationRequest request = new StationRequest("잠실나루");
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().put("/stations/1")
                    .then().log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("단일 역을 삭제하는 deleteStation 메서드 테스트")
    class DeleteStationTest {

        @BeforeEach
        void setUp() {
            RestAssured.port = port;
        }

        @DisplayName("역을 성공적으로 삭제하면 상태코드 204 NoContent 를 반환하는지 확인한다.")
        @Test
        void successTest() {
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().delete("/stations/1")
                    .then().log().all()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        }

        @DisplayName("없는 역을 삭제하려할 경우 상태코드 400 BadRequest 를 반환하는지 확인한다.")
        @Test
        void failTest() {
            RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().delete("/stations/999")
                    .then().log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

}
