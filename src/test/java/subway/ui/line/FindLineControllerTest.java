package subway.ui.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import subway.common.IntegrationTest;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.repository.StationRepository;
import subway.application.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class FindLineControllerTest extends IntegrationTest {

    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private SectionRepository sectionRepository;

    @Test
    @DisplayName("get /lines/{id}  정렬된 구간이 출력됩니다.")
    void findStationsByLine() {
        Long lineId = lineRepository.createLine(new Line("1호선"));

        stationRepository.createStation(new Station("비버"));
        stationRepository.createStation(new Station("라빈"));
        stationRepository.createStation(new Station("허브신"));
        stationRepository.createStation(new Station("허브신도"));

        List<Section> sections = List.of(
                new Section(lineId, new Station("비버"), new Station("라빈"), 5L),
                new Section(lineId, new Station("허브신"), new Station("허브신도"), 5L),
                new Section(lineId, new Station("라빈"), new Station("허브신"), 5L));

        sectionRepository.saveSection(lineId, sections);
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines/" + lineId)
                .then().log().all()
                .extract();


        List<StationResponse> result = List.of(new StationResponse("비버"), new StationResponse("라빈"), new StationResponse("허브신"), new StationResponse("허브신도"));

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.body().jsonPath().getList(".", StationResponse.class))
                        .usingRecursiveComparison()
                        .isEqualTo(result)
        );
    }
}