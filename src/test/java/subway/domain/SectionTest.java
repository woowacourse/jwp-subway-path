package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionTest {
    @DisplayName("거리를 받아 전체 거리에서 뺀 거리를 계산한다.")
    @Test
    void shouldReturnRemainingDistanceWhenInputDistance() {
        Station upwardStation = new Station("잠실역");
        Station downwardStation = new Station("몽촌토성역");
        Section section = new Section(upwardStation, downwardStation, 5);

        int remainingDistance = section.calculateRemainingDistance(3);
        assertThat(remainingDistance).isEqualTo(2);
    }

    @DisplayName("이미 구간에 포함된 역이면 true를 반환한다.")
    @Test
    void shouldReturnTrueWhenAlreadyHasStation() {
        Station upwardStation = new Station("잠실역");
        Station downwardStation = new Station("몽촌토성역");
        Section section = new Section(upwardStation, downwardStation, 5);

        assertThat(section.hasStation(upwardStation)).isTrue();
    }

    @DisplayName("구간에 포함되지 않은 역이면 false를 반환한다.")
    @Test
    void shouldReturnFalseWhenDoesNotHaveStation() {
        Station upwardStation = new Station("잠실역");
        Station downwardStation = new Station("몽촌토성역");
        Section section = new Section(upwardStation, downwardStation, 5);

        Station station = new Station("강남역");
        assertThat(section.hasStation(station)).isFalse();
    }

    @DisplayName("입력 받은 역이 상행역이면 true를 반환한다.")
    @Test
    void shouldReturnTrueWhenInputStationIsSameWithUpwardStation() {
        Station upwardStation = new Station("잠실역");
        Station downwardStation = new Station("몽촌토성역");
        Section section = new Section(upwardStation, downwardStation, 5);

        assertThat(section.isUpwardStation(upwardStation)).isTrue();
    }

    @DisplayName("입력 받은 역이 상행역이 아니면 false를 반환한다.")
    @Test
    void shouldReturnFalseWhenInputStationIsDifferentWithUpwardStation() {
        Station upwardStation = new Station("잠실역");
        Station downwardStation = new Station("몽촌토성역");
        Section section = new Section(upwardStation, downwardStation, 5);

        assertThat(section.isUpwardStation(new Station("강남역"))).isFalse();
    }

    @DisplayName("입력 받은 역이 하행역이면 true를 반환한다.")
    @Test
    void shouldReturnTrueWhenInputStationIsSameWithDownwardStation() {
        Station upwardStation = new Station("잠실역");
        Station downwardStation = new Station("몽촌토성역");
        Section section = new Section(upwardStation, downwardStation, 5);

        assertThat(section.isDownwardStation(downwardStation)).isTrue();
    }

    @DisplayName("입력 받은 역이 하행역이 아니면 false를 반환한다.")
    @Test
    void shouldReturnFalseWhenInputStationIsDifferentWithDownwardStation() {
        Station upwardStation = new Station("잠실역");
        Station downwardStation = new Station("몽촌토성역");
        Section section = new Section(upwardStation, downwardStation, 5);

        assertThat(section.isDownwardStation(new Station("강남역"))).isFalse();
    }
}
