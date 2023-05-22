package subway.persistence.repository;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.section.Section;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static subway.fixtures.domain.DistanceFixture.TEN_DISTANCE;
import static subway.fixtures.domain.LineFixture.SECOND_LINE;
import static subway.fixtures.domain.StationFixture.JAMSIL;
import static subway.fixtures.domain.StationFixture.SEOLLEUNG;

@SuppressWarnings("NonAsciiCharacters")
class SectionRepositoryTest extends RepositoryTest {

    @Test
    void 역과_역의_관계를_저장한다() {
        assertDoesNotThrow(() -> sectionRepository.insert(SECOND_LINE));
    }

    @Test
    void 노선에_저장된_모든_역을_조회한다() {
        // given
        final Station station1 = stationRepository.insert(JAMSIL);
        final Station station2 = stationRepository.insert(SEOLLEUNG);
        final Line persistLine = lineRepository.insert(SECOND_LINE);
        persistLine.addSection(Section.of(station1, station2, TEN_DISTANCE));
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
