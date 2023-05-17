package subway.acceptance;

import static fixtures.LineFixtures.LINE_FIND_RESPONSE_LINE2_A_TO_C_AND_C_TO_E;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import fixtures.LineFixtures;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dto.LineFindResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {

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
        jdbcTemplate.update("INSERT INTO STATION (name, line_id) VALUES ('A역', 1)");
        jdbcTemplate.update("INSERT INTO STATION (name, line_id) VALUES ('C역', 1)");
        jdbcTemplate.update("INSERT INTO STATION (name, line_id) VALUES ('E역', 1)");
        jdbcTemplate.update("INSERT INTO STATION (name, line_id) VALUES ('B역', 2)");
        jdbcTemplate.update("INSERT INTO STATION (name, line_id) VALUES ('D역', 2)");
        jdbcTemplate.update("INSERT INTO GENERAL_SECTION (up_station_id, down_station_id, line_id, distance) VALUES (1, 2, 1, 3)");
        jdbcTemplate.update("INSERT INTO GENERAL_SECTION (up_station_id, down_station_id, line_id, distance) VALUES (2, 3, 1, 5)");
        jdbcTemplate.update("INSERT INTO GENERAL_SECTION (up_station_id, down_station_id, line_id, distance) VALUES (4, 5, 2, 4)");
    }


    @Nested
    @DisplayName("노선 ID로 하나의 노선을 조회하는 경우")
    class findOneLine {

        @Test
        @DisplayName("노선 ID에 해당하는 노선이 존재하지 않으면 NOT FOUND 상태 코드를 반환하고 예외 메시지를 응답한다.")
        void throwWhenNotFoundLine() {
            // when
            Response response = RestAssured.given().log().all()
                    .get("/lines/" + -1)
                    .then().log().all()
                    .extract().response();

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
            assertThat(response.getBody().asString()).isEqualTo("노선 ID에 해당하는 노선이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("정상적으로 요청하면, OK 상태 코드를 반환 후 해당 노선의 이름과 해당 노선의 역 이름 순서대로 정렬된 역 이름 리스트를 응답한다.")
        void findLineResponse() {
            // given
            LineFindResponse expectedResponse = LINE_FIND_RESPONSE_LINE2_A_TO_C_AND_C_TO_E.RESPONSE;
            String expectedLineName = expectedResponse.getLineName();
            List<String> expectedStationNames = expectedResponse.getStationNames();

            // when
            Response response = RestAssured.given().log().all()
                    .get("/lines/" + 1L)
                    .then().log().all()
                    .extract().response();

            JsonPath jsonPath = response.jsonPath();
            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value()),

                    () -> assertThat(jsonPath.getString("lineName")).isEqualTo(expectedLineName),
                    () -> assertThat(jsonPath.getString("stationNames[0]")).isEqualTo(expectedStationNames.get(0)),
                    () -> assertThat(jsonPath.getString("stationNames[1]")).isEqualTo(expectedStationNames.get(1)),
                    () -> assertThat(jsonPath.getString("stationNames[2]")).isEqualTo(expectedStationNames.get(2))
            );
        }
    }

    @Nested
    @DisplayName("노선 ID로 모든 노선을 조회하는 경우")
    class findAllLine {

        @Test
        @DisplayName("OK 상태 코드를 반환 후, 노선별로 노선의 이름과 해당 노선의 역 이름 순서대로 정렬된 역 이름 리스트를 응답한다.")
        void findAllLineResponse() {
            // given
            LineFindResponse expectedResponse1 = LINE_FIND_RESPONSE_LINE2_A_TO_C_AND_C_TO_E.RESPONSE;
            String expectedLineName1 = expectedResponse1.getLineName();
            List<String> expectedStationNames1 = expectedResponse1.getStationNames();

            LineFindResponse expectedResponse2 = LineFixtures.LINE_FIND_RESPONSE_LINE7_B_TO_D.RESPONSE;
            String expectedLineName2 = expectedResponse2.getLineName();
            List<String> expectedStationNames2 = expectedResponse2.getStationNames();

            // when
            Response response = RestAssured.given().log().all()
                    .get("/lines")
                    .then().log().all()
                    .extract().response();

            JsonPath jsonPath = response.jsonPath();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value()),

                    () -> assertThat(jsonPath.getString("[0].lineName")).isEqualTo(expectedLineName1),
                    () -> assertThat(jsonPath.getString("[0].stationNames[0]")).isEqualTo(expectedStationNames1.get(0)),
                    () -> assertThat(jsonPath.getString("[0].stationNames[1]")).isEqualTo(expectedStationNames1.get(1)),
                    () -> assertThat(jsonPath.getString("[0].stationNames[2]")).isEqualTo(expectedStationNames1.get(2)),

                    () -> assertThat(jsonPath.getString("[1].lineName")).isEqualTo(expectedLineName2),
                    () -> assertThat(jsonPath.getString("[1].stationNames[0]")).isEqualTo(expectedStationNames2.get(0)),
                    () -> assertThat(jsonPath.getString("[1].stationNames[1]")).isEqualTo(expectedStationNames2.get(1))
            );

        }

    }
}
