package subway.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static subway.fixture.PathRequestFixture.PATH_REQUEST_1_3;
import static subway.fixture.PathRequestFixture.PATH_REQUEST_1_7;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@Sql(scripts = {"/truncate.sql", "/insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("경로 탐색 관련 기능")
class PathIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("두 역의 간의 경로와 거리, 요금을 반환한다.")
    void findPath() {
        RestAssured.given().log().all()
                .body(PATH_REQUEST_1_3)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/paths")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("distance", equalTo(10))
                .body("fare", equalTo(1_250))
                .body("path", hasSize(3))
                .rootPath("path")
                .body("[0].id", equalTo(1), "[0].name", equalTo("강남"))
                .body("[1].id", equalTo(2), "[1].name", equalTo("잠실"))
                .body("[2].id", equalTo(3), "[2].name", equalTo("몽촌토성"));
    }

    @Test
    @DisplayName("두 역의 간의 경로를 조회할 때, 존재하지 않는 ID로 요청하면 400 을 반환한다.")
    void findPath_fail() {
        RestAssured.given().log().all()
                .body(PATH_REQUEST_1_7)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/paths")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
