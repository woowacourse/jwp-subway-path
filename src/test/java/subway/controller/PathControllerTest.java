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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql({"/schema.sql", "/dummyDataForPath.sql"})
@DisplayName("PathController 테스트")
class PathControllerTest {

    @LocalServerPort
    int port;

    @Nested
    @DisplayName("출발역과 도착역 사이의 최단 경로를 찾는 findShortestPath 메서드 테스트")
    class FindShortestPathTest {

        @BeforeEach
        void setUp() {
            RestAssured.port = port;
        }

        @DisplayName("최단 경로를 성공적으로 조회하면 상태코드 200 OK를 반환하는지 확인한다.")
        @Test
        void successTest() {
            RestAssured.given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/paths?from=1&to=2&age=adult")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value());
        }
    }

}
