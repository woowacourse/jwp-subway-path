package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.line.dao.LineDao;
import subway.line.domain.Line;
import subway.line.dto.LineRequest;
import subway.line.dto.LineSearchResponse;
import subway.section.dao.SectionDao;
import subway.section.entity.SectionEntity;
import subway.station.dao.StationDao;
import subway.station.domain.Station;

@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @Autowired
    private StationDao stationDao;
    @Autowired
    private LineDao lineDao;
    @Autowired
    private SectionDao sectionDao;
    private Long lineId1;
    private Long lineId2;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        lineRequest1 = new LineRequest("신분당선", "bg-red-600");
        lineRequest2 = new LineRequest("구신분당선", "bg-red-600");

        lineId1 = lineDao.insert(new Line("2호선", "초록색")).getId();
        lineId2 = lineDao.insert(new Line("3호선", "파랑색")).getId();

        stationDao.insert(new Station("1L"));
        stationDao.insert(new Station("2L"));
        stationDao.insert(new Station("3L"));
        stationDao.insert(new Station("4L"));
        stationDao.insert(new Station("5L"));
        stationDao.insert(new Station("6L"));
        stationDao.insert(new Station("7L"));
        stationDao.insert(new Station("8L"));
        stationDao.insert(new Station("9L"));
        stationDao.insert(new Station("10L"));
        stationDao.insert(new Station("11L"));

        sectionDao.insert(new SectionEntity(1L, 1L, 1L, 2L, 3));
        sectionDao.insert(new SectionEntity(2L, 1L, 2L, 3L, 3));
        sectionDao.insert(new SectionEntity(3L, 1L, 3L, 4L, 3));
        sectionDao.insert(new SectionEntity(4L, 1L, 4L, 5L, 3));
        sectionDao.insert(new SectionEntity(5L, 1L, 5L, 6L, 3));
        sectionDao.insert(new SectionEntity(6L, 1L, 6L, 7L, 3));

        sectionDao.insert(new SectionEntity(7L, 2L, 8L, 9L, 3));
        sectionDao.insert(new SectionEntity(8L, 2L, 9L, 4L, 3));
        sectionDao.insert(new SectionEntity(9L, 2L, 4L, 10L, 3));
        sectionDao.insert(new SectionEntity(10L, 2L, 10L, 11L, 3));
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        final ExtractableResponse<Response> response = RestAssured
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
        final ExtractableResponse<Response> response = RestAssured
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
        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final List<LineSearchResponse> result = response.jsonPath().getList(".", LineSearchResponse.class);
        System.out.println();
        System.out.println(result.get(0).getStations());
        System.out.println(result.get(1).getId());
        System.out.println(result.get(1).getStations());
        assertAll(
                () -> assertThat(result.get(0).getId()).isEqualTo(lineId1),
                () -> assertThat(result.get(0).getName()).isEqualTo("2호선"),
                () -> assertThat(result.get(0).getColor()).isEqualTo("초록색"),
                () -> assertThat(result.get(0).getStations()).containsExactly(
                        new Station(1L, "1L"),
                        new Station(2L, "2L"),
                        new Station(3L, "3L"),
                        new Station(4L, "4L"),
                        new Station(5L, "5L"),
                        new Station(6L, "6L"),
                        new Station(7L, "7L")
                ),
                () -> assertThat(result.get(1).getId()).isEqualTo(lineId2),
                () -> assertThat(result.get(1).getName()).isEqualTo("3호선"),
                () -> assertThat(result.get(1).getColor()).isEqualTo("파랑색"),
                () -> assertThat(result.get(1).getStations()).containsExactly(
                        new Station(8L, "8L"),
                        new Station(9L, "9L"),
                        new Station(4L, "4L"),
                        new Station(10L, "10L"),
                        new Station(11L, "11L")
                )
        );
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", lineId1)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final LineSearchResponse lineSearchResponse = response.as(LineSearchResponse.class);
        assertThat(lineSearchResponse.getId()).isEqualTo(lineId1);
        assertThat(lineSearchResponse.getName()).isEqualTo("2호선");
        assertThat(lineSearchResponse.getColor()).isEqualTo("초록색");
        assertThat(lineSearchResponse.getStations()).containsExactly(
                new Station(1L, "1L"),
                new Station(2L, "2L"),
                new Station(3L, "3L"),
                new Station(4L, "4L"),
                new Station(5L, "5L"),
                new Station(6L, "6L"),
                new Station(7L, "7L")
        );
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        final ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        final Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        final ExtractableResponse<Response> response = RestAssured
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
        final ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        final Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
