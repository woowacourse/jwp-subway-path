package subway.ui;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.StationDao;
import subway.domain.subway.Station;
import subway.dto.StationRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@Sql("classpath:/remove-station.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StationControllerTest {

    @Autowired
    StationDao stationDao;

    @BeforeEach
    void init(@LocalServerPort int port) {
        RestAssured.port = port;
    }

    @Test
    void 역을_등록한다() {
        // given
        final StationRequest 잠실역 = new StationRequest("잠실역");

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(잠실역)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        // then
        final JsonPath result = response.jsonPath();
        assertAll(
                () -> assertThat(result.getLong("id")).isEqualTo(1L),
                () -> assertThat(result.getString("name")).isEqualTo("잠실역")
        );
    }

    @Test
    void 역을_수정한다() {
        // given
        final Long id = stationDao.insert(new Station("수정전역")).getId();
        final StationRequest 수정후역 = new StationRequest("수정후역");

        // when, then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(수정후역)
                .when().put("/stations/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 역을_삭제한다() {
        // given
        final Long id = stationDao.insert(new Station("수정전역")).getId();

        // when, then
        RestAssured.given().log().all()
                .when().delete("/stations/" + id)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 역을_조회한다() {
        final Long id = stationDao.insert(new Station("조회역")).getId();

        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/stations/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        final JsonPath result = response.jsonPath();

        assertThat(result.getLong("id")).isEqualTo(id);
    }

    @Test
    void 역을_전체_조회한다() {
        stationDao.insert(new Station("조회역"));
        stationDao.insert(new Station("강남역"));
        stationDao.insert(new Station("잠실역"));

        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        final JsonPath result = response.jsonPath();

        assertThat(result.getList("name")).hasSize(3);
    }

}
