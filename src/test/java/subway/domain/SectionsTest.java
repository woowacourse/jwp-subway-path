package subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.domain.subway.Distance;
import subway.domain.subway.Section;
import subway.domain.subway.Sections;
import subway.domain.subway.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SectionsTest {

    private Sections sections;

    @BeforeEach
    void init() {
        Station station1 = new Station(1L, "잠실역1");
        Station station2 = new Station(2L, "잠실역2");
        Station station3 = new Station(3L, "잠실역3");
        Station station4 = new Station(4L, "잠실역4");
        final Section section1 = new Section(new Distance(3), station1, station2, 1L);
        final Section section2 = new Section(new Distance(3), station2, station3, 1L);
        final Section section3 = new Section(new Distance(3), station3, station4, 1L);

        this.sections = Sections.from(List.of(section3, section1, section2));
    }

    @Test
    void 정렬된_구간으로_만들어_진다() {
        // when
        final List<Section> sortedSections = this.sections.getSections();

        // then
        assertAll(
                () -> assertThat(sortedSections.get(0).getUpStationId()).isEqualTo(1L),
                () -> assertThat(sortedSections.get(sortedSections.size() - 1).getDownStationId()).isEqualTo(4L)
        );

    }

    @Test
    void 상행역_기준으로_역의_존재를_확인한다() {
        // when, then
        assertAll(
                () -> assertThat(sections.isUpStationPoint(1L)).isTrue(),
                () -> assertThat(sections.isUpStationPoint(4L)).isFalse()
        );
    }

    @Test
    void 상행역_기준으로_구간을_찾아온다() {
        // when
        final Section result = sections.getTargetUpStationSection(2L);

        // then
        assertAll(
                () -> assertThat(result.getUpStationId()).isEqualTo(2L),
                () -> assertThat(result.getDownStationId()).isEqualTo(3L)
        );
    }

    @Test
    void 하행역_기준으로_구간을_찾아온다() {
        // when
        final Section result = sections.getTargetDownStationSection(2L);

        // then
        assertAll(
                () -> assertThat(result.getUpStationId()).isEqualTo(1L),
                () -> assertThat(result.getDownStationId()).isEqualTo(2L)
        );
    }

    @Test
    void 역을_포함한_구간들을_찾아온다() {
        // when
        final List<Section> result = sections.findIncludeTargetSection(2L);

        // then
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).getUpStationId()).isEqualTo(1L),
                () -> assertThat(result.get(1).getDownStationId()).isEqualTo(3L)
        );
    }
}
