package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.domain.line.dto.AddStationToLineRequest;
import subway.domain.line.dto.LineCreateRequest;
import subway.domain.station.Station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {

    private final LineCreateRequest createRequest =
            new LineCreateRequest("3호선", "경복궁", "안국", 10);
    private Station initialUpStation;
    private Station initialDownStation;

    @Autowired
    private StationDao stationDao;

    @Autowired
    private LineDao lineDao;

    @BeforeEach
    public void setUp() {
        super.setUp();

        initialUpStation = stationDao.insert(new Station("경복궁"));
        initialDownStation = stationDao.insert(new Station("안국"));
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine_success() {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/lines/1");
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 노선을 생성하려 하면 BAD_REQUEST가 발생한다.")
    @Test
    void createLine_fail() {
        // given
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest)
                .when().post("/lines");

        // when, then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("기존 노선에 역을 추가한다.")
    @Test
    void addStationToLine() {
        // given
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest)
                .when().post("/lines")
                .then().log().all().
                extract();
        String location = response.header("Location");
        Long lineId = Long.parseLong(location.split("/")[2]);

        // when
        Station newStation = stationDao.insert(new Station("충무로"));
        AddStationToLineRequest addRequest = new AddStationToLineRequest(initialUpStation.getId(),
                newStation.getId(), 5);

        // then
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(addRequest)
                .when().post("/lines/{lineId}/stations", lineId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("기존 노선에 존재하는 역을 제거한다.")
    @Test
    void deleteStationFromLine() {
        // given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest)
                .when().post("/lines")
                .then().log().all().
                extract();
        String location = createResponse.header("Location");
        Long lineId = Long.parseLong(location.split("/")[2]);

        Station newStation = stationDao.insert(new Station("충무로"));
        AddStationToLineRequest addRequest = new AddStationToLineRequest(initialUpStation.getId(), newStation.getId(),
                5);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(addRequest)
                .when().post("/lines/{lineId}/stations", lineId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        // when
        ExtractableResponse<Response> deleteResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{lineId}/stations/{stationId}", lineId, newStation.getId())
                .then().log().all().
                extract();

        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("기존 노선에 존재하는 모든 역을 조회한다")
    void findLineById() {
        // given
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest)
                .when().post("/lines")
                .then().log().all().
                extract();
        String location = response.header("Location");
        Long lineId = Long.parseLong(location.split("/")[2]);

        Station newStation = stationDao.insert(new Station("충무로"));
        AddStationToLineRequest addRequest = new AddStationToLineRequest(initialUpStation.getId(),
                newStation.getId(), 5);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(addRequest)
                .when().post("/lines/{lineId}/stations", lineId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        // when
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(addRequest)
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("lineId", is(1))
                .body("lineName", is("3호선"))
                .body("stations[0].stationName", is("경복궁"))
                .body("stations[1].stationName", is("충무로"))
                .body("stations[2].stationName", is("안국"));
    }

    @Test
    @DisplayName("모든 노선의 모든 역을 조회한다")
    void findAllLines() {
        // given
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest)
                .when().post("/lines")
                .then().log().all();

        Station upStation = stationDao.insert(new Station("잠실"));
        Station downStation = stationDao.insert(new Station("잠실새내"));
        LineCreateRequest createRequest2 = new LineCreateRequest("2호선", "잠실", "잠실새내", 5);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest2)
                .when().post("/lines")
                .then().log().all();

        // when
        RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

    }
}