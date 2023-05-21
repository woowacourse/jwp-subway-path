package subway.acceptance;

import static fixtures.path.PathLineFixtures.INITIAL_LINE2;
import static fixtures.path.PathLineFixtures.INITIAL_LINE3;
import static fixtures.path.PathStationFixtures.INITIAL_STATION_LINE2_A;
import static fixtures.path.PathStationFixtures.INITIAL_STATION_LINE3_E;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SectionAcceptanceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        jdbcTemplate.update("set FOREIGN_KEY_CHECKS = 0");
        jdbcTemplate.update("TRUNCATE TABLE LINE");
        jdbcTemplate.update("TRUNCATE TABLE STATION");
        jdbcTemplate.update("TRUNCATE TABLE GENERAL_SECTION");
        jdbcTemplate.update("set FOREIGN_KEY_CHECKS = 1");

        jdbcTemplate.update("INSERT INTO LINE (name) VALUES ('2호선')");
        jdbcTemplate.update("INSERT INTO LINE (name) VALUES ('7호선')");
        jdbcTemplate.update("INSERT INTO LINE (name) VALUES ('3호선')");

        jdbcTemplate.update("INSERT INTO STATION (name, line_id) VALUES ('A역', 1)");
        jdbcTemplate.update("INSERT INTO STATION (name, line_id) VALUES ('C역', 1)");
        jdbcTemplate.update("INSERT INTO STATION (name, line_id) VALUES ('A역', 2)");
        jdbcTemplate.update("INSERT INTO STATION (name, line_id) VALUES ('D역', 2)");
        jdbcTemplate.update("INSERT INTO STATION (name, line_id) VALUES ('C역', 3)");
        jdbcTemplate.update("INSERT INTO STATION (name, line_id) VALUES ('D역', 3)");
        jdbcTemplate.update("INSERT INTO STATION (name, line_id) VALUES ('E역', 3)");

        jdbcTemplate.update("INSERT INTO GENERAL_SECTION (up_station_id, down_station_id, line_id, distance) VALUES (1, 2, 1, 10)");
        jdbcTemplate.update("INSERT INTO GENERAL_SECTION (up_station_id, down_station_id, line_id, distance) VALUES (5, 7, 3, 8)");
        jdbcTemplate.update("INSERT INTO GENERAL_SECTION (up_station_id, down_station_id, line_id, distance) VALUES (3, 4, 2, 12)");
        jdbcTemplate.update("INSERT INTO GENERAL_SECTION (up_station_id, down_station_id, line_id, distance) VALUES (6, 7, 3, 5)");
    }

    @Test
    @DisplayName("요청한 노선 이름과 역 이름에 해당하는 역이 없다면 NOT FOUND 상태코드를 반환하고 예외 메시지를 응답한다.")
    void findShortestPath_not_found() {
        // given
        final Map<String, String> params = Map.of(
                "startLine", "없는 노선1",
                "startStation", "없는 역1",
                "endLine", "없는 노선2",
                "endStation", "없는 역2"
        );

        // when
        Response response = RestAssured.given().log().all()
                .params(params)
                .get("/sections/short-path")
                .then()
                .extract().response();

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(response.getBody().asString()).isEqualTo("역 이름과 노선 이름에 해당하는 역이 존재하지 않습니다.")
        );
    }

    @Test
    @DisplayName("요청한 노선 이름과 역 이름에 해당하는 역이 있다면 OK 상태코드를 반환하고 최단 경로 정보를 응답한다.")
    void findShortestPath() {
        // given
        final Map<String, String> params = Map.of(
                "startLine", INITIAL_LINE2.NAME,
                "startStation", INITIAL_STATION_LINE2_A.NAME,
                "endLine", INITIAL_LINE3.NAME,
                "endStation", INITIAL_STATION_LINE3_E.NAME
        );

        Response response = RestAssured.given().log().all()
                .params(params)
                .get("/sections/short-path")
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value()),

                () -> assertThat(jsonPath.getString("pathSections[0].currentStation.lineName")).isEqualTo("2호선"),
                () -> assertThat(jsonPath.getString("pathSections[0].currentStation.stationName")).isEqualTo("A역"),
                () -> assertThat(jsonPath.getString("pathSections[0].nextStation.lineName")).isEqualTo("7호선"),
                () -> assertThat(jsonPath.getString("pathSections[0].nextStation.stationName")).isEqualTo("A역"),
                () -> assertThat(jsonPath.getInt("pathSections[0].distance")).isEqualTo(0),
                () -> assertThat(jsonPath.getBoolean("pathSections[0].transferSection")).isEqualTo(true),

                () -> assertThat(jsonPath.getString("pathSections[1].currentStation.lineName")).isEqualTo("7호선"),
                () -> assertThat(jsonPath.getString("pathSections[1].currentStation.stationName")).isEqualTo("A역"),
                () -> assertThat(jsonPath.getString("pathSections[1].nextStation.lineName")).isEqualTo("7호선"),
                () -> assertThat(jsonPath.getString("pathSections[1].nextStation.stationName")).isEqualTo("D역"),
                () -> assertThat(jsonPath.getInt("pathSections[1].distance")).isEqualTo(12),
                () -> assertThat(jsonPath.getBoolean("pathSections[1].transferSection")).isEqualTo(false),

                () -> assertThat(jsonPath.getString("pathSections[2].currentStation.lineName")).isEqualTo("7호선"),
                () -> assertThat(jsonPath.getString("pathSections[2].currentStation.stationName")).isEqualTo("D역"),
                () -> assertThat(jsonPath.getString("pathSections[2].nextStation.lineName")).isEqualTo("3호선"),
                () -> assertThat(jsonPath.getString("pathSections[2].nextStation.stationName")).isEqualTo("D역"),
                () -> assertThat(jsonPath.getInt("pathSections[2].distance")).isEqualTo(0),
                () -> assertThat(jsonPath.getBoolean("pathSections[2].transferSection")).isEqualTo(true),

                () -> assertThat(jsonPath.getString("pathSections[3].currentStation.lineName")).isEqualTo("3호선"),
                () -> assertThat(jsonPath.getString("pathSections[3].currentStation.stationName")).isEqualTo("D역"),
                () -> assertThat(jsonPath.getString("pathSections[3].nextStation.lineName")).isEqualTo("3호선"),
                () -> assertThat(jsonPath.getString("pathSections[3].nextStation.stationName")).isEqualTo("E역"),
                () -> assertThat(jsonPath.getInt("pathSections[3].distance")).isEqualTo(5),
                () -> assertThat(jsonPath.getBoolean("pathSections[3].transferSection")).isEqualTo(false),

                () -> assertThat(jsonPath.getInt("totalDistance")).isEqualTo(17),
                () -> assertThat(jsonPath.getInt("fare")).isEqualTo(1450)
        );
    }
}
