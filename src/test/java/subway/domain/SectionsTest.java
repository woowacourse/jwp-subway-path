package subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SectionsTest {

    private Sections sections;

    @BeforeEach
    void init() {
        final Section section1 = new Section(3, 1L, 2L, 1L);
        final Section section2 = new Section(3, 2L, 3L, 1L);
        final Section section3 = new Section(3, 3L, 4L, 1L);

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
        final Section result = sections.getTargtUpStationSection(2L);

        // then
        assertAll(
                () -> assertThat(result.getUpStationId()).isEqualTo(2L),
                () -> assertThat(result.getDownStationId()).isEqualTo(3L)
        );
    }

    @Test
    void 하행역_기준으로_구간을_찾아온다() {
        // when
        final Section result = sections.getTargtDownStationSection(2L);

        // then
        assertAll(
                () -> assertThat(result.getUpStationId()).isEqualTo(1L),
                () -> assertThat(result.getDownStationId()).isEqualTo(2L)
        );
    }

    @Test
    void 역을_포함한_구간들을_찾아온다() {
        // when
        final Sections sections = this.sections.findIncludeTargetSection(2L);

        // then
        final List<Section> result = sections.getSections();
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).getUpStationId()).isEqualTo(1L),
                () -> assertThat(result.get(1).getDownStationId()).isEqualTo(3L)
        );
    }
}
