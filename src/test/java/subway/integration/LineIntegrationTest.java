package subway.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.ui.dto.LineRequest;
import subway.ui.dto.LineResponse;
import subway.ui.dto.StationInsertRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {

    private static long stationId1;
    private static long stationId2;

    @BeforeEach
    public void setUp() {
        super.setUp();
        stationId1 = createStation("잠실");
        stationId2 = createStation("잠실새내");
    }

    private Long createStation(final String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        // /stations/{id}
        return Long.valueOf(response.header("Location").split("/")[2]);
    }

    private LineRequest createBasicLineRequest(final String name, final String color) {
        return new LineRequest(name, color, stationId1, stationId2, 7);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createBasicLineRequest("2호선", "#00A84D"))
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
                .body(createBasicLineRequest("2호선", "#00A84D"))
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createBasicLineRequest("2호선", "#00A84D"))
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
                .body(createBasicLineRequest("2호선", "#00A84D"))
                .when().post("/lines")
                .then().log().all().
                extract();

        ExtractableResponse<Response> createResponse2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createBasicLineRequest("1호선", "#00A84D"))
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
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createBasicLineRequest("2호선", "#00A84D"))
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse resultResponse = response.as(LineResponse.class);
        assertThat(resultResponse.getId()).isEqualTo(lineId);
    }

    @Disabled
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createBasicLineRequest("2호선", "#00A84D"))
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createBasicLineRequest("3호선", "#00A84D"))
                .when().put("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Disabled
    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createBasicLineRequest("2호선", "#00A84D"))
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

    @Test
    @DisplayName("노선에 역을 추가한다.")
    void insertStation() {
        //given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createBasicLineRequest("2호선", "#00A84D"))
                .when().post("/lines")
                .then().log().all().
                extract();

        //when
        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        Long stationId = createStation("건대");
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(new StationInsertRequest(stationId, lineId, stationId1, "DOWN", 1))
                .when().post("/lines/stations")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("노선의 역을 삭제한다.")
    void deleteStation() {
        //given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createBasicLineRequest("2호선", "#00A84D"))
                .when().post("/lines")
                .then().log().all().
                extract();
        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        Long stationId = createStation("성수");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationInsertRequest(stationId, lineId, stationId1, "DOWN", 1))
                .when().post("/lines/stations");

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}/{stationId}", lineId, stationId)
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("역이 2개 이하인 노선의 역을 삭제한다.")
    void deleteStationHavingUnderTwoStations() {
        //given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createBasicLineRequest("2호선", "#00A84D"))
                .when().post("/lines")
                .then().log().all().
                extract();
        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}/{stationId}", lineId, stationId1)
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("경로를 조회한다.")
    void find_path_test() {
        // given
        final Long st1 = createStation("st1");
        final Long st2 = createStation("st2");
        final Long st3 = createStation("st3");
        final Long st4 = createStation("st4");
        final Long st5 = createStation("st5");
        final Long st6 = createStation("st6");
        final Long st7 = createStation("st7");

        final Long line1Id = createLine("line1", "blue", st1, st3, 1);
        insertStation(line1Id, st4, st3, "DOWN", 2);
        insertStation(line1Id, st5, st4, "DOWN", 2);
        insertStation(line1Id, st6, st5, "DOWN", 2);
        insertStation(line1Id, st2, st6, "DOWN", 1);

        final Long line2Id = createLine("line2", "red", st7, st3, 2);
        insertStation(line2Id, st5, st3, "DOWN", 3);


        // when
        // then
        RestAssured
                .given().log().all()
                .queryParam("startstation", st1)
                .queryParam("destinationstation", st2)
                .when().get("/path")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("distance", is(7))
                .body("fare", is(1250))
                .body("paths", hasSize(3))
                .body("paths[0].id.toLong()", is(line1Id))
                .body("paths[0].stations", hasSize(2))
                .body("paths[1].id.toLong()", is(line2Id))
                .body("paths[1].stations", hasSize(2))
                .body("paths[2].id.toLong()", is(line1Id))
                .body("paths[2].stations", hasSize(3));
    }

    private Long createLine(String name, String color, Long initialUpStation, Long initialDownStation, int initialDistance) {
        final ExtractableResponse<Response> createResponse = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LineRequest(name, color, initialUpStation, initialDownStation, initialDistance))
                .when().post("/lines")
                .then()
                .extract();
        return Long.parseLong(createResponse.header("Location").split("/")[2]);
    }

    private void insertStation(Long lineId, Long stationId, Long adjacentStationId, String direction, int distance) {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new StationInsertRequest(stationId, lineId, adjacentStationId, direction, distance))
                .when().post("/lines/stations")
                .then()
                .extract();
    }

}
