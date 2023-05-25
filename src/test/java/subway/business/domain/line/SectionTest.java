package subway.business.domain.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.fixture.FixtureForLineTest.station1;
import static subway.fixture.FixtureForLineTest.station2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class SectionTest {
    @DisplayName("거리를 받아 전체 거리에서 뺀 거리를 계산한다.")
    @Test
    void shouldReturnRemainingDistanceWhenInputDistance() {
        Station upwardStation = station1();
        Station downwardStation = station2();
        Section section = new Section(1L, upwardStation, downwardStation, 5);

        int remainingDistance = section.calculateRemainingDistance(3);
        assertThat(remainingDistance).isEqualTo(2);
    }

    @DisplayName("이미 구간에 포함된 역의 이름이면 true를 반환한다.")
    @Test
    void shouldReturnTrueWhenAlreadyHasStation() {
        Station upwardStation = station1();
        Station downwardStation = station2();
        Section section = new Section(1L, upwardStation, downwardStation, 5);

        assertThat(section.hasStationNameOf(upwardStation)).isTrue();
    }

    @DisplayName("구간에 포함되지 않은 역의 이름이면 false를 반환한다.")
    @Test
    void shouldReturnFalseWhenDoesNotHaveStation() {
        Station upwardStation = station1();
        Station downwardStation = station2();
        Section section = new Section(1L, upwardStation, downwardStation, 5);

        Station station = new Station(3L, "강남역");
        assertThat(section.hasStationNameOf(station)).isFalse();
    }

    @DisplayName("입력 받은 역이 상행역이면 true를 반환한다.")
    @Test
    void shouldReturnTrueWhenInputStationIsSameWithUpwardStation() {
        Station upwardStation = station1();
        Station downwardStation = station2();
        Section section = new Section(1L, upwardStation, downwardStation, 5);

        assertThat(section.isUpwardStation(upwardStation)).isTrue();
    }

    @DisplayName("입력 받은 역이 상행역이 아니면 false를 반환한다.")
    @Test
    void shouldReturnFalseWhenInputStationIsDifferentWithUpwardStation() {
        Station upwardStation = station1();
        Station downwardStation = station2();
        Section section = new Section(1L, upwardStation, downwardStation, 5);

        assertThat(section.isUpwardStation(new Station(3L, "강남역"))).isFalse();
    }

    @DisplayName("입력 받은 역이 하행역이면 true를 반환한다.")
    @Test
    void shouldReturnTrueWhenInputStationIsSameWithDownwardStation() {
        Station upwardStation = station1();
        Station downwardStation = station2();
        Section section = new Section(1L, upwardStation, downwardStation, 5);

        assertThat(section.isDownwardStation(downwardStation)).isTrue();
    }

    @DisplayName("입력 받은 역이 하행역이 아니면 false를 반환한다.")
    @Test
    void shouldReturnFalseWhenInputStationIsDifferentWithDownwardStation() {
        Station upwardStation = station1();
        Station downwardStation = station2();
        Section section = new Section(1L, upwardStation, downwardStation, 5);

        assertThat(section.isDownwardStation(new Station(3L, "강남역"))).isFalse();
    }

    @DisplayName("동등성")
    @Nested
    class equals {

        @DisplayName("ID가 같은 Section을 비교하면 true를 반환한다.")
        @Test
        void shouldReturnTrueWhenCompareSectionsHaveSameId() {
            Station upwardStation = station1();
            Station downwardStation = station2();

            Section section1 = new Section(1L, upwardStation, downwardStation, 5);
            Section section2 = new Section(1L, upwardStation, downwardStation, 5);

            assertThat(section1.equals(section2)).isTrue();
        }

        @DisplayName("ID가 다른 Section을 비교하면 false를 반환한다.")
        @Test
        void shouldReturnFalseWhenCompareSectionsHaveDifferentId() {
            Station upwardStation = station1();
            Station downwardStation = station2();

            Section section1 = new Section(1L, upwardStation, downwardStation, 5);
            Section section2 = new Section(2L, upwardStation, downwardStation, 5);

            assertThat(section1.equals(section2)).isFalse();
        }

        @DisplayName("ID가 null인 Section을 기준으로 비교하면 예외가 발생한다.")
        @Test
        void shouldThrowExceptionWhenCompareFromSectionHaveNullId() {
            Station upwardStation = station1();
            Station downwardStation = station2();

            Section section1 = new Section(null, upwardStation, downwardStation, 5);
            Section section2 = new Section(2L, upwardStation, downwardStation, 5);

            assertThatThrownBy(() -> section1.equals(section2))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("ID가 존재하지 않는 Section을 기준으로 비교했습니다.");
        }

        @DisplayName("ID가 null인 Section을 기준으로 비교하면 예외가 발생한다.")
        @Test
        void shouldThrowExceptionWhenCompareToSectionHaveNullId() {
            Station upwardStation = station1();
            Station downwardStation = station2();

            Section section1 = new Section(1L, upwardStation, downwardStation, 5);
            Section section2 = new Section(null, upwardStation, downwardStation, 5);

            assertThatThrownBy(() -> section1.equals(section2))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("ID가 존재하지 않는 Section을 인자로 넣어 비교했습니다.");
        }
    }
}
