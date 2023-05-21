package subway.adapter.in.web.section;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import subway.adapter.in.web.section.dto.SectionDeleteRequest;
import subway.adapter.out.persistence.repository.LineJdbcRepository;
import subway.adapter.out.persistence.repository.SectionJdbcRepository;
import subway.adapter.out.persistence.repository.StationJdbcRepository;
import subway.common.IntegrationTest;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class DetachStationControllerTest extends IntegrationTest {

    @Autowired
    private LineJdbcRepository lineRepository;
    @Autowired
    private StationJdbcRepository stationRepository;
    @Autowired
    private SectionJdbcRepository sectionRepository;

    @Test
    @DisplayName("delete /line/{id}/station 구간의 역을 삭제한다.")
    void deleteSection() {
        final Long lineId = lineRepository.createLine(new Line("1호선", 1000));
        stationRepository.createStation(new Station("비버"));
        stationRepository.createStation(new Station("라빈"));
        sectionRepository.saveSection(lineId, List.of(new Section(lineId, new Station("비버"), new Station("라빈"), 5L)));
        final SectionDeleteRequest 비버 = new SectionDeleteRequest("비버");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(비버)
                .when().delete("/line/" + lineId + "/station")
                .then().log().all()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(sectionRepository.findAllByLineId(lineId)).hasSize(0)
        );
    }
}
