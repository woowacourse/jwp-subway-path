package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.application.dto.SectionRequest;
import subway.application.dto.SectionStations;

public class SectionIntegrationTest extends IntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("비어있는 지하철 노선도에 새로운 노선을 등록할 수 있다.")
    @Test
    void addStations_empty() {
        // given
        SectionRequest sectionRequest = new SectionRequest(1L, new SectionStations(1L, 2L, 10));

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        // then
        Integer updatedDistance = jdbcTemplate.queryForObject(
                "select distance FROM SECTIONS WHERE left_station_id = 1 and right_station_id = 2", Integer.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(updatedDistance).isEqualTo(10)
        );
    }

    @DisplayName("비어있지 않은 지하철 노선도의 종점에 노선을 등록할 수 있다.")
    @Test
    void addStations_end() {
        // given
        jdbcTemplate.update(
                "insert into SECTIONS (line_id, left_station_id, right_station_id, distance) VALUES (1L, 1L, 2L, 16)");
        SectionRequest sectionRequest = new SectionRequest(1L, new SectionStations(2L, 3L, 10));

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        // then
        Integer updatedDistance = jdbcTemplate.queryForObject(
                "select distance FROM SECTIONS WHERE left_station_id = 2 and right_station_id = 3", Integer.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(updatedDistance).isEqualTo(10)
        );
    }

    @DisplayName("비어있지 않은 지하철 노선도의 시작점에 노선을 등록할 수 있다.")
    @Test
    void addStations_start() {
        // given
        jdbcTemplate.update(
                "insert into SECTIONS (line_id, left_station_id, right_station_id, distance) VALUES (1L, 2L, 3L, 16)");
        SectionRequest sectionRequest = new SectionRequest(1L, new SectionStations(1L, 2L, 10));

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        // then
        Integer updatedDistance = jdbcTemplate.queryForObject(
                "select distance FROM SECTIONS WHERE left_station_id = 1 and right_station_id = 2", Integer.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(updatedDistance).isEqualTo(10)
        );
    }

    @DisplayName("지하철역을 노선의 중간에 등록하면 기존 노선의 거리를 조정한다.")
    @Test
    void addStationsInBetweenDownDirection() {
        // given
        jdbcTemplate.update(
                "insert into SECTIONS (line_id, left_station_id, right_station_id, distance) VALUES (1L, 1L, 2L, 16)");
        SectionRequest sectionRequest = new SectionRequest(1L, new SectionStations(1L, 3L, 10));

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        // then
        Integer updatedDistance = jdbcTemplate.queryForObject(
                "select distance FROM SECTIONS WHERE left_station_id = 3 and right_station_id = 2", Integer.class);
        Integer newDistance = jdbcTemplate.queryForObject(
                "select distance FROM SECTIONS WHERE left_station_id = 1 and right_station_id = 3", Integer.class);


        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(updatedDistance).isEqualTo(6),
                () -> assertThat(newDistance).isEqualTo(10)
        );
    }

    @DisplayName("지하철역을 노선에서 삭제한다.")
    @Test
    void deleteStation() {
        // given
        jdbcTemplate.update(
                "insert into SECTIONS (line_id, left_station_id, right_station_id, distance) VALUES (1L, 1L, 2L, 16)");
        jdbcTemplate.update(
                "insert into SECTIONS (line_id, left_station_id, right_station_id, distance) VALUES (1L, 2L, 3L, 16)");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .delete("/sections/1/2")
                .then().log().all()
                .extract();

        // then
        Integer updatedDistance = jdbcTemplate.queryForObject(
                "select distance FROM SECTIONS WHERE left_station_id = 1 and right_station_id = 3", Integer.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(updatedDistance).isEqualTo(32)
        );
    }

    @DisplayName("지하철역을 노선에서 삭제할 떄, 노선에 역이 두 개뿐이면 모두 삭제한다.")
    @ParameterizedTest
    @ValueSource(strings = {"1", "2"})
    void deleteStationOnlyTwoExisting(String stationId) {
        // given
        jdbcTemplate.update(
                "insert into SECTIONS (line_id, left_station_id, right_station_id, distance) values (1L, 1L, 2L, 16)");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .delete("/sections/1/" + stationId)
                .then().log().all()
                .extract();

        // then
        Boolean isExist = jdbcTemplate.queryForObject(
                "select EXISTS (select id from SECTIONS where line_id = 1 limit 1) as success", Boolean.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(isExist).isFalse()
        );
    }
}
