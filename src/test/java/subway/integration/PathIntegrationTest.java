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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.application.PathService;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.ui.dto.PathRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:schema-truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class PathIntegrationTest {

    @LocalServerPort
    int port;
    @Autowired
    private StationDao stationDao;
    @Autowired
    private LineDao lineDao;
    @Autowired
    private SectionDao sectionDao;
    private Station persistJamsil;
    private Station persistCheonho;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;

        // given
        //                      왕십리
        //                  /    |
        //              /        |
        //           10          10
        //        /              |
        //     /                 |
        // [잠실] - 10 - 몽촌 - 11 - [천호]
        persistCheonho = stationDao.insert(cheonho);
        persistJamsil = stationDao.insert(jamsil);
        Station persistMongchon = stationDao.insert(mongchon);
        Station persistWangsimni = stationDao.insert(wangsimni);

        Line persistLine8 = lineDao.insert(pink);
        Line persistLine5 = lineDao.insert(purple);
        Line persistLine2 = lineDao.insert(green);

        Section wangsimniCheonho10 = new Section(persistWangsimni, persistCheonho, persistLine5, 10);
        Section wangsimniJamsil10 = new Section(persistWangsimni, persistJamsil, persistLine2, 10);
        Section cheonhoMongchon11 = new Section(persistCheonho, persistMongchon, persistLine8, 11);
        Section mongchonJamsil10 = new Section(persistMongchon, persistJamsil, persistLine8, 10);
        sectionDao.insertAll(List.of(wangsimniCheonho10, wangsimniJamsil10, cheonhoMongchon11, mongchonJamsil10));
    }

    @Test
    void 최단_경로를_탐색한다() {
        // given (잠실 -> 천호)
        PathRequest request = new PathRequest(persistJamsil.getId(), persistCheonho.getId());

        // when
        ExtractableResponse<Response> response = RestAssured
            .given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/path")
            .then().log().all()
            .extract();

        // then
        JsonPath jsonPath = response.body().jsonPath();
        System.out.println(response.body());
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(jsonPath.getString("stations[0].name")).isEqualTo("잠실");
            assertThat(jsonPath.getString("stations[1].name")).isEqualTo("왕십리");
            assertThat(jsonPath.getString("stations[2].name")).isEqualTo("천호");
            assertThat(jsonPath.getString("fare")).isEqualTo("1450");
        });
    }
}
