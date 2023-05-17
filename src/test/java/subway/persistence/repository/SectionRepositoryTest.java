package subway.persistence.repository;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.section.Section;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SuppressWarnings("NonAsciiCharacters")
class SectionRepositoryTest extends RepositoryTest {

    private Line line;

    @BeforeEach
    void setUp() {
        line = Line.of("경의중앙선", "bg-blue-600");
    }

    @Test
    void 역과_역의_관계를_저장한다() {
        assertDoesNotThrow(() -> sectionRepository.insert(line));
    }

    @Test
    void 노선에_저장된_모든_역을_조회한다() {
        // given
        final Station dataStation1 = Station.from("잠실역");
        final Station dataStation2 = Station.from("선릉역");
        final Distance distance = Distance.from(10);
        final Station station1 = stationRepository.insert(dataStation1);
        final Station station2 = stationRepository.insert(dataStation2);
        final Line persistLine = lineRepository.insert(line);
        persistLine.addSection(Section.of(station1, station2, distance));
        sectionRepository.insert(persistLine);

        // when
        final Line tempLine = lineRepository.findById(persistLine.getId());
        final Line actualLine = sectionRepository.findAllSectionByLine(tempLine);
        final List<Station> actual = actualLine.findStationsByOrdered();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0).getName()).isEqualTo("잠실역");
            softAssertions.assertThat(actual.get(1).getName()).isEqualTo("선릉역");
        });
    }
}
