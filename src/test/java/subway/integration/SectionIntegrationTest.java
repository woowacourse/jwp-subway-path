package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.jdbc.Sql;
import subway.controller.dto.request.SectionRequest;
import subway.controller.dto.response.LineResponse;
import subway.entity.SectionEntity;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
class SectionIntegrationTest extends IntegrationTest {

    private static final RowMapper<SectionEntity> sectionEntityRowMapper = (rs, rn) -> new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("line_id"),
                    rs.getInt("distance"),
                    rs.getLong("previous_station_id"),
                    rs.getLong("next_station_id")
    );
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     *
     * INSERT INTO section(line_id, distance, previous_station_id, next_station_id)
     * VALUES(1, 3, 1, 2), (1, 4, 2, 3), (2, 5, 1, 4), (2, 6, 4, 5);
     *
     * INSERT INTO station(name) VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @ParameterizedTest(name = "{0} 인 경우 BAD_REQUEST 를 반환한다.")
    @DisplayName("BAD REQUEST 를 반환하는 경우")
    @MethodSource("createSectionInvalidRequest")
    @Sql(scripts = {"/line_test_data.sql", "/station_test_data.sql", "/section_test_data.sql"})
    void createSectionBadRequest(String testName, String lineName, String direction, String standardStationName, String additionalStationName, Integer distance) {
        SectionRequest sectionRequest = new SectionRequest(lineName, direction, standardStationName, additionalStationName, distance);

        ExtractableResponse<Response> badRequestByDistance = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/sections")
                .then().extract();

        assertThat(badRequestByDistance.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    private static Stream<Arguments> createSectionInvalidRequest() {
        return Stream.of(
                Arguments.of("Distance 가 정수가 아닌 경우", "2호선", "UP", "잠실", "송파", 0),
                Arguments.of("인수 중 null 이 포함된 경우", null, "UP", "잠실", "송파", 1),
                Arguments.of("인수 중 공백이 포함된 경우", "", "UP", "잠실", "송파", 1),
                Arguments.of("노선이 존재하지 않는 경우", "빙봉당선", "UP", "잠실", "송파", 1),
                Arguments.of("역과 역 사이에 구간 (Down) 을 추가한 경우 Distance 가 정수가 아니게 되어", "2호선", "DOWN", "잠실새내", "송파", 3),
                Arguments.of("역과 역 사이에 구간 (Up) 을 추가한 경우 Distance 가 정수가 아니게 되어", "2호선", "UP", "잠실새내", "송파", 4),
                Arguments.of("해당 노선에 두 역 모두 포함되지 않은 경우", "2호선", "UP", "석촌", "송파", 5),
                Arguments.of("해당 노선에 두 역 모두 포함된 경우", "2호선", "UP", "잠실", "종합운동장", 5)
        );
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     *
     * INSERT INTO section(line_id, distance, previous_station_id, next_station_id)
     * VALUES(1, 3, 1, 2), (1, 4, 2, 3), (2, 5, 1, 4), (2, 6, 4, 5);
     *
     * INSERT INTO station(name) VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @Test
    @DisplayName("종점에 Down 방향으로 역을 추가한다.")
    @Sql(scripts = {"/line_test_data.sql", "/station_test_data.sql", "/section_test_data.sql"})
    void addSection_down() {
        SectionRequest sectionRequest = new SectionRequest("2호선", "DOWN", "잠실", "송파", 5);

        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/sections")
                .then().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse.getId()).isEqualTo(1L);
        assertThat(lineResponse.getName()).isEqualTo("2호선");
        assertThat(lineResponse.getColor()).isEqualTo("bg-green-600");
        assertThat(lineResponse.getSections().get(0).getPreviousStation().getName()).isEqualTo("송파");
        assertThat(lineResponse.getSections().get(0).getNextStation().getName()).isEqualTo("잠실");
        assertThat(lineResponse.getSections().get(0).getDistance()).isEqualTo(5);
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     *
     * INSERT INTO section(line_id, distance, previous_station_id, next_station_id)
     * VALUES(1, 3, 1, 2), (1, 4, 2, 3), (2, 5, 1, 4), (2, 6, 4, 5);
     *
     * INSERT INTO station(name) VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @Test
    @DisplayName("종점에 Up 방향으로 역을 추가한다.")
    @Sql(scripts = {"/line_test_data.sql", "/station_test_data.sql", "/section_test_data.sql"})
    void addSection_up() {
        SectionRequest sectionRequest = new SectionRequest("2호선", "UP", "종합운동장", "송파", 5);

        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/sections")
                .then().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse.getId()).isEqualTo(1L);
        assertThat(lineResponse.getName()).isEqualTo("2호선");
        assertThat(lineResponse.getColor()).isEqualTo("bg-green-600");
        assertThat(lineResponse.getSections()).hasSize(1);
        assertThat(lineResponse.getSections().get(0).getPreviousStation().getName()).isEqualTo("종합운동장");
        assertThat(lineResponse.getSections().get(0).getNextStation().getName()).isEqualTo("송파");
        assertThat(lineResponse.getSections().get(0).getDistance()).isEqualTo(5);
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     *
     * INSERT INTO section(line_id, distance, previous_station_id, next_station_id)
     * VALUES(1, 3, 1, 2), (1, 4, 2, 3), (2, 5, 1, 4), (2, 6, 4, 5);
     *
     * INSERT INTO station(name) VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @Test
    @DisplayName("역 사이 Down 방향으로 역을 추가한다.")
    @Sql(scripts = {"/line_test_data.sql", "/station_test_data.sql", "/section_test_data.sql"})
    void addSectionBetweenTwoStation_down() {
        SectionRequest sectionRequest = new SectionRequest("2호선", "DOWN", "잠실새내", "송파", 2);

        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/sections")
                .then().extract();

        new SectionRequest("2호선", "DOWN", "잠실새내", "송파", 2);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse.getId()).isEqualTo(1L);
        assertThat(lineResponse.getName()).isEqualTo("2호선");
        assertThat(lineResponse.getColor()).isEqualTo("bg-green-600");
        assertThat(lineResponse.getSections()).hasSize(2);
        assertThat(lineResponse.getSections().get(0).getPreviousStation().getName()).isEqualTo("잠실");
        assertThat(lineResponse.getSections().get(0).getNextStation().getName()).isEqualTo("송파");
        assertThat(lineResponse.getSections().get(0).getDistance()).isEqualTo(1);
        assertThat(lineResponse.getSections().get(1).getPreviousStation().getName()).isEqualTo("송파");
        assertThat(lineResponse.getSections().get(1).getNextStation().getName()).isEqualTo("잠실새내");
        assertThat(lineResponse.getSections().get(1).getDistance()).isEqualTo(2);
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     *
     * INSERT INTO section(line_id, distance, previous_station_id, next_station_id)
     * VALUES(1, 3, 1, 2), (1, 4, 2, 3), (2, 5, 1, 4), (2, 6, 4, 5);
     *
     * INSERT INTO station(name) VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @Test
    @DisplayName("역 사이 Up 방향으로 역을 추가한다.")
    @Sql(scripts = {"/line_test_data.sql", "/station_test_data.sql", "/section_test_data.sql"})
    void addSectionBetweenTwoStation_up() {
        SectionRequest sectionRequest = new SectionRequest("2호선", "UP", "잠실새내", "송파", 3);

        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/sections")
                .then().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse.getId()).isEqualTo(1L);
        assertThat(lineResponse.getName()).isEqualTo("2호선");
        assertThat(lineResponse.getColor()).isEqualTo("bg-green-600");
        assertThat(lineResponse.getSections()).hasSize(2);
        assertThat(lineResponse.getSections().get(0).getPreviousStation().getName()).isEqualTo("잠실새내");
        assertThat(lineResponse.getSections().get(0).getNextStation().getName()).isEqualTo("송파");
        assertThat(lineResponse.getSections().get(0).getDistance()).isEqualTo(3);
        assertThat(lineResponse.getSections().get(1).getPreviousStation().getName()).isEqualTo("송파");
        assertThat(lineResponse.getSections().get(1).getNextStation().getName()).isEqualTo("종합운동장");
        assertThat(lineResponse.getSections().get(1).getDistance()).isEqualTo(1);
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     *
     * INSERT INTO section(line_id, distance, previous_station_id, next_station_id)
     * VALUES(1, 3, 1, 2), (1, 4, 2, 3), (2, 5, 1, 4), (2, 6, 4, 5);
     *
     * INSERT INTO station(name) VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @ParameterizedTest(name = "{0} Bad Request 발생")
    @DisplayName("삭제 시 BadRequest")
    @MethodSource("deleteSectionBadRequest")
    @Sql(scripts = {"/line_test_data.sql", "/station_test_data.sql", "/section_test_data.sql"})
    void deleteLineFailByLineId(String testName, Long lineId, Long stationId) {
        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("lineid", lineId)
                .queryParam("stationid", stationId)
                .when().delete("/sections")
                .then().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static Stream<Arguments> deleteSectionBadRequest() {
        return Stream.of(
                Arguments.of("없는 Line 을 입력한 경우", 3L, 1L),
                Arguments.of("없는 Station 을 입력한 경우", 1L, 4L)
        );
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     *
     * INSERT INTO section(line_id, distance, previous_station_id, next_station_id)
     * VALUES(1, 3, 1, 2), (1, 4, 2, 3), (2, 5, 1, 4), (2, 6, 4, 5);
     *
     * INSERT INTO station(name) VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @Test
    @DisplayName("중간역을 삭제한 후 Section 이 잘 연결되었는지 확인한다.")
    @Sql(scripts = {"/line_test_data.sql", "/station_test_data.sql", "/section_test_data.sql"})
    void deleteStationBetweenTwoStation() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("lineid", 1)
                .queryParam("stationid", 2)
                .when().delete("/sections")
                .then().statusCode(HttpStatus.NO_CONTENT.value());

        String findNewSectionSql = "SELECT * FROM section WHERE previous_station_id = ? AND next_station_id = ?";
        List<SectionEntity> newSection = jdbcTemplate.query(findNewSectionSql, sectionEntityRowMapper, 1, 3);
        assertThat(newSection).hasSize(1);
        assertThat(newSection.get(0).getDistance()).isEqualTo(7);
        assertThat(newSection.get(0).getPreviousStationId()).isEqualTo(1L);
        assertThat(newSection.get(0).getNextStationId()).isEqualTo(3L);
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     *
     * INSERT INTO section(line_id, distance, previous_station_id, next_station_id)
     * VALUES(1, 3, 1, 2), (1, 4, 2, 3), (2, 5, 1, 4), (2, 6, 4, 5);
     *
     * INSERT INTO station(name) VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @ParameterizedTest
    @DisplayName("종점을 삭제한다.")
    @ValueSource(ints = {1, 3})
    @Sql(scripts = {"/line_test_data.sql", "/station_test_data.sql", "/section_test_data.sql"})
    void deleteLastStation(int stationId) {
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("lineid", 1)
                .queryParam("stationid", (long) stationId)
                .when().delete("/sections")
                .then().statusCode(HttpStatus.NO_CONTENT.value());
    }

}
