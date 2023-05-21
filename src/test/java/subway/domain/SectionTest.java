package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

class SectionTest {

    private final Station upStation = new Station(1L, "신림");
    private final Station downStation = new Station(2L, "봉천");

    private final Line line = new Line(1L, "2호선", "초록색", 0);
    private final Line newLine = new Line(2L, "newLine", "newColor", 0);

    private final Distance distance = new Distance(10);

    private final Section section = new Section(1L, line, upStation, downStation, distance);

    @Test
    void 두_역과_하나의_호선과_거리를_가진다() {
        assertThatNoException().isThrownBy(() -> new Section(line, upStation, downStation, distance));
    }

    @Test
    void 모든_필드가_같으면_같은_객체이다() {
        Section section1 = new Section(1L, line, upStation, downStation, distance);
        Section section2 = new Section(1L, line, upStation, downStation, distance);

        assertThat(section1).isEqualTo(section2);
    }

    @Test
    void 방향을_뒤집는다() {
        // given
        Section section = new Section(1L, line, upStation, downStation, distance);

        // when
        Section reversedSection = section.reverseDirection();

        // then
        assertAll(
                () -> assertThat(reversedSection.getUpStation()).isEqualTo(downStation),
                () -> assertThat(reversedSection.getDownStation()).isEqualTo(upStation)
        );
    }

    @Test
    void 같은_방향인지_확인한다() {
        Section section1 = new Section(1L, line, upStation, downStation, new Distance(10));
        Section section2 = new Section(2L, line, upStation, downStation, new Distance(5));

        assertThat(section1.isSameDirection(section2)).isTrue();
    }

    @Test
    void 반대_방향인지_확인한다() {
        Section section1 = new Section(1L, line, upStation, downStation, new Distance(10));
        Section section2 = new Section(2L, line, downStation, upStation, new Distance(5));

        assertThat(section1.isReverseDirection(section2)).isTrue();
    }

    @Test
    void 두_Section을_하나로_조합한다() {
        // given
        Station thirdStation = new Station(3L, "ThirdStation");

        Section upSection = new Section(1L, line, upStation, downStation, new Distance(10));
        Section downSection = new Section(2L, line, downStation, thirdStation, new Distance(5));

        // when
        Section resultSection = Section.combineSection(upSection, downSection);

        // then
        assertAll(
                () -> assertThat(resultSection.getLine()).isEqualTo(line),
                () -> assertThat(resultSection.getUpStation()).isEqualTo(upStation),
                () -> assertThat(resultSection.getDownStation()).isEqualTo(thirdStation),
                () -> assertThat(resultSection.getDistance()).isEqualTo(new Distance(15))
        );
    }

    @Test
    void 다른_Section과_같은_라인인지_확인한다() {
        // given
        Section sameSection = new Section(1L, line, upStation, downStation, distance);
        Section differentSection = new Section(2L, newLine, downStation, upStation, distance);

        // expect
        assertAll(
                () -> assertThat(section.isSameLine(sameSection)).isTrue(),
                () -> assertThat(section.isSameLine(differentSection)).isFalse()
        );
    }

    @Test
    void 호선과_역으로_상행_역이_같은_역인지_확인한다() {
        // expect
        assertAll(
                () -> assertThat(section.isSameUpStation(upStation)).isTrue(),
                () -> assertThat(section.isSameUpStation(downStation)).isFalse()
        );
    }

    @Test
    void 호선과_역으로_하행_역이_같은_역인지_확인한다() {
        // expect
        assertAll(
                () -> assertThat(section.isSameDownStation(downStation)).isTrue(),
                () -> assertThat(section.isSameDownStation(upStation)).isFalse()
        );
    }

    @Test
    void 두_Section의_거리의_합과_거리가_같은지_확인한다() {
        // given
        Section upSection_distance5 = new Section(2L, line, upStation, downStation, new Distance(5));
        Section downSection_distance5 = new Section(3L, line, downStation, upStation, new Distance(5));
        Section downSection_distance10 = new Section(3L, line, downStation, upStation, new Distance(10));

        // expect
        assertAll(
                () -> assertThat(section.isSameDistance(upSection_distance5, downSection_distance5)).isTrue(),
                () -> assertThat(section.isSameDistance(upSection_distance5, downSection_distance10)).isFalse()
        );
    }

    @Test
    void 다른_Section이_합쳐질_때_갈림길의_조건인지_확인한다() {
        // given
        Station 낙성대역 = new Station(3L, "낙성대역");
        Section otherSection = new Section(2L, line, upStation, 낙성대역, new Distance(10));

        // expect
        assertThat(section.isForkRoad(otherSection)).isTrue();
    }

    @Test
    void 같은_역을_가지는지_확인한다() {
        assertThat(section.isSameStations(upStation, downStation)).isTrue();
    }
}
