package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.application.dto.SectionDto;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.dto.LineDto;
import subway.domain.Station;
import subway.ui.dto.LineRequest;
import subway.ui.dto.LineResponse;

@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {

    @Autowired
    private LineDao lineDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private StationDao stationDao;

    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    private String lineName1 = "1호선";
    private String lineName2 = "2호선";

    private Long lineId1;
    private Long lineId2;
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;

    @BeforeEach
    public void setUp() {
        super.setUp();

        lineRequest1 = new LineRequest("신분당선");
        lineRequest2 = new LineRequest("구신분당선");

        station1 = stationDao.insert(new Station("강남역"));
        station2 = stationDao.insert(new Station("서초역"));
        station3 = stationDao.insert(new Station("선릉역"));

        lineId1 = lineDao.insert(new LineDto(null, lineName1));

        sectionDao.insert(new SectionDto(lineId1, station1.getId(), station2.getId(), 5));
        sectionDao.insert(new SectionDto(lineId1, station2.getId(), station3.getId(), 3));
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
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse resultResponse = response.as(LineResponse.class);
        assertThat(resultResponse.getId()).isEqualTo(lineId);
    }

    @DisplayName("해당 노선의 모든 역을 가져온다.")
    @Test
    void findStationsByLine() {
        // when, then
        RestAssured
                .given().log().all()
                .when().get("/lines/{id}/stations", lineId1)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(lineId1.intValue()))
                .body("name", equalTo(lineName1))
                .rootPath("stations")
                .body("[0].id", equalTo(station1.getId().intValue()))
                .body("[0].name", equalTo(station1.getName()))
                .body("[1].id", equalTo(station2.getId().intValue()))
                .body("[1].name", equalTo(station2.getName()))
                .body("[2].id", equalTo(station3.getId().intValue()))
                .body("[2].name", equalTo(station3.getName()));
    }

    @DisplayName("모든 노선과 역을 조회한다.")
    @Test
    void findAllLinesAndStations() {
        // given
        station4 = stationDao.insert(new Station("잠실역"));
        lineId2 = lineDao.insert(new LineDto(null, lineName2));
        sectionDao.insert(new SectionDto(lineId2, station3.getId(), station4.getId(), 6));

        // when

        // then
        RestAssured
                .given().log().all()
                .when().get("/lines/stations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(2))
                .body("[0].id", equalTo(lineId1.intValue()))
                .body("[0].name", equalTo(lineName1))
                .body("[0].stations", hasSize(3))
                .body("[0].stations[0].id", equalTo(station1.getId().intValue()))
                .body("[0].stations[0].name", equalTo(station1.getName()))
                .body("[0].stations[1].id", equalTo(station2.getId().intValue()))
                .body("[0].stations[1].name", equalTo(station2.getName()))
                .body("[0].stations[2].id", equalTo(station3.getId().intValue()))
                .body("[0].stations[2].name", equalTo(station3.getName()))
                .body("[1].id", equalTo(lineId2.intValue()))
                .body("[1].name", equalTo(lineName2))
                .body("[1].stations", hasSize(2))
                .body("[1].stations[0].id", equalTo(station3.getId().intValue()))
                .body("[1].stations[0].name", equalTo(station3.getName()))
                .body("[1].stations[1].id", equalTo(station4.getId().intValue()))
                .body("[1].stations[1].name", equalTo(station4.getName()));
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
}
