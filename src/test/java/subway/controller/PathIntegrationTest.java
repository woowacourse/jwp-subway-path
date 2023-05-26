package subway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import subway.service.line.LineRepository;
import subway.service.line.domain.Line;
import subway.service.section.domain.Distance;
import subway.service.section.domain.Section;
import subway.service.section.dto.PathResult;
import subway.service.section.repository.SectionRepository;
import subway.service.station.StationRepository;
import subway.service.station.domain.Station;
import subway.service.station.dto.StationResponse;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.fixture.LineFixture.EIGHT_LINE_NO_ID;
import static subway.fixture.LineFixture.SECOND_LINE_NO_ID;
import static subway.fixture.StationFixture.GANGNAM_NO_ID;
import static subway.fixture.StationFixture.JAMSIL_NO_ID;
import static subway.fixture.StationFixture.JANGJI_NO_ID;
import static subway.fixture.StationFixture.MONGCHON_NO_ID;
import static subway.fixture.StationFixture.SEONLEUNG_NO_ID;
import static subway.fixture.StationFixture.YUKSAM_NO_ID;

public class PathIntegrationTest extends IntegrationTest {

    @Autowired
    StationRepository stationRepository;

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    LineRepository lineRepository;

    @Autowired
    ObjectMapper objectMapper;

    Line savedSecondLine;
    Station savedUpStation;
    Station savedDownStation;

    @BeforeEach
    public void setUp() {
        super.setUp();
        savedSecondLine = lineRepository.insert(SECOND_LINE_NO_ID);

        savedUpStation = stationRepository.insert(JAMSIL_NO_ID);
        savedDownStation = stationRepository.insert(YUKSAM_NO_ID);
    }

    @Test
    void 최단경로_요금_조회() {
        Line savedEightLine = lineRepository.insert(EIGHT_LINE_NO_ID);

        Station mongchon = stationRepository.insert(MONGCHON_NO_ID);
        Station seonleung = stationRepository.insert(SEONLEUNG_NO_ID);
        Station jangji = stationRepository.insert(JANGJI_NO_ID);
        Station gangnam = stationRepository.insert(GANGNAM_NO_ID);

        Section jangjiJamsil = new Section(mongchon, jangji, new Distance(5));
        Section gangnamJangji = new Section(jangji, gangnam, new Distance(10));

        // gangnam ->(3) seonleung ->(7) mongchon
        Section seonleungJamsil = new Section(mongchon, seonleung, new Distance(7));
        Section gangnamSeonleung = new Section(seonleung, gangnam, new Distance(3));

        sectionRepository.insertSection(jangjiJamsil, savedEightLine);
        sectionRepository.insertSection(gangnamJangji, savedEightLine);

        sectionRepository.insertSection(seonleungJamsil, savedSecondLine);
        sectionRepository.insertSection(gangnamSeonleung, savedSecondLine);

        Map<String, Long> params = new HashMap<>();
        params.put("sourceStationId", gangnam.getId());
        params.put("targetStationId", mongchon.getId());

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/path")
                .then().log().all()
                .extract();

        PathResult result = response.body().as(PathResult.class);

        Assertions.assertAll(
                () -> assertThat(result.getFee()).isEqualTo(1250),
                () -> assertThat(result.getStations()).containsExactlyInAnyOrder(
                        new StationResponse(gangnam.getId(), gangnam.getName()),
                        new StationResponse(seonleung.getId(), seonleung.getName()),
                        new StationResponse(mongchon.getId(), mongchon.getName())
                )
        );
    }
}
