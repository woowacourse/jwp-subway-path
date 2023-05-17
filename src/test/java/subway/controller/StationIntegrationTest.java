package subway.controller;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.fixture.LineFixture;
import subway.service.line.LineRepository;
import subway.service.line.domain.Line;
import subway.service.section.domain.Distance;
import subway.service.section.domain.Section;
import subway.service.section.repository.SectionRepository;
import subway.service.station.StationRepository;
import subway.service.station.domain.Station;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.fixture.StationFixture.JAMSIL_NO_ID;
import static subway.fixture.StationFixture.JANGJI_NO_ID;
import static subway.fixture.StationFixture.SEONLEUNG_NO_ID;
import static subway.fixture.StationFixture.YUKSAM_NO_ID;

@SuppressWarnings("NonAsciiCharacters")
public class StationIntegrationTest extends IntegrationTest {

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    LineRepository lineRepository;

    @Test
    void 지하철역을_생성한다() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @Test
    void 기존에_존재하는_지하철역_이름으로_지하철역을_생성한다() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");
        RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .log().all()
                .extract();

        JsonPath responseBody = response.body().jsonPath();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(responseBody.getString("message")).isEqualTo("해당 역 이름이 이미 존재합니다.");
    }

    @Test
    void 종점역을_삭제한다() {
        Station savedJamsil = stationRepository.insert(JAMSIL_NO_ID);
        Station savedYuksam = stationRepository.insert(YUKSAM_NO_ID);
        Station savedSeonleung = stationRepository.insert(SEONLEUNG_NO_ID);

        Line savedSecondLine = lineRepository.insert(LineFixture.SECOND_LINE_NO_ID);

        Section seonleungJamsil = new Section(savedJamsil, savedSeonleung, new Distance(10));
        Section yuksamSeonleung = new Section(savedSeonleung, savedYuksam, new Distance(4));

        sectionRepository.insertSection(seonleungJamsil, savedSecondLine);
        sectionRepository.insertSection(yuksamSeonleung, savedSecondLine);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .delete("/stations/{stationId}", savedJamsil.getId())
                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 중간역을_삭제한다() {
        Station savedJamsil = stationRepository.insert(JAMSIL_NO_ID);
        Station savedYuksam = stationRepository.insert(YUKSAM_NO_ID);
        Station savedSeonleung = stationRepository.insert(SEONLEUNG_NO_ID);

        Line savedSecondLine = lineRepository.insert(LineFixture.SECOND_LINE_NO_ID);

        Section seonleungJamsil = new Section(savedJamsil, savedSeonleung, new Distance(10));
        Section yuksamSeonleung = new Section(savedSeonleung, savedYuksam, new Distance(4));

        sectionRepository.insertSection(seonleungJamsil, savedSecondLine);
        sectionRepository.insertSection(yuksamSeonleung, savedSecondLine);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .delete("/stations/{stationId}", savedSeonleung.getId())
                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 존재하지_않는_역_삭제요청_예외() {
        Station savedJamsil = stationRepository.insert(JAMSIL_NO_ID);
        Station savedYuksam = stationRepository.insert(YUKSAM_NO_ID);
        Station savedSeonleung = stationRepository.insert(SEONLEUNG_NO_ID);
        Station savedJangji = stationRepository.insert(JANGJI_NO_ID);

        Line savedSecondLine = lineRepository.insert(LineFixture.SECOND_LINE_NO_ID);

        Section seonleungJamsil = new Section(savedJamsil, savedSeonleung, new Distance(10));
        Section yuksamSeonleung = new Section(savedSeonleung, savedYuksam, new Distance(4));

        sectionRepository.insertSection(seonleungJamsil, savedSecondLine);
        sectionRepository.insertSection(yuksamSeonleung, savedSecondLine);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .delete("/stations/{stationId}", savedJangji.getId())
                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
