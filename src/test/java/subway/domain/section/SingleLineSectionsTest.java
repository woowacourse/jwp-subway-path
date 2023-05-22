package subway.domain.section;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
class SingleLineSectionsTest {

    private SingleLineSections sections;
    private Station 잠실역;
    private Station 잠실새내역;
    private Station 삼성역;
    private Station 선릉역;

    /**
     * 잠실역 -- 10 -- 잠실새내역 -- 15 -- 삼성역 -- 20 -- 선릉역
     */
    @BeforeEach
    void init() {
        잠실역 = new Station(1L, "잠실역");
        잠실새내역 = new Station(2L, "잠실새내역");
        삼성역 = new Station(3L, "삼성역");
        선릉역 = new Station(4L, "선릉역");

        final Section section1 = new Section(10, 잠실역, 잠실새내역, 1L);
        final Section section2 = new Section(15, 잠실새내역, 삼성역, 1L);
        final Section section3 = new Section(20, 삼성역, 선릉역, 1L);

        this.sections = SingleLineSections.from(List.of(section3, section1, section2));
    }

    @Test
    void 정렬된_구간으로_만들어_진다() {
        // when
        final List<Section> sortedSections = this.sections.getSections();

        // then
        assertAll(
                () -> assertThat(sortedSections.get(0).getUpStation()).isEqualTo(잠실역),
                () -> assertThat(sortedSections.get(sortedSections.size() - 1).getDownStation()).isEqualTo(선릉역)
        );

    }

    @Test
    void 상행역_기준으로_역의_존재를_확인한다() {
        // when, then
        assertAll(
                () -> assertThat(sections.isUpwardStation(잠실역)).isTrue(),
                () -> assertThat(sections.isUpwardStation(선릉역)).isFalse()
        );
    }

    @Test
    void 상행역_기준으로_구간을_찾아온다() {
        // when
        final Section result = sections.findUpwardStationSection(잠실새내역);

        // then
        assertAll(
                () -> assertThat(result.getUpStation()).isEqualTo(잠실새내역),
                () -> assertThat(result.getDownStation()).isEqualTo(삼성역)
        );
    }

    @Test
    void 하행역_기준으로_구간을_찾아온다() {
        // when
        final Section result = sections.findDownwardStationSection(잠실새내역);

        // then
        assertAll(
                () -> assertThat(result.getUpStation()).isEqualTo(잠실역),
                () -> assertThat(result.getDownStation()).isEqualTo(잠실새내역)
        );
    }

    @Test
    void 역을_포함한_구간들을_찾아온다() {
        // when
        final Section forwardSection = this.sections.findIncludeSectionByBackwardStation(잠실새내역);
        final Section backwardSection = this.sections.findIncludeSectionByForwardStation(잠실새내역);

        // then
        assertAll(
                () -> assertThat(forwardSection.getUpStation()).isEqualTo(잠실역),
                () -> assertThat(backwardSection.getDownStation()).isEqualTo(삼성역)
        );
    }

    @Test
    void 구간이_이미_존재하는지_확인한다() {
        // when, then
        assertAll(
                () -> assertThat(sections.hasSection(잠실새내역, 잠실역)).isTrue(),
                () -> assertThat(sections.hasSection(잠실역, 잠실새내역)).isTrue(),
                () -> assertThat(sections.hasSection(잠실역, 삼성역)).isFalse()
        );
    }
}
