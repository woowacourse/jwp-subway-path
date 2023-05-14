package subway.integration;

import static org.assertj.core.api.Assertions.*;
import static subway.TestSource.*;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.ui.dto.SectionRequest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SectionIntegrationTest extends IntegrationTest {

    @Autowired
    private StationDao stationDao;
    @Autowired
    private LineDao lineDao;
    @Autowired
    private SectionDao sectionDao;

    @Test
    void 구간을_생성한다() {
        // given
        // 잠실 - 10 - 천호
        Station persistCheonho = stationDao.insert(cheonho);
        Station persistJamsil = stationDao.insert(jamsil);
        Station persistJangji = stationDao.insert(jangji);
        Line persistLine8 = lineDao.insert(new Line("8호선", "pink"));
        sectionDao.insert(new Section(persistCheonho, persistJamsil, persistLine8, 10));

        // when
        // 장지 - 10 - 잠실 - 10 - 천호
        SectionRequest sectionRequest = new SectionRequest(persistJamsil.getId(), persistJangji.getId(),
            persistLine8.getId(), 10);
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(sectionRequest)
            .when().post("/sections")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }
}
