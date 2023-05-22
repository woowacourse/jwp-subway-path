package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.dao.StubLineDao;
import subway.dao.StubSectionDao;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.dto.LineResponseWithSections;
import subway.dto.LineResponseWithStations;

class SubwayMapServiceTest {

    private SubwayMapService subwayMapService;

    @BeforeEach
    void setUp() {
        final StubLineDao stubLineDao = new StubLineDao();
        stubLineDao.insert(new Line(1L, "1호선", "파란색"));
        stubLineDao.insert(new Line(2L, "2호선", "초록색"));
        final StubSectionDao stubSectionDao = new StubSectionDao();
        subwayMapService = new SubwayMapService(stubLineDao, stubSectionDao);
    }

    @DisplayName("lineId로 해당 노선의 모든 역을 순서대로 가져온다.")
    @Test
    void getLineResponseWithStations() {
        final LineResponseWithStations lineResponseWithStations = subwayMapService.getLineResponseWithStations(1L);
        assertAll(
                () -> assertThat(lineResponseWithStations.getId()).isEqualTo(1L),
                () -> assertThat(lineResponseWithStations.getName()).isEqualTo("1호선"),
                () -> assertThat(lineResponseWithStations.getColor()).isEqualTo("파란색"),
                () -> assertThat(lineResponseWithStations.getStations()).containsExactly(
                        new Station(1L),
                        new Station(2L),
                        new Station(3L),
                        new Station(4L)
                )
        );
    }

    @DisplayName("모든 노선의 모든 역을 순서대로 가져온다.")
    @Test
    void getLineResponsesWithStations() {
        final List<LineResponseWithStations> lineResponsesWithStations = subwayMapService.getLineResponsesWithStations();
        final LineResponseWithStations line1 = lineResponsesWithStations.get(0);
        final LineResponseWithStations line2 = lineResponsesWithStations.get(1);
        assertAll(
                () -> assertThat(line1.getId()).isEqualTo(1L),
                () -> assertThat(line1.getName()).isEqualTo("1호선"),
                () -> assertThat(line1.getColor()).isEqualTo("파란색"),
                () -> assertThat(line1.getStations()).containsExactly(
                        new Station(1L),
                        new Station(2L),
                        new Station(3L),
                        new Station(4L)
                ),
                () -> assertThat(line2.getId()).isEqualTo(2L),
                () -> assertThat(line2.getName()).isEqualTo("2호선"),
                () -> assertThat(line2.getColor()).isEqualTo("초록색"),
                () -> assertThat(line2.getStations()).containsExactly(
                        new Station(3L),
                        new Station(5L),
                        new Station(6L)
                )
        );
    }


    @DisplayName("lineId로 해당 노선의 모든 구간을 순서대로 가져온다.")
    @Test
    void getLineResponseWithSections() {
        final LineResponseWithSections lineResponseWithSections = subwayMapService.getLineResponseWithSections(1L);
        assertAll(
                () -> assertThat(lineResponseWithSections.getId()).isEqualTo(1L),
                () -> assertThat(lineResponseWithSections.getName()).isEqualTo("1호선"),
                () -> assertThat(lineResponseWithSections.getColor()).isEqualTo("파란색"),
                () -> assertThat(lineResponseWithSections.getSections()).containsExactly(
                        Section.builder().id(1L).build(),
                        Section.builder().id(2L).build(),
                        Section.builder().id(3L).build()
                )
        );
    }

    @DisplayName("모든 노선의 모든 구간을 순서대로 가져온다.")
    @Test
    void getLineResponsesWithSections() {
        final List<LineResponseWithSections> lineResponsesWithSections = subwayMapService.getLineResponsesWithSections();
        final LineResponseWithSections line1 = lineResponsesWithSections.get(0);
        final LineResponseWithSections line2 = lineResponsesWithSections.get(1);
        assertAll(
                () -> assertThat(line1.getId()).isEqualTo(1L),
                () -> assertThat(line1.getName()).isEqualTo("1호선"),
                () -> assertThat(line1.getColor()).isEqualTo("파란색"),
                () -> assertThat(line1.getSections()).containsExactly(
                        Section.builder().id(1L).build(),
                        Section.builder().id(2L).build(),
                        Section.builder().id(3L).build()
                ),
                () -> assertThat(line2.getId()).isEqualTo(2L),
                () -> assertThat(line2.getName()).isEqualTo("2호선"),
                () -> assertThat(line2.getColor()).isEqualTo("초록색"),
                () -> assertThat(line2.getSections()).containsExactly(
                        Section.builder().id(4L).build(),
                        Section.builder().id(5L).build()
                )
        );
    }
}
