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
import org.springframework.test.context.jdbc.Sql;
import subway.controller.dto.LineRequest;
import subway.controller.dto.LineResponse;
import subway.dao.SectionDao;
import subway.dao.entity.SectionEntity;
import subway.domain.fare.Fare;
import subway.domain.line.Line;
import subway.domain.line.Station;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Sql("/truncate.sql")
@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private StationRepository stationRepository;

    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    private final String lineName1 = "1호선";
    private final String lineName2 = "2호선";

    private Line line1;
    private Line line2;
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;

    @BeforeEach
    public void setUp() {
        super.setUp();

        lineRequest1 = new LineRequest("신분당선", 100);
        lineRequest2 = new LineRequest("구신분당선", 500);

        station1 = stationRepository.save(new Station(1L, "강남역"));
        station2 = stationRepository.save(new Station(2L, "서초역"));
        station3 = stationRepository.save(new Station(3L, "선릉역"));

        line1 = lineRepository.save(new Line(null, lineName1, new Fare(100), null));

        sectionDao.insert(new SectionEntity(null, line1.getId(), station1.getId(), station2.getId(), 5));
        sectionDao.insert(new SectionEntity(null, line1.getId(), station2.getId(), station3.getId(), 3));
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
                .when().get("/lines/{id}", line1.getId())
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(line1.getId().intValue()))
                .body("name", equalTo(lineName1))
                .body("extraFare", equalTo(line1.getExtraFare()))
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
        station4 = stationRepository.save(new Station(4L, "잠실역"));
        line2 = lineRepository.save(new Line(null, lineName2, new Fare(500), null));
        sectionDao.insert(new SectionEntity(null, line2.getId(), station3.getId(), station4.getId(), 6));

        // when

        // then
        RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(2))
                .body("[0].id", equalTo(line1.getId().intValue()))
                .body("[0].name", equalTo(lineName1))
                .body("[0].extraFare", equalTo(line1.getExtraFare()))
                .body("[0].stations", hasSize(3))
                .body("[0].stations[0].id", equalTo(station1.getId().intValue()))
                .body("[0].stations[0].name", equalTo(station1.getName()))
                .body("[0].stations[1].id", equalTo(station2.getId().intValue()))
                .body("[0].stations[1].name", equalTo(station2.getName()))
                .body("[0].stations[2].id", equalTo(station3.getId().intValue()))
                .body("[0].stations[2].name", equalTo(station3.getName()))
                .body("[1].id", equalTo(line2.getId().intValue()))
                .body("[1].name", equalTo(lineName2))
                .body("[1].extraFare", equalTo(line2.getExtraFare()))
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
