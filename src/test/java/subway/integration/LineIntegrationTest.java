package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dao.StationDao;
import subway.domain.line.Station;
import subway.dto.request.AddStationToExistLineRequest;
import subway.dto.request.LineCreateRequest;

public class LineIntegrationTest extends IntegrationTest {

    private Station line_3_initialUpStation;
    private Station line_3_initialDownStation;
    private Station line_3_newStation;
    private Station line_2_initialUpStation;
    private Station line_2_initialDownStation;

    private LineCreateRequest line_3_lineCreateRequest;
    private LineCreateRequest line_2_lineCreateRequest;
    private AddStationToExistLineRequest line_3_addStationToExistLineRequest;

    @Autowired
    private StationDao stationDao;

    @BeforeEach
    public void setUp() {
        super.setUp();

        line_3_initialUpStation = stationDao.insert(new Station("경복궁"));
        line_3_initialDownStation = stationDao.insert(new Station("안국"));
        line_3_newStation = stationDao.insert(new Station("충무로"));

        line_3_lineCreateRequest = new LineCreateRequest("3호선", 1000, line_3_initialUpStation.getId(), line_3_initialDownStation.getId(), 10);
        line_3_addStationToExistLineRequest = new AddStationToExistLineRequest(line_3_initialUpStation.getId(), line_3_newStation.getId(), 5);

        line_2_initialUpStation = stationDao.insert(new Station("잠실"));
        line_2_initialDownStation = stationDao.insert(new Station("잠실새내"));

        line_2_lineCreateRequest = new LineCreateRequest("2호선", 1000, line_2_initialUpStation.getId(), line_2_initialDownStation.getId(), 5);
    }

    @Test
    @DisplayName("새로운 노선 생성 API")
    void createLine() {
        // given
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(line_3_lineCreateRequest)
                .when().post("/lines")
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
        assertThat(response.jsonPath().getString("lineName")).isEqualTo("3호선");
        assertThat(response.jsonPath().getList("stationIds")).isEqualTo(
                List.of(line_3_initialUpStation.getId().intValue(), line_3_initialDownStation.getId().intValue()));
    }

    @DisplayName("기존 노선에 역 추가 API")
    @Test
    void addStationToLine() {
        // given
        ExtractableResponse<Response> initialResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(line_3_lineCreateRequest)
                .when().post("/lines")
                .then().log().all().
                extract();
        String location = initialResponse.header("Location");
        Long lineId = Long.parseLong(location.split("/")[2]);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(line_3_addStationToExistLineRequest)
                .when().post("/lines/{lineId}/stations", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getLong("lineId")).isEqualTo(lineId);
        assertThat(response.jsonPath().getString("lineName")).isEqualTo("3호선");
        assertThat(response.jsonPath().getList("stationIds")).isEqualTo(
                List.of(line_3_initialUpStation.getId().intValue(), line_3_newStation.getId().intValue(), line_3_initialDownStation.getId().intValue()));
    }

    @DisplayName("기존 노선에 존재하는 역 제거 API")
    @Test
    void deleteStationFromLine() {
        // given
        ExtractableResponse<Response> initialResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(line_3_lineCreateRequest)
                .when().post("/lines")
                .then().log().all().
                extract();
        String location = initialResponse.header("Location");
        Long lineId = Long.parseLong(location.split("/")[2]);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(line_3_addStationToExistLineRequest)
                .when().post("/lines/{lineId}/stations", lineId)
                .then().log().all();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{lineId}/stations/{stationId}", lineId, line_3_initialDownStation.getId())
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getLong("lineId")).isEqualTo(lineId);
        assertThat(response.jsonPath().getString("lineName")).isEqualTo("3호선");
        assertThat(response.jsonPath().getList("stationIds")).isEqualTo(
                List.of(line_3_initialUpStation.getId().intValue(), line_3_newStation.getId().intValue()));
    }

    @Test
    @DisplayName("특정 노선의 모든 역 조회 API")
    void findLineById() {
        // given
        ExtractableResponse<Response> initialResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(line_3_lineCreateRequest)
                .when().post("/lines")
                .then().log().all().
                extract();
        String location = initialResponse.header("Location");
        Long lineId = Long.parseLong(location.split("/")[2]);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(line_3_addStationToExistLineRequest)
                .when().post("/lines/{lineId}/stations", lineId)
                .then().log().all();

        // when
        // then
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("lineId", is(lineId.intValue()))
                .body("lineName", is("3호선"))
                .body("stations[0].stationName", is("경복궁"))
                .body("stations[1].stationName", is("충무로"))
                .body("stations[2].stationName", is("안국"));
    }

    @Test
    @DisplayName("모든 노선의 모든 역 조회 API")
    void findAllLines() {
        // given
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(line_3_lineCreateRequest)
                .when().post("/lines")
                .then().log().all();

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(line_2_lineCreateRequest)
                .when().post("/lines")
                .then().log().all();

        // when
        // then
        RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("[0].lineName", is(line_3_lineCreateRequest.getLineName()))
                .body("[0].stations[0].stationName", is("경복궁"))
                .body("[0].stations[1].stationName", is("안국"))
                .body("[1].lineName", is(line_2_lineCreateRequest.getLineName()))
                .body("[1].stations[0].stationName", is("잠실"))
                .body("[1].stations[1].stationName", is("잠실새내"));
    }
}
