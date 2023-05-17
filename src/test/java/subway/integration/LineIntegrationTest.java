package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.api.LineRequest;
import subway.dto.api.LineResponse;
import subway.dto.api.StationInsertRequest;

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

    private LineRequest createLineRequest(final String name, final String color) {
        return new LineRequest(name, color, stationId1, stationId2, 7);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createLineRequest("2호선", "#00A84D"))
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
                .body(createLineRequest("2호선", "#00A84D"))
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createLineRequest("2호선", "#00A84D"))
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
                .body(createLineRequest("2호선", "#00A84D"))
                .when().post("/lines")
                .then().log().all().
                extract();

        ExtractableResponse<Response> createResponse2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createLineRequest("1호선", "#00A84D"))
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
                .body(createLineRequest("2호선", "#00A84D"))
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
                .body(createLineRequest("2호선", "#00A84D"))
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createLineRequest("3호선", "#00A84D"))
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
                .body(createLineRequest("2호선", "#00A84D"))
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
                .body(createLineRequest("2호선", "#00A84D"))
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
    @DisplayName("이미 등록된 역을 노선에 등록한다.")
    void insertAlreadyExistingStation() {
        //given
        ExtractableResponse<Response> createResponse = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createLineRequest("2호선", "#00A84D"))
                .when().post("/lines")
                .then().extract();

        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        Long stationId = createStation("건대");
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new StationInsertRequest(stationId, lineId, stationId1, "DOWN", 1))
                .when().post("/lines/stations");
        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(new StationInsertRequest(stationId, lineId, stationId1, "UP", 1))
                .when().post("/lines/stations")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(400);
    }

    @Test
    @DisplayName("노선의 역을 삭제한다.")
    void deleteStation() {
        //given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createLineRequest("2호선", "#00A84D"))
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
    @DisplayName("노선에 존재하지 않는 역을 삭제한다.")
    void deleteStationDoesNotExist() {
        //given
        ExtractableResponse<Response> createResponse = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createLineRequest("2호선", "#00A84D"))
                .when().post("/lines")
                .then().extract();
        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        Long stationId = createStation("성수");
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StationInsertRequest(stationId, lineId, stationId1, "DOWN", 1))
                .when().post("/lines/stations");

        //when
        Long stationToDelete = createStation("석촌");
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}/{stationId}", lineId, stationToDelete)
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("역이 2개 이하인 노선의 역을 삭제한다.")
    void deleteStationHavingUnderTwoStations() {
        //given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createLineRequest("2호선", "#00A84D"))
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
}
