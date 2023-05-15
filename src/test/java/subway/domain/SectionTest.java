package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SectionTest {

    @Test
    void 두_역이_같으면_예외를_발생한다() {
        Line line = new Line(1L, "2호선", "green");
        Long lineId = line.getId();
        final Station station1 = new Station("잠실역");
        final Station station2 = new Station("잠실역");
        assertThatThrownBy(() -> new Section(lineId, station1, station2, 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("입력한 두 역은 같습니다.");
    }

    @Test
    void 특정_역을_가지고_있는지_확인한다() {
        Line line = new Line(1L, "2호선", "green");
        Long lineId = line.getId();
        final Station station1 = new Station("잠실역");
        final Station station2 = new Station("잠실새내역");
        final Section section = new Section(lineId, station1, station2, 3);

        assertSoftly(softly -> {
            softly.assertThat(section.exist(new Station("잠실역"))).isTrue();
            softly.assertThat(section.exist(new Station("잠실새내역"))).isTrue();
            softly.assertThat(section.exist(new Station("없는역"))).isFalse();
        });
    }

    @Test
    void 특정_역이_구간의_왼쪽에_있는지_확인한다() {
        Line line = new Line(1L, "2호선", "green");
        Long lineId = line.getId();
        final Station station1 = new Station("잠실역");
        final Station station2 = new Station("잠실새내역");
        final Section section = new Section(lineId, station1, station2, 3);

        assertSoftly(softly -> {
            softly.assertThat(section.existLeft(new Station("잠실역"))).isTrue();
            softly.assertThat(section.existLeft(new Station("잠실새내역"))).isFalse();
        });
    }

    @Test
    void 특정_역이_구간의_오른쪽에_있는지_확인한다() {
        Line line = new Line(1L, "2호선", "green");
        Long lineId = line.getId();
        final Station station1 = new Station("잠실역");
        final Station station2 = new Station("잠실새내역");
        final Section section = new Section(lineId, station1, station2, 3);

        assertSoftly(softly -> {
            softly.assertThat(section.existRight(new Station("잠실역"))).isFalse();
            softly.assertThat(section.existRight(new Station("잠실새내역"))).isTrue();
        });
    }

    @ParameterizedTest
    @CsvSource({"2, true", "3, false", "4, false"})
    void 구간_사이에_역을_추가할_수_있는_거리인지_확인한다(final int otherDistance, final boolean expected) {
        // given
        Line line = new Line(1L, "2호선", "green");
        Long lineId = line.getId();
        final Station station1 = new Station("잠실역");
        final Station station2 = new Station("잠실새내역");
        final Section section = new Section(lineId, station1, station2, 3);
        // when
        final boolean actual = section.isInsertable(otherDistance);
        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 역을_수정한다() {
        // given
        Line line = new Line(1L, "2호선", "green");
        Long lineId = line.getId();
        final Station 잠실 = new Station("잠실역");
        final Station 잠실새내 = new Station("잠실새내역");
        final Section section = new Section(lineId, 잠실, 잠실새내, 3);
        // when
        section.updateStation(잠실, new Station("디노"));
        // then
        assertThat(section).isEqualTo(new Section(lineId, new Station("디노"), new Station("잠실새내역"), 3));
    }

}
