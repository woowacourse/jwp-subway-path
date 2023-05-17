package subway.acceptance;

import static fixtures.GeneralSectionFixtures.*;
import static fixtures.LineFixtures.INITIAL_Line2;
import static fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StationAcceptanceTest {

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
    }

    @Nested
    @DisplayName("역 등록 시")
    class saveStation {

        @Nested
        @DisplayName("없는 노선에 역을 등록하는 경우")
        class saveStationWhenLineNotExist {

            @Test
            @DisplayName("요청의 노선과 역 2개, 구간을 저장하고, Location 헤더에 저장된 노선을 담아서 CREATED 상태 코드 반환 후 노선, 저장된 역들, 저장된 구간들 정보를 응답한다.")
            void saveRequestInfo() {
                // when
                Response response = RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(INITIAL_STATION_REQUEST_A_TO_C.REQUEST)
                        .post("/stations")
                        .then()
                        .log().all()
                        .extract().response();

                JsonPath jsonPath = response.jsonPath();

                // then
                assertAll(
                        () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED.value()),
                        () -> assertThat(response.getHeader("Location")).isEqualTo("/lines/1"),

                        () -> assertThat(jsonPath.getLong("line.id")).isEqualTo(INITIAL_Line2.ID),
                        () -> assertThat(jsonPath.getString("line.name")).isEqualTo(INITIAL_Line2.NAME),

                        () -> assertThat(jsonPath.getLong("savedStations[0].id")).isEqualTo(INITIAL_STATION_A.ID),
                        () -> assertThat(jsonPath.getString("savedStations[0].name")).isEqualTo(INITIAL_STATION_A.NAME),
                        () -> assertThat(jsonPath.getLong("savedStations[1].id")).isEqualTo(INITIAL_STATION_C.ID),
                        () -> assertThat(jsonPath.getString("savedStations[1].name")).isEqualTo(INITIAL_STATION_C.NAME),

                        () -> assertThat(jsonPath.getLong("savedSections[0].id")).isEqualTo(INITIAL_GENERAL_SECTION_A_TO_C.ID),
                        () -> assertThat(jsonPath.getString("savedSections[0].upStationName")).isEqualTo(INITIAL_STATION_A.NAME),
                        () -> assertThat(jsonPath.getString("savedSections[0].downStationName")).isEqualTo(INITIAL_STATION_C.NAME),
                        () -> assertThat(jsonPath.getInt("savedSections[0].distance")).isEqualTo(INITIAL_GENERAL_SECTION_A_TO_C.DISTANCE.getDistance())
                );
            }
        }

        @Nested
        @DisplayName("존재하는 노선에 역을 등록하는 경우")
        class saveStationWhenLineExist {

            @BeforeEach
            void setUp() {
                jdbcTemplate.update("INSERT INTO LINE (name) VALUES ('2호선')");
                jdbcTemplate.update("INSERT INTO STATION (name, line_id) VALUES ('A역', 1)");
                jdbcTemplate.update("INSERT INTO STATION (name, line_id) VALUES ('C역', 1)");
                jdbcTemplate.update("INSERT INTO GENERAL_SECTION (up_station_id, down_station_id, line_id, distance) VALUES (1, 2, 1, 10)");
            }

            @Test
            @DisplayName("두 개의 요청 역이 모두 존재하지 않으면 NOT FOUND 상태코드를 반환하고 예외 메시지를 응답한다.")
            void throwWhenAllStationNotExist() {
                // when
                Response response = RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(NOT_EXIST_ALL_STATION_REQUEST_D_TO_E.REQUEST)
                        .post("/stations")
                        .then()
                        .extract().response();

                // then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
                assertThat(response.getBody().asString()).isEqualTo("두개의 역 모두가 존재하지 않습니다.");
            }

            @Test
            @DisplayName("두 개의 요청 역이 이미 존재하면  BAD REQUEST 상태코드를 반환하고 예외 메시지를 응답한다.")
            void throwWhenAllStationAlreadyExist() {
                // when
                Response response = RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(INITIAL_STATION_REQUEST_A_TO_C.REQUEST)
                        .post("/stations")
                        .then()
                        .extract().response();

                // then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                assertThat(response.getBody().asString()).isEqualTo("두개의 역이 이미 모두 존재합니다.");
            }

            @Test
            @DisplayName("등록할 역이 가운데 역일 때 역과 새로운 상행 구간, 하행 구간을 저장하고, Location 헤더에 저장된 노선을 담아서 CREATED 상태 코드 반환 후 노선, 저장된 역들, 저장된 구간들 정보를 응답한다.")
            void saveStationAndNewUpAndDownSection() {
                // when
                Response response = RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(DOWN_MIDDLE_STATION_REQUEST_A_TO_B.REQUEST)
                        .post("/stations")
                        .then()
                        .extract().response();

                JsonPath jsonPath = response.jsonPath();

                // then
                assertAll(
                        () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED.value()),
                        () -> assertThat(response.getHeader("Location")).isEqualTo("/lines/1"),

                        () -> assertThat(jsonPath.getLong("line.id")).isEqualTo(INITIAL_Line2.ID),
                        () -> assertThat(jsonPath.getString("line.name")).isEqualTo(INITIAL_Line2.NAME),

                        () -> assertThat(jsonPath.getLong("savedStations[0].id")).isEqualTo(3L),
                        () -> assertThat(jsonPath.getString("savedStations[0].name")).isEqualTo(STATION_B.NAME),

                        () -> assertThat(jsonPath.getLong("savedSections[0].id")).isEqualTo(2L),
                        () -> assertThat(jsonPath.getString("savedSections[0].upStationName")).isEqualTo(INITIAL_STATION_A.NAME),
                        () -> assertThat(jsonPath.getString("savedSections[0].downStationName")).isEqualTo(STATION_B.NAME),
                        () -> assertThat(jsonPath.getInt("savedSections[0].distance")).isEqualTo(GENERAL_SECTION_A_TO_B.DISTANCE.getDistance()),

                        () -> assertThat(jsonPath.getLong("savedSections[1].id")).isEqualTo(3L),
                        () -> assertThat(jsonPath.getString("savedSections[1].upStationName")).isEqualTo(STATION_B.NAME),
                        () -> assertThat(jsonPath.getString("savedSections[1].downStationName")).isEqualTo(INITIAL_STATION_C.NAME),
                        () -> assertThat(jsonPath.getInt("savedSections[1].distance")).isEqualTo(GENERAL_SECTION_B_TO_C.DISTANCE.getDistance())
                );
            }

            @Test
            @DisplayName("등록할 역이 상행 종점일 때 역과 새로운 상행 구간을 저장하고, Location 헤더에 저장된 노선을 담아서 CREATED 상태 코드 반환 후 노선, 저장된 역들, 저장된 구간들 정보를 응답한다..")
            void saveStationAndNewUpSection() {
                // when
                Response response = RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(UP_END_STATION_REQUEST_D_TO_A.REQUEST)
                        .post("/stations")
                        .then()
                        .extract().response();

                JsonPath jsonPath = response.jsonPath();

                // then
                assertAll(
                        () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED.value()),
                        () -> assertThat(response.getHeader("Location")).isEqualTo("/lines/1"),

                        () -> assertThat(jsonPath.getLong("line.id")).isEqualTo(INITIAL_Line2.ID),
                        () -> assertThat(jsonPath.getString("line.name")).isEqualTo(INITIAL_Line2.NAME),

                        () -> assertThat(jsonPath.getLong("savedStations[0].id")).isEqualTo(3L),
                        () -> assertThat(jsonPath.getString("savedStations[0].name")).isEqualTo(STATION_D.NAME),

                        () -> assertThat(jsonPath.getLong("savedSections[0].id")).isEqualTo(2L),
                        () -> assertThat(jsonPath.getString("savedSections[0].upStationName")).isEqualTo(STATION_D.NAME),
                        () -> assertThat(jsonPath.getString("savedSections[0].downStationName")).isEqualTo(INITIAL_STATION_A.NAME),
                        () -> assertThat(jsonPath.getInt("savedSections[0].distance")).isEqualTo(GENERAL_SECTION_D_TO_A.DISTANCE.getDistance())
                );
            }

            @Test
            @DisplayName("등록할 역이 하행 종점일 때 역과 새로운 하행 구간을 저장하고, Location 헤더에 저장된 노선을 담아서 CREATED 상태 코드 반환 후 노선, 저장된 역들, 저장된 구간들 정보를 응답한다.")
            void saveStationAndNewDownSection() {
                // when
                Response response = RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(DOWN_END_STATION_REQUEST_C_TO_E.REQUEST)
                        .post("/stations")
                        .then()
                        .extract().response();

                JsonPath jsonPath = response.jsonPath();

                // then
                assertAll(
                        () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED.value()),
                        () -> assertThat(response.getHeader("Location")).isEqualTo("/lines/1"),

                        () -> assertThat(jsonPath.getLong("line.id")).isEqualTo(INITIAL_Line2.ID),
                        () -> assertThat(jsonPath.getString("line.name")).isEqualTo(INITIAL_Line2.NAME),

                        () -> assertThat(jsonPath.getLong("savedStations[0].id")).isEqualTo(3L),
                        () -> assertThat(jsonPath.getString("savedStations[0].name")).isEqualTo(STATION_E.NAME),

                        () -> assertThat(jsonPath.getLong("savedSections[0].id")).isEqualTo(2L),
                        () -> assertThat(jsonPath.getString("savedSections[0].upStationName")).isEqualTo(INITIAL_STATION_C.NAME),
                        () -> assertThat(jsonPath.getString("savedSections[0].downStationName")).isEqualTo(STATION_E.NAME),
                        () -> assertThat(jsonPath.getInt("savedSections[0].distance")).isEqualTo(GENERAL_SECTION_C_TO_E.DISTANCE.getDistance())
                );
            }
        }
    }

    @Nested
    @DisplayName("역 삭제 시")
    class removeStation {

        @BeforeEach
        void setUp() {
            jdbcTemplate.update("INSERT INTO LINE (name) VALUES ('2호선')");
            jdbcTemplate.update("INSERT INTO STATION (name, line_id) VALUES ('A역', 1)");
            jdbcTemplate.update("INSERT INTO STATION (name, line_id) VALUES ('C역', 1)");
            jdbcTemplate.update("INSERT INTO GENERAL_SECTION (up_station_id, down_station_id, line_id, distance) VALUES (1, 2, 1, 10)");
        }

        @Test
        @DisplayName("요청 ID에 해당하는 역이 없으면 NOT FOUND 상태코드를 반환하고 예외 메시지를 응답한다.")
        void throwWhenNotFoundStation() {
            // when
            Response response = RestAssured.given().log().all()
                    .delete("/stations/" + -1)
                    .then()
                    .extract().response();

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
            assertThat(response.getBody().asString()).isEqualTo("역 ID에 해당하는 역이 존재하지 않습니다.");
        }

        @Nested
        @DisplayName("노선에 역이 2개만 있을 때 역을 삭제하는 경우")
        class removeStationWhenStationByLineExistOnlyTwo {

            @Test
            @DisplayName("해당하는 노선, 구간, 역 2개를 모두 삭제한 후, NO CONTENT 상태코드를 반환한다.")
            void removeAll() {
                // when
                Response response = RestAssured.given().log().all()
                        .delete("/stations/" + 1)
                        .then()
                        .extract().response();

                // then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
            }
        }

        @Nested
        @DisplayName("노선에 역이 2개 이상 존재할 때 역을 삭제하는 경우")
        class removeStationWhenStationByLineExistOverTwo {

            @Test
            @DisplayName("가운데 역을 정상적으로 삭제하고, NO CONTENT 상태코드를 반환한다.")
            void removeMiddleStation() {
                // given
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(DOWN_MIDDLE_STATION_REQUEST_A_TO_B.REQUEST)
                        .post("/stations");

                // when
                int deleteStationBId = 3;
                Response response = RestAssured.given().log().all()
                        .delete("/stations/" + deleteStationBId)
                        .then()
                        .extract().response();

                // then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
            }

            @Test
            @DisplayName("상행 종점 역을 정상적으로 삭제하고, NO CONTENT 상태코드를 반환한다.")
            void removeUpEndStation() {
                // given
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(UP_END_STATION_REQUEST_D_TO_A.REQUEST)
                        .post("/stations");

                // when
                int deleteStationDId = 3;
                Response response = RestAssured.given().log().all()
                        .delete("/stations/" + deleteStationDId)
                        .then()
                        .extract().response();

                // then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
            }

            @Test
            @DisplayName("하행 종점 역을 정상적으로 삭제하고, NO CONTENT 상태코드를 반환한다.")
            void removeDownEndStation() {
                // given
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(DOWN_END_STATION_REQUEST_C_TO_E.REQUEST)
                        .post("/stations");

                // when
                int deleteStationEId = 3;
                Response response = RestAssured.given().log().all()
                        .delete("/stations/" + deleteStationEId)
                        .then()
                        .extract().response();

                // then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
            }
        }

    }
}
