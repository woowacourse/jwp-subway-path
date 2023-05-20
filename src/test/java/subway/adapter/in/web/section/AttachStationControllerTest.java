package subway.adapter.in.web.section;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import subway.adapter.in.web.section.dto.SectionCreateRequest;
import subway.adapter.out.persistence.repository.LineJdbcAdapter;
import subway.adapter.out.persistence.repository.SectionJdbcAdapter;
import subway.adapter.out.persistence.repository.StationJdbcAdapter;
import subway.common.IntegrationTest;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class AttachStationControllerTest extends IntegrationTest {

    @Autowired
    private LineJdbcAdapter lineRepository;
    @Autowired
    private StationJdbcAdapter stationRepository;
    @Autowired
    private SectionJdbcAdapter sectionRepository;

    @Test
    @DisplayName("post /line/{id}/station  구간을 추가한다.")
    void createSection() {
        final Long lineId = lineRepository.createLine(new Line("1호선", 10));
        stationRepository.createStation(new Station("비버"));
        stationRepository.createStation(new Station("라빈"));

        final SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("비버", "라빈", 5L);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(sectionCreateRequest)
                .when().post("/line/" + lineId + "/station")
                .then().log().all()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(sectionRepository.findAllByLineId(lineId))
                        .usingRecursiveComparison()
                        .isEqualTo(List.of(new Section(lineId, new Station("비버"), new Station("라빈"), 5L)))
        );
    }
}
