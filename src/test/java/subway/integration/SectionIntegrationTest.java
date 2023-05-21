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
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.application.SectionService;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.ui.dto.DeleteSectionRequest;
import subway.ui.dto.PostSectionRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:schema-truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SectionIntegrationTest {

    @LocalServerPort
    int port;
    @Autowired
    private StationDao stationDao;
    @Autowired
    private LineDao lineDao;
    @Autowired
    private SectionDao sectionDao;
    @Autowired
    private SectionService sectionService;

    private Station persistCheonho;
    private Station persistJamsil;
    private Line persistLine8;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        // 장지 - 10 - 잠실 - 10 - 천호
        persistCheonho = stationDao.insert(cheonho);
        persistJamsil = stationDao.insert(jamsil);
        Station persistJangji = stationDao.insert(jangji);
        persistLine8 = lineDao.insert(new Line("8호선", "pink", 0));
        sectionDao.insert(new Section(persistCheonho, persistJamsil, persistLine8, 10));
        sectionDao.insert(new Section(persistJamsil, persistJangji, persistLine8, 10));
    }

    @Test
    void 구간을_추가한다() {
        // given
        // 장지 - 10 - 잠실 - 10 - 천호
        Station persistMongchon = stationDao.insert(mongchon);
        PostSectionRequest postSectionRequest = new PostSectionRequest(persistMongchon.getId(), persistJamsil.getId(), 5);

        // when
        // 장지 - 10 - 잠실 - 5 - 몽촌 - 5 - 천호
        ExtractableResponse<Response> response = RestAssured
            .given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(postSectionRequest)
            .when().patch("/lines/" + persistLine8.getId() + "/register")
            .then()
            .extract();

        // then
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(response.header("Location")).isNotBlank();
            assertThat(sectionDao.findAllByLineId(persistLine8.getId()).size()).isEqualTo(3);
        });
    }

    @Test
    void 마지막이_아닌_구간을_삭제한다() {
        // given
        DeleteSectionRequest request = new DeleteSectionRequest(persistJamsil.getId());

        // when
        // 장지 - 20 - 천호
        ExtractableResponse<Response> response = RestAssured
            .given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().patch("/lines/" + persistLine8.getId() + "/unregister")
            .then()
            .extract();

        // then
        List<Section> sections = sectionDao.findAllByLineId(persistLine8.getId());
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
            assertThat(sections.size()).isEqualTo(1);
            assertThat(sections.get(0).getDistance()).isEqualTo(20);
        });
    }

    @Test
    void 노선의_마지막_구간을_삭제한다() {
        // given
        // 장지 - 10 - 잠실
        sectionService.deleteSection(persistLine8.getId(), new DeleteSectionRequest(persistCheonho.getId()));

        // when
        DeleteSectionRequest request = new DeleteSectionRequest(persistJamsil.getId());
        ExtractableResponse<Response> response = RestAssured
            .given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().patch("/lines/" + persistLine8.getId() + "/unregister")
            .then()
            .extract();

        // then
        List<Section> sections = sectionDao.findAllByLineId(persistLine8.getId());
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
            assertThat(lineDao.findById(persistLine8.getId())).isEmpty();
            assertThat(sections.size()).isZero();
        });
    }

    @Test
    void 환승_역을_특정_노선에서_제거한다() {
        // given
        //            건대
        //            |
        //            10
        //            |
        // 장지 - 10 - 잠실 - 10 - 천호
        //            |
        //            10
        //            |
        //            강남
        Station persistKundae = stationDao.insert(kundae);
        Station persistGangnam = stationDao.insert(gangnam);
        Line persistLine2 = lineDao.insert(green);
        sectionDao.insertAll(List.of(
            new Section(persistKundae, persistJamsil, persistLine2, 10),
            new Section(persistJamsil, persistGangnam, persistLine2, 10)
        ));

        // when
        DeleteSectionRequest request = new DeleteSectionRequest(persistJamsil.getId());
        ExtractableResponse<Response> response = RestAssured
            .given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().patch("/lines/" + persistLine8.getId() + "/unregister")
            .then()
            .extract();

        // then
        // 장지 - 20 - 천호
        // 강남 - 10 - 잠실 - 10 - 건대
        List<Section> line8Sections = sectionDao.findAllByLineId(persistLine8.getId());
        List<Section> line2Sections = sectionDao.findAllByLineId(persistLine2.getId());

        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
            assertThat(line8Sections.size()).isEqualTo(1);
            assertThat(line2Sections.size()).isEqualTo(2);
        });
    }

    @Test
    void 등록되지_않은_노선에_등록된_역을_삭제하면_예외가_발생한다() {
        // given
        // 장지 - 10 - 잠실 - 10 - 천호
        long wrongLineId = 2L;

        // when
        DeleteSectionRequest request = new DeleteSectionRequest(persistJamsil.getId());
        ExtractableResponse<Response> response = RestAssured
            .given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().patch("/lines/" + wrongLineId + "/unregister")
            .then()
            .extract();

        // then
        List<Section> sections = sectionDao.findAllByLineId(persistLine8.getId());
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(response.body().asString()).contains(
                "해당 노선에 stationId = " + persistJamsil.getId() + " 인 역이 존재하지 않습니다");
            assertThat(sections.size()).isEqualTo(2);
        });
    }

    @Test
    void 등록된_노선에_속하지_않은_역을_삭제하면_예외가_발생한다() {
        // given
        // 장지 - 10 - 잠실 - 10 - 천호
        long wrongStationId = 999L;

        // when
        DeleteSectionRequest request = new DeleteSectionRequest(wrongStationId);
        ExtractableResponse<Response> response = RestAssured
            .given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().patch("/lines/" + persistLine8.getId() + "/unregister")
            .then()
            .extract();

        // then
        List<Section> sections = sectionDao.findAllByLineId(persistLine8.getId());
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(response.body().asString()).contains(
                "해당 노선에 stationId = " + wrongStationId + " 인 역이 존재하지 않습니다");
            assertThat(sections.size()).isEqualTo(2);
        });
    }
}
