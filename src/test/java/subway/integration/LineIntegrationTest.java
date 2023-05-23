package subway.integration;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static subway.TestSource.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.ui.dto.LineRequest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class LineIntegrationTest extends IntegrationTest {

    @Autowired
    private StationDao stationDao;
    @Autowired
    private LineDao lineDao;
    @Autowired
    private SectionDao sectionDao;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;

        Station persistCheonho = stationDao.insert(cheonho);
        Station persistJamsil = stationDao.insert(jamsil);
        Station persistJangji = stationDao.insert(jangji);
        Station persistGangnam = stationDao.insert(gangnam);
        Station persistKundae = stationDao.insert(kundae);

        Line persistLine8 = lineDao.insert(pink);
        Line persistLine2 = lineDao.insert(green);

        sectionDao.insertAll(List.of(
            new Section(persistJamsil, persistGangnam, persistLine2, 10),
            new Section(persistKundae, persistJamsil, persistLine2, 10),
            new Section(persistJamsil, persistJangji, persistLine8, 10),
            new Section(persistCheonho, persistJamsil, persistLine8, 10)
        ));

    }

    @Test
    void 지하철_노선을_생성한다() {
        // when
        // 건대 - 10 - 장지
        LineRequest lineRequest = new LineRequest("new", "new", 5L, 2L, 10, 0);
        ExtractableResponse<Response> response = RestAssured
            .given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(lineRequest)
            .when().post("/lines")
            .then()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @Test
    void 지하철_노선을_전체_조회한다() {
        // when
        ExtractableResponse<Response> response = RestAssured
            .given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines")
            .then()
            .extract();

        // then
        JsonPath jsonPath = response.body().jsonPath();
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(jsonPath.getString("stations[0].name")).isEqualTo("[천호, 잠실, 장지]");
            assertThat(jsonPath.getString("stations[1].name")).isEqualTo("[건대입구, 잠실, 강남]");
        });
    }

    @Test
    void 지하철의_특정_노선을_조회한다() {
        // when
        ExtractableResponse<Response> response = RestAssured
            .given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines/1")
            .then()
            .extract();

        // then
        JsonPath jsonPath = response.body().jsonPath();
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(jsonPath.getString("stations.name")).isEqualTo("[천호, 잠실, 장지]");
        });
    }
}
