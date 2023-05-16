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
import subway.dto.SectionRequest;
import subway.dto.SectionStations;

public class SectionIntegrationTest extends IntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("지하철역을 노선에 등록한다.(하행)")
    @Test
    void addStationsDownDirection() {
        // given
        SectionRequest sectionRequest = new SectionRequest(1L, new SectionStations(1L, 2L, 10),
                "down");

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

    @DisplayName("지하철역을 노선에 등록한다. (상행)")
    @Test
    void addStationsUpDirection() {
        // given
        jdbcTemplate.update(
                "insert into SECTIONS (line_id, left_station_id, right_station_id, distance) VALUES (1L, 1L, 2L, 16)");
        SectionRequest sectionRequest = new SectionRequest(1L, new SectionStations(1L, 3L, 10),
                "up");

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
                "select distance FROM SECTIONS WHERE left_station_id = 3 and right_station_id = 1", Integer.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(updatedDistance).isEqualTo(10)
        );
    }

    @DisplayName("지하철역을 노선의 중간에 등록하면, 거리를 조정해준다. (하행)")
    @Test
    void addStationsInBetweenDownDirection() {
        // given
        jdbcTemplate.update(
                "insert into SECTIONS (line_id, left_station_id, right_station_id, distance) VALUES (1L, 1L, 2L, 16)");
        SectionRequest sectionRequest = new SectionRequest(1L, new SectionStations(1L, 3L, 10),
                "down");

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

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(updatedDistance).isEqualTo(6)
        );
    }

    @DisplayName("지하철역을 노선의 중간에 등록하면, 거리를 조정해준다. (상행)")
    @Test
    void addStationsInBetweenUpDirection() {
        // given
        jdbcTemplate.update(
                "insert into SECTIONS (line_id, left_station_id, right_station_id, distance) VALUES (1L, 1L, 2L, 16)");
        SectionRequest sectionRequest = new SectionRequest(1L, new SectionStations(2L, 3L, 10),
                "up");

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
                "select distance FROM SECTIONS WHERE left_station_id = 1 and right_station_id = 3", Integer.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(updatedDistance).isEqualTo(6)
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
