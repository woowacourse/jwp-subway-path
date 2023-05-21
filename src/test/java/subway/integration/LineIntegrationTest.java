package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.dto.LineCreateRequest;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationRequest;
import subway.entity.LineEntity;
import subway.entity.StationEntity;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {

    @Autowired
    private LineDao lineDao;
    @Autowired
    private StationDao stationDao;

    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        lineRequest1 = new LineRequest("신분당선", "bg-red-600");
        lineRequest2 = new LineRequest("구신분당선", "bg-red-600");
    }

    @AfterEach
    public void after() {
        stationDao.removeAll();
        lineDao.removeAll();
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() throws SQLException {
        //given
        StationEntity station = new StationEntity(1L, "역삼역", 2L, 10, 1L);
        StationEntity nextStation = new StationEntity(2L, "선릉역", null, 0, 1L);
        Long insert = stationDao.insert(station);
        Long insert1 = stationDao.insert(nextStation);

        //when
        LineCreateRequest lineCreateRequest =
                new LineCreateRequest(lineRequest1, new StationRequest("역삼역", "선릉역", 10));

        ExtractableResponse<Response> response = RestAssured.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE).body(lineCreateRequest).when().post("/lines").then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        StationEntity station = new StationEntity(1L, "역삼역", 2L, 10, 1L);
        StationEntity nextStation = new StationEntity(2L, "선릉역", null, 0, 1L);

        stationDao.insert(station);
        stationDao.insert(nextStation);

        //when
        LineCreateRequest lineCreateRequest = new LineCreateRequest(lineRequest1, new StationRequest("역삼역", "선릉역", 10));
        LineCreateRequest lineCreateRequest2 = new LineCreateRequest(lineRequest2, new StationRequest("역삼역", "선릉역", 10));

        lineDao.insert(new LineEntity(1L, lineRequest1.getName(), lineRequest1.getColor(), 1L));
        lineDao.insert(new LineEntity(2L, lineRequest2.getName(), lineRequest2.getColor(), 1L));


        ExtractableResponse<Response> createResponse1 = RestAssured.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE).body(lineCreateRequest).when().post("/lines").then().log().all().extract();

        ExtractableResponse<Response> createResponse2 = RestAssured.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE).body(lineCreateRequest2).when().post("/lines").then().log().all().extract();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all().accept(MediaType.APPLICATION_JSON_VALUE).when().get("/lines").then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        RestAssured.given().contentType(MediaType.APPLICATION_JSON_VALUE).when().get("/lines").then().statusCode(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given

        StationEntity station = new StationEntity(1L, "역삼역", 2L, 10, 1L);
        StationEntity nextStation = new StationEntity(2L, "선릉역", null, 0, 1L);
        stationDao.insert(station);
        stationDao.insert(nextStation);

        lineDao.insert(new LineEntity(1L, lineRequest1.getName(), lineRequest1.getColor(), 1L));

        //when
        LineCreateRequest lineCreateRequest =
                new LineCreateRequest(lineRequest1, new StationRequest("역삼역", "선릉역", 10));

        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineCreateRequest)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", 1)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse resultResponse = response.as(LineResponse.class);
        assertThat(resultResponse.getId()).isEqualTo(1);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        StationEntity station = new StationEntity(1L, "역삼역", 2L, 10, 1L);
        StationEntity nextStation = new StationEntity(2L, "선릉역", null, 0, 1L);
        StationEntity thirdStation = new StationEntity(3L, "강남역", null, 3, 1L);
        stationDao.insert(station);
        stationDao.insert(nextStation);
        stationDao.insert(thirdStation);

        LineCreateRequest lineCreateRequest = new LineCreateRequest(lineRequest1, new StationRequest("역삼역", "선릉역", 10));
        LineCreateRequest lineCreateRequest2 = new LineCreateRequest(lineRequest1, new StationRequest("강남역", "선릉역", 10));

        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineCreateRequest)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineCreateRequest2)
                .when().put("/lines/{lineId}", 1)
                .then().log().all()
                .extract();


        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        StationEntity station = new StationEntity(1L, "역삼역", 2L, 10, 1L);
        StationEntity nextStation = new StationEntity(2L, "선릉역", null, 0, 1L);
        stationDao.insert(station);
        stationDao.insert(nextStation);

        LineCreateRequest lineCreateRequest = new LineCreateRequest(lineRequest1, new StationRequest("역삼역", "선릉역", 10));

        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineCreateRequest)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}", 1)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
