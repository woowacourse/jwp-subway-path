package subway.adapter.in.web.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import subway.adapter.out.persistence.repository.LineJdbcAdapter;
import subway.adapter.out.persistence.repository.SectionJdbcAdapter;
import subway.adapter.out.persistence.repository.StationJdbcAdapter;
import subway.application.dto.StationResponse;
import subway.common.IntegrationTest;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FindLineControllerTest extends IntegrationTest {

    @Autowired
    private LineJdbcAdapter lineJdbcAdapter;
    @Autowired
    private StationJdbcAdapter stationJdbcAdapter;
    @Autowired
    private SectionJdbcAdapter sectionJdbcAdapter;

    @Test
    @DisplayName("get /lines/{id}  정렬된 구간이 출력됩니다.")
    void findStationsByLine() {
        Long lineId = lineJdbcAdapter.createLine(new Line("1호선"));

        stationJdbcAdapter.createStation(new Station("비버"));
        stationJdbcAdapter.createStation(new Station("라빈"));
        stationJdbcAdapter.createStation(new Station("허브신"));
        stationJdbcAdapter.createStation(new Station("허브신도"));

        List<Section> sections = List.of(
                new Section(lineId, new Station("비버"), new Station("라빈"), 5L),
                new Section(lineId, new Station("허브신"), new Station("허브신도"), 5L),
                new Section(lineId, new Station("라빈"), new Station("허브신"), 5L));

        sectionJdbcAdapter.saveSection(lineId, sections);
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