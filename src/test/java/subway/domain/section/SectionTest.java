package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.domain.station.Station;
import subway.domain.station.StationName;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class SectionTest {

    @Test
    void 구간은_상행역이_존재하지_않으면_예외가_발생한다() {
        // given
        final Station downStation = new Station(new StationName("잠실"));
        final Distance distance = new Distance(1);

        // expect
        assertThatThrownBy(() -> new Section(null, downStation, distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구간에 역은 필수로 존재해야 합니다.");
    }

    @Test
    void 구간은_하행역이_존재하지_않으면_예외가_발생한다() {
        // given
        final Station upStation = new Station(new StationName("잠실"));
        final Distance distance = new Distance(1);

        // expect
        assertThatThrownBy(() -> new Section(upStation, null, distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구간에 역은 필수로 존재해야 합니다.");
    }

    @Test
    void 구간은_상행역_하행역이_같다면_예외를_발생한다() {
        // given
        final Station upStation = new Station(new StationName("잠실"));
        final Station downStation = new Station(new StationName("잠실"));
        final Distance distance = new Distance(1);

        // expect
        assertThatThrownBy(() -> new Section(upStation, downStation, distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("한 구간은 서로 다른 역으로 이루어져야 합니다.");
    }

    @Test
    void 구간은_거리_정보가_없다면_예외를_발생한다() {
        // given
        final Station upStation = new Station(new StationName("잠실"));
        final Station downStation = new Station(new StationName("잠실새내"));

        // expect
        assertThatThrownBy(() -> new Section(upStation, downStation, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구간은 역 사이에 거리가 필수로 존재해야합니다.");
    }

    @Test
    void 구간은_서로다른_상행_하행역과_거리정보를_받아_생성된다() {
        // given
        final Station upStation = new Station(new StationName("잠실"));
        final Station downStation = new Station(new StationName("잠실새내"));
        final Distance distance = new Distance(1);

        // expect
        assertDoesNotThrow(() -> new Section(upStation, downStation, distance));
    }

    @Test
    void 구간은_다른_구간이_내_앞에_생성될_수_있다면_true_를_반환한다() {
        // given
        final Station upStationOfA = new Station(new StationName("잠실새내"));
        final Station downStationOfA = new Station(new StationName("잠실"));
        final Distance distanceOfA = new Distance(1);
        final Section sectionA = new Section(upStationOfA, downStationOfA, distanceOfA);

        final Station upStationOfOther = new Station(new StationName("종합운동장"));
        final Station downStationOfOther = new Station(new StationName("잠실새내"));
        final Distance distanceOfOther = new Distance(5);
        final Section sectionOther = new Section(upStationOfOther, downStationOfOther, distanceOfOther);

        assertThat(sectionA.isAssemblableOnFront(sectionOther)).isTrue();
    }

    @Test
    void 구간은_다른_구간이_내_앞에_생성될_수_없다면_false_를_반환한다() {
        // given
        final Station upStationOfA = new Station(new StationName("잠실새내"));
        final Station downStationOfA = new Station(new StationName("잠실"));
        final Distance distanceOfA = new Distance(1);
        final Section sectionA = new Section(upStationOfA, downStationOfA, distanceOfA);

        final Station upStationOfOther = new Station(new StationName("잠실새내"));
        final Station downStationOfOther = new Station(new StationName("종합운동장"));
        final Distance distanceOfOther = new Distance(5);
        final Section sectionOther = new Section(upStationOfOther, downStationOfOther, distanceOfOther);

        assertThat(sectionA.isAssemblableOnFront(sectionOther)).isFalse();
    }

    @Test
    void 구간은_다른_구간이_내_뒤에_생성될_수_있다면_true_를_반환한다() {
        // given
        final Station upStationOfA = new Station(new StationName("잠실새내"));
        final Station downStationOfA = new Station(new StationName("잠실"));
        final Distance distanceOfA = new Distance(1);
        final Section sectionA = new Section(upStationOfA, downStationOfA, distanceOfA);

        final Station upStationOfOther = new Station(new StationName("종합운동장"));
        final Station downStationOfOther = new Station(new StationName("잠실새내"));
        final Distance distanceOfOther = new Distance(5);
        final Section sectionOther = new Section(upStationOfOther, downStationOfOther, distanceOfOther);

        assertThat(sectionOther.isAssemblableOnBack(sectionA)).isTrue();
    }

    @Test
    void 구간은_다른_구간이_내_뒤에_생성될_수_없다면_false_를_반환한다() {
        // given
        final Station upStationOfA = new Station(new StationName("잠실"));
        final Station downStationOfA = new Station(new StationName("잠실새내"));
        final Distance distanceOfA = new Distance(1);
        final Section sectionA = new Section(upStationOfA, downStationOfA, distanceOfA);

        final Station upStationOfOther = new Station(new StationName("종합운동장"));
        final Station downStationOfOther = new Station(new StationName("잠실새내"));
        final Distance distanceOfOther = new Distance(5);
        final Section sectionOther = new Section(upStationOfOther, downStationOfOther, distanceOfOther);

        assertThat(sectionOther.isAssemblableOnBack(sectionA)).isFalse();
    }
}
