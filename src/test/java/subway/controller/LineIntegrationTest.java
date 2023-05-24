package subway.controller;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.controller.line.dto.LineStationsResponse;
import subway.controller.station.dto.StationWebResponse;
import subway.service.line.LineRepository;
import subway.service.line.domain.Line;
import subway.service.section.SectionCaching;
import subway.service.section.domain.Distance;
import subway.service.section.domain.Section;
import subway.service.section.repository.SectionRepository;
import subway.service.station.StationRepository;
import subway.service.station.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.fixture.LineFixture.EIGHT_LINE_NO_ID;
import static subway.fixture.LineFixture.SECOND_LINE_NO_ID;
import static subway.fixture.StationFixture.GANGNAM_NO_ID;
import static subway.fixture.StationFixture.JAMSIL_NO_ID;
import static subway.fixture.StationFixture.JANGJI_NO_ID;
import static subway.fixture.StationFixture.MONGCHON_NO_ID;
import static subway.fixture.StationFixture.SEONLEUNG_NO_ID;

@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {

    @Autowired
    StationRepository stationRepository;

    @Autowired
    LineRepository lineRepository;

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    SectionCaching sectionCaching;

    Line secondLine;
    Line eightLine;

    Station jamsil;
    Station gangnam;
    Station seonleung;

    Station jangji;
    Station mongchon;

    // 강남 -> 선릉
    Section gangnamSeonleung;
    // 선릉 -> 잠실
    Section seonleungJamsil;

    // 장지 -> 잠실
    Section jangjiJamsil;
    // 잠실 -> 몽촌
    Section jamsilMongchon;

    @BeforeEach
    public void setUp() {
        super.setUp();
        sectionCaching.clearSectionsCache();
        secondLine = lineRepository.insert(SECOND_LINE_NO_ID);
        eightLine = lineRepository.insert(EIGHT_LINE_NO_ID);

        jamsil = stationRepository.insert(JAMSIL_NO_ID);
        gangnam = stationRepository.insert(GANGNAM_NO_ID);
        seonleung = stationRepository.insert(SEONLEUNG_NO_ID);

        jangji = stationRepository.insert(JANGJI_NO_ID);
        mongchon = stationRepository.insert(MONGCHON_NO_ID);

        gangnamSeonleung = new Section(seonleung, gangnam, new Distance(5));
        seonleungJamsil = new Section(jamsil, seonleung, new Distance(7));

        jangjiJamsil = new Section(jamsil, jangji, new Distance(9));
        jamsilMongchon = new Section(mongchon, jamsil, new Distance(3));

        sectionRepository.insertSection(gangnamSeonleung, secondLine);
        sectionRepository.insertSection(seonleungJamsil, secondLine);

        sectionRepository.insertSection(jangjiJamsil, eightLine);
        sectionRepository.insertSection(jamsilMongchon, eightLine);
    }

    @DisplayName("해당 지하철 노선의 지하철 역을 조회한다 상행 -> 하행 순")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", secondLine.getId())
                .then().log().all().
                extract();

        // then
        LineStationsResponse lineStationsResponse = response.as(LineStationsResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(lineStationsResponse.getId()).isEqualTo(secondLine.getId()),
                () -> assertThat(lineStationsResponse.getStations()).containsExactly(
                        new StationWebResponse(jamsil.getId(), jamsil.getName()),
                        new StationWebResponse(seonleung.getId(), seonleung.getName()),
                        new StationWebResponse(gangnam.getId(), gangnam.getName())
                )
        );
    }
}
