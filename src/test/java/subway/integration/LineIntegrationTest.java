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
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.ui.dto.request.LineRequest;
import subway.ui.dto.response.LineResponse;

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

    private LineEntity lineEntity1;
    private LineEntity lineEntity2;
    private StationEntity stationEntity1;
    private StationEntity stationEntity2;
    private StationEntity stationEntity3;
    private StationEntity stationEntity4;

    @BeforeEach
    public void setUp() {
        super.setUp();

        lineRequest1 = new LineRequest("신분당선");
        lineRequest2 = new LineRequest("구신분당선");

        stationEntity1 = stationDao.save(new StationEntity(1L, "강남역"));
        stationEntity2 = stationDao.save(new StationEntity(2L, "서초역"));
        stationEntity3 = stationDao.save(new StationEntity(3L, "선릉역"));

        lineEntity1 = lineDao.save(new LineEntity(1L, lineName1));

        sectionDao.save(new SectionEntity(1L, lineEntity1.getId(), stationEntity1.getId(), stationEntity2.getId(), 5));
        sectionDao.save(new SectionEntity(2L, lineEntity1.getId(), stationEntity2.getId(), stationEntity3.getId(), 3));
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
                .when().get("/lines/{id}/stations", lineEntity1.getId())
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(lineEntity1.getId().intValue()))
                .body("name", equalTo(lineName1))
                .rootPath("stations")
                .body("[0].id", equalTo(stationEntity1.getId().intValue()))
                .body("[0].name", equalTo(stationEntity1.getName()))
                .body("[1].id", equalTo(stationEntity2.getId().intValue()))
                .body("[1].name", equalTo(stationEntity2.getName()))
                .body("[2].id", equalTo(stationEntity3.getId().intValue()))
                .body("[2].name", equalTo(stationEntity3.getName()));
    }

    @DisplayName("모든 노선과 역을 조회한다.")
    @Test
    void findAllLinesAndStations() {
        // given
        stationEntity4 = stationDao.save(new StationEntity(4L, "잠실역"));
        lineEntity2 = lineDao.save(new LineEntity(2L, lineName2));
        sectionDao.save(new SectionEntity(3L, lineEntity2.getId(), stationEntity3.getId(), stationEntity4.getId(), 6));

        // when, then
        RestAssured
                .given().log().all()
                .when().get("/lines/stations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(2))
                .body("[0].id", equalTo(lineEntity1.getId().intValue()))
                .body("[0].name", equalTo(lineName1))
                .body("[0].stations", hasSize(3))
                .body("[0].stations[0].id", equalTo(stationEntity1.getId().intValue()))
                .body("[0].stations[0].name", equalTo(stationEntity1.getName()))
                .body("[0].stations[1].id", equalTo(stationEntity2.getId().intValue()))
                .body("[0].stations[1].name", equalTo(stationEntity2.getName()))
                .body("[0].stations[2].id", equalTo(stationEntity3.getId().intValue()))
                .body("[0].stations[2].name", equalTo(stationEntity3.getName()))
                .body("[1].id", equalTo(lineEntity2.getId().intValue()))
                .body("[1].name", equalTo(lineName2))
                .body("[1].stations", hasSize(2))
                .body("[1].stations[0].id", equalTo(stationEntity3.getId().intValue()))
                .body("[1].stations[0].name", equalTo(stationEntity3.getName()))
                .body("[1].stations[1].id", equalTo(stationEntity4.getId().intValue()))
                .body("[1].stations[1].name", equalTo(stationEntity4.getName()));
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
