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
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.ui.dto.FareAndPathRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:schema-truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class FareAndPathIntegrationTest {

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
        FareAndPathRequest request = new FareAndPathRequest(persistJamsil.getId(), persistCheonho.getId(), 20);

        // when
        ExtractableResponse<Response> response = RestAssured
            .given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/path")
            .then()
            .extract();

        // then
        JsonPath jsonPath = response.body().jsonPath();
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(jsonPath.getString("stations[0].name")).isEqualTo("잠실");
            assertThat(jsonPath.getString("stations[1].name")).isEqualTo("왕십리");
            assertThat(jsonPath.getString("stations[2].name")).isEqualTo("천호");
            assertThat(jsonPath.getString("fare")).isEqualTo("1450");
        });
    }

    @Test
    void 청소년_요금_정책이_포함된_최단_경로를_탐색한다() {
        // given (잠실 -> 천호)
        FareAndPathRequest request = new FareAndPathRequest(persistJamsil.getId(), persistCheonho.getId(), 18);

        // when
        ExtractableResponse<Response> response = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/path")
            .then()
            .extract();

        // then
        JsonPath jsonPath = response.body().jsonPath();
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(jsonPath.getString("stations[0].name")).isEqualTo("잠실");
            assertThat(jsonPath.getString("stations[1].name")).isEqualTo("왕십리");
            assertThat(jsonPath.getString("stations[2].name")).isEqualTo("천호");
            assertThat(jsonPath.getInt("fare")).isEqualTo(1250 + 200 - ((1450 - 350) * 20 / 100));
        });
    }

    @Test
    void 어린이와_추가_금액이_존재하는_노선을_통한_최단_경로를_탐색한다() {
        // given (잠실 -> 신규 역)
        // given
        //                      왕십리
        //                  /    |
        //              /        |
        //           10          10
        //        /              |
        //     /                 |
        // [잠실] - 10 - 몽촌 - 11 - 천호 - (추가금 900 노선, 거리 1) - [신규 역]
        Station newStation = stationDao.insert(new Station("신규"));
        Line newLine = lineDao.insert(new Line("newLine", "color", 900));
        sectionDao.insert(new Section(newStation, persistCheonho, newLine, 1));

        FareAndPathRequest request = new FareAndPathRequest(persistJamsil.getId(), newStation.getId(), 12);
        System.out.println(sectionDao.findAllByLineId(newLine.getId()));

        // when
        ExtractableResponse<Response> response = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/path")
            .then().log().all()
            .extract();

        // then
        JsonPath jsonPath = response.body().jsonPath();
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(jsonPath.getString("stations[0].name")).isEqualTo("잠실");
            assertThat(jsonPath.getString("stations[1].name")).isEqualTo("왕십리");
            assertThat(jsonPath.getString("stations[2].name")).isEqualTo("천호");
            assertThat(jsonPath.getString("stations[3].name")).isEqualTo("신규");
            assertThat(jsonPath.getInt("fare")).isEqualTo(1250 + 300 + 900 - ((2450 - 350) * 50 / 100));
        });
    }
}
