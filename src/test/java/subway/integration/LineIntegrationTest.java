package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.fixture.StationFixture.FIXTURE_STATION_1;
import static subway.fixture.StationFixture.FIXTURE_STATION_2;
import static subway.fixture.StationFixture.FIXTURE_STATION_3;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;
import subway.dto.SectionStations;
import subway.dto.StationResponse;

@DisplayName("지하철 노선 관련 기능 통합 테스트")
public class LineIntegrationTest extends IntegrationTest {
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // TODO Request Fixture로 빼기
        lineRequest1 = new LineRequest("신분당선", "bg-red-600");
        lineRequest2 = new LineRequest("구신분당선", "bg-red-600");
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createResponse1 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        ExtractableResponse<Response> createResponse2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest2)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Long> expectedLineIds = Stream.of(createResponse1, createResponse2)
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        jdbcTemplate.update(
                "insert into SECTIONS (line_id, left_station_id, right_station_id, distance) VALUES (1L, 2L, 3L, 16)");
        jdbcTemplate.update(
                "insert into SECTIONS (line_id, left_station_id, right_station_id, distance) VALUES (1L, 1L, 2L, 16)");

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", 1)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse resultResponse = response.as(LineResponse.class);

        Assertions.assertAll(
                () -> assertThat(resultResponse.getId()).isEqualTo(1),
                () -> assertThat(resultResponse.getStationResponses())
                        .containsExactly(
                                StationResponse.from(FIXTURE_STATION_1),
                                StationResponse.from(FIXTURE_STATION_2),
                                StationResponse.from(FIXTURE_STATION_3)
                        )
        );

    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest2)
                .when().put("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("지하철역을 노선에 등록한다.(하행)")
    @Test
    void addStationsDownDirection() {
        // given
        // TODO Request Fixture로 빼기
        SectionRequest sectionRequest = new SectionRequest(new SectionStations(1L, 2L, 10), "down");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/1")
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
        SectionRequest sectionRequest = new SectionRequest(new SectionStations(1L, 3L, 10), "up");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/1")
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
        SectionRequest sectionRequest = new SectionRequest(new SectionStations(1L, 3L, 10), "down");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/1")
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
        SectionRequest sectionRequest = new SectionRequest(new SectionStations(2L, 3L, 10), "up");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/1")
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
                .delete("/lines/1/2")
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
                .delete("/lines/1/{stationId}", stationId)
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
