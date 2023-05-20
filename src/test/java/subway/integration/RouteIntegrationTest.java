package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.TestData;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Station;
import subway.repository.SectionRepository;

public class RouteIntegrationTest extends IntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        StationDao stationDao = new StationDao(jdbcTemplate, jdbcTemplate.getDataSource());
        LineDao lineDao = new LineDao(jdbcTemplate, jdbcTemplate.getDataSource());
        SectionDao sectionDao = new SectionDao(jdbcTemplate, jdbcTemplate.getDataSource());
        SectionRepository sectionRepository = new SectionRepository(stationDao, lineDao, sectionDao);

        for (final Station station : TestData.getAllStations()) {
            stationDao.insert(station);
        }
        for (final Line line : TestData.getAllLines()) {
            lineDao.insert(line);
        }
        sectionRepository.saveSections(TestData.getAllSections().getSections());
    }

    @Test
    void 최단_경로를_성공적으로_조회한다() {
        // given
        Long 서원역ID = TestData.서원역.getId();
        Long 상도역ID = TestData.상도역.getId();

        // when
        ExtractableResponse<Response> response = RestAssured.when()
                .get("/routes?startStationId=" + 서원역ID + "&endStationId=" + 상도역ID)
                .then()
                .contentType(ContentType.JSON)
                .extract();

        // then
        assertThat(response.body().jsonPath().getList("stations")).isNotEmpty();
        assertThat(response.body().jsonPath().getDouble("distance")).isNotEqualTo(0);
        assertThat(response.body().jsonPath().getInt("fee")).isNotEqualTo(0);
    }

    @Test
    void 존재하지_않은_역으로_최단_경로를_조회하면_BAD_REQUEST를_응답한다() {
        // given
        Long 서원역ID = TestData.서원역.getId();
        long 없는ID = 99L;

        // when
        ExtractableResponse<Response> response = RestAssured.when()
                .get("/routes?startStationId=" + 서원역ID + "&endStationId=" + 없는ID)
                .then()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
